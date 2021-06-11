package com.ynz.demo.dockerjdbc.dao;

import com.ynz.demo.dockerjdbc.conn.DatabaseConnFactory;
import com.ynz.demo.dockerjdbc.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
class PostgresSqlDBDaoTest {
    private CrudeMethods<Person, Integer> crudeMethods;

    public PostgresSqlDBDaoTest() {
        this.crudeMethods = new PostgresSqlDBDao(DatabaseConnFactory.defaultPostgresSqlConn());
    }

    @Test
    @Order(1)
    void insertOneRaw_ThenOneRowImpacted() {
        Person person = Person.builder().firstName("Mike").lastName("Zhao").personId(100).build();

        int result = crudeMethods.insert(person);
        log.info("insert result: {} \n", result);
        assertEquals(1, result);
    }

    @Test
    @Order(2)
    void insertAnotherEntry_OneRowHavingBeenImpacted() {
        Person person = Person.builder().firstName("Tom").lastName("Bruce").personId(101).build();
        int result = crudeMethods.insert(person);
        log.info("insert result: {} \n", result);
        assertEquals(1, result);
    }

    @Test
    @Order(7)
    void findById() {
        Person found = crudeMethods.findById(101);

        assertAll(
                () -> assertNotNull(found),
                () -> assertThat(found.getFirstName(), is("Tom"))
        );
    }

    @Test
    @Order(4)
    void update() {
        Person pPerson = Person.builder().firstName("xyz").lastName("opq").build();

        Person updatedPerson = crudeMethods.update(100, pPerson);
        assertAll(
                () -> assertNotNull(updatedPerson),
                () -> assertThat(updatedPerson.getFirstName(), is("xyz")),
                () -> assertThat(updatedPerson.getLastName(), is("opq"))
        );
    }

    @Test
    @Order(5)
    void whenUpdateUnExistedPerson_ItReturnsNull() {
        Person found = crudeMethods.findById(200);
        assertNull(found);

        Person pPerson = Person.builder().firstName("xyz").lastName("opq").build();

        Person updatedPerson = crudeMethods.update(200, pPerson);
        assertNull(updatedPerson);
    }

    @Test
    @Order(6)
    void deleteById() {
        crudeMethods.deleteById(100);
        Person found = crudeMethods.findById(100);
        assertNull(found);

        crudeMethods.deleteById(101);
        Person pFound = crudeMethods.findById(101);
        assertNull(pFound);
    }

}