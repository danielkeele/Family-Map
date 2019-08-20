package com.example.familymap.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;
import com.example.familymap.HelperClasses.SearchResult;
import com.example.familymap.RecyclerViewHelpers.SearchRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Models.EventModel;
import Models.PersonModel;

public class PersonActivity extends AppCompatActivity
{
    private PersonModel person;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    private ArrayList<SearchResult> events;
    private RecyclerView lifeEvents;
    private RecyclerView.Adapter lifeEventsAdapter;
    private RecyclerView.LayoutManager lifeEventsLayoutManager;

    private ArrayList<SearchResult> persons;
    private RecyclerView familyMembers;
    private RecyclerView.Adapter familyMembersAdapter;
    private RecyclerView.LayoutManager familyMembersLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        GetPersonFromID();
        GetSortedPersonEvents();
        GetPersonFamilyRoles();
        GetWidgetReferences();
        PopulateWidgets();
    }

    public void ToggleEventListVisibility(View view)
    {
        TextView t = (TextView)view;

        if (lifeEvents.getVisibility() == View.VISIBLE)
        {
            lifeEvents.setVisibility(View.GONE);
            t.setText(R.string.life_events_closed);
        }
        else
        {
            lifeEvents.setVisibility(View.VISIBLE);
            t.setText(R.string.life_events_open);
        }
    }

    public void TogglePersonListVisibility(View view)
    {
        TextView t = (TextView)view;

        if (familyMembers.getVisibility() == View.VISIBLE)
        {
            familyMembers.setVisibility(View.GONE);
            t.setText(R.string.family_closed);
        }
        else
        {
            familyMembers.setVisibility(View.VISIBLE);
            t.setText(R.string.family_open);
        }
    }

    private void PopulateWidgets()
    {
        firstName.setText(person.GetFirstName());
        lastName.setText(person.GetLastName());
        gender.setText(person.GetGender());

        lifeEventsLayoutManager = new LinearLayoutManager(this);
        lifeEvents.setLayoutManager(lifeEventsLayoutManager);
        lifeEventsAdapter = new SearchRecyclerViewAdapter(events, this);
        lifeEvents.setAdapter(lifeEventsAdapter);

        familyMembersLayoutManager = new LinearLayoutManager(this);
        familyMembers.setLayoutManager(familyMembersLayoutManager);
        familyMembersAdapter = new SearchRecyclerViewAdapter(persons, this);
        familyMembers.setAdapter(familyMembersAdapter);
    }

    private void GetSortedPersonEvents()
    {
        events = new ArrayList<>();
        String personID = person.GetPersonID();

        ArrayList<SearchResult> birthEvents = new ArrayList<>();
        ArrayList<SearchResult> otherEvents = new ArrayList<>();
        ArrayList<SearchResult> deathEvents = new ArrayList<>();

        for (EventModel e : Model.GetInstance().GetFilteredEvents())
        {
            if (e.GetPersonID().equals(personID))
            {
                if (e.GetEventType().toLowerCase().equals("birth"))
                {
                    birthEvents.add(new SearchResult("event", e.GetEventID(), e.GetEventType(), e.GetCity(), e.GetCountry(), String.valueOf(e.GetYear()), (person.GetFirstName() + " " + person.GetLastName())));
                }
                else if (e.GetEventType().toLowerCase().equals("death"))
                {
                    deathEvents.add(new SearchResult("event", e.GetEventID(), e.GetEventType(), e.GetCity(), e.GetCountry(), String.valueOf(e.GetYear()), (person.GetFirstName() + " " + person.GetLastName())));
                }
                else
                {
                    otherEvents.add(new SearchResult("event", e.GetEventID(), e.GetEventType(), e.GetCity(), e.GetCountry(), String.valueOf(e.GetYear()), (person.GetFirstName() + " " + person.GetLastName())));
                }
            }
        }

        Collections.sort(birthEvents, eventComparator);
        Collections.sort(otherEvents, eventComparator);
        Collections.sort(deathEvents, eventComparator);

        events.addAll(birthEvents);
        events.addAll(otherEvents);
        events.addAll(deathEvents);
    }

    private Comparator<SearchResult> eventComparator = new Comparator<SearchResult>()
    {
        @Override
        public int compare(SearchResult o1, SearchResult o2)
        {
            if (o1.GetYear().equals(o2.GetYear()))
            {
                return o1.GetEventType().compareTo(o2.GetEventType());
            }
            else
            {
                return Integer.parseInt(o1.GetYear()) - Integer.parseInt(o2.GetYear());
            }
        }
    };

    private void GetPersonFamilyRoles()
    {
        String personID = person.GetPersonID();
        persons = new ArrayList<>();

        for (PersonModel pm : Model.GetInstance().GetPersons())
        {
            // parents, spouse, and children
            SearchResult toBeAdded = new SearchResult("person", pm.GetPersonID(),
                    pm.GetFirstName(), pm.GetLastName());

            if (pm.GetSpouseID() != null && pm.GetSpouseID().equals(personID))
            {
                toBeAdded.SetRole("spouse");
                persons.add(toBeAdded);
            }
            else if ((pm.GetFatherID() != null && pm.GetFatherID().equals(personID)) ||
                    (pm.GetMotherID() != null && pm.GetMotherID().equals(personID)))
            {
                toBeAdded.SetRole("child");
                persons.add(toBeAdded);
            }
            else if ((person.GetFatherID() != null && person.GetFatherID().equals(pm.GetPersonID())) || (person.GetMotherID() != null && person.GetMotherID().equals(pm.GetPersonID())))
            {
                toBeAdded.SetRole("parent");
                persons.add(toBeAdded);
            }
        }
    }

    private void GetWidgetReferences()
    {
        firstName = findViewById(R.id.personFirstName);
        lastName = findViewById(R.id.personLastName);
        gender = findViewById(R.id.personGender);
        lifeEvents = findViewById(R.id.lifeEventRecyclerView);
        familyMembers = findViewById(R.id.familyMembersRecyclerView);
    }

    private void GetPersonFromID()
    {
        String personID = getIntent().getStringExtra("personID");

        for (PersonModel p : Model.GetInstance().GetPersons())
        {
            if (p.GetPersonID().equals(personID))
            {
                person = p;
                break;
            }
        }
    }

}
