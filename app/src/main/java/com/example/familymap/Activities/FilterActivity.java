package com.example.familymap.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;
import com.example.familymap.RecyclerViewHelpers.FilterRecyclerViewAdapter;

public class FilterActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        SetRecyclerView();
    }

    private void SetRecyclerView()
    {
        recyclerView = findViewById(R.id.filter_recycler_view);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        recyclerViewAdapter = new FilterRecyclerViewAdapter(Model.GetInstance().GetFilter().GetEventTypeToBooleanMapping());
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
