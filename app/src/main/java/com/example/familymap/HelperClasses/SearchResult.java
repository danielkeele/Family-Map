package com.example.familymap.HelperClasses;

public class SearchResult
{
    private String type;
    private String id;

    private String firstName;
    private String lastName;
    private String role;

    private String eventType;
    private String city;
    private String county;
    private String year;
    private String associatedPerson;

    public SearchResult(String type, String id, String firstName, String lastName)
    {
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SearchResult(String type, String id, String eventType, String city, String county, String year, String associatedPerson)
    {
        this.type = type;
        this.id = id;
        this.eventType = eventType;
        this.city = city;
        this.county = county;
        this.year = year;
        this.associatedPerson = associatedPerson;
    }

    public String GetType()
    {
        return type;
    }

    public String GetRole()
    {
        return role;
    }

    public void SetRole(String role)
    {
        this.role = role;
    }

    public String GetId()
    {
        return id;
    }

    public String GetFirstName()
    {
        return firstName;
    }

    public String GetLastName()
    {
        return lastName;
    }

    public String GetEventType()
    {
        return eventType;
    }

    public String GetCity()
    {
        return city;
    }

    public String GetCounty()
    {
        return county;
    }

    public String GetYear()
    {
        return year;
    }

    public String GetAssociatedPerson()
    {
        return associatedPerson;
    }
}
