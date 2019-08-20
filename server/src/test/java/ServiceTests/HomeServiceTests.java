package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import Helpers.DatabaseHelper;
import Services.HomeService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HomeServiceTests
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
    public void GetStaticFilesPositive()
    {
        HomeService homeService = new HomeService();
        String path = "/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/web/index.html";

        try
        {
            assertTrue(homeService.GetStaticFiles(null, new File(path), null));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }
    };

    @Test
    public void GetStaticFilesNegative()
    {
        HomeService homeService = new HomeService();
        String path = "/Users/nobody/";

        try
        {
            assertFalse(homeService.GetStaticFiles(null, new File(path), null));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }
    };
}
