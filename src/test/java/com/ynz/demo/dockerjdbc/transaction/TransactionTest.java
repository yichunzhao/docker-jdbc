package com.ynz.demo.dockerjdbc.transaction;


import com.ynz.demo.dockerjdbc.conn.DatabaseConnFactory;
import com.ynz.demo.dockerjdbc.tables.TableManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class TransactionTest {
    private static final String ACC_SQL = "CREATE TABLE ACCOUNTS(ACCT_NO INT NOT NULL PRIMARY KEY, ACCT_NAME VARCHAR(50) NOT NULL, BALANCE DECIMAL)";
    private static final String TRANS_SQL =
            "CREATE TABLE TRANSACTIONS(TRANS_ID INT NOT NULL PRIMARY KEY, ACCT_NO INT NOT NULL, TYPE VARCHAR(50), AMOUNT DECIMAL NOT NULL, TRANS_DATE DATE, FOREIGN KEY(ACCT_NO) REFERENCES ACCOUNTS(ACCT_NO))";

    private static Connection conn;

    @BeforeAll
    public static void prepareTables() {
        conn = DatabaseConnFactory.defaultPostgresSqlConn();

        TableManager tableManager = new TableManager(DatabaseConnFactory.defaultPostgresSqlConn());

        tableManager.createTable(ACC_SQL);

        tableManager.createTable(TRANS_SQL);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("insert into accounts values(5555, 'Selvan Rajan', 999.0)");
            stmt.executeUpdate("insert into accounts values(7777, 'Paul Rosenthal', 100.0)");
        } catch (SQLException e) {
            log.error("Preparing table: ", e);
        }

    }

    @Test
    @DisplayName("moving from 5555 to 7777")
    void TransferMoneyFromOneAccountTwoAnotherInOneTransaction() {
        //consisting of several steps
        try {
            //start a transaction by disabling the auto-commit on the connection.
            conn.setAutoCommit(false);

            Statement statement = conn.createStatement();

            //insert row 1 in the table transaction
            statement.executeUpdate("insert into transactions values(1, 5555, 'debit', 55.0, '2000-01-02')");

            //insert row 2 in the table transaction
            statement.executeUpdate("insert into transactions values(2, 7777,'credit', 55.0, '2000-01-02')");

            //debit 55.0 from account 5555
            statement.executeUpdate("update accounts set balance = 944.0 where acct_no=5555");

            //credit 55.0 on account 77777
            statement.executeUpdate("update accounts set balance = 155.0 where acct_no=7777");

            //the end of transaction;
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            log.error("trans-money transaction", e);

            //if any exception, rolling back.
            try {
                conn.rollback();
            } catch (SQLException ex) {
                log.error("during rolling back transaction: ", e);
            }
        }

        //checking both accounts
        double balance_5555 = 0;
        double balance_7777 = 0;
        try (Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery("select a.balance from accounts a where acct_no=5555");

            while (result.next()) {
                balance_5555 = result.getDouble("balance");
            }

            ResultSet result1 = statement.executeQuery("select a.balance from accounts a where acct_no=7777");
            while (result1.next()) {
                balance_7777 = result1.getDouble("balance");
            }

        } catch (SQLException e) {
            log.error("accessing accounts", e);
        }

        assertEquals(944.0, balance_5555);
        assertEquals(155.0, balance_7777);
    }

    @AfterAll
    public static void tearDown() {
        log.info("......... after-all ......... ");
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("drop table transactions");
            stmt.executeUpdate("drop table accounts");
        } catch (SQLException e) {
            log.error("drop accounts and transactions tables: ", e);
        }
    }

}
