package DAOTests;

import org.junit.* ;

import static org.junit.Assert.*;

import DAOs.EventDAO;
import Helpers.DatabaseHelper;
import Models.EventModel;

public class EventDAOTests
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
        EventModel em = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        EventDAO ed = new EventDAO(db.GetConnection());

        assertTrue(ed.Insert(em));
    }

    @Test
    public void InsertNegative()
    {
        EventModel em = null;
        EventDAO ed = new EventDAO(db.GetConnection());

        assertFalse(ed.Insert(em));
    }

    @Test
    public void FindForEventIDPositive()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        EventModel em3 = ed.FindForEventID(em1.GetEventID(), "b");

        assertEquals(em3.GetEventID(), em1.GetEventID());
        assertEquals(em3.GetPersonID(), em1.GetPersonID());
    }

    @Test
    public void FindForEventIDNegative()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);

        EventModel em2 = ed.FindForEventID(em1.GetEventID(), "asdf");

        assertEquals(em2, null);
    }

    @Test
    public void FindForUsernamePositive()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        EventModel[] emArray = ed.FindForUsername("b");

        assertEquals(emArray.length, 2);
    }

    @Test
    public void FindForUsernameNegative()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        EventModel[] emArray = ed.FindForUsername("c");

        assertEquals(emArray.length, 0);
    }

    @Test
    public void DeleteAllWithUsernamePositive()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        ed.DeleteAllWithUsername("b");

        EventModel[] emArray = ed.FindForUsername("b");

        assertEquals(emArray.length, 0);
    }

    @Test
    public void DeleteAllWithUsernameNegative()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        ed.DeleteAllWithUsername("c");

        EventModel[] emArray = ed.FindForUsername("b");

        assertEquals(emArray.length, 2);
    }

    @Test
    public void GetNewIDPositive()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        EventModel em1 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em1);
        EventModel em2 = new EventModel(db.GetConnection(), "b", "c", 0.5f, 0.5f, "f", "g", "h", 1234);
        ed.Insert(em2);

        String newID = ed.GetNewID();

        assertTrue(Integer.parseInt(newID) > Integer.parseInt(em1.GetEventID()));
    }

    @Test
    public void GetNewIDNegative()
    {
        EventDAO ed = new EventDAO(db.GetConnection());
        String id1 = ed.GetNewID();
        String id2 = ed.GetNewID();

        assertTrue(id1.equals(id2));
    }
}