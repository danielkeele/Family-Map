package com.example.familymap.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.familymap.Activities.EventActivity;
import com.example.familymap.Activities.PersonActivity;
import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Models.EventModel;
import Models.PersonModel;

public class MapsFragment extends Fragment implements OnMapReadyCallback
{
    private GoogleMap map;
    private PersonModel person;
    private EventModel event;
    private TextView eventInfo;
    private TextView buttonArrow;
    private ArrayList<Polyline> lines = new ArrayList<>();
    private int spouseAndLifeStoryLineWidth = 5;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                                                                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        InitializeWidgets(view);
        SetInfoBoxListener();
        GetCurrentEventAndPersonIfExist();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;


        if (event != null)
        {
            GoToCurrentEvent();
            InitializeInfoBox();
            ClearLines();
            CreateAllLines();
        }

        CreateMarkers();
        SetMapType();
        SetMarkerListener();
    }

    private void SetMarkerListener()
    {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                String eventID = marker.getTag().toString();

                for (EventModel e : Model.GetInstance().GetFilteredEvents())
                {
                    if (e.GetEventID().equals(eventID))
                    {
                        event = e;
                        break;
                    }
                }

                for (PersonModel p : Model.GetInstance().GetPersons())
                {
                    if (p.GetPersonID().equals(event.GetPersonID()))
                    {
                        person = p;
                        break;
                    }
                }

                if (event != null)
                {
                    GoToCurrentEvent();
                }

                if (person != null)
                {
                    InitializeInfoBox();
                    ClearLines();
                    CreateAllLines();
                }

                return true;
            }
        });
    }

    private void GoToCurrentEvent()
    {
        LatLng center = new LatLng(event.GetLatitude(), event.GetLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(center));
    }

    private void InitializeInfoBox()
    {
        String eventText = person.GetFirstName() + " " + person.GetLastName() + " (" + person.GetGender() + ")\n" + event.GetEventType() +
                ": " + event.GetCity() + ", " + event.GetCountry() + "\n" + "(" + event.GetYear() +
                ")";

        eventInfo.setText(eventText);
        buttonArrow.setVisibility(View.VISIBLE);
    }

    private void InitializeWidgets(View view)
    {
        eventInfo = view.findViewById(R.id.event_info);
        buttonArrow = view.findViewById(R.id.arrow_button);
        buttonArrow.setVisibility(View.INVISIBLE);
    }

    private void GetCurrentEventAndPersonIfExist()
    {
        try
        {
            EventActivity eventActivity = (EventActivity)getActivity();
            Bundle b = eventActivity.SendDataBundle();

            String personID = b.getString("personID");
            String eventID = b.getString("eventID");

            for (PersonModel p : Model.GetInstance().GetPersons())
            {
                if (p.GetPersonID().equals(personID))
                {
                    person = p;
                    break;
                }
            }

            for (EventModel e : Model.GetInstance().GetFilteredEvents())
            {
                if (e.GetEventID().equals(eventID))
                {
                    event = e;
                    break;
                }
            }
        }
        catch (Exception e)
        {
            //if you make it here, the fragment is called from the main activity.
        }
    }

    private void SetInfoBoxListener()
    {
        eventInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), PersonActivity.class);

                intent.putExtra("personID", person.GetPersonID());
                getActivity().startActivity(intent);
            }

        });
    }

    private void SetMapType()
    {
        String type = Model.GetInstance().GetSetting().GetMapType();
        int googleType = GoogleMap.MAP_TYPE_HYBRID;

        if (type.equals("Normal"))
        {
            googleType = GoogleMap.MAP_TYPE_NORMAL;
        }
        else if (type.equals("Hybrid"))
        {
            googleType = GoogleMap.MAP_TYPE_HYBRID;
        }
        else if (type.equals("Satellite"))
        {
            googleType = GoogleMap.MAP_TYPE_SATELLITE;
        }
        else if (type.equals("Terrain"))
        {
            googleType = GoogleMap.MAP_TYPE_TERRAIN;
        }

        map.setMapType(googleType);
    }

    private void ClearLines()
    {
        if (lines != null)
        {
            for (Polyline p : lines)
            {
                p.remove();
            }
        }

        lines = new ArrayList<>();
    }

    private void CreateMarkers()
    {
        for (EventModel e : Model.GetInstance().GetFilteredEvents())
        {
            LatLng position = new LatLng(e.GetLatitude(), e.GetLongitude());
            BitmapDescriptor color = GetColorForEvent(e);

            MarkerOptions marker = new MarkerOptions();
            marker.position(position).icon(color);

            Marker m = map.addMarker(marker);
            m.setTag(e.GetEventID());
        }
    }

    private BitmapDescriptor GetColorForEvent(EventModel e)
    {
        BitmapDescriptor color = null;

        if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Red"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Blue"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Green"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Purple"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Rose"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Orange"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Cyan"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Magenta"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Yellow"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        }
        else if (Model.GetInstance().GetSetting().GetMarkerColorValueForKey(e.GetEventType()).equals("Azure"))
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }
        else
        {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
        }

        return color;
    }

    private void CreateAllLines()
    {
        CreateSpouseLines();
        CreateFamilyTreeLines();
        CreateLifeStoryLines();
    }

    private void CreateLifeStoryLines()
    {
        /*
        Life Story Line
        Lines are drawn connecting each event in a person’s life story (i.e., the person
        associated with the selected event), ordered chronologically. (See the Person Activity
        section for information on how events are ordered chronologically.) If some of the
        person’s life events are invisible due to the current event filter settings, connect whatever
        events are visible.
        */

        if (Model.GetInstance().GetSetting().GetLifeStoryLines())
        {
            Comparator<EventModel> eventComparator = new Comparator<EventModel>()
            {
                @Override
                public int compare(EventModel o1, EventModel o2)
                {
                    if (o1.GetYear() == o2.GetYear())
                    {
                        return o1.GetEventType().compareTo(o2.GetEventType());
                    }
                    else
                    {
                        return o1.GetYear() - o2.GetYear();
                    }
                }
            };

            ArrayList<EventModel> personEvents = Model.GetInstance().GetAllFilteredEventsForPerson(person);

            Collections.sort(personEvents, eventComparator);

            for (int x = 0; x < personEvents.size() - 1; x++)
            {
                DrawLineForEvent(personEvents.get(x), personEvents.get(x + 1), spouseAndLifeStoryLineWidth, "lifeStory");
            }
        }
    }

    private void CreateFamilyTreeLines()
    {
        /*
        Family Tree Lines
        Lines linking the selected event to the person’s ancestors (i.e., the person
        associated with the selected event) are drawn as follows:

             A line is drawn between the selected event and the birth event of the selected
            person’s father. If the person’s father does not have a birth event, or if their birth
            event is currently invisible due to the current event filter settings, the earliest
            available event for the father is used instead. If the person has no recorded
            father, or the recorded father has no events, no line is drawn.

             A line is drawn between the selected event and the birth event of the selected
            person’s mother. The same logic that applies to the father also applies to the
            mother.

             Lines are drawn from parents’ birth events to grandparents’ birth
            events, from grandparents’ birth events to great grandparents’ birth events, etc.
            including all available generations. In all cases, if a person’s birth event does not
            exist, or is invisible due to the current event filter settings, the earliest event in the
            person’s life should be used instead of their birth event. As lines are drawn
            recursively up the family tree, they should become progressively and noticeably
            thinner.
        */

        if (Model.GetInstance().GetSetting().GetFamilyTreeLines())
        {
            ArrayList<PersonModel> currentGeneration = new ArrayList<>();
            ArrayList<PersonModel> nextGeneration = new ArrayList<>();
            Boolean firstRunThrough = true;
            int lineWidth = Model.GetInstance().GetPersons().size();

            currentGeneration.add(person);

            while (currentGeneration.size() != 0)
            {
                for (int x = 0; x < currentGeneration.size(); x++)
                {
                    PersonModel currentPerson = currentGeneration.get(x);

                    PersonModel father = Model.GetInstance().GetPersonForID(currentPerson.GetFatherID());
                    PersonModel mother = Model.GetInstance().GetPersonForID(currentPerson.GetMotherID());

                    if (father != null)
                    {
                        nextGeneration.add(father);

                        EventModel fatherBirth = GetBirthOrEarliestEventForPerson(father);

                        if (firstRunThrough)
                        {
                            DrawLineForEvent(event, fatherBirth, lineWidth, "familyTree");
                        }
                        else
                        {
                            EventModel currentPersonBirth = GetBirthOrEarliestEventForPerson(currentPerson);
                            DrawLineForEvent(currentPersonBirth, fatherBirth, lineWidth, "familyTree");
                        }
                    }

                    if (mother != null)
                    {
                        nextGeneration.add(mother);

                        EventModel motherBirth = GetBirthOrEarliestEventForPerson(mother);

                        if (firstRunThrough)
                        {
                            DrawLineForEvent(event, motherBirth, lineWidth, "familyTree");
                        }
                        else
                        {
                            EventModel currentPersonBirth = GetBirthOrEarliestEventForPerson(currentPerson);
                            DrawLineForEvent(currentPersonBirth, motherBirth, lineWidth, "familyTree");
                        }
                    }

                    firstRunThrough = false;
                }

                lineWidth /= 2;
                currentGeneration = new ArrayList<>(nextGeneration);
                nextGeneration.clear();
            }
        }
    }

    private void CreateSpouseLines()
    {
        /*
        Spouse Lines
        A line is drawn linking the selected event to the birth event of the person’s
        spouse (i.e., the person associated with the selected event). If there is no birth event
        recorded for the spouse, or if their birth event is currently invisible due to the current
        event filter settings, the earliest available event for the spouse is used instead. If the
        person has no recorded spouse, or the recorded spouse has no events, no line is drawn.
        */

        if (Model.GetInstance().GetSetting().GetSpouseLines())
        {
            PersonModel spouse = Model.GetInstance().GetPersonForID(person.GetSpouseID());

            EventModel spouseBirth = GetBirthOrEarliestEventForPerson(spouse);

            DrawLineForEvent(event, spouseBirth, spouseAndLifeStoryLineWidth, "spouse");
        }
    }

    private EventModel GetBirthOrEarliestEventForPerson(PersonModel p)
    {
        if (p == null)
        {
            return null;
        }

        ArrayList<EventModel> personEvents = Model.GetInstance().GetAllFilteredEventsForPerson(p);

        EventModel personLineEvent = null;
        int lowestYear = 100000;
        for (EventModel e : personEvents)
        {
            if (e.GetEventType().toLowerCase().equals("birth"))
            {
                personLineEvent = e;
                break;
            }
            else
            {
                if (e.GetYear() < lowestYear)
                {
                    personLineEvent = e;
                    lowestYear = personLineEvent.GetYear();
                }
            }
        }

        return personLineEvent;
    }

    private void DrawLineForEvent(EventModel fromEvent, EventModel toEvent, int width, String lineType)
    {
        if (toEvent != null && fromEvent != null)
        {
            int color;
            if (Model.GetInstance().GetSetting().GetLineColorValueForKey(lineType).toLowerCase().equals("red"))
            {
                color = Color.RED;
            }
            else if (Model.GetInstance().GetSetting().GetLineColorValueForKey(lineType).toLowerCase().equals("blue"))
            {
                color = Color.BLUE;
            }
            else if (Model.GetInstance().GetSetting().GetLineColorValueForKey(lineType).toLowerCase().equals("green"))
            {
                color = Color.GREEN;
            }
            else
            {
                color = Color.GRAY;
            }

            Polyline eventLine = map.addPolyline(new PolylineOptions()
                    .add(new LatLng(fromEvent.GetLatitude(), fromEvent.GetLongitude()), new LatLng(toEvent.GetLatitude(), toEvent.GetLongitude()))
                    .width(width)
                    .color(color));

            lines.add(eventLine);
        }
    }
}
