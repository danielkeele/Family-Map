package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import Helpers.DatabaseHelper;
import Services.EventService;
import Services.PersonService;

public class PersonHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] segments = path.split("/");
        int personIDIndex = 2;
        PersonService personService = new PersonService();
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();
        Boolean commit;
        String tokenValue = exchange.getRequestHeaders().get("Authorization").get(0);

        if (segments.length > personIDIndex)
        {
            commit = personService.GetPersonForPersonID(exchange, segments[personIDIndex], db, tokenValue);
        }
        else
        {
            commit = personService.GetAllPersonsForToken(exchange, db, tokenValue);
        }

        db.CloseConnection(commit);
    }
}
