package Services.ServiceRequestResponseObjects;

public class RegisterResponse
{
        String authToken;
        String userName;
        String personID;

        public RegisterResponse(String authToken, String userName, String personID)
        {
                this.authToken = authToken;
                this.userName = userName;
                this.personID = personID;
        }

        public String GetAuthToken()
        {
                return authToken;
        }

        public String GetPersonID()
        {
                return personID;
        }
}
