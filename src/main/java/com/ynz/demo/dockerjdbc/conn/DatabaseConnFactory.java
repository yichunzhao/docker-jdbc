package com.ynz.demo.dockerjdbc.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnFactory {

    public static Connection createPostgresSqlConn(String host, String port, String user, String password, String databaseName) {
        Connection conn = null;

        Properties dbProperties = new Properties();
        dbProperties.put("user", user);
        dbProperties.put("password", password);

        String url = getUrl("postgresql", host, port, databaseName);

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

    public static String getUrl(String subProtocol, String host, String port, String databaseName) {
        return new StringBuilder("jdbc").append(":").append(subProtocol).append("://").append(host).append(":").append(port).append("/").append(databaseName).toString();
    }

}
