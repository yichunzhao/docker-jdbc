package com.ynz.demo.dockerjdbc.tables;

import com.ynz.demo.dockerjdbc.conn.DatabaseConnFactory;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TableManagerTest {

    private TableManager manager;

    public TableManagerTest() {
        manager = new TableManager(DatabaseConnFactory.defaultPostgresSqlConn());
    }

    @Test
    void createTable() {
        String sql = "CREATE TABLE Person(PersonId int, LastName varchar(255), FirstName varchar(255))";
        int result = manager.createTable(sql);
        assertThat(result, is(not(-1)));
    }

}