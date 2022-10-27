package com.majorproject.model;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.sql.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RatesDbTest
{
    private static final String dbName = "test.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    private void deleteTestDb()
    {
        File dbFile = new File(dbName);
        if (dbFile.exists())
        {
            boolean result = dbFile.delete();

            if(!result)
                System.out.println("Could not delete DB.");
        }
    }

    /** Test whether a file and a database inside it were created. */
    @Test
    public void createDb() throws Exception
    {
        deleteTestDb();

        RatesDb.createDb(dbName);

        File file = new File(dbName);
        assertThat(file.exists(), is(true));
        assertThat(file.getName(), is(dbName));

        try(Connection connection = DriverManager.getConnection(dbURL))
        {
            Statement statement = connection.createStatement();

            String checkTableSQL = "SELECT name FROM sqlite_master " +
                    "WHERE type='table' AND name='rates';";
            boolean exists = statement.execute(checkTableSQL);
            assertThat(exists, is(true));
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }

        deleteTestDb();
    }

    /** Test whether the file really is removed. */
    @Test
    public void removeDb() throws Exception
    {
        RatesDb.createDb(dbName);
        RatesDb.removeDb();

        File file = new File(dbName);
        assertThat(file.exists(), is(false));
    }

    /** Test whether the data was correctly inserted for when both overwriting and not,
     * and if an error shows when duplicating a primary key.
     */
    @Test
    public void insertRateDb() throws Exception
    {
        deleteTestDb();

        RatesDb.createDb(dbName);
        RatesDb.insertRateDb("ABCDEF", 1.0, false);

        try(Connection connection = DriverManager.getConnection(dbURL))
        {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select * from rates where currenciesConcat = 'ABCDEF'");
            assertThat(result.getString("rate"), is("1.0"));
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }

        RatesDb.insertRateDb("ABCDEF", 2.0, true);

        try(Connection connection = DriverManager.getConnection(dbURL))
        {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select * from rates where currenciesConcat = 'ABCDEF'");
            assertThat(result.getString("rate"), is("2.0"));
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }

        assertThrows(Exception.class, () -> RatesDb.insertRateDb("ABCDEF", 2.0, false));

        deleteTestDb();
    }

    /** Tests whether . */
    @Test
    public void checkRateInDb() throws Exception
    {
        deleteTestDb();
        RatesDb.createDb(dbName);
        RatesDb.insertRateDb("ABCDEF", 1.0, false);

        assertThat(RatesDb.checkRateInDb("ABCDEF"), is(true));
        assertThat(RatesDb.checkRateInDb("abcdef"), is(false));
        assertThat(RatesDb.checkRateInDb("ABC DEF"), is(false));

        deleteTestDb();
    }


    @Test
    public void getRateDb() throws Exception
    {
        deleteTestDb();
        RatesDb.createDb(dbName);

        RatesDb.insertRateDb("ABCDEF", 1.0, false);

        assertThat(RatesDb.getRateDb("ABCDEF"), is("1.0"));

        assertThrows(Exception.class,() -> RatesDb.getRateDb(""));

        deleteTestDb();
    }
}
