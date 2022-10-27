package com.majorproject.model;

import java.io.File;
import java.sql.*;

public class RatesDb
{
    private static String dbName;
    private static String dbURL;

    /**
     * Creates the file and database to store rates if needed.
     * Primary key is the from and to currencies concatenated; each key is linked to a rate.
     */
    public static void createDb(String name) throws Exception
    {
        dbName = name;

        dbURL = "jdbc:sqlite:" + dbName;

        File dbFile = new File(dbName);
        if(dbFile.exists())
        {
            return;
        }

        String createRatesTableSQL =
                """
                create table rates (
                currenciesConcat string primary key not null,
                rate real not null
                )
                """;

        try(Connection connection = DriverManager.getConnection(dbURL))
        {
            Statement statement = connection.createStatement();
            statement.execute(createRatesTableSQL);
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    /** Deletes the file to clear cached rates. */
    public static void removeDb() throws Exception
    {
        dbURL = "jdbc:sqlite:" + dbName;

        File dbFile = new File(dbName);
        if (dbFile.exists())
        {
            boolean result = dbFile.delete();

            if(!result)
                throw new Exception("Could not clear cache");
        }
        else
            throw new Exception("No cache found.");
    }

    /**
     * Inserts concatenated codes and rate into table.
     * If the rate is already stored, the old record is deleted beforehand.
     * @param currenciesConcat concatenated conversion currency codes
     * @param rate the conversion rate between the from and to currencies
     * @param overwrite whether a previously stored rate needs to be deleted
     */
    public static void insertRateDb(String currenciesConcat, double rate, boolean overwrite) throws Exception
    {
        dbURL = "jdbc:sqlite:" + dbName;

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName))
        {
            Statement statement = connection.createStatement();

            if(overwrite)
            {
                String removeDataSQL = "delete from rates where currenciesConcat='" + currenciesConcat + "'";
                statement.execute(removeDataSQL);
            }

            String insertDataSQL = "insert into rates values ('" + currenciesConcat + "', '" + rate + "')";
            statement.execute(insertDataSQL);
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Checks if a rate is stored in the table for a conversion.
     * @param currenciesConcat concatenated conversion currency codes
     * @return whether rate is in table
     */
    public static boolean checkRateInDb(String currenciesConcat) throws Exception
    {
        dbURL = "jdbc:sqlite:" + dbName;

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName))
        {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select * from rates where currenciesConcat = '" +
                    currenciesConcat + "'");

            return result.next() && result.getString("currenciesConcat").equals(currenciesConcat);
        }
        catch(SQLException e)
        {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Gets a previously stored rate.
     * Will never be null since checkRateInDb is always used beforehand.
     * @param currenciesConcat concatenated conversion currency codes
     * @return the cached rate
     */
    public static String getRateDb(String currenciesConcat) throws Exception
    {
        dbURL = "jdbc:sqlite:" + dbName;

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbName))
        {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select * from rates where currenciesConcat='" +  currenciesConcat + "'");

            return result.getString("rate");

        }
        catch(Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }
}
