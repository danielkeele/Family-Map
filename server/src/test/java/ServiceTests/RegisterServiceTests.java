package ServiceTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Helpers.DatabaseHelper;
import Services.RegisterService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegisterServiceTests
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
    public void RegisterPositive()
    {
        RegisterService registerService = new RegisterService();
        String reqData =
                "{\n" +
                        "\t\"userName\":\"username\",\n" +
                        "\t\"password\":\"password\",\n" +
                        "\t\"email\":\"email\",\n" +
                        "\t\"firstName\":\"firstname\",\n" +
                        "\t\"lastName\":\"lastname\",\n" +
                        "\t \"gender\":\"m/f\"\n" +
                        "}";
        try
        {
            assertTrue(registerService.Register(null, db, reqData));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }
    }

    @Test
    public void RegisterNegative()
    {
        RegisterService registerService = new RegisterService();
        String reqData =
                "{\n" +
                        "\t\"userName\":\"username\",\n" +
                        "\t\"password\":\"password\",\n" +
                        "\t\"lastName\":\"lastname\",\n" +
                        "\t \"gender\":\"m/f\"\n" +
                        "}";
        try
        {
            assertFalse(registerService.Register(null, db, reqData));
        }
        catch (Exception e)
        {
            assertEquals(1,2);
        }

    }
}
