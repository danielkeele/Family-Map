package Services;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import DAOs.AuthTokenDAO;
import Helpers.DatabaseHelper;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import Helpers.HTTPHelper;
import Models.*;
import Services.ServiceRequestResponseObjects.RegisterRequest;
import Services.ServiceRequestResponseObjects.RegisterResponse;

public class RegisterService
{
    public Boolean Register(HttpExchange exchange, DatabaseHelper db, String reqData) throws IOException
    {
        Boolean success = true;
        Gson gson = new Gson();

        HTTPHelper httpHelper = new HTTPHelper(exchange);

        RegisterRequest newRegistree = gson.fromJson(reqData, RegisterRequest.class);

        if (newRegistree.GetUserName() == null || newRegistree.GetPassword() == null || newRegistree.GetEmail() == null ||
                newRegistree.GetFirstName() == null || newRegistree.GetLastName() == null || newRegistree.GetGender() == null)
        {
            httpHelper.RespondWithMessage("Request property missing", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }


        PersonDAO personDAO = new PersonDAO(db.GetConnection());
        PersonModel person = new PersonModel(db.GetConnection(), newRegistree.GetUserName(), newRegistree.GetFirstName(),
                newRegistree.GetLastName(), newRegistree.GetGender());

        UserModel user = new UserModel(newRegistree.GetUserName(), newRegistree.GetPassword(),
                newRegistree.GetEmail(), newRegistree.GetFirstName(),
                newRegistree.GetLastName(), newRegistree.GetGender(),
                person.GetPersonID());

        UserDAO userDAO = new UserDAO(db.GetConnection());

        if (userDAO.FindForUsername(newRegistree.GetUserName()) != null)
        {
            httpHelper.RespondWithMessage("Username already taken by another user", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        if (!userDAO.Insert(user))
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        AuthTokenDAO authTokenDAO = new AuthTokenDAO(db.GetConnection());
        AuthTokenModel authToken = new AuthTokenModel(newRegistree.GetUserName());

        if (!authTokenDAO.Insert(authToken))
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        if (!personDAO.Insert(person))
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        //create generations
        personDAO.GenerateAncestors(newRegistree.GetUserName(), db);

        RegisterResponse registerResponse = new RegisterResponse(String.valueOf(authToken.GetValue()), user.GetUsername(), person.GetPersonID());
        String jsonStr = gson.toJson(registerResponse);

        httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);

        return success;
    }
}
