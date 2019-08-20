package Services.ServiceRequestResponseObjects;

public class LoadRequestUserDetail
{
    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;
    String personID;

    public String GetUserName()
    {
        return userName;
    }

    public String GetPassword()
    {
        return password;
    }

    public String GetEmail()
    {
        return email;
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

    public String GetPersonID()
    {
        return personID;
    }
}
