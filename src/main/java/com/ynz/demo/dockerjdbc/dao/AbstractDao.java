package com.ynz.demo.dockerjdbc.dao;

import java.sql.Connection;

public abstract class AbstractDao {
    protected Connection conn;

    public AbstractDao(Connection conn) {
        this.conn = conn;
    }

}
