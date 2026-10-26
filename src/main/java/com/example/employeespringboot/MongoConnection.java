
package com.example.employeespringboot;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "employee_db";

    private static final MongoClient CLIENT = MongoClients.create(URI);

    public static MongoDatabase getDatabase() {
        return CLIENT.getDatabase(DATABASE_NAME);
    }
}

