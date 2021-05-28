package com.ynz.demo.dockerjdbc.conn;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseConnFactory {

    public static Connection createPostgresSqlConn(String host, String port, String user, String password, String databaseName) {
        Connection con = null;

        Properties dbProperties = new Properties();
        dbProperties.put("user", user);
        dbProperties.put("password", password);

        String url = getUrl("postgresql", host, port, databaseName);

        try {
            con = DriverManager.getConnection(url, dbProperties);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return con;
    }

    public static Connection defaultPostgresSqlConn() {
        return createPostgresSqlConn("localhost", "5435", "postgres", "test", "postgres");
    }

    public static String getUrl(String subProtocol, String host, String port, String databaseName) {
        return new StringBuilder("jdbc").append(":").append(subProtocol).append("://").append(host).append(":").append(port).append("/").append(databaseName).toString();
    }

}
