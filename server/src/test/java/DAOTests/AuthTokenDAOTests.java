package DAOTests;

import org.junit.* ;

import static org.junit.Assert.*;

import DAOs.AuthTokenDAO;
import Helpers.DatabaseHelper;
import Models.AuthTokenModel;

public class AuthTokenDAOTests
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
        AuthTokenModel atm = new AuthTokenModel("a");
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());

        assertTrue(atd.Insert(atm));
    }

    @Test
    public void InsertNegative()
    {
        AuthTokenModel atm = null;
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());

        assertFalse(atd.Insert(atm));
    }

    @Test
    public void FindForTokenIDPositive()
    {
        AuthTokenModel atm1 = new AuthTokenModel("1");
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        atd.Insert(atm1);

        AuthTokenModel atm2 = atd.FindForTokenID(String.valueOf(atm1.GetValue()));

        assertEquals(atm1.GetValue(), atm2.GetValue());
    }

    @Test
    public void FindForTokenIDNegative()
    {
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());

        AuthTokenModel atm = atd.FindForTokenID("5");

        assertEquals(atm, null);
    }

}