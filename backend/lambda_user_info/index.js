const AWS = require("aws-sdk");
const bcrypt = require('bcryptjs');
const dynamo = new AWS.DynamoDB.DocumentClient(); 
const jwt = require('jsonwebtoken');

exports.handler = async (event, context, callback) => { 
    let body; 
    let response; 

    switch(event.routeKey){

        case 'POST /user-login':
            body = JSON.parse(event.body); 
            
            var params = { 
                TableName: 'users', 
                KeyConditionExpression: 'nric = :nric', 
                ExpressionAttributeValues: { 
                    ':nric': body.nric
                }
            };
            
            try {
                const result = await dynamo.query(params).promise();
                if (result.Items.length > 0) {
                    const user = result.Items[0]; 
                    const match = await bcrypt.compare(body.password, user.password);
                    
                    if (match) {
                        const token = jwt.sign({ nric: user.nric, role: user.role }, process.env.APP_KEY, { expiresIn: '3h' });
                        response = {
                            statusCode: 200,
                            headers: {
                                authorization: `Bearer ${token}`
                            },
                            body: JSON.stringify({
                                message: "Login successful",
                                token: token,
                                nric: user.nric,
                                role: user.role
                            }),
                        };
                    } else {
                        response = {
                            statusCode: 403,
                            body: JSON.stringify({ message: "Invalid credentials" }),
                        };
                    }
                } else {
                    response = {
                        statusCode: 403,
                        body: JSON.stringify({ message: "User not found" }),
                    };
                }
            } catch (err) {
                response = {
                    statusCode: 500,
                    body: JSON.stringify({ message: "Internal Server Error", error: err.message }),
                };
            }
            callback(null, response);
            break;

        case 'POST /user-sign-up':
            body = JSON.parse(event.body);
            
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$#!%*?&]).{8,}$/;

            if (!emailRegex.test(body.email)) {
                return callback(null, { statusCode: 400, body: JSON.stringify({ message: "Invalid email format" }) });
            }

            if (!passwordRegex.test(body.password)) {
                return { statusCode: 400, body: JSON.stringify({ message: "Password must be at least 8 characters long and contain at least one letter and one number" }) };
            }

            const hashedPassword = await bcrypt.hash(body.password, 10);

            var params = {
                TableName: 'users',
                KeyConditionExpression: 'nric = :nric',
                ExpressionAttributeValues: {
                    ':nric': body.nric,
                },
            };
            
            try {
                const result = await dynamo.query(params).promise();
                if (result.Count === 1) {
                    return callback(null, { statusCode: 400, body: JSON.stringify({ message: "nric already in use!" }) });
                }

                const userParams = {
                    TableName: 'users',
                    Item: {
                        ...body,
                        password: hashedPassword
                    }
                };

                await dynamo.put(userParams).promise();
                
                const token = jwt.sign({ nric: body.nric, role: body.role }, process.env.APP_KEY, { expiresIn: '3h' });
                response = {
                    statusCode: 201,
                    headers: {
                        authorization: `Bearer ${token}`
                    },
                    body: JSON.stringify({
                        message: "User added",
                    }),
                };
            } catch (err) {
                response = {
                    statusCode: 500,
                    body: JSON.stringify({ message: "Internal Server Error", error: err.message }),
                };
            }
            callback(null, response);
            break;

        case 'PUT /user-profile':
            body = JSON.parse(event.body);

            // TODO - APPLY EMAIL REGEX - Later on I need to make Pop Up errors for this
            const newEmailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!newEmailRegex.test(body.email)) {
                return callback(null, { statusCode: 400, body: JSON.stringify({ message: "Invalid email format" }) });
            }
        
            var params = {
                TableName: 'users',
                Key: { 
                    "nric": body.nric,
                    "role": body.role, // To be obtained from the internal database (fixed)
                },
                UpdateExpression: "set email = :email, fullname = :fullname",
                ExpressionAttributeValues: {
                    ":email": body.email,
                    ":fullname": body.fullname,
                },
                ReturnValues: "UPDATED_NEW" 
            };
            
            try {
                const result = await dynamo.update(params).promise();
                response = {
                    statusCode: 200,
                    body: JSON.stringify({
                        message: "User profile updated",
                        updatedAttributes: result.Attributes 
                    }),
                };
            } catch (err) {
                response = {
                    statusCode: 500,
                    body: JSON.stringify({ message: "Internal Server Error", error: err.message }),
                };
            }
            callback(null, response);
            break;

        case 'PUT /user-password':
            body = JSON.parse(event.body);
            
            // (1)Params when: Enter Old Password (Checks first)
            var paramsCheck = { 
                TableName: 'users', 
                KeyConditionExpression: 'nric = :nric', 
                ExpressionAttributeValues: { 
                    ':nric': body.nric
                }
            };

            // (2)Once Approved, Obtain New Password and Hash
            const newPasswordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$#!%*?&]).{8,}$/;
            if (!newPasswordRegex.test(body.newpassword)) {
                return callback(null, { statusCode: 400, body: JSON.stringify({ message: "Password must be at least 8 characters long and contain at least one letter and one number" }) });
            }
            const newHashedPassword = await bcrypt.hash(body.newpassword, 10);

            // Params when: (3)Once Hashed, Update New Password 
            var paramsUpdate = {
                TableName: 'users',
                Key: {
                    "nric": body.nric,
                    "role": body.role, // To be obtained from the internal database (fixed)
                },
                UpdateExpression: "set password = :password",
                ExpressionAttributeValues: {
                    ":password": newHashedPassword
                },
                ReturnValues: "UPDATED_NEW"
            };


            // (A) Login logic - with the checking - just query
            // (B) SignUp logic - with the putting - but update instead
            try { 
                const result = await dynamo.query(paramsCheck).promise();
                if (result.Items.length > 0) { 
                    const user = result.Items[0];
                    const match = await bcrypt.compare(body.password, user.password);

                    if(match) { // If Old Password Checks Out...
                        const result = await dynamo.update(paramsUpdate).promise();
                        response = {
                            statusCode: 200,
                            body: JSON.stringify({
                                message: "User password updated",
                                updatedAttributes: result.Attributes
                            }),
                        };
                    } else {
                        response = {
                            statusCode: 403,
                            body: JSON.stringify({ message: "Invalid credentials" }), // Because it doesn't match (Old Password Issue)
                        };
                    }
                } else {
                    response = {
                        statusCode: 403,
                        body: JSON.stringify({ message: "User not found" }),
                    };
                }
            } catch (err) {
                response = {
                    statusCode: 500,
                    body: JSON.stringify({ message: "Internal Server Error", error: err.message }),
                };
            }
            callback(null, response);
            break;

        case 'DELETE /user-profile': 
            body = JSON.parse(event.body);
            
            // The user will be prompted to confirm their password one last time.
            var paramsVerify = {
                TableName: 'users',
                KeyConditionExpression: 'nric = :nric',
                ExpressionAttributeValues: {
                    ':nric': body.nric
                }
            };
            
            // Params for deletion once password has been deleted.
            var paramsDelete = {
                TableName: 'users',
                Key: {
                    "nric": body.nric,
                    "role": body.role
                },
            };

            try {
                const result = await dynamo.query(paramsVerify).promise();
                if (result.Items.length > 0) {
                    const user = result.Items[0];
                    const match = await bcrypt.compare(body.password, user.password);

                    if(match) {
                         await dynamo.delete(paramsDelete).promise();
                        response = {
                            statusCode: 200,
                            body: JSON.stringify({
                                message: "User account deleted",
                            }),
                        };
                    } else {
                        response = {
                            statusCode: 403,
                            body: JSON.stringify({ message: "Invalid credentials" }),
                        };
                    }
                } else {
                    response = {
                        statusCode: 403,
                        body: JSON.stringify({ message: "User not found" }),
                    };
                }
            } catch (err) {
                response = {
                    statusCode: 500,
                    body: JSON.stringify({ message: "Internal Server Error", error: err.message }),
                };
            }
            callback(null, response);
            break;
        default:
            throw new Error("Unsupported route: " + event.routeKey);
    }
};

