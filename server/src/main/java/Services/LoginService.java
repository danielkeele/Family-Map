package Services;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import DAOs.AuthTokenDAO;
import Helpers.DatabaseHelper;
import DAOs.UserDAO;
import Helpers.HTTPHelper;
import Models.*;
import Services.ServiceRequestResponseObjects.LoginRequest;
import Services.ServiceRequestResponseObjects.RegisterResponse;

public class LoginService
{
    public Boolean Login(HttpExchange exchange, DatabaseHelper db) throws IOException
    {
        Boolean success = true;

        Gson gson = new Gson();

        HTTPHelper httpHelper = new HTTPHelper(exchange);

        if (exchange != null)
        {
            String reqData = db.ReadString(exchange.getRequestBody());
            LoginRequest loginRequest = gson.fromJson(reqData, LoginRequest.class);

            if (loginRequest.GetUserName() == null || loginRequest.GetPassword() == null)
            {
                httpHelper.RespondWithMessage("Request property missing", HttpURLConnection.HTTP_BAD_REQUEST);
                success = false;
                return success;
            }

            UserDAO userDAO = new UserDAO(db.GetConnection());
            UserModel user = userDAO.FindForUsername(loginRequest.GetUserName());

            if (user == null)
            {
                httpHelper.RespondWithMessage("Username not in system", HttpURLConnection.HTTP_BAD_REQUEST);
                success = false;
                return success;
            }

            if (!user.GetPassword().equals(loginRequest.GetPassword()))
            {
                httpHelper.RespondWithMessage("Incorrect password", HttpURLConnection.HTTP_BAD_REQUEST);
                success = false;
                return success;
            }

            AuthTokenModel authToken = new AuthTokenModel(loginRequest.GetUserName());
            RegisterResponse registerResponse = new RegisterResponse(String.valueOf(authToken.GetValue()), user.GetUsername(), user.GetPersonID());

            new AuthTokenDAO(db.GetConnection()).Insert(authToken);

            String jsonStr = gson.toJson(registerResponse);

            httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);
        }

        return success;
    }
}
