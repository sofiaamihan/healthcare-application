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

describe('GET /info/{nric}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return a User based on NRIC', async () => {
        const mockUser = {
            id: 1,
            nric: "S1234567A",
            role: "user",
            age: 25,
            gender: "M",
            weight: 70,
            height: 170
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, [mockUser]);
        });

        const event = { routeKey: 'GET /info/{nric}', pathParameters: { nric: 'S1234567A' } };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `health_service`.`user` WHERE nric=?;",
            ['S1234567A'],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockUser);
    });
});

describe('POST /user', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should insert a new User and return a Success Response', async () => {
        const mockInsertResponse = { insertId: 1 };
        const newUser = {
            nric: "S1234567A",
            role: "user",
            age: 25,
            gender: "M",
            weight: 70,
            height: 170
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockInsertResponse);
        });

        const event = { routeKey: 'POST /user', body: JSON.stringify(newUser) };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "INSERT INTO `health_service`.`user` (`nric`, `role`, `age`, `gender`, `weight`, `height`) VALUES (?, ?, ?, ?, ?, ?);",
            ["S1234567A", "user", 25, "M", 70, 170],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, insertId: 1 });
    });
});

describe('PUT /user/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should update User measurements and return a Success Response', async () => {
        const mockUpdateResponse = { changedRows: 1 };
        const updatedUser = {
            age: 26,
            gender: "M",
            weight: 72,
            height: 172
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockUpdateResponse);
        });

        const event = {
            routeKey: 'PUT /user/{id}',
            body: JSON.stringify(updatedUser),
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "UPDATE `health_service`.`user` SET `age` = ?, `gender` = ?, `weight` = ?, `height` = ? WHERE id=?;",
            [26, "M", 72, 172, "1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, changedRows: 1 });
    });
});

describe('DELETE /user/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should delete a User and return a Success Response', async () => {
        const mockDeleteResponse = { affectedRows: 1 };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockDeleteResponse);
        });

        const event = {
            routeKey: 'DELETE /user/{id}',
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "DELETE FROM `health_service`.`user` WHERE id=?;",
            ["1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, affectedRows: 1 });
    });
});

describe('GET /activity/{user_id}/{day}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return a list of Activities for a User on a specific Day', async () => {
        const mockActivities = [
            { id: 1, user_id: "1", time_taken: "2025-02-15 10:00:00", calories_burnt: 100, step_count: 2000, distance: 2, walking_speed: 4, walking_steadiness: 0.8 },
            { id: 2, user_id: "1", time_taken: "2025-02-15 12:00:00", calories_burnt: 150, step_count: 3000, distance: 3, walking_speed: 4.5, walking_steadiness: 0.9 }
        ];

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockActivities);
        });

        const event = { routeKey: 'GET /activity/{user_id}/{day}', pathParameters: { user_id: '1', day: '2025-02-15' } };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `health_service`.`activity` WHERE user_id=? AND DATE(time_taken)=?;",
            ['1', '2025-02-15'],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockActivities);
    });
});

describe('POST /activity', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should insert a new Activity and return a Success Response', async () => {
        const mockInsertResponse = { insertId: 1 };
        const newActivity = {
            user_id: "1",
            category_id: 2,
            time_taken: "2025-02-15 14:00:00",
            calories_burnt: 200,
            step_count: 4000,
            distance: 4,
            walking_speed: 5,
            walking_steadiness: 0.95
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockInsertResponse);
        });

        const event = { routeKey: 'POST /activity', body: JSON.stringify(newActivity) };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "INSERT INTO `health_service`.`activity` (`user_id`, `category_id`, `time_taken`, `calories_burnt`, `step_count`, `distance`, `walking_speed`, `walking_steadiness`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
            ["1", 2, "2025-02-15 14:00:00", 200, 4000, 4, 5, 0.95],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, insertId: 1 });
    });
});

describe('PUT /activity/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should update Activity details and return a Success Response', async () => {
        const mockUpdateResponse = { changedRows: 1 };
        const updatedActivity = {
            time_taken: "2025-02-15 16:00:00",
            calories_burnt: 250,
            step_count: 5000,
            distance: 5,
            walking_speed: 5.5,
            walking_steadiness: 0.97
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockUpdateResponse);
        });

        const event = {
            routeKey: 'PUT /activity/{id}',
            body: JSON.stringify(updatedActivity),
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "UPDATE `health_service`.`activity` SET `time_taken` = ?, `calories_burnt` = ?, `step_count` = ?, `distance` = ?, `walking_speed` = ?, `walking_steadiness` = ? WHERE id=?;",
            ["2025-02-15 16:00:00", 250, 5000, 5, 5.5, 0.97, "1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, changedRows: 1 });
    });
});

