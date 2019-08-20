package Services;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import Helpers.DatabaseHelper;
import DAOs.EventDAO;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import Helpers.HTTPHelper;
import Models.UserModel;

public class FillService
{
    public Boolean Fill(HttpExchange exchange, String username, DatabaseHelper db, String generationsString) throws IOException
    {
        Gson gson = new Gson();
        Boolean success = true;

        HTTPHelper httpHelper = new HTTPHelper(exchange);

        //check that the user is in the database
        UserDAO userDAO = new UserDAO(db.GetConnection());
        UserModel user = userDAO.FindForUsername(username);

        if (user == null)
        {
            httpHelper.RespondWithMessage("Username not in system", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        //delete all events with that user's username
        if (!new EventDAO(db.GetConnection()).DeleteAllWithUsername(username))
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        PersonDAO personDAO = new PersonDAO(db.GetConnection());

        //delete all persons with that user's username (except the person with the same personID)
        if (!personDAO.DeleteAllAssociatedPeopleForUsername(user.GetUsername(), user.GetPersonID()))
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        int peopleAdded = (int)Math.pow(2, 4 + 1) - 1;
        int eventsCreated = (peopleAdded * 3) - 2;

        //check if there are generations specified, and that it's a non-negative integer
        if (generationsString != null)
        {
            int generations = Integer.parseInt(generationsString);
            peopleAdded = (int)Math.pow(2, generations + 1) - 1;
            eventsCreated = (peopleAdded * 3) - 2;

            personDAO.GenerateAncestors(user.GetUsername(), generations, db);
        }
        else
        {
            personDAO.GenerateAncestors(user.GetUsername(), db);
        }

        httpHelper.RespondWithMessage("Successfully added " + peopleAdded + " persons and " + eventsCreated + " events to the database.", HttpURLConnection.HTTP_OK);
        return success;
    }
}
