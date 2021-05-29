package com.ynz.demo.dockerjdbc.dao;

import com.ynz.demo.dockerjdbc.conn.DatabaseConnFactory;
import com.ynz.demo.dockerjdbc.domain.Person;
import lombok.extern.slf4j.Slf4j;
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
    void findById() {
        Person found = crudeMethods.findById(101);

        assertAll(
                () -> assertNotNull(found),
                () -> assertThat(found.getFirstName(), is("Tom"))
        );
    }

    @Test
    void insertOneRaw_ThenOneRowImpacted() {
        Person person = Person.builder().firstName("Mike").lastName("Zhao").personId(100).build();

        int result = crudeMethods.insert(person);
        log.info("insert result: {} \n", result);
        assertEquals(1, result);
    }

    @Test
    void insertAnotherEntry_OneRowHavingBeenImpacted() {
        Person person = Person.builder().firstName("Tom").lastName("Bruce").personId(101).build();
        int result = crudeMethods.insert(person);
        log.info("insert result: {} \n", result);
        assertEquals(1, result);
    }

    @Test
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
    void whenUpdateUnExistedPerson_ItReturnsNull() {
        Person found = crudeMethods.findById(200);
        assertNull(found);

        Person pPerson = Person.builder().firstName("xyz").lastName("opq").build();

        Person updatedPerson = crudeMethods.update(200, pPerson);
        assertNull(updatedPerson);
    }

    @Test
    void deleteById() {
        crudeMethods.deleteById(100);

        Person found = crudeMethods.findById(100);
        assertNull(found);
    }

}