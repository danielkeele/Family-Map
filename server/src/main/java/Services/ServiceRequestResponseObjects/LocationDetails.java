package Services.ServiceRequestResponseObjects;

public class LocationDetails
{
    String country;
    String city;
    Float latitude;
    Float longitude;

    public String GetCountry()
    {
        return country;
    }

    public String GetCity()
    {
        return city;
    }

    public Float GetLatitude()
    {
        return latitude;
    }

    public Float GetLongitude()
    {
        return longitude;
    }
}
