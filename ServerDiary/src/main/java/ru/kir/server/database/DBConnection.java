package ru.kir.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Kirill Zhitelev on 28.10.2015.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/study";
    private static final String LOGIN = "root";
    private static final String PASS = "9181203w";

    private Connection connection;
    private static DBConnection dbConnection;

    private DBConnection() {}

    public static DBConnection getDbConnection() {
        if(dbConnection == null)
            dbConnection = new DBConnection();

        return dbConnection;
    }

    public Connection connectionToBase() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
