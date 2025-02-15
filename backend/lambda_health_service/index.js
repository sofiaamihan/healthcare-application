const mysql = require('mysql');
const con = mysql.createConnection({
    host : "health-service.cvewtgj0uqha.us-east-1.rds.amazonaws.com",
    user : "admin",
    password : "12345#Password",
    port : "3306",
    database : "health_service"
});
exports.handler = (event, context, callback) => {
    
    let sql;
    context.callbackWaitsForEmptyEventLoop = false;
    
    switch (event.routeKey){
    
        case 'GET /info/{nric}':
            // Gets the user's id upon login.
            sql = "SELECT * FROM `health_service`.`user` WHERE nric=?;";
            con.query(sql, [event.pathParameters.nric], function(err, result){
                if (err) throw err;
                    const singleUser = result[0];
                    return callback(null, singleUser);
            })
            break;
        
        case 'POST /user':
            // User's measurements get taken and added to their account.
            body = JSON.parse(event.body)
            sql = "INSERT INTO `health_service`.`user` (`nric`, `role`, `age`, `gender`, `weight`, `height`) VALUES (?, ?, ?, ?, ?, ?);";
            con.query(sql, [body.nric, body.role, body.age, body.gender, body.weight, body.height], function (err, result){
                if (err) throw err;
                return callback(null, { success: true, insertId: result.insertId });
            });
            break;
            
        case 'PUT /user/{id}':
            // Edit the user's measurements via their profile.
            body = JSON.parse(event.body)
            sql = "UPDATE `health_service`.`user` SET `age` = ?, `gender` = ?, `weight` = ?, `height` = ? WHERE id=?;";
            con.query(sql, [body.age, body.gender, body.weight, body.height, event.pathParameters.id], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, changedRows: result.changedRows });
            });
            break;
            
        case 'DELETE /user/{id}':
            // Deletes the user's account and all its corresponding data. (Prompted once successful in DynamoDB)
            sql = "DELETE FROM `health_service`.`user` WHERE id=?;";
            con.query(sql, [event.pathParameters.id], function(err, result) {
                if (err) throw err;
                return callback(null, { success: true, affectedRows: result.affectedRows });
            });
            break;
            
        case 'GET /activity/{user_id}/{day}':
            // Gets all of the user's activity based on their ID and date/time.
            // Also returns the activity's id based on above filter.
            sql = "SELECT * FROM `health_service`.`activity` WHERE user_id=? AND DATE(time_taken)=?;";
            con.query(sql, [event.pathParameters.user_id, event.pathParameters.day], function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;
            
        case 'POST /activity':
            // Adds new activities into the database.
            body = JSON.parse(event.body)
            sql = "INSERT INTO `health_service`.`activity` (`user_id`, `category_id`, `time_taken`, `calories_burnt`, `step_count`, `distance`, `walking_speed`, `walking_steadiness`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            con.query(sql, [body.user_id, body.category_id, body.time_taken, body.calories_burnt, body.step_count, body.distance, body.walking_speed, body.walking_steadiness], function (err, result){
                if (err) throw err;
                return callback(null, { success: true, insertId: result.insertId });
            });
            break;
            
        case 'PUT /activity/{id}':
            // Edit activity details based on the obtained activity_id.
            body = JSON.parse(event.body)
            sql = "UPDATE `health_service`.`activity` SET `time_taken` = ?, `calories_burnt` = ?, `step_count` = ?, `distance` = ?, `walking_speed` = ?, `walking_steadiness` = ? WHERE id=?;";
            con.query(sql, [body.time_taken, body.calories_burnt, body.step_count, body.distance, body.walking_speed, body.walking_steadiness, event.pathParameters.id], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, changedRows: result.changedRows });
            });
            break;
            
        case 'DELETE /activity/{id}':
            // Delete an activity based on the obtained activity_id.
            sql = "DELETE FROM `health_service`.`activity` WHERE id=?;";
            con.query(sql, [event.pathParameters.id], function(err, result) {
                if (err) throw err;
                return callback(null, { success: true, affectedRows: result.affectedRows });
            });
            break;
            
        case 'GET /medication/{user_id}':
            // Gets all of the user's medications.
            // Also returns the medication's id based on the above filter.
            sql = "SELECT * FROM `health_service`.`medication` WHERE user_id=?;";
            con.query(sql, [event.pathParameters.user_id], function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;
            
        case 'POST /medication':
            // Adds new medications into the database.
            body = JSON.parse(event.body)
            sql = "INSERT INTO `health_service`.`medication` (`user_id`, `time_id`, `name`, `type`, `measure_amount`, `measure_unit`, `frequency`) VALUES (?, ?, ?, ?, ?, ?, ?);";
            con.query(sql, [body.user_id, body.time_id, body.name, body.type, body.measure_amount, body.measure_unit, body.frequency], function (err, result){
                if (err) throw err;
                return callback(null, { success: true, insertId: result.insertId });
            });
            break;
            
        case 'PUT /medication/{id}':
            // Edit medication based on the obtained medication_id.
            body = JSON.parse(event.body)
            sql = "UPDATE `health_service`.`medication` SET `time_id` = ?, `name` = ?, `type` = ?, `measure_amount` = ?, `measure_unit` = ?, `frequency` = ? WHERE id=?;";
            con.query(sql, [body.time_id, body.name, body.type, body.measure_amount, body.measure_unit, body.frequency, event.pathParameters.id], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, changedRows: result.changedRows });
            });
            break;
        
        case 'DELETE /medication/{id}':
            // Delete medication based on the obtained medication_id.
            sql = "DELETE FROM `health_service`.`medication` WHERE id=?;";
            con.query(sql, [event.pathParameters.id], function(err, result) {
                if (err) throw err;
                return callback(null, { success: true, affectedRows: result.affectedRows });
            });
            break;
            
        case 'GET /category':
            // Gets all categories.
            sql = "SELECT * FROM `health_service`.`category`;";
            con.query(sql, function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;
            
        case 'GET /time':
            // Gets all time slots.
            sql = "SELECT * FROM `health_service`.`time`";
            con.query(sql, function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;
            
        default:
            throw new Error("Unsupported route: " + event.routeKey);
    }
};