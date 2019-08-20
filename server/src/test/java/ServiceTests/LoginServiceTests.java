package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Helpers.DatabaseHelper;
import Services.LoadService;
import Services.LoginService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginServiceTests
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
    public void LoginPositive()
    {
        LoginService loginService = new LoginService();

        try
        {
            assertTrue(loginService.Login(null, db));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }
    }

    @Test
    public void LoginNegative()
    {
        LoadService loadService = new LoadService();
        DatabaseHelper db2 = new DatabaseHelper();
        db2.OpenConnection();

        try
        {
            assertFalse(loadService.Load(null, db2));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }
        finally
        {
            db2.CloseConnection(false);
        }
    }
}
