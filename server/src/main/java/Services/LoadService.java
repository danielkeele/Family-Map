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
import Models.*;
import Services.ServiceRequestResponseObjects.LoadRequest;
import Services.ServiceRequestResponseObjects.LoadRequestEventDetail;
import Services.ServiceRequestResponseObjects.LoadRequestPersonDetail;
import Services.ServiceRequestResponseObjects.LoadRequestUserDetail;

public class LoadService
{
    public Boolean Load(HttpExchange exchange, DatabaseHelper db) throws IOException
    {
        Boolean success = true;
        Gson gson = new Gson();

        HTTPHelper httpHelper = new HTTPHelper(exchange);
        String reqData = null;

        if (exchange != null)
        {
            reqData = db.ReadString(exchange.getRequestBody());
        }

        if (!db.DeleteTables() | !db.CreateTables())
        {
            httpHelper.RespondWithMessage("Internal Server Error", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        if (exchange != null)
        {
            LoadRequest loadRequest = gson.fromJson(reqData, LoadRequest.class);
            int usersAdded;
            int personsAdded;
            int eventsAdded;

            UserDAO userDAO = new UserDAO(db.GetConnection());
            LoadRequestUserDetail[] users = loadRequest.GetUsers();
            usersAdded = users.length;
            for (LoadRequestUserDetail user:users)
            {
                userDAO.Insert(new UserModel(user.GetUserName(), user.GetPassword(), user.GetEmail(), user.GetFirstName(), user.GetLastName(), user.GetGender(), user.GetPersonID()));
            }

            PersonDAO personDAO = new PersonDAO(db.GetConnection());
            LoadRequestPersonDetail[] persons = loadRequest.GetPersons();
            personsAdded = persons.length;
            for (LoadRequestPersonDetail person:persons)
            {
                PersonModel pm = new PersonModel(db.GetConnection(), person.GetAssociatedUsername(), person.GetFirstName(), person.GetLastName(), person.GetGender(), person.GetFatherID(), person.GetMotherID(), person.GetSpouseID());
                pm.SetPersonID(person.GetPersonID());
                personDAO.Insert(pm);
            }

            EventDAO eventDAO = new EventDAO(db.GetConnection());
            LoadRequestEventDetail[] events = loadRequest.GetEvents();
            eventsAdded = events.length;
            for (LoadRequestEventDetail event:events)
            {
                EventModel em = new EventModel(db.GetConnection(), event.GgetAssociatedUsername(), event.GetPersonID(), Float.parseFloat(event.GetLatitude()), Float.parseFloat(event.GetLongitude()), event.GetCountry(), event.GetCity(), event.GetEventType(), Integer.parseInt(event.GetYear()));
                em.SetEventID(event.GetEventID());
                eventDAO.Insert(em);

            }

            httpHelper.RespondWithMessage("Successfully added " + usersAdded + " users, " + personsAdded + " persons, and " + eventsAdded + " events to the database.", HttpURLConnection.HTTP_OK);
        }

        return success;
    }
}
