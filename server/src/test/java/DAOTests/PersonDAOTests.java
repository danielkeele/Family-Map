package DAOTests;

import org.junit.* ;

import javax.xml.crypto.Data;

import static org.junit.Assert.*;

import DAOs.PersonDAO;
import DAOs.UserDAO;
import Helpers.DatabaseHelper;
import Models.PersonModel;
import Models.UserModel;

public class PersonDAOTests
{

    DatabaseHelper db;

    @Before
    public void SetUp()
    {
        db = new DatabaseHelper();
        db.OpenConnection();
        db.CreateTables();
    }

    @After
    public void TearDown()
    {
        db.DeleteTables();
        db.CloseConnection(false);
        db = null;
    }

    @Test
    public void InsertPositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "b", "c", "d", "e", "f", "g", "h");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        assertTrue(pd.Insert(pm));
        assertTrue(true);
    }

    @Test
    public void InsertNegative()
    {
        PersonModel pm = null;
        PersonDAO pd = new PersonDAO(db.GetConnection());

        assertFalse(pd.Insert(pm));
    }

    @Test
    public void UpdatePositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "b", "c", "d", "m", "f", "g", "h");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm);

        pd.Update(pm.GetPersonID(),"Gender", "f");

        assertEquals("f", pd.FindForPersonID(pm.GetPersonID(), "b").GetGender());
    }

    @Test
    public void UpdateNegative()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "b", "c", "d", "m", "f", "g", "h");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm);

        pd.Update(pm.GetPersonID(),"Gender", "f");

        assertNotEquals("m", pd.FindForPersonID(pm.GetPersonID(), "b").GetGender());
    }

    @Test
    public void FindForUsernamePositive()
    {
        PersonModel pm1 = new PersonModel(db.GetConnection(), "a", "a", "a", "a", "a", "a", "a");
        PersonModel pm2 = new PersonModel(db.GetConnection(), "b", "b", "b", "b", "b", "b", "b");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm1);
        pd.Insert(pm2);

        PersonModel[] personArray = pd.FindForUsername(pm1.GetUserName());

        assertEquals(1, personArray.length);
        assertEquals("a", personArray[0].GetUserName());
    }

    @Test
    public void FindForUsernameNegative()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", "a", "a", "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm);

        PersonModel[] personArray = pd.FindForUsername("b");

        assertEquals(0, personArray.length);
    }

    @Test
    public void DeleteAllAssociatedPeopleForUsernamePositive()
    {
        PersonModel pm1 = new PersonModel(db.GetConnection(), "a", "a", "a", "a", "a", "a", "a");
        PersonModel pm2 = new PersonModel(db.GetConnection(), "a", "b", "b", "b", "b", "b", "b");


        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm1);
        pd.Insert(pm2);

        pd.DeleteAllAssociatedPeopleForUsername(pm1.GetUserName(), pm1.GetPersonID());

        PersonModel[] personArray = pd.FindForUsername(pm1.GetUserName());

        assertEquals(1, personArray.length);
    }

    @Test
    public void DeleteAllAssociatedPeopleForUsernameNegative()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", "a", "a", "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());

        pd.Insert(pm);

        pd.DeleteAllAssociatedPeopleForUsername(pm.GetUserName(), pm.GetPersonID());

        PersonModel[] personArray = pd.FindForUsername(pm.GetUserName());

        assertEquals(1, personArray.length);
    }

    @Test
    public void GenerateAncestorsPositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", null, null, "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        UserModel um = new UserModel(pm.GetUserName(), "a", "b", pm.GetFirstName(), pm.GetLastName(), pm.GetGender(), pm.GetPersonID());
        new UserDAO(db.GetConnection()).Insert(um);

        pd.GenerateAncestors(pm.GetUserName(), 4, db);

        assertTrue(pd.FindForPersonID(pm.GetPersonID(), "a").GetFatherID() != null);
        PersonModel grandFather = pd.FindForPersonID(pd.FindForPersonID(pm.GetPersonID(), "a").GetFatherID(), "a");
        assertTrue(grandFather.GetMotherID() != null);
    }

    @Test
    public void GenerateAncestorsNegative()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", null, null, "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        UserModel um = new UserModel(pm.GetUserName(), "a", "b", pm.GetFirstName(), pm.GetLastName(), pm.GetGender(), pm.GetPersonID());
        new UserDAO(db.GetConnection()).Insert(um);

        DatabaseHelper dbh = new DatabaseHelper();
        pd.GenerateAncestors(pm.GetUserName(), 0, dbh);

        assertTrue(pd.FindForPersonID(pm.GetPersonID(), "a").GetFatherID() == null);
    }

    @Test
    public void GetNewIDPositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", null, null, "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        String newID = pd.GetNewID();

        assertTrue(Integer.parseInt(newID) > Integer.parseInt(pm.GetPersonID()));
    }

    @Test
    public void GetNewIDNegative()
    {
        PersonDAO pd = new PersonDAO(db.GetConnection());
        String id1 = pd.GetNewID();
        String id2 = pd.GetNewID();

        assertTrue(id1.equals(id2));
    }

    @Test
    public void FindForPersonIDPositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", "a", "a", "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        assertEquals(pm.GetPersonID(), pd.FindForPersonID(pm.GetPersonID(), "a").GetPersonID());
        assertTrue(true);
    }

    @Test
    public void FindForPersonIDNegative()
    {
        PersonDAO pd = new PersonDAO(db.GetConnection());

        assertEquals(null, pd.FindForPersonID("a", "asdf"));
    }

    @Test
    public void DeletePositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "b", "c", "d", "e", "f", "g", "h");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        assertTrue(pd.Delete(pm.GetPersonID()));

        assertTrue(true);
    }

    @Test
    public void DeleteNegative()
    {
        PersonDAO pd = new PersonDAO(db.GetConnection());

        assertFalse(pd.Delete("a"));
    }
}