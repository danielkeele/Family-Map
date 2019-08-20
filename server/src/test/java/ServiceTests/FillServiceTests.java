package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DAOs.PersonDAO;
import DAOs.UserDAO;
import Helpers.DatabaseHelper;
import Models.PersonModel;
import Models.UserModel;
import Services.FillService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FillServiceTests
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
    public void FillPositive()
    {
        PersonModel pm = new PersonModel(db.GetConnection(), "a", "a", "a", "a", null, null, "a");
        PersonDAO pd = new PersonDAO(db.GetConnection());
        pd.Insert(pm);

        UserModel um = new UserModel(pm.GetUserName(), "a", "b", pm.GetFirstName(), pm.GetLastName(), pm.GetGender(), pm.GetPersonID());
        new UserDAO(db.GetConnection()).Insert(um);

        FillService fillService = new FillService();
        Boolean success = false;

        try
        {
            success = fillService.Fill(null, um.GetUsername(), db, "5");
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }

        assertTrue(success);
    }

    @Test
    public void FillNegative()
    {

        FillService fillService = new FillService();
        Boolean success = false;

        try
        {
            success = fillService.Fill(null, "123", db, "5");
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }

        assertFalse(success);
    }
}
