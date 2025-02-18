const mysql = require('mysql');
const lambda = require('../index'); 

jest.mock('mysql', () => {
    const mQuery = jest.fn();
    const mConnection = {
        query: mQuery,
        connect: jest.fn(),
        end: jest.fn(),
    };
    return {
        createConnection: jest.fn(() => mConnection),
    };
});

describe('GET /content', () => {
    let callback;
    let context;
    
    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return a list of Content', async () => {
        const mockData = [
            {
                id: 2,
                content_category_id: 2,
                title: "HIIT",
                summary: "Home HIIT Workouts",
                description: "10min Home HIIT Workouts.",
                picture: { type: "Buffer", data: [112, 105, 99] },
            },
        ];

        mysql.createConnection().query.mockImplementation((sql, callback) => {
            callback(null, mockData);
        });

        const event = { routeKey: 'GET /content' };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `discover_service`.`content`;",
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockData);
    });

});

describe('GET /content-category', () => {
    let callback;
    let context;
    
    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return a list of Content Categories', async () => {
        const mockCategories = [
            { id: 1, name: "Diet" },
            { id: 2, name: "Workout" }
        ];

        mysql.createConnection().query.mockImplementation((sql, callback) => {
            callback(null, mockCategories);
        });

        const event = { routeKey: 'GET /content-category' };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `discover_service`.`content_category`;",
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockCategories);
    });

});

describe('POST /content', () => {
    let callback;
    let context;
    
    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should insert new Content and return a Success Response', async () => {
        const mockInsertResponse = { insertId: 6 };
        const newContent = {
            content_category_id: 1,
            title: "Keto 2",
            summary: "Keto Diet",
            description: "Meal-Prep Ideas for Keto Diets",
            picture: "not working"
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockInsertResponse);
        });

        const event = { routeKey: 'POST /content', body: JSON.stringify(newContent) };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "INSERT INTO `discover_service`.`content` (`content_category_id`, `title`, `summary`, `description`, `picture`) VALUES (?, ?, ?, ?, ?);",
            [1, "Keto 2", "Keto Diet", "Meal-Prep Ideas for Keto Diets", "not working"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, insertId: 6 });
    });

});

describe('PUT /content/{id}', () => {
    let callback;
    let context;
    
    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should update the specified Content and return a Success Response', async () => {
        const mockUpdateResponse = { changedRows: 1 };

        const updatedContent = {
            content_category_id: 1,
            title: "Keto",
            summary: "Keto Dieting",
            description: "Meal-Prep Ideas for Keto Diets",
            picture: "not working"
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockUpdateResponse);
        });

        const event = {
            routeKey: 'PUT /content/{id}',
            body: JSON.stringify(updatedContent),
            pathParameters: { id: '6' } 
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "UPDATE `discover_service`.`content` SET `content_category_id` = ?, `title` = ?, `summary` = ?, `description` = ?, `picture` = ? WHERE id=?;",
            [1, "Keto", "Keto Dieting", "Meal-Prep Ideas for Keto Diets", "not working", "6"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, changedRows: 1 });
    });

});

describe('DELETE /content/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should delete the specified Content and return a Success Response', async () => {
        const mockDeleteResponse = { affectedRows: 1 };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockDeleteResponse);
        });

        const event = {
            routeKey: 'DELETE /content/{id}',
            pathParameters: { id: '6' } // Simulate an ID value
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "DELETE FROM `discover_service`.`content` WHERE id=?;",
            ["6"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, affectedRows: 1 });
    });
});
