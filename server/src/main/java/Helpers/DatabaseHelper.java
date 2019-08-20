package Helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper
{
    static
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private Connection conn;

    public Connection GetConnection()
    {
        return conn;
    }

    public void OpenConnection()
    {
        try
        {
            final String CONNECTION_URL = "jdbc:sqlite:/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/DB/FamilyMapDB.db";

            if (conn == null)
            {
                conn = DriverManager.getConnection(CONNECTION_URL);
            }

            conn.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            System.out.println("failed to open db");
            e.printStackTrace();
        }
    }

    public void CloseConnection(boolean commit)
    {
        try
        {
            if (commit)
            {
                conn.commit();
            }
            else
            {
                conn.rollback();
            }

            conn.close();
            conn = null;
        }
        catch (SQLException e)
        {
            System.out.println("failed to close db");
            e.printStackTrace();
        }
    }

    public Boolean CreateTables()
    {
        Boolean success = true;

        try
        {
            Statement stmt = null;

            try
            {
                stmt = conn.createStatement();

                stmt.executeUpdate("drop table if exists Person");
                stmt.executeUpdate("drop table if exists Event");
                stmt.executeUpdate("drop table if exists User");
                stmt.executeUpdate("drop table if exists Auth_Token");
                stmt.executeUpdate
                        (
                                "create table Person " +
                                        "( " +
                                        "PersonID varchar(255) not null primary key, " +
                                        "Username varchar(255) not null, /* foreign key */ " +
                                        "Firstname varchar(255) not null, " +
                                        "Lastname varchar(255) not null, " +
                                        "Gender varchar(255) not null, " +
                                        "FatherID varchar(255), /* foreign key */ " +
                                        "MotherID varchar(255), /* foreign key */ " +
                                        "SpouseID varchar(255) /* foreign key */" +
                                        ");" +

                                        "create table Event " +
                                        "(" +
                                        "EventID varchar(255) not null primary key, " +
                                        "Username varchar(255) not null, /* foreign key */" +
                                        "PersonID varchar(255) not null, /* foreign key */" +
                                        "Latitude float(25) not null, " +
                                        "Longitude float(25) not null, " +
                                        "Country varchar(255) not null, " +
                                        "City varchar(255) not null, " +
                                        "EventType varchar(255) not null, " +
                                        "Year int not null " +
                                        ");" +

                                        "create table User " +
                                        "(" +
                                        "Username varchar(255) not null primary key," +
                                        "Password varchar(255) not null," +
                                        "Email varchar(255) not null," +
                                        "Fname varchar(255) not null," +
                                        "Lname varchar(255) not null," +
                                        "Gender varchar(255) not null," +
                                        "PersonID varchar(255) not null /* foreign key */" +
                                        ");" +

                                        "create table Auth_Token " +
                                        "(" +
                                        "Value integer primary key," +
                                        "Username varchar(255) not null" +
                                        ");"
                        );
            }
            finally
            {
                if (stmt != null)
                {
                    stmt.close();
                    stmt = null;
                }
            }
        }
        catch (SQLException e)
        {
            //System.out.println("failed to create tables");
            //e.printStackTrace();
            success = false;
        }

        return success;
    }

    public Boolean DeleteTables()
    {
        Boolean success = true;
        Statement stmt = null;

        try
        {
            stmt = conn.createStatement();

            stmt.executeUpdate("drop table if exists Person");
            stmt.executeUpdate("drop table if exists Event");
            stmt.executeUpdate("drop table if exists User");
            stmt.executeUpdate("drop table if exists Auth_Token");
        }
        catch (Exception e)
        {
            //System.out.println("Failed to delete tables");
            //e.printStackTrace();
            success = false;
        }
        finally
        {
            try
            {
                if (stmt != null)
                {
                    stmt.close();
                    stmt = null;
                }
            }
            catch (Exception e)
            {
                System.out.println("Failed to close statement");
                e.printStackTrace();
                success = false;
            }
        }

        return success;
    }

    public String ReadString(InputStream is) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;

        while ((len = sr.read(buf)) > 0)
        {
            sb.append(buf, 0, len);
        }

        return sb.toString();
    }
}
