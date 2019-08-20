package com.example.familymap.RecyclerViewHelpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.familymap.Activities.EventActivity;
import com.example.familymap.Activities.PersonActivity;
import com.example.familymap.HelperClasses.SearchResult;
import com.example.familymap.R;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>
{
    private ArrayList<SearchResult> dataToList;
    private Context context;

    public static class SearchViewHolder extends RecyclerView.ViewHolder
    {
        public TextView listItemText;
        public RelativeLayout parentLayout;

        public SearchViewHolder(View listItem)
        {
            super(listItem);
            listItemText = listItem.findViewById(R.id.searchItemText);
            parentLayout = listItem.findViewById(R.id.search_list_item_layout);
        }
    }

    public SearchRecyclerViewAdapter(ArrayList<SearchResult> results, Context context)
    {
        dataToList = results;
        this.context = context;
    }


    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_list_item, parent, false);

        SearchViewHolder vh = new SearchViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position)
    {
        final SearchResult currentResult = dataToList.get(position);

        if (currentResult.GetType().equals("event"))
        {
            String eventText = "Event:\n" + currentResult.GetEventType() + ": " + currentResult.GetCity() + ", "
                    + currentResult.GetCounty() + "(" + currentResult.GetYear() + ")"
                    + "\n" + currentResult.GetAssociatedPerson();

            holder.listItemText.setText(eventText);

            holder.parentLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, EventActivity.class);

                    intent.putExtra("eventID", currentResult.GetId());
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            String personText;
            if (currentResult.GetRole() == null)
            {
                personText = "Person:\n" + currentResult.GetFirstName() + " " + currentResult.GetLastName();
            }
            else
            {
                personText = currentResult.GetFirstName() + " " + currentResult.GetLastName() + "\n" + currentResult.GetRole();
            }

            holder.listItemText.setText(personText);

            holder.parentLayout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, PersonActivity.class);

                    intent.putExtra("personID", currentResult.GetId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return dataToList.size();
    }
}

