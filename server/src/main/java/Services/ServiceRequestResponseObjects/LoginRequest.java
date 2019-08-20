package Services.ServiceRequestResponseObjects;

public class LoginRequest
{
    String userName;
    String password;

    public LoginRequest(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public String GetUserName()
    {
        return userName;
    }

    public String GetPassword()
    {
        return password;
    }
}
