package Services;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

import Helpers.DatabaseHelper;
import Helpers.HTTPHelper;

public class ClearService
{
    public Boolean Clear(HttpExchange exchange, DatabaseHelper db) throws IOException
    {
        Boolean success = true;
        HTTPHelper httpHelper = new HTTPHelper(exchange);

        if (!db.DeleteTables() | !db.CreateTables())
        {
            httpHelper.RespondWithMessage("Internal server error", HttpURLConnection.HTTP_INTERNAL_ERROR);
            success = false;
            return success;
        }

        httpHelper.RespondWithMessage("Clear succeeded", HttpURLConnection.HTTP_OK);
        return success;
    }
}
