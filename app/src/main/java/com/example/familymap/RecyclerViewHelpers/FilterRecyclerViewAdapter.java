package com.example.familymap.RecyclerViewHelpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;

import java.util.Iterator;
import java.util.Map;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder>
{
    private Map<String, Boolean> dataSet;

    public static class FilterViewHolder extends RecyclerView.ViewHolder
    {
        public TextView listItemText;
        public Switch listItemSwitch;

        public FilterViewHolder(View listItem)
        {
            super(listItem);
            listItemText = listItem.findViewById(R.id.filter_list_item_text);
            listItemSwitch = listItem.findViewById(R.id.filter_list_item_switch);
        }
    }

    public FilterRecyclerViewAdapter(Map<String, Boolean> dataSet)
    {
        this.dataSet = dataSet;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_list_item, parent, false);

        FilterViewHolder vh = new FilterViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position)
    {
        //move the iterator to right position
        Iterator it = dataSet.entrySet().iterator();
        for (int x = 0; x != position; x++)
        {
            it.next();
        }

        //get the data
        Map.Entry dataSetEntry = (Map.Entry)it.next();
        final String eventType = (String)dataSetEntry.getKey();
        Boolean b = (Boolean)dataSetEntry.getValue();

        //set the layout to the data
        holder.listItemSwitch.setChecked(b);

        String listItemText = eventType + " events";
        holder.listItemText.setText(listItemText);

        //set on click listener
        holder.listItemSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.GetInstance().GetFilter().SetBoolForEventType(eventType, isChecked);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
}
