package Handlers;

import java.io.*;
import java.net.URI;

import com.sun.net.httpserver.*;

import Services.HomeService;

public class HomeHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        URI url = exchange.getRequestURI();
        String baseDirectory = "/Users/danielkeele/AndroidStudioProjects/FamilyMap/server/src/web";
        File file;

        if (url.toString().equals("/"))
        {
            file = new File(baseDirectory, "index.html");
        }
        else
        {
            file = new File(baseDirectory, url.toString());
        }

        Headers h = exchange.getResponseHeaders();

        new HomeService().GetStaticFiles(exchange, file, h);
    }
}
