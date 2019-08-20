package Services.ServiceRequestResponseObjects;

public class LoadRequest
{
    LoadRequestUserDetail[] users;
    LoadRequestPersonDetail[] persons;
    LoadRequestEventDetail[] events;

    public LoadRequestUserDetail[] GetUsers()
    {
        return users;
    }

    public LoadRequestPersonDetail[] GetPersons()
    {
        return persons;
    }

    public LoadRequestEventDetail[] GetEvents()
    {
        return events;
    }
}
