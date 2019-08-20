package DAOs;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;


import Helpers.DatabaseHelper;
import Models.*;
import Services.ServiceRequestResponseObjects.LocationDetails;
import Services.ServiceRequestResponseObjects.Locations;
import Services.ServiceRequestResponseObjects.Names;

public class PersonDAO
{
    Connection conn;

    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    };

    public boolean Update(String PersonID, String field, String value)
    {
        boolean success = true;

        String sql = "Update Person Set " + field + " = \'" + value + "\' where PersonID = \'" + PersonID + "\';";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean Insert(PersonModel person)
    {
        boolean commit = true;

        String sql = "INSERT INTO Person (" +
                "PersonID, " +
                "Username, " +
                "Firstname, " +
                "Lastname, " +
                "Gender, " +
                "MotherID, " +
                "FatherID, " +
                "SpouseID" +
                ") VALUES(?,?,?,?,?,?,?,?)";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, person.GetPersonID());
            stmt.setString(2, person.GetUserName());
            stmt.setString(3, person.GetFirstName());
            stmt.setString(4, person.GetLastName());
            stmt.setString(5, person.GetGender());
            stmt.setString(6, person.GetMotherID());
            stmt.setString(7, person.GetFatherID());
            stmt.setString(8, person.GetSpouseID());

            stmt.executeUpdate();
        }
        catch (Exception e)
        {
            commit = false;
            //e.printStackTrace();
        }

        return commit;
    }

    public PersonModel FindForPersonID(String personID, String username)
    {
        PersonModel person = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Person WHERE PersonID = ? and Username = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, personID);
            stmt.setString(2, username);

            rs = stmt.executeQuery();

            if (rs.next())
            {
                person = new PersonModel(conn, rs.getString("Username"),
                                        rs.getString("Firstname"), rs.getString("Lastname"),
                                        rs.getString("Gender"), rs.getString("FatherID"),
                                        rs.getString("MotherID"), rs.getString("SpouseID"));
                person.SetPersonID(personID);
                return person;
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

    public PersonModel[] FindForUsername(String username)
    {
        ResultSet rs = null;

        String sql = "SELECT * FROM Person WHERE Username = ?;";

        ArrayList<PersonModel> persons = new ArrayList<>();

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);

            rs = stmt.executeQuery();

            while (rs.next())
            {
                PersonModel person = new PersonModel(conn, rs.getString("Username"), rs.getString("Firstname"),
                        rs.getString("Lastname"), rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));

                person.SetPersonID(rs.getString("PersonID"));

                persons.add(person);
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

        PersonModel[] personsArray = new PersonModel[persons.size()];
        persons.toArray(personsArray);

        return personsArray;
    }

    public Boolean Delete(String personID)
    {
        String sql = "Delete FROM Person WHERE PersonID = ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, personID);

            if (stmt.executeUpdate() == 1)
            {
                return true;
            }
            else
            {
                //System.out.println("Problem removing row with PersonID " + personID + ".");
                return false;
            }
        }
        catch (SQLException e)
        {
            //e.printStackTrace();
            return false;
        }
    }

    public Boolean DeleteAllAssociatedPeopleForUsername(String username, String personID)
    {
        String sql = "Delete FROM Person WHERE Username = ? and PersonID != ?;";

        try
        {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, personID);

            stmt.executeUpdate();

            return true;
        }
        catch (SQLException e)
        {
            //e.printStackTrace();
            return false;
        }
    }
    
    public void GenerateAncestors(String username, int generations, DatabaseHelper db)
    {
        UserModel user = new UserDAO(conn).FindForUsername(username);
        PersonModel person = this.FindForPersonID(user.GetPersonID(), username);
        Names maleFirstNames;
        Names femaleFirstNames;
        Names lastNames;
        Locations locations;
        Random r = new Random();
        EventDAO ed = new EventDAO(conn);

        try
        {
            String mNames = db.ReadString(new FileInputStream("/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/DB/maleFirstNames.json"));
            maleFirstNames = new Gson().fromJson(mNames, Names.class);

            String fNames = db.ReadString(new FileInputStream("/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/DB/femaleFirstNames.json"));
            femaleFirstNames = new Gson().fromJson(fNames, Names.class);

            String lNames = db.ReadString(new FileInputStream("/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/DB/lastNames.json"));
            lastNames = new Gson().fromJson(lNames, Names.class);

            String locationsString = db.ReadString(new FileInputStream("/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/DB/locations.json"));
            locations = new Gson().fromJson(locationsString, Locations.class);
        }
        catch (Exception e)
        {
            System.out.println("Failed to load names and locations");
            return;
        }

        ArrayList<PersonModel> currentGeneration = new ArrayList<>();
        ArrayList<PersonModel> nextGeneration = new ArrayList<>();

        currentGeneration.add(person);

        int currentBirthYear = 1992;
        int currentBaptismYear = 2000;
        int currentMarriageYear = 2015;
        int randomFactor = 5;
        int decrement = 35;

        LocationDetails UserEventlocation = locations.data[r.nextInt(locations.data.length)];
        ed.Insert(new EventModel(conn, username, person.GetPersonID(), UserEventlocation.GetLatitude(),
                UserEventlocation.GetLongitude(), UserEventlocation.GetCountry(),
                UserEventlocation.GetCity(),"Birth", currentBirthYear + r.nextInt(randomFactor)));

        for (int x = 0; x < generations; x++)
        {
            currentBirthYear -= decrement;
            currentBaptismYear -= decrement;
            currentMarriageYear -= decrement;

            for (int y = 0; y < currentGeneration.size(); y++)
            {
                LocationDetails marriageLocation = locations.data[r.nextInt(locations.data.length)];

                //first father, then mother
                for (int z = 0; z < 2; z++)
                {
                    //generate and insert parent
                    Boolean father = (z == 0);
                    String fName;
                    String gender;

                    if (father)
                    {
                        fName = maleFirstNames.data[r.nextInt(maleFirstNames.data.length)];
                        gender = "m";
                    }
                    else
                    {
                        fName = femaleFirstNames.data[r.nextInt(femaleFirstNames.data.length)];
                        gender = "f";
                    }

                    String lName = lastNames.data[r.nextInt(lastNames.data.length)];

                    PersonModel parent = new PersonModel(conn, username, fName, lName, gender);

                    if (father)
                    {
                        this.Update(currentGeneration.get(y).GetPersonID(),"FatherID", parent.GetPersonID());
                        parent.SetSpouseID(String.valueOf(Integer.parseInt(parent.GetPersonID()) + 1));
                    }
                    else
                    {
                        this.Update(currentGeneration.get(y).GetPersonID(),"MotherID", parent.GetPersonID());
                        parent.SetSpouseID(String.valueOf(Integer.parseInt(parent.GetPersonID()) - 1));
                    }

                    if (!this.Insert(parent))
                    {
                        System.err.println("failed to insert parent");
                    }

                    //generate and insert events for parent
                    //birth
                    LocationDetails location = locations.data[r.nextInt(locations.data.length)];
                    if (!ed.Insert(new EventModel(conn, username, parent.GetPersonID(), location.GetLatitude(),
                                                    location.GetLongitude(), location.GetCountry(),
                                                    location.GetCity(),"Birth", currentBirthYear + r.nextInt(randomFactor))))
                    {
                        System.err.println("failed to insert event");
                    }

                    //baptism
                    location = locations.data[r.nextInt(locations.data.length)];
                    if (!ed.Insert(new EventModel(conn, username, parent.GetPersonID(), location.GetLatitude(),
                            location.GetLongitude(), location.GetCountry(),
                            location.GetCity(),"Baptism", currentBaptismYear + r.nextInt(randomFactor))))
                    {
                        System.err.println("failed to insert event");
                    }

                    //marriage
                    if (!ed.Insert(new EventModel(conn, username, parent.GetPersonID(), marriageLocation.GetLatitude(),
                            marriageLocation.GetLongitude(), marriageLocation.GetCountry(),
                            marriageLocation.GetCity(),"Marriage", currentMarriageYear)))
                    {
                        System.err.println("failed to insert event");
                    }

                    //add to next generation
                    nextGeneration.add(parent);
                }
            }

            currentGeneration = new ArrayList<>(nextGeneration);
            nextGeneration.clear();
        }
    }

    public void GenerateAncestors(String username, DatabaseHelper db)
    {
        GenerateAncestors(username, 4, db);
    }

    public String GetNewID()
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int maxID = -1;

        try
        {
            String sql = "select PersonID from Person";
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
