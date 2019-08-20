package Services.ServiceRequestResponseObjects;

public class EventResponseDetail
{
    String associatedUsername;
    String eventID;
    String personID;
    Float latitude;
    Float longitude;
    String country;
    String city;
    String eventType;
    int year;

    public EventResponseDetail(String associatedUsername, String eventID,
                               String personID, Float latitude, Float longitude,
                               String country, String city, String eventType,
                               int year)
    {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String GetAssociatedUsername()
    {
        return associatedUsername;
    }

    public String GetEventID()
    {
        return eventID;
    }

    public String GetPersonID()
    {
        return personID;
    }

    public Float GetLatitude()
    {
        return latitude;
    }

    public Float GetLongitude()
    {
        return longitude;
    }

    public String GetCountry()
    {
        return country;
    }

    public String GetCity()
    {
        return city;
    }

    public String GetEventType()
    {
        return eventType;
    }

    public int GetYear()
    {
        return year;
    }

    public void SetEventType(String eventType)
    {
        this.eventType = eventType;
    }
}
