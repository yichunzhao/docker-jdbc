package com.ynz.demo.conn;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseConnFactoryTest {

    @Test
    void testVCreatePostgresSqlConn() {
        Connection con = DatabaseConnFactory.createPostgresSqlConn("localhost", "5435", "postgres", "test", "postgres");
        assertNotNull(con);
    }

    @Test
    void testCreateDefaultPostgresSqlConn() {
        Connection con = DatabaseConnFactory.defaultPostgresSqlConn();
        assertNotNull(con);
    }

}