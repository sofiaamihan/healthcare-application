const jwt = require('jsonwebtoken');
require("dotenv").config();

exports.handler = async (event) => {
    console.log('Received event:', JSON.stringify(event, null, 2));
    const token = event.headers.authorization;

    if (!token) {
        console.log('No token provided');
        return {
            isAuthorized: false,
            context: {}
        };
    }

    try {
        const decoded = jwt.verify(token.replace('Bearer ', ''), process.env.APP_KEY); 
        console.log('Token decoded successfully:', decoded);

        return {
            isAuthorized: true,
            context: {
                user: decoded
            }
        };
    } catch (err) {
        console.log('Token verification failed:', err.message);
        return {
            isAuthorized: false,
            context: {}
        };
    }
};