package DAOs;

import java.sql.*;
import Models.*;

public class UserDAO
{
    Connection conn;

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    };

    public boolean Insert(UserModel user)
    {
        boolean commit = true;

        String sql = "INSERT INTO User (" +
                "Username, " +
                "Password, " +
                "Email, " +
                "Fname, " +
                "Lname, " +
                "Gender, " +
                "PersonID " +
                ") VALUES(?,?,?,?,?,?,?);";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.GetUsername());
            stmt.setString(2, user.GetPassword());
            stmt.setString(3, user.GetEmail());
            stmt.setString(4, user.GetfName());
            stmt.setString(5, user.GetlName());
            stmt.setString(6, user.GetGender());
            stmt.setString(7, user.GetPersonID());

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            commit = false;
        }

        return commit;
    }

    public UserModel FindForUsername(String username)
    {
        UserModel user = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM User WHERE Username = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            rs = stmt.executeQuery();

            if (rs.next() == true)
            {
                user = new UserModel(rs.getString("Username"), rs.getString("Password"),
                                    rs.getString("Email"), rs.getString("Fname"), rs.getString("Lname"),
                                    rs.getString("Gender"), rs.getString("PersonID"));

                return user;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public Boolean Delete(String username)
    {

        String sql = "Delete FROM User WHERE Username = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            if (stmt.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                //System.out.println("Problem removing row with Username " + username + ".");
                return false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
