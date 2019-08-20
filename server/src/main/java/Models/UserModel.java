package Models;

public class UserModel
{
    private String username;
    private String password;
    private String email;
    private String fName;
    private String lName;
    private String gender;
    private String personID;

    public UserModel(String username, String password, String email, String fName, String lName, String gender, String personID)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.personID = personID;
    }

    public String GetUsername()
    {
        return username;
    }

    public String GetPassword()
    {
        return password;
    }

    public String GetEmail()
    {
        return email;
    }

    public String GetfName()
    {
        return fName;
    }

    public String GetlName()
    {
        return lName;
    }

    public String GetGender()
    {
        return gender;
    }

    public String GetPersonID()
    {
        return personID;
    }

    public void SetUsername(String username)
    {
        this.username = username;
    }

    public void SetPassword(String password)
    {
        this.password = password;
    }

    public void SetEmail(String email)
    {
        this.email = email;
    }

    public void SetfName(String fName)
    {
        this.fName = fName;
    }

    public void SetlName(String lName)
    {
        this.lName = lName;
    }

    public void SetGender(String gender)
    {
        this.gender = gender;
    }

    public void SetPersonID(String personID)
    {
        this.personID = personID;
    }
}