describe('DELETE /activity/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should delete an Activity and return a Success Response', async () => {
        const mockDeleteResponse = { affectedRows: 1 };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockDeleteResponse);
        });

        const event = {
            routeKey: 'DELETE /activity/{id}',
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "DELETE FROM `health_service`.`activity` WHERE id=?;",
            ["1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, affectedRows: 1 });
    });
});

describe('GET /medication/{user_id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return all Medications for a User', async () => {
        const mockMedications = [
            { id: 1, user_id: "1", name: "Med1", type: "Capsule", measure_amount: 10, measure_unit: "mg", frequency: "Daily" },
            { id: 2, user_id: "1", name: "Med2", type: "Liquid", measure_amount: 20, measure_unit: "mg", frequency: "Weekly" }
        ];

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockMedications);
        });

        const event = { routeKey: 'GET /medication/{user_id}', pathParameters: { user_id: '1' } };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `health_service`.`medication` WHERE user_id=?;",
            ['1'],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockMedications);
    });
});

describe('POST /medication', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should insert a new Medication and return a Success Response', async () => {
        const mockInsertResponse = { insertId: 1 };
        const newMedication = {
            user_id: "1",
            time_id: "time1",
            name: "Med1",
            type: "Type1",
            measure_amount: 10,
            measure_unit: "mg",
            frequency: "once"
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockInsertResponse);
        });

        const event = { routeKey: 'POST /medication', body: JSON.stringify(newMedication) };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "INSERT INTO `health_service`.`medication` (`user_id`, `time_id`, `name`, `type`, `measure_amount`, `measure_unit`, `frequency`) VALUES (?, ?, ?, ?, ?, ?, ?);",
            ["1", "time1", "Med1", "Type1", 10, "mg", "once"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, insertId: 1 });
    });
});

describe('PUT /medication/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should update specified Medication details and return a Success Response', async () => {
        const mockUpdateResponse = { changedRows: 1 };
        const updatedMedication = {
            time_id: "time2",
            name: "Med2",
            type: "Type2",
            measure_amount: 20,
            measure_unit: "mg",
            frequency: "twice"
        };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockUpdateResponse);
        });

        const event = {
            routeKey: 'PUT /medication/{id}',
            body: JSON.stringify(updatedMedication),
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "UPDATE `health_service`.`medication` SET `time_id` = ?, `name` = ?, `type` = ?, `measure_amount` = ?, `measure_unit` = ?, `frequency` = ? WHERE id=?;",
            ["time2", "Med2", "Type2", 20, "mg", "twice", "1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, changedRows: 1 });
    });
});

describe('DELETE /medication/{id}', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should delete a Medication and return a Success Response', async () => {
        const mockDeleteResponse = { affectedRows: 1 };

        mysql.createConnection().query.mockImplementation((sql, values, callback) => {
            callback(null, mockDeleteResponse);
        });

        const event = {
            routeKey: 'DELETE /medication/{id}',
            pathParameters: { id: '1' }
        };

        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "DELETE FROM `health_service`.`medication` WHERE id=?;",
            ["1"],
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, { success: true, affectedRows: 1 });
    });
});

describe('GET /category', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return all Categories', async () => {
        const mockCategories = [
            { id: 1, name: "Walking" },
            { id: 2, name: "Running" }
        ];

        mysql.createConnection().query.mockImplementation((sql, callback) => {
            callback(null, mockCategories);
        });

        const event = { routeKey: 'GET /category' };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `health_service`.`category`;",
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockCategories);
    });
});

describe('GET /time', () => {
    let callback;
    let context;

    beforeEach(() => {
        callback = jest.fn();
        context = { callbackWaitsForEmptyEventLoop: false };
    });

    it('Should return all Time Slots', async () => {
        const mockTimes = [
            { id: 1, time_slot: "09:00" },
            { id: 2, time_slot: "10:00" }
        ];

        mysql.createConnection().query.mockImplementation((sql, callback) => {
            callback(null, mockTimes);
        });

        const event = { routeKey: 'GET /time' };
        lambda.handler(event, context, callback);

        expect(mysql.createConnection().query).toHaveBeenCalledWith(
            "SELECT * FROM `health_service`.`time`",
            expect.any(Function)
        );
        expect(callback).toHaveBeenCalledWith(null, mockTimes);
    });
});


