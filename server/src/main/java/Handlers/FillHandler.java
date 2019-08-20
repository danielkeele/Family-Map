package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;

import Helpers.DatabaseHelper;
import Services.FillService;

public class FillHandler implements HttpHandler
{

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] segments = path.split("/");
        int usernameIndex = 2;
        int generationsIndex = 3;
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();
        String generationsString = null;

        if (segments.length > generationsIndex)
        {
            int generations;

            try
            {
                generations = Integer.parseInt(segments[generationsIndex]);

                if (generations < 0)
                {
                    throw new Exception();
                }

                generationsString = String.valueOf(generations);
            }
            catch (Exception e)
            {
                generationsString = null;
            }
        }

        Boolean commit = new FillService().Fill(exchange, segments[usernameIndex], db, generationsString);
        db.CloseConnection(commit);
    }
}
