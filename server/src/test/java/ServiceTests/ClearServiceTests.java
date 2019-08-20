package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DAOs.AuthTokenDAO;
import DAOs.EventDAO;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import Helpers.DatabaseHelper;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import Services.ClearService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClearServiceTests
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
    public void ClearPositiveSingle()
    {
        ClearService clearService = new ClearService();

        PersonDAO pd = new PersonDAO(db.GetConnection());
        PersonModel pm1 = new PersonModel(db.GetConnection(), "b", "c", "d", "e", "f", "g", "h");
        pd.Insert(pm1);

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm1 = new AuthTokenModel("1");
        atd.Insert(atm1);

        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);

        UserDAO ud = new UserDAO(db.GetConnection());
        UserModel um1 = new UserModel("a", "b", "c", "d", "e", "f", "g");
        ud.Insert(um1);

        try
        {
            clearService.Clear(null, db);
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }

        PersonModel pm2 = pd.FindForPersonID(pm1.GetPersonID(), "asdf");
        AuthTokenModel atm2 = atd.FindForTokenID(String.valueOf(atm1.GetValue()));
        EventModel em2 = ed.FindForEventID(em1.GetEventID(), "asdf");
        UserModel um2 = ud.FindForUsername(um1.GetUsername());

        assertNull(pm2);
        assertNull(atm2);
        assertNull(em2);
        assertNull(um2);
    }

    @Test
    public void ClearPositiveMultiple()
    {
        ClearService clearService = new ClearService();

        PersonDAO pd = new PersonDAO(db.GetConnection());
        PersonModel pm1 = new PersonModel(db.GetConnection(), "b", "c", "d", "e", "f", "g", "h");
        pd.Insert(pm1);

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm1 = new AuthTokenModel("1");
        atd.Insert(atm1);

        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);

        UserDAO ud = new UserDAO(db.GetConnection());
        UserModel um1 = new UserModel("a", "b", "c", "d", "e", "f", "g");
        ud.Insert(um1);

        try
        {
            clearService.Clear(null, db);
            clearService.Clear(null, db);
            clearService.Clear(null, db);
            clearService.Clear(null, db);
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }

        PersonModel pm2 = pd.FindForPersonID(pm1.GetPersonID(), "asdf");
        AuthTokenModel atm2 = atd.FindForTokenID(String.valueOf(atm1.GetValue()));
        EventModel em2 = ed.FindForEventID(em1.GetEventID(), "asdf");
        UserModel um2 = ud.FindForUsername(um1.GetUsername());

        assertNull(pm2);
        assertNull(atm2);
        assertNull(em2);
        assertNull(um2);
    }


}
