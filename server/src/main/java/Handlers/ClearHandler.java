package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

import Helpers.DatabaseHelper;
import Services.ClearService;

public class ClearHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();

        Boolean commit = new ClearService().Clear(exchange, db);

        db.CloseConnection(commit);
    }
}
