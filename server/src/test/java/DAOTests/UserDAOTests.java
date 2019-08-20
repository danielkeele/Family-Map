package DAOTests;

import org.junit.* ;
import static org.junit.Assert.*;

import DAOs.UserDAO;
import Helpers.DatabaseHelper;
import Models.UserModel;

public class UserDAOTests
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
        UserModel um = new UserModel("a", "b", "c", "d", "e", "f", "g");
        UserDAO ud = new UserDAO(db.GetConnection());

        assertTrue(ud.Insert(um));
    }

    @Test
    public void InsertNegative()
    {
        UserModel um = null;
        UserDAO ud = new UserDAO(db.GetConnection());

        assertFalse(ud.Insert(um));
    }

    @Test
    public void FindForUsernamePositive()
    {
        UserModel um = new UserModel("a", "b", "c", "d", "e", "f", "g");
        UserDAO ud = new UserDAO(db.GetConnection());
        ud.Insert(um);

        assertEquals(um.GetUsername(), ud.FindForUsername(um.GetUsername()).GetUsername());
    }

    @Test
    public void FindForUsernameNegative()
    {
        UserDAO ud = new UserDAO(db.GetConnection());

        assertEquals(null, ud.FindForUsername("a"));
    }

    @Test
    public void DeletePositive()
    {
        UserModel um = new UserModel("a", "b", "c", "d", "e", "f", "g");
        UserDAO ud = new UserDAO(db.GetConnection());
        ud.Insert(um);

        assertTrue(ud.Delete("a"));
    }

    @Test
    public void DeleteNegative()
    {
        UserDAO ud = new UserDAO(db.GetConnection());

        assertFalse(ud.Delete("a"));
    }

}