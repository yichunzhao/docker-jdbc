package com.ynz.demo.dockerjdbc.conn;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void givenSubProtocolHostPortDatabaseName_ReturnsValidUrl() {
        String expected = "jdbc:postgresql://localhost:5435/postgres";
        String actual = DatabaseConnFactory.getUrl("postgresql", "localhost", "5435", "postgres");
        assertEquals(expected, actual);
    }

}