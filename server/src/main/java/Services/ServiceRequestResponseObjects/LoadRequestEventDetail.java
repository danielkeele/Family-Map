package Services.ServiceRequestResponseObjects;

public class LoadRequestEventDetail
{
     String eventType;
     String personID;
     String city;
     String country;
     String latitude;
     String longitude;
     String year;
     String eventID;
     String associatedUsername;

    public String GetEventType()
    {
        return eventType;
    }

    public String GetPersonID()
    {
        return personID;
    }

    public String GetCity() {
        return city;
    }

    public String GetCountry()
    {
        return country;
    }

    public String GetLatitude()
    {
        return latitude;
    }

    public String GetLongitude()
    {
        return longitude;
    }

    public String GetYear()
    {
        return year;
    }

    public String GetEventID()
    {
        return eventID;
    }

    public String GgetAssociatedUsername()
    {
        return associatedUsername;
    }
}
