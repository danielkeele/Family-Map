package Handlers;

import java.io.*;
import com.sun.net.httpserver.*;

import Helpers.DatabaseHelper;
import Services.RegisterService;

public class RegisterUserHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        DatabaseHelper db = new DatabaseHelper();
        db.OpenConnection();
        String reqData = db.ReadString(exchange.getRequestBody());

        Boolean commit = new RegisterService().Register(exchange, db, reqData);

        db.CloseConnection(commit);
    }
}
