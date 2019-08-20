package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import Helpers.DatabaseHelper;
import Services.EventService;

public class EventHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] segments = path.split("/");
        int eventIDIndex = 2;
        EventService eventService = new EventService();
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();
        Boolean commit;
        String tokenValue = exchange.getRequestHeaders().get("Authorization").get(0);

        if (segments.length > 2)
        {
            commit = eventService.GetEventForEventID(exchange, segments[eventIDIndex], db, tokenValue);
        }
        else
        {
            commit = eventService.GetAllEventsForToken(exchange, db, tokenValue);
        }

        db.CloseConnection(commit);
    }
}
