package com.example.familymap.HelperClasses;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Models.EventModel;

public class Filter
{
    private Set<String> allEventTypes;
    private Map<String, Boolean> eventTypeBooleanMapping;

    public void InitializeFilter()
    {
        GetAllEventTypes();
        InitializeAllEventTypes();
    }

    public Boolean IsInitialized()
    {
        return (allEventTypes != null && eventTypeBooleanMapping != null);
    }

    public void Clear()
    {
        allEventTypes = null;
        eventTypeBooleanMapping = null;
    }

    public Map<String, Boolean> GetEventTypeToBooleanMapping()
    {
        return eventTypeBooleanMapping;
    }

    public void SetBoolForEventType(String eventType, Boolean b)
    {
        eventTypeBooleanMapping.put(eventType, b);
    }

    private void InitializeAllEventTypes()
    {
        eventTypeBooleanMapping = new HashMap<>();

        Iterator<String> it = allEventTypes.iterator();

        while (it.hasNext())
        {
            eventTypeBooleanMapping.put(it.next(), true);
        }
    }

    private void GetAllEventTypes()
    {
        allEventTypes = new HashSet<>();

        for (EventModel e : Model.GetInstance().GetEvents())
        {
            allEventTypes.add(e.GetEventType());
        }

        allEventTypes.add("Father's Side");
        allEventTypes.add("Mother's Side");
        allEventTypes.add("Male");
        allEventTypes.add("Female");
    }
}
