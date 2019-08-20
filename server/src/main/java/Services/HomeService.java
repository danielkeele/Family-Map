package Services;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HomeService
{
    public Boolean GetStaticFiles(HttpExchange exchange, File file, Headers h) throws IOException
    {
        Boolean success = true;
        String path = file.getPath();

        String type = "text/html";
        String js = ".js";
        String css = ".css";
        String jpeg = ".jpeg";
        String jpg = ".jpg";
        if (path.substring(path.length() - js.length()).equals(js))
        {
            type = "application/javascript";
        }
        else if (path.substring(path.length() - css.length()).equals(css))
        {
            type = "text/css";
        }
        else if (path.substring(path.length() - jpeg.length()).equals(jpeg) || path.substring(path.length() - jpg.length()).equals(jpg))
        {
            type = "image/jpeg";
        }

        if (h != null)
        {
            h.add("Content-Type", type);
        }

        OutputStream out = null;

        if (exchange != null)
        {
            out = exchange.getResponseBody();
        }


        if (file.exists())
        {
            if (exchange != null)
            {
                exchange.sendResponseHeaders(200, file.length());
                out.write(Files.readAllBytes(file.toPath()));
                out.close();
            }

            return success;
        }
        else
        {
            if (exchange != null)
            {
                System.err.println("File not found: " + path);

                exchange.sendResponseHeaders(404, 0);
                out.write("404 File not found.".getBytes());
                out.close();
            }

            success = false;
            return success;
        }
    }
}
