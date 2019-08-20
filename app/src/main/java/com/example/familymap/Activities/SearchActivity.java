package com.example.familymap.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;
import com.example.familymap.HelperClasses.SearchResult;
import com.example.familymap.RecyclerViewHelpers.SearchRecyclerViewAdapter;

import java.util.ArrayList;

import Models.EventModel;
import Models.PersonModel;

public class SearchActivity extends AppCompatActivity
{
    private EditText searchBar;
    private ArrayList<SearchResult> results = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ConnectToWidgets();
    }

    public void ExecuteSearch(View view)
    {
        results = GetSearchResults();
        UpdateRecyclerView();
    }

    private void UpdateRecyclerView()
    {
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewAdapter = new SearchRecyclerViewAdapter(results, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void ConnectToWidgets()
    {
        searchBar = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.searchRecyclerView);
    }

    private ArrayList<SearchResult> GetSearchResults()
    {
        String query = searchBar.getText().toString().toLowerCase();
        ArrayList<SearchResult> results = new ArrayList<>();

        for (PersonModel p : Model.GetInstance().GetPersons())
        {
            //people’s first and last names
            if (p.GetFirstName().toLowerCase().contains(query) ||
                p.GetLastName().toLowerCase().contains(query))
            {
                results.add((new SearchResult("person", p.GetPersonID(), p.GetFirstName(), p.GetLastName())));
            }
        }

        for (EventModel e : Model.GetInstance().GetFilteredEvents())
        {
            //currentEvent’s countries, cities, currentEvent types, and years
            if (e.GetCountry().toLowerCase().contains(query) ||
                e.GetCity().toLowerCase().contains(query) ||
                e.GetEventType().toLowerCase().contains(query) ||
                String.valueOf(e.GetYear()).contains(query))
            {

                PersonModel associatedPerson = Model.GetInstance().GetPersonForID(e.GetPersonID());
                String associatedPersonName = associatedPerson.GetFirstName() + " " + associatedPerson.GetLastName();

                results.add((new SearchResult("event", e.GetEventID(), e.GetEventType(), e.GetCity(), e.GetCountry(), String.valueOf(e.GetYear()), associatedPersonName)));
            }
        }

        return results;
    }
}
