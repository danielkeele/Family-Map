package com.example.familymap.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;

import Models.EventModel;

public class EventActivity extends AppCompatActivity
{
    private EventModel currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        GetEventFromEventID();
    }

    public Bundle SendDataBundle()
    {
        GetEventFromEventID();
        Bundle bundle = new Bundle();
        bundle.putString("eventID", currentEvent.GetEventID());
        bundle.putString("personID", currentEvent.GetPersonID());

        return bundle;
    }

    private void GetEventFromEventID()
    {
        String eventID = getIntent().getStringExtra("eventID");

        for (EventModel e : Model.GetInstance().GetEvents())
        {
            if (e.GetEventID().equals(eventID))
            {
                currentEvent = e;
                break;
            }
        }
    }
}
