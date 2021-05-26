package com.ynz.demo.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnFactory {

    public static Connection createPostgresSqlConn(String host, String port, String user, String password, String database) {
        Connection conn = null;

        Properties dbProperties = new Properties();
        dbProperties.put("user", user);
        dbProperties.put("password", password);

        String url = new StringBuilder("jdbc:postgresql://")
                .append(host).append(":").append(port).append("/").append(database).toString();

        try {
            conn = DriverManager.getConnection(url, dbProperties);
        } catch (SQLException e) {
            System.err.println(e);
        }

        return conn;
    }

    public static Connection defaultPostgresSqlConn() {
        return createPostgresSqlConn("localhost", "5435", "postgres", "test", "postgres");
    }

}
