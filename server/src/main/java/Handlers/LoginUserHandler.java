package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

import Helpers.DatabaseHelper;
import Services.LoginService;

public class LoginUserHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();

        Boolean commit = new LoginService().Login(exchange, db);
        db.CloseConnection(commit);
    }
}
