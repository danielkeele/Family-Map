package DAOs;
import Models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDAO
{
    Connection conn;

    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    public boolean Insert(AuthTokenModel atm)
    {
        boolean commit = true;

        String sql = "INSERT INTO Auth_Token (" +
                "Value, " +
                "Username " +
                ") VALUES(?,?);";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, atm.GetValue());
            stmt.setString(2, atm.GetUsername());

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            commit = false;
            //e.printStackTrace();
        }

        return commit;
    }

    public AuthTokenModel FindForTokenID(String tokenID)
    {
        AuthTokenModel authToken = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Auth_Token WHERE Value = ?;";

        try
        {
            int tokenIDAsInt = Integer.parseInt(tokenID);
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, tokenIDAsInt);

            rs = stmt.executeQuery();

            if (rs.next())
            {
                authToken = new AuthTokenModel(rs.getString("Username"));
                authToken.SetValue(tokenIDAsInt);

                return authToken;
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
}
