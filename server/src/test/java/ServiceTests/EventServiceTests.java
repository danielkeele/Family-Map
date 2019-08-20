package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DAOs.AuthTokenDAO;
import DAOs.EventDAO;
import Helpers.DatabaseHelper;
import Models.AuthTokenModel;
import Models.EventModel;
import Services.EventService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventServiceTests
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
    public void GetAllEventsForTokenNegative()
    {
        EventService eventService = new EventService();

        Boolean success = false;

        try
        {
            success = eventService.GetAllEventsForToken(null, db, "22");
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertFalse(success);
    };

    @Test
    public void GetAllEventsForTokenPositive()
    {
        AuthTokenModel atm = new AuthTokenModel("a");
        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        atd.Insert(atm);

        EventService eventService = new EventService();

        Boolean success = false;

        try
        {
            success = eventService.GetAllEventsForToken(null, db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertTrue(success);
    }

    @Test
    public void GetEventForEventIDPositive()
    {
        EventModel em = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        EventDAO ed = new EventDAO(db.GetConnection());
        ed.Insert(em);

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm = new AuthTokenModel("b");
        atd.Insert(atm);

        EventService eventService = new EventService();

        Boolean success = false;

        try
        {
            success = eventService.GetEventForEventID(null, em.GetEventID(), db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertTrue(success);
    }

    @Test
    public void GetEventForEventIDNegative()
    {
        EventService eventService = new EventService();

        Boolean success = false;

        AuthTokenDAO atd = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel atm = new AuthTokenModel("b");
        atd.Insert(atm);

        try
        {
            success = eventService.GetEventForEventID(null, "1234", db, String.valueOf(atm.GetValue()));
        }
        catch (Exception e)
        {
            assertEquals(1, 2);
        }

        assertFalse(success);
    };
}
