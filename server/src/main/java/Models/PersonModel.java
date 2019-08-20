package Models;

import java.sql.Connection;

import DAOs.PersonDAO;

public class PersonModel
{
    public PersonModel(Connection conn, String userName, String firstName, String lastName, String gender)
    {
        this.personID = new PersonDAO(conn).GetNewID();

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public PersonModel(Connection conn, String userName, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID)
    {
        this.personID = new PersonDAO(conn).GetNewID();

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public PersonModel(String personID, String userName, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID)
    {
        this.personID = personID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    private String personID;
    private String userName;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    public String GetPersonID()
    {
        return personID;
    }

    public String GetUserName()
    {
        return userName;
    }

    public String GetFirstName()
    {
        return firstName;
    }

    public String GetLastName()
    {
        return lastName;
    }

    public String GetGender()
    {
        return gender;
    }

    public String GetFatherID()
    {
        return fatherID;
    }

    public String GetMotherID()
    {
        return motherID;
    }

    public String GetSpouseID()
    {
        return spouseID;
    }

    public void SetUserName(String userName)
    {
        this.userName = userName;
    }

    public void SetPersonID(String personID)
    {
        this.personID = personID;
    }

    public void SetFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void SetLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void SetGender(String gender)
    {
        this.gender = gender;
    }

    public void SetFatherID(String fatherID)
    {
        this.fatherID = fatherID;
    }

    public void SetMotherID(String motherID)
    {
        this.motherID = motherID;
    }

    public void SetSpouseID(String spouseID)
    {
        this.spouseID = spouseID;
    }
}
