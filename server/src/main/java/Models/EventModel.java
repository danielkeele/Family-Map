package Models;

import java.sql.Connection;

import DAOs.EventDAO;

public class EventModel
{
    private String eventID;
    private String username;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    public EventModel(Connection conn, String username, String personID, float latitude, float longitude, String country, String city, String eventType, int year)
    {
        this.eventID = new EventDAO(conn).GetNewID();
        this.username = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public EventModel(String eventID, String username, String personID, float latitude, float longitude, String country, String city, String eventType, int year)
    {
        this.eventID = eventID;
        this.username = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public void SetUsername(String username)
    {
        this.username = username;
    }

    public void SetEventID(String eventID)
    {
        this.eventID = eventID;
    }

    public void SetPersonID(String personID)
    {
        this.personID = personID;
    }

    public void SetLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    public void SetLongitude(float longitude)
    {
        this.longitude = longitude;
    }

    public void SetCountry(String country)
    {
        this.country = country;
    }

    public void SetCity(String city)
    {
        this.city = city;
    }

    public void SetEventType(String eventType)
    {
        this.eventType = eventType;
    }

    public void SetYear(int year)
    {
        this.year = year;
    }

    public String GetEventID()
    {
        return eventID;
    }

    public String GetUsername()
    {
        return username;
    }

    public String GetPersonID()
    {
        return personID;
    }

    public float GetLatitude()
    {
        return latitude;
    }

    public float GetLongitude()
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
}
