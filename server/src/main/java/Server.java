import Handlers.*;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class Server
{
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void Run(String portNumber)
    {
        System.out.println("Initializing HTTP Server");

        try
        {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");
        server.createContext("/", new HomeHandler());
        server.createContext("/user/register", new RegisterUserHandler());
        server.createContext("/user/login", new LoginUserHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());

        System.out.println("Starting server");
        server.start();

        System.out.println("Server started on port " + portNumber);
    }

    public static void main(String[] args)
    {
        //String portNumber = args[0];
        String portNumber = "8080";
        new Server().Run(portNumber);
    }
}