public class TestDriver
{
    public static void main(String args[])
    {
        org.junit.runner.JUnitCore.main("DAOTests.PersonDAOTests",
                                                "DAOTests.UserDAOTests",
                                                "DAOTests.EventDAOTests",
                                                "DAOTests.AuthTokenDAOTests",
                                                "ServiceTests.ClearServiceTests",
                                                "ServiceTests.FillServiceTests",
                                                "ServiceTests.HomeServiceTests",
                                                "ServiceTests.LoadServiceTests",
                                                "ServiceTests.LoginServiceTests",
                                                "ServiceTests.PersonServiceTests",
                                                "ServiceTests.RegisterServiceTests",
                                                "ServiceTests.EventServiceTests");
    }
}
