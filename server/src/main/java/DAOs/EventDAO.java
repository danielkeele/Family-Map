package DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Models.*;

public class EventDAO
{
    Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    public boolean Insert(EventModel event)
    {
        boolean commit = true;

        String sql = "INSERT INTO Event (" +
                "EventID, " +
                "Username, " +
                "PersonID, " +
                "Latitude, " +
                "Longitude, " +
                "Country, " +
                "City, " +
                "EventType, " +
                "Year" +
                ") VALUES(?,?,?,?,?,?,?,?,?)";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, event.GetEventID());
            stmt.setString(2, event.GetUsername());
            stmt.setString(3, event.GetPersonID());
            stmt.setFloat(4, event.GetLatitude());
            stmt.setFloat(5, event.GetLongitude());
            stmt.setString(6, event.GetCountry());
            stmt.setString(7, event.GetCity());
            stmt.setString(8, event.GetEventType());
            stmt.setInt(9, event.GetYear());

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            commit = false;
            //e.printStackTrace();
        }

        return commit;
    }

    public EventModel FindForEventID(String eventID, String username)
    {
        EventModel event = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Event WHERE EventID = ? and Username = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, eventID);
            stmt.setString(2, username);

            rs = stmt.executeQuery();

            if (rs.next())
            {
                event = new EventModel(conn, rs.getString("Username"), rs.getString("PersonID"),
                        rs.getFloat("Latitude"), rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"), rs.getInt("Year"));

                event.SetEventID(rs.getString("EventID"));

                return event;
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

    public EventModel[] FindForUsername(String username)
    {
        ResultSet rs = null;

        String sql = "SELECT * FROM Event WHERE Username = ?;";

        ArrayList<EventModel> events = new ArrayList<>();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            rs = stmt.executeQuery();

            while (rs.next())
            {
                EventModel event = new EventModel(conn, rs.getString("Username"), rs.getString("PersonID"),
                        rs.getFloat("Latitude"), rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"), rs.getInt("Year"));

                event.SetEventID(rs.getString("EventID"));

                events.add(event);
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

        EventModel[] eventsArray = new EventModel[events.size()];
        events.toArray(eventsArray);

        return eventsArray;
    }

    public Boolean DeleteAllWithUsername(String username)
    {
        String sql = "Delete FROM Event WHERE Username = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            stmt.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public String GetNewID()
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int maxID = -1;

        try
        {
            String sql = "select EventID from Event";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next())
            {
                int currentID;

                try
                {
                    currentID = Integer.parseInt(rs.getString(1));
                }
                catch (Exception e)
                {
                    currentID = -1;
                }

                if (maxID == -1)
                {
                    maxID = currentID;
                }
                else if (currentID > maxID)
                {
                    maxID = currentID;
                }
            }

            if (maxID == -1)
            {
                return "0";
            }

            return String.valueOf((maxID + 1));
        }
        catch (SQLException e)
        {
            System.out.println("failed to get maxID");
            return null;
        }
        finally
        {
            try
            {

                if (rs != null)
                {
                    rs.close();
                }

                if (stmt != null)
                {
                    stmt.close();
                }
            }
            catch (Exception e)
            {
                System.out.println("failed to close statement or result set");
            }
        }
    }
}
