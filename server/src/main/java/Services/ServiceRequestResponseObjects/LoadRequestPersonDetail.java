package Services.ServiceRequestResponseObjects;

public class LoadRequestPersonDetail
{
          String firstName;
          String lastName;
          String gender;
          String personID;
          String spouseID;
          String fatherID;
          String motherID;
          String associatedUsername;

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

    public String GetPersonID()
    {
        return personID;
    }

    public String GetSpouseID()
    {
        return spouseID;
    }

    public String GetFatherID()
    {
        return fatherID;
    }

    public String GetMotherID()
    {
        return motherID;
    }

    public String GetAssociatedUsername()
    {
        return associatedUsername;
    }
}
