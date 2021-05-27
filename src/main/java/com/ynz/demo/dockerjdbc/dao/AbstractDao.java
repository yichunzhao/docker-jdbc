package com.ynz.demo.dockerjdbc.dao;

import org.apache.commons.text.StringSubstitutor;

import java.sql.Connection;
import java.util.Map;

public abstract class AbstractDao {
    protected Connection conn;

    public AbstractDao(Connection conn) {
        this.conn = conn;
    }

    protected String singleQuotedString(String decoratedString) {
        return new StringBuilder("'").append(decoratedString).append("'").toString();
    }

    protected String replaceQueryParameters(Map<String, String> queryParameters, String sqlWithPlaceHolders) {
        StringSubstitutor sub = new StringSubstitutor(queryParameters, "{", "}");
        return sub.replace(sqlWithPlaceHolders);
    }

}
