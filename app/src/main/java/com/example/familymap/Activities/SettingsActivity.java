package com.example.familymap.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;

public class SettingsActivity extends AppCompatActivity
{
    private Spinner lifeStorySpinner;
    private Spinner familyTreeSpinner;
    private Spinner spouseLinesSpinner;
    private Spinner mapTypeSpinner;
    private Switch lifeStorySwitch;
    private Switch familyTreeSwitch;
    private Switch spouseLinesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ConnectToWidgets();
        SetDropDowns();
        SetSwitches();
        AddListeners();
    }

    public void Logout(View view)
    {
        Model.GetInstance().Clear();
        Model.GetInstance().ResetSettings();
        Model.GetInstance().GetFilter().Clear();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    public void ReSync(View view)
    {
        Model.GetInstance().InitializeModel(Model.GetInstance().GetAuthToken());
        Model.GetInstance().GetFilter().Clear();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void AddListeners()
    {
        spouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.GetInstance().GetSetting().SetSpouseLines(isChecked);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.GetInstance().GetSetting().SetFamilyTreeLines(isChecked);
            }
        });

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Model.GetInstance().GetSetting().SetLifeStoryLines(isChecked);
            }
        });

        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                Model.GetInstance().GetSetting().SetLineColorInMap("lifeStory", ((TextView)selectedItemView).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //do nothing
            }
        });

        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                Model.GetInstance().GetSetting().SetLineColorInMap("familyTree", ((TextView)selectedItemView).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //do nothing
            }
        });

        spouseLinesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                Model.GetInstance().GetSetting().SetLineColorInMap("spouse", ((TextView)selectedItemView).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //do nothing
            }
        });

        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                Model.GetInstance().GetSetting().SetMapType(((TextView)selectedItemView).getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //do nothing
            }
        });
    }

    private void SetSwitches()
    {
        lifeStorySwitch.setChecked(Model.GetInstance().GetSetting().GetLifeStoryLines());
        familyTreeSwitch.setChecked(Model.GetInstance().GetSetting().GetFamilyTreeLines());
        spouseLinesSwitch.setChecked(Model.GetInstance().GetSetting().GetSpouseLines());
    }

    private void ConnectToWidgets()
    {
        lifeStorySpinner = findViewById(R.id.lifeStoryDropDown);
        familyTreeSpinner = findViewById(R.id.familyTreeDropDown);
        spouseLinesSpinner = findViewById(R.id.spouseLinesDropDown);
        mapTypeSpinner = findViewById(R.id.mapTypeDropDown);

        lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
    }

    private void SetDropDowns()
    {
        String[] colors = new String[]{"Red", "Blue", "Green"};
        String[] mapTypes = new String[]{"Normal", "Hybrid", "Satellite", "Terrain"};

        ArrayAdapter<String> colorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        ArrayAdapter<String> mapTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mapTypes);

        lifeStorySpinner.setAdapter(colorsAdapter);
        spouseLinesSpinner.setAdapter(colorsAdapter);
        familyTreeSpinner.setAdapter(colorsAdapter);
        mapTypeSpinner.setAdapter(mapTypesAdapter);

        String lifeStoryColor = Model.GetInstance().GetSetting().GetLineColorValueForKey("lifeStory");
        lifeStorySpinner.setSelection(colorsAdapter.getPosition(lifeStoryColor));

        String spouseLinesColor = Model.GetInstance().GetSetting().GetLineColorValueForKey("spouse");
        spouseLinesSpinner.setSelection(colorsAdapter.getPosition(spouseLinesColor));

        String familyTreeColor = Model.GetInstance().GetSetting().GetLineColorValueForKey("familyTree");
        familyTreeSpinner.setSelection(colorsAdapter.getPosition(familyTreeColor));

        mapTypeSpinner.setSelection(mapTypesAdapter.getPosition(Model.GetInstance().GetSetting().GetMapType()));
    }

}
