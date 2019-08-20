package Services;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import DAOs.AuthTokenDAO;
import Helpers.DatabaseHelper;
import DAOs.EventDAO;
import Helpers.HTTPHelper;
import Models.*;
import Services.ServiceRequestResponseObjects.EventResponse;
import Services.ServiceRequestResponseObjects.EventResponseDetail;

public class EventService
{
    public Boolean GetAllEventsForToken(HttpExchange exchange, DatabaseHelper db, String tokenValue) throws IOException
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

        EventModel[] events = new EventDAO(db.GetConnection()).FindForUsername(username);

        EventResponseDetail[] eventResponseDetails = new EventResponseDetail[events.length];

        for (int x = 0; x < events.length; x++)
        {
            eventResponseDetails[x] = new EventResponseDetail(events[x].GetUsername(), events[x].GetEventID(),
                    events[x].GetPersonID(), events[x].GetLatitude(),
                    events[x].GetLongitude(), events[x].GetCountry(),
                    events[x].GetCity(), events[x].GetEventType(),
                    events[x].GetYear());
        }

        jsonStr = gson.toJson(new EventResponse(eventResponseDetails));

        httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);
        return success;
    }

    public Boolean GetEventForEventID(HttpExchange exchange, String eventID, DatabaseHelper db, String tokenValue) throws IOException
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

        EventModel event = new EventDAO(db.GetConnection()).FindForEventID(eventID, username);

        if (event == null)
        {
            httpHelper.RespondWithMessage("Event not in system", HttpURLConnection.HTTP_BAD_REQUEST);
            success = false;
            return success;
        }

        EventResponseDetail eventResponseDetail = new EventResponseDetail(event.GetUsername(), event.GetEventID(), event.GetPersonID(),
                event.GetLatitude(), event.GetLongitude(), event.GetCountry(), event.GetCity(),
                event.GetEventType(), event.GetYear());

        jsonStr = gson.toJson(eventResponseDetail);

        httpHelper.RespondWithJson(jsonStr, HttpURLConnection.HTTP_OK);
        return success;
    }
}
