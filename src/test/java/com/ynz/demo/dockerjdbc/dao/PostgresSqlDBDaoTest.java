package com.ynz.demo.dockerjdbc.dao;

import com.ynz.demo.dockerjdbc.conn.DatabaseConnFactory;
import com.ynz.demo.dockerjdbc.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PostgresSqlDBDaoTest {
    private CrudeMethods crudeMethods;

    public PostgresSqlDBDaoTest() {
        this.crudeMethods = new PostgresSqlDBDao(DatabaseConnFactory.defaultPostgresSqlConn());
    }

    @Test
    void findById() {
    }

    @Test
    void insert() {
        Person person = Person.builder().firstName("Mike").lastName("Zhao").personId(100).build();
        int result = crudeMethods.insert(person);
        log.info("insert result: %d \n", result);
        assertEquals(1, result);
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

}