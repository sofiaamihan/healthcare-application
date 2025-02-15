const mysql = require('mysql');
const con = mysql.createConnection({
    host : "discover-service.cvewtgj0uqha.us-east-1.rds.amazonaws.com",
    user : "admin",
    password : "12345#Password",
    port : "3306",
    database : "discover_service",
});

exports.handler = (event, context, callback) => {

    let sql;
    context.callbackWaitsForEmptyEventLoop = false;

    switch (event.routeKey){

        case 'GET /content':
            // Gets all content.
            sql = "SELECT * FROM `discover_service`.`content`;";
            con.query(sql, function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;
            
        case 'GET /content-category':
            // Gets all content.
            sql = "SELECT * FROM `discover_service`.`content_category`;";
            con.query(sql, function(err, result){
                if (err) throw err;
                return callback(null, result);
            });
            break;

        case 'POST /content':
            // Adds new content into the database.
            body = JSON.parse(event.body)
            sql = "INSERT INTO `discover_service`.`content` (`content_category_id`, `title`, `summary`, `description`, `picture`) VALUES (?, ?, ?, ?, ?);";
            con.query(sql, [body.content_category_id, body.title, body.summary, body.description, body.picture], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, insertId: result.insertId });
            });
            break;

        case 'PUT /content/{id}':
            // Edit the details of one content.
            body = JSON.parse(event.body)
            sql = "UPDATE `discover_service`.`content` SET `content_category_id` = ?, `title` = ?, `summary` = ?, `description` = ?, `picture` = ? WHERE id=?;";
            con.query(sql, [body.content_category_id, body.title, body.summary, body.description, body.picture, event.pathParameters.id], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, changedRows: result.changedRows });
            });
            break;

        case 'DELETE /content/{id}':
            // Deletes one content.
            sql = "DELETE FROM `discover_service`.`content` WHERE id=?;";
            con.query(sql, [event.pathParameters.id], function(err, result){
                if (err) throw err;
                return callback(null, { success: true, affectedRows: result.affectedRows });
            });
            break;

        default:
            throw new Error("Unsupported route: " + event.routeKey);
    }
};