package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DAOs.AuthTokenDAO;
import DAOs.PersonDAO;
import Helpers.DatabaseHelper;
import Models.AuthTokenModel;
import Models.PersonModel;
import Services.PersonService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PersonServiceTests
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
    public void GetAllPersonsForTokenPositive()
    {
        PersonService personService = new PersonService();

        AuthTokenModel atm = new AuthTokenModel("a");
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        atd.Insert(atm);

        Boolean success = false;

        try
        {
            success = personService.GetAllPersonsForToken(null, db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertTrue(success);

    };

    @Test
    public void GetAllPersonsForTokenNegative()
    {
        PersonService personService = new PersonService();
        Boolean success = false;

        try
        {
            success = personService.GetAllPersonsForToken(null, db, "123");
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertFalse(success);
    };

    @Test
    public void GetPersonForPersonIDPositive()
    {
        PersonDAO pd = new PersonDAO(db.GetConnection());
        PersonModel pm = new PersonModel(db.GetConnection(), "b", "c", "d", "e", "f", "g", "h");
        pd.Insert(pm);

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm = new AuthTokenModel("b");
        atd.Insert(atm);

        PersonService personService = new PersonService();

        Boolean success = false;

        try
        {
            success = personService.GetPersonForPersonID(null, pm.GetPersonID(), db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertTrue(success);
    };

    @Test
    public void GetPersonForPersonIDNegative()
    {
        PersonService personService = new PersonService();

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm = new AuthTokenModel("b");
        atd.Insert(atm);

        Boolean success = false;

        try
        {
            success = personService.GetPersonForPersonID(null, "abc", db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertFalse(success);
    };
}
