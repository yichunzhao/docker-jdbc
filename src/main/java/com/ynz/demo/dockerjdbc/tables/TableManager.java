package com.ynz.demo.dockerjdbc.tables;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class TableManager {
    private Connection conn;

    public TableManager(Connection conn) {
        this.conn = conn;
    }

    public int createTable(String sql) {

        log.info("create table: " + sql);

        int result = -1;
        try {
            Statement statement = this.conn.createStatement();
            result = statement.executeUpdate(sql);
            log.info("create table result: " + result);
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }

        return result;
    }

}
