package com.example.familymap.HelperClasses;

import android.app.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import Models.*;

public class Model
{
    private String authToken;
    private String userPersonID;
    private ArrayList<PersonModel> persons = new ArrayList<>();
    private ArrayList<EventModel> events = new ArrayList<>();
    private Setting setting = new Setting(true, true,true,"Normal");
    private Filter filter = new Filter();
    private static final Model instance = new Model();

    public static Model GetInstance()
    {
        return instance;
    }

    public static Boolean IsReady()
    {
        return (GetInstance().GetPersons().size() != 0);
    }

    public ArrayList<PersonModel> GetPersons()
    {
        return persons;
    }

    public ArrayList<EventModel> GetEvents()
    {
        return events;
    }

    public Setting GetSetting()
    {
        return setting;
    }

    public ArrayList<EventModel> GetFilteredEvents()
    {
        ArrayList<EventModel> filteredEvents = new ArrayList<>(GetInstance().GetEvents());
        ArrayList<String> offFilters = new ArrayList<>();
        PersonModel userPerson = Model.GetInstance().GetPersonForID(Model.GetInstance().GetUserPersonID());

        Iterator it = Model.GetInstance().GetFilter().GetEventTypeToBooleanMapping().entrySet().iterator();

        //get all of the filters that are off
        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            if (!(Boolean)pair.getValue())
            {
                offFilters.add((String)pair.getKey());
            }
        }

        //make a list of people on father's side
        PersonModel father = Model.GetInstance().GetPersonForID(userPerson.GetFatherID());
        ArrayList<PersonModel> fatherSide = GetAllPeopleInTreeFromPersonUpward(father);

        //make a list of people on mother's side
        PersonModel mother = Model.GetInstance().GetPersonForID(userPerson.GetMotherID());
        ArrayList<PersonModel> motherSide = GetAllPeopleInTreeFromPersonUpward(mother);

        //cycle through all of the off filters, removing those event types from filteredEvents.
        for (int x = 0; x < offFilters.size(); x++)
        {
            String currentOffFilter = offFilters.get(x);

            for (int y = 0; y < filteredEvents.size(); y++)
            {
                EventModel currentfilteredEvent = filteredEvents.get(y);

                if (currentOffFilter.equals(currentfilteredEvent.GetEventType()))
                {
                    filteredEvents.remove(y);
                    y--;
                }
                else if (currentOffFilter.equals("Father's Side"))
                {
                    for (int z = 0; z < fatherSide.size(); z++)
                    {
                        PersonModel currentFatherSidePerson = fatherSide.get(z);
                        if (currentfilteredEvent.GetPersonID().equals(currentFatherSidePerson.GetPersonID()))
                        {
                            filteredEvents.remove(y);
                            y--;
                        }
                    }
                }
                else if (currentOffFilter.equals("Mother's Side"))
                {
                    for (int z = 0; z < motherSide.size(); z++)
                    {
                        PersonModel currentMotherSidePerson = motherSide.get(z);
                        if (currentfilteredEvent.GetPersonID().equals(currentMotherSidePerson.GetPersonID()))
                        {
                            filteredEvents.remove(y);
                            y--;
                        }
                    }
                }
                else if (currentOffFilter.equals("Male") || offFilters.get(x).equals("Female"))
                {
                    String personId = currentfilteredEvent.GetPersonID();
                    String gender = Model.GetInstance().GetPersonForID(personId).GetGender();

                    if (gender.equals("m") && currentOffFilter.equals("Male"))
                    {
                        filteredEvents.remove(y);
                        y--;
                    }
                    else if ((gender.equals("f") && currentOffFilter.equals("Female")))
                    {
                        filteredEvents.remove(y);
                        y--;
                    }
                }
            }
        }

        return filteredEvents;
    }

    public Filter GetFilter()
    {
        return filter;
    }

    public Boolean InitializeModel(String authTokenValue)
    {
        Clear();

        try
        {
            //get all persons
            ServerProxy.GetPersons getPersons = new ServerProxy().new GetPersons();
            if (getPersons.execute(authTokenValue).get() == null)
            {
                Clear();
                return false;
            }

            //get all events
            ServerProxy.GetEvents getEvents = new ServerProxy().new GetEvents();
            if (getEvents.execute(authTokenValue).get() == null)
            {
                Clear();
                return false;
            }
        }
        catch (Exception e)
        {
            //catch any problems
            return false;
        }

        return true;
    }

    public void Clear()
    {
        persons = new ArrayList<>();
        events = new ArrayList<>();
    }

    public PersonModel GetPersonForID(String personID)
    {
        for (PersonModel p : persons)
        {
            if (p.GetPersonID().equals(personID))
            {
                return p;
            }
        }

        return null;
    }

    public void ResetSettings()
    {
        setting = new Setting(true, true,true,"Normal");
    }

    public void AddPerson(PersonModel personModel)
    {
        persons.add(personModel);
    }

    public void SetAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public void SetUserPersonID(String personID)
    {
        this.userPersonID = personID;
    }

    public String GetAuthToken()
    {
        return authToken;
    }

    public void AddEvent(EventModel eventModel)
    {
        events.add(eventModel);
    }

    public ArrayList<EventModel> GetAllFilteredEventsForPerson(PersonModel p)
    {
        ArrayList<EventModel> personEvents = new ArrayList<>();

        for (EventModel e : Model.GetInstance().GetFilteredEvents())
        {
            if (e.GetPersonID().equals(p.GetPersonID()))
            {
                personEvents.add(e);
            }
        }

        return personEvents;
    }

    private ArrayList<PersonModel> GetAllPeopleInTreeFromPersonUpward(PersonModel person)
    {
        ArrayList<PersonModel> allPeople = new ArrayList<>();
        ArrayList<PersonModel> currentGeneration = new ArrayList<>();
        ArrayList<PersonModel> nextGeneration = new ArrayList<>();

        currentGeneration.add(person);

        while (currentGeneration.size() != 0)
        {
            for (int x = 0; x < currentGeneration.size(); x++)
            {
                PersonModel currentPerson = currentGeneration.get(x);

                if (currentPerson.GetFatherID() != null)
                {
                    nextGeneration.add(Model.GetInstance().GetPersonForID(currentPerson.GetFatherID()));
                }

                if (currentPerson.GetMotherID() != null)
                {
                    nextGeneration.add(Model.GetInstance().GetPersonForID(currentPerson.GetMotherID()));
                }
            }

            allPeople.addAll(currentGeneration);
            currentGeneration = new ArrayList<>(nextGeneration);
            nextGeneration.clear();
        }

        return allPeople;
    }

    private String GetUserPersonID()
    {
        return userPersonID;
    }
}
