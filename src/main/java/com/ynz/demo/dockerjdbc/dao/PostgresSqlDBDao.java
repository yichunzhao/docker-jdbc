package com.ynz.demo.dockerjdbc.dao;

import com.ynz.demo.dockerjdbc.domain.Person;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;

import java.sql.Connection;
import java.sql.ResultSet;
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
    public Person findById(Integer id) {
        Person found = null;
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("id", String.valueOf(id));

        String sql = "select * from Person p where p.personId = {id}";
        String query = replaceQueryParameters(placeholderMap, sql);

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int personId = resultSet.getInt("personId");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                found = new Person(personId, firstName, lastName);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }

        return found;
    }

    @Override
    public int insert(@NonNull Person person) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("personId", String.valueOf(person.getPersonId()));
        placeholderMap.put("firstName", singleQuotedString(person.getFirstName()));
        placeholderMap.put("lastName", singleQuotedString(person.getLastName()));

        String sql = "insert into Person(personId, firstName, lastName) values({personId}, {firstName}, {lastName})";

        StringSubstitutor sub = new StringSubstitutor(placeholderMap, "{", "}");
        String query = sub.replace(sql);

        int result = -1;

        try (Statement statement = conn.createStatement()) {
            result = statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error(e.toString());
        }

        return result;
    }

    @Override
    public Person update(Integer id, Person pPerson) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("personId", String.valueOf(id));
        placeholderMap.put("firstName", super.singleQuotedString(pPerson.getFirstName()));
        placeholderMap.put("lastName", super.singleQuotedString(pPerson.getLastName()));

        String sql = "update Person set firstName = {firstName}, lastName={lastName} where personId = {personId}";

        Person updatedPerson = null;

        String query = super.replaceQueryParameters(placeholderMap, sql);
        try (Statement statement = super.conn.createStatement()) {
            int ret = statement.executeUpdate(query);

            log.info("update : return {} \n", ret);
            if (ret == 1) {
                updatedPerson = Person.builder()
                        .firstName(pPerson.getFirstName()).lastName(pPerson.getLastName())
                        .personId(pPerson.getPersonId()).build();
            }
        } catch (SQLException e) {
            log.error("update table: ", e);
        }

        return updatedPerson;
    }

    @Override
    public void deleteById(Integer id) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("id", String.valueOf(id));

        String sql = "delete from Person where personId = {id}";
        String query = super.replaceQueryParameters(placeholderMap, sql);

        try (Statement statement = super.conn.createStatement()) {
            int ret = statement.executeUpdate(query);
            log.info("deleteById ret : {} \n", ret);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

}
