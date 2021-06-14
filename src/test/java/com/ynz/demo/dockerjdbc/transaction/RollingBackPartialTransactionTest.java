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
import java.sql.Savepoint;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Rolling back partial transaction
 * Creating savingPoints within a transaction;
 * and then rolling back partial transactions from the bottom to a savingPoint.
 */
@Slf4j
public class RollingBackPartialTransactionTest {

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
            stmt.executeUpdate("insert into accounts values(5555, 'Selvan Rajan', 1000.0)");
            stmt.executeUpdate("insert into accounts values(7777, 'Paul Rosenthal', 10000.0)");
            stmt.executeUpdate("insert into accounts values(9999, 'Bruce Lee', 100.0)");
        } catch (SQLException e) {
            log.error("Preparing tables: ", e);
        }

    }

    @Test
    @DisplayName("creating savingPoints and roll back partial trans")
    void TransferMoneyFromOneAccountTwoAnotherInOneTransaction() {
        //consisting of several steps
        try {
            //start a transaction by disabling the auto-commit on the connection.
            conn.setAutoCommit(false);

            Statement statement = conn.createStatement();

            statement.executeUpdate("insert into transactions values(1, 5555, 'cr', 2000.0, '2000-01-02')");
            statement.executeUpdate("update accounts set balance = 3000.0 where acct_no=5555");
            Savepoint s1 = conn.setSavepoint();

            statement.executeUpdate("insert into transactions values(2, 7777,'cr', 20000.0, '2000-01-02')");
            statement.executeUpdate("update accounts set balance = 40000.0 where acct_no=7777");
            Savepoint s2 = conn.setSavepoint();

            statement.executeUpdate("insert into transactions values(3, 9999,'cr', 900.0, '2000-01-02')");
            statement.executeUpdate("update accounts set balance = 1000.0 where acct_no=9999");
            Savepoint s3 = conn.setSavepoint();

            //rolling back transactions to savingPoint 2
            conn.rollback(s2);

            //saving the rest transactions, i.e. the ones above s2
            conn.commit();

            conn.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("trans-money transaction", e);

            //if any exception, rolling back.
            try {
                //roll back transactions.
                conn.rollback();
            } catch (SQLException ex) {
                log.error("during rolling back transaction: ", e);
            }
        }

        //checking both accounts
        double balance5555 = 0D;
        double balance7777 = 0D;
        double balance9999 = 0D;
        try (Statement statement = conn.createStatement()) {
            ResultSet result5555 = statement.executeQuery("select a.balance from accounts a where acct_no=5555");

            while (result5555.next()) {
                balance5555 = result5555.getDouble("balance");
            }

            ResultSet result7777 = statement.executeQuery("select a.balance from accounts a where acct_no=7777");
            while (result7777.next()) {
                balance7777 = result7777.getDouble("balance");
            }

            ResultSet result9999 = statement.executeQuery("select a.balance from accounts a where acct_no=9999");
            while (result9999.next()) {
                balance9999 = result9999.getDouble("balance");
            }

        } catch (SQLException e) {
            log.error("accessing accounts", e);
        }

        assertEquals(3_000D, balance5555);
        assertEquals(40_000D, balance7777);
        assertEquals(100D, balance9999);
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
