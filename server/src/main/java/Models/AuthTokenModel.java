package Models;

import java.util.Random;

public class AuthTokenModel
{
    private int value;
    private String username;

    public AuthTokenModel(String username)
    {
        this.value = GenerateAuthToken();
        this.username = username;
    }

    private int GenerateAuthToken()
    {
        int tokenLength = 8;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int x = 1; x <= tokenLength; x++)
        {
            sb.append(r.nextInt(9));
        }

        return Integer.parseInt(sb.toString());
    }

    public int GetValue()
    {
        return value;
    }

    public String GetUsername()
    {
        return username;
    }

    public void SetValue(int value)
    {
        this.value = value;
    }

    public void SetUsername(String username)
    {
        this.username = username;
    }
}
