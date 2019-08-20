package Services;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import DAOs.AuthTokenDAO;
import Helpers.DatabaseHelper;
import DAOs.PersonDAO;
import Helpers.HTTPHelper;
import Models.*;
import Services.ServiceRequestResponseObjects.PersonResponse;
import Services.ServiceRequestResponseObjects.PersonResponseDetail;

public class PersonService
{
    public Boolean GetAllPersonsForToken(HttpExchange exchange, DatabaseHelper db, String tokenValue) throws IOException
    {
        Boolean success = true;

        Gson gson = new Gson();
        String jsonStr;

        HTTPHelper httpHelper = new HTTPHelper(exchange);

        AuthTokenModel authToken = new AuthTokenDAO(db.GetConnection()).FindForTokenID(tokenValue);

        if (authToken == null)
        {
            httpHelper.RespondWithMessage("AuthToken not in system", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        String username = authToken.GetUsername();

        PersonModel[] persons = new PersonDAO(db.GetConnection()).FindForUsername(username);

        PersonResponseDetail[] personResponseDetails = new PersonResponseDetail[persons.length];

        for (int x = 0; x < persons.length; x++)
        {
            PersonModel current = persons[x];
            personResponseDetails[x] = new PersonResponseDetail(current.GetUserName(), current.GetPersonID(),
                    current.GetFirstName(), current.GetLastName(),
                    current.GetGender(), current.GetFatherID(),
                    current.GetMotherID(), current.GetSpouseID());
        }

        jsonStr = gson.toJson(new PersonResponse(personResponseDetails));

        httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);
        return success;
    }

    public Boolean GetPersonForPersonID(HttpExchange exchange, String personID, DatabaseHelper db, String tokenValue) throws IOException
    {
        Boolean success = true;
        Gson gson = new Gson();

        HTTPHelper httpHelper = new HTTPHelper(exchange);

        AuthTokenModel authToken = new AuthTokenDAO(db.GetConnection()).FindForTokenID(tokenValue);

        if (authToken == null)
        {
            httpHelper.RespondWithMessage("AuthToken not in system", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        String username = authToken.GetUsername();

        PersonModel person = new PersonDAO(db.GetConnection()).FindForPersonID(personID, username);

        if (person == null)
        {
            httpHelper.RespondWithMessage("Person not in system", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        PersonResponseDetail personResponseDetail = new PersonResponseDetail(person.GetUserName(), person.GetPersonID(),
                                                                                person.GetFirstName(), person.GetLastName(),
                                                                                person.GetGender(), person.GetFatherID(),
                                                                                person.GetMotherID(), person.GetSpouseID());

        String jsonStr = gson.toJson(personResponseDetail);

        httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);
        return success;
    }

}



