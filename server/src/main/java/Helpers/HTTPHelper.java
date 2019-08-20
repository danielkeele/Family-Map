package Helpers;

import com.google.gson.Gson;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;

import Services.ServiceRequestResponseObjects.Message;

public class HTTPHelper
{
    HttpExchange httpExchange;

    public HTTPHelper(HttpExchange httpExchange)
    {
        this.httpExchange = httpExchange;
    }

    public void RespondWithMessage(String message, int code) throws IOException
    {
        Gson gson = new Gson();
        Message messageObject = new Message(message);
        String jsonMessage = gson.toJson(messageObject);

        RespondWithJson(jsonMessage, code);
    }

    public void RespondWithJson(String json, int code) throws IOException
    {
        if (this.httpExchange != null)
        {
            httpExchange.sendResponseHeaders(code, 0);
            httpExchange.getResponseBody().write(json.getBytes());
            httpExchange.getResponseBody().close();
        }
    }
}
