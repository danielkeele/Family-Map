package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import Helpers.DatabaseHelper;
import Services.LoadService;

public class LoadHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();

        Boolean commit = new LoadService().Load(exchange, db);

        db.CloseConnection(commit);
    }
}
