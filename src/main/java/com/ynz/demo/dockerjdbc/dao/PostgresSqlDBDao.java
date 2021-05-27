package com.ynz.demo.dockerjdbc.dao;

import com.ynz.demo.dockerjdbc.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PostgresSqlDBDao extends AbstractDao implements CrudeMethods<Person, Integer> {

    public PostgresSqlDBDao(Connection conn) {
        super(conn);
    }

    @Override
    public Person findById(Integer integer) {
        return null;
    }

    @Override
    public int insert(Person person) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("personId", String.valueOf(person.getPersonId()));
        placeholderMap.put("firstName", "'" + person.getFirstName() + "'");
        placeholderMap.put("lastName", "'" + person.getLastName() + "'");

        String sql = "insert into Person values({personId}, {firstName}, {lastName})";

        StringSubstitutor sub = new StringSubstitutor(placeholderMap, "{", "}");
        String query = sub.replace(sql);

        int result = -1;

        try {
            Statement statement = conn.createStatement();
            result = statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error(e.toString());
        }

        return result;
    }

    @Override
    public Person update(Integer integer, Person entity) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }

}
