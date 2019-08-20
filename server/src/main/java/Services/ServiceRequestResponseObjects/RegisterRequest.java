package Services.ServiceRequestResponseObjects;

public class RegisterRequest
{
    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;

    public RegisterRequest(){};

    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender)
    {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

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
}
