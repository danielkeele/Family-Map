package com.example.familymap.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.familymap.Activities.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Models.EventModel;
import Models.PersonModel;
import Services.ServiceRequestResponseObjects.EventResponse;
import Services.ServiceRequestResponseObjects.EventResponseDetail;
import Services.ServiceRequestResponseObjects.LoginRequest;
import Services.ServiceRequestResponseObjects.PersonResponse;
import Services.ServiceRequestResponseObjects.PersonResponseDetail;
import Services.ServiceRequestResponseObjects.RegisterRequest;
import Services.ServiceRequestResponseObjects.RegisterResponse;

public class ServerProxy
{
    private Gson gson = new Gson();
    private Context context;

    //defaults
    private String serverHost = "10.0.2.2";
    private String serverPort = "8080";

    public ServerProxy(String serverHost, String serverPort, Context context)
    {
        //this.serverHost = serverHost;
        //this. serverPort = serverPort;
        this.context = context;
    }

    public ServerProxy(){};

    public class RegisterUser extends AsyncTask<String, String, RegisterResponse>
    {
        protected RegisterResponse doInBackground(String... parameters)
        {
            try
            {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");

                http.setDoOutput(true);

                http.addRequestProperty("Accept", "application/json");

                http.connect();
                RegisterRequest registerRequest = new RegisterRequest(parameters[0], parameters[1],
                                                                        parameters[2], parameters[3],
                                                                        parameters[4], parameters[5]);

                String reqData = gson.toJson(registerRequest);

                OutputStream reqBody = http.getOutputStream();

                WriteString(reqData, reqBody);

                reqBody.close();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream respBody = http.getInputStream();
                    String respData = ReadString(respBody);

                    RegisterResponse registerResponse = gson.fromJson(respData, RegisterResponse.class);
                    return registerResponse;
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(RegisterResponse registerResponse)
        {
            if (registerResponse == null)
            {
                Toast.makeText(context, "Unable to Register User", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Successfully registered.", Toast.LENGTH_SHORT).show();

                if (CommitToModel(registerResponse))
                {
                    Intent intent = new Intent(context, MainActivity.class);
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                }
            }
        }
    }

    public class SignIn extends AsyncTask<String, String, RegisterResponse>
    {
        protected RegisterResponse doInBackground(String... parameters)
        {
            try
            {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("POST");

                http.setDoOutput(true);

                http.addRequestProperty("Accept", "application/json");
                http.connect();

                LoginRequest loginRequest = new LoginRequest(parameters[0], parameters[1]);

                String reqData = gson.toJson(loginRequest);

                OutputStream reqBody = http.getOutputStream();

                WriteString(reqData, reqBody);

                reqBody.close();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream respBody = http.getInputStream();
                    String respData = ReadString(respBody);

                    RegisterResponse registerResponse = gson.fromJson(respData, RegisterResponse.class);
                    return registerResponse;
                }
                else
                {
                    Toast.makeText(context, "Unable to log in User", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(RegisterResponse registerResponse)
        {
            if (registerResponse == null)
            {
                Toast.makeText(context, "Unable to log in User", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Successfully logged in.", Toast.LENGTH_SHORT).show();

                if (CommitToModel(registerResponse))
                {
                    Intent intent = new Intent(context, MainActivity.class);
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                }
            }
        }
    }

    public class GetPersons extends AsyncTask<String, String, PersonResponse>
    {

        protected PersonResponse doInBackground(String... parameters)
        {
            try
            {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");

                http.setDoOutput(false);

                http.addRequestProperty("Authorization", parameters[0]);
                http.addRequestProperty("Accept", "application/json");

                http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream respBody = http.getInputStream();
                    String respData = ReadString(respBody);

                    PersonResponse personResponse = gson.fromJson(respData, PersonResponse.class);
                    return personResponse;
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(PersonResponse personResponse)
        {
            if (personResponse != null)
            {
                CommitToModel(personResponse);
            }
        }

    }

    public class GetEvents extends AsyncTask<String, String, EventResponse>
    {

        protected EventResponse doInBackground(String... parameters)
        {
            try
            {
                URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");

                http.setDoOutput(false);

                http.addRequestProperty("Authorization", parameters[0]);
                http.addRequestProperty("Accept", "application/json");

                http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    InputStream respBody = http.getInputStream();
                    String respData = ReadString(respBody);

                    EventResponse eventResponse = gson.fromJson(respData, EventResponse.class);
                    return eventResponse;
                }
                else
                {
                    return null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(EventResponse eventResponse)
        {
            if (eventResponse != null)
            {
                CommitToModel(eventResponse);
            }
        }
    }

    private Boolean CommitToModel(RegisterResponse registerResponse)
    {
        Boolean success = true;

        if (Model.GetInstance().InitializeModel(registerResponse.GetAuthToken()))
        {
            Model.GetInstance().SetAuthToken(registerResponse.GetAuthToken());
            Model.GetInstance().SetUserPersonID(registerResponse.GetPersonID());
        }
        else
        {
            success = false;
        }

        return success;
    }

    private void CommitToModel(PersonResponse personResponse)
    {
        for (PersonResponseDetail prd : personResponse.GetData())
        {
            Model.GetInstance().AddPerson(new PersonModel(prd.GetPersonID(), prd.GetAssociatedUsername(),
                    prd.GetFirstName(), prd.GetLastName(), prd.GetGender(),
                    prd.GetFatherID(), prd.GetMotherID(), prd.GetSpouseID()));
        }
    }

    private void CommitToModel(EventResponse eventResponse)
    {
        for (EventResponseDetail erd : eventResponse.GetData())
        {
            //check for duplicates of different cases
            for (EventModel e : Model.GetInstance().GetEvents())
            {
                if (e.GetEventType().toLowerCase().equals(erd.GetEventType().toLowerCase()))
                {
                    //match the case of the first one.
                    erd.SetEventType(e.GetEventType());
                    break;
                }
            }

            Model.GetInstance().AddEvent(new EventModel(erd.GetEventID(), erd.GetAssociatedUsername(),
                    erd.GetPersonID(), erd.GetLatitude(),
                    erd.GetLongitude(), erd.GetCountry(),
                    erd.GetCity(), erd.GetEventType(), erd.GetYear()));
        }
    }

    private static String ReadString(InputStream is) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int length;

        while ((length = streamReader.read(buffer)) > 0)
        {
            stringBuilder.append(buffer, 0, length);
        }

        return stringBuilder.toString();
    }

    private static void WriteString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter streamWriter = new OutputStreamWriter(os);
        streamWriter.write(str);
        streamWriter.flush();
    }
}
