package com.example.familymap.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.familymap.HelperClasses.ServerProxy;
import com.example.familymap.HelperClasses.Model;
import com.example.familymap.R;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int mapsInt = R.layout.activity_main_maps;
        int loginInt = R.layout.activity_main_login;

        if (!Model.IsReady())
        {
            setContentView(loginInt);
        }
        else
        {
            if (!Model.GetInstance().GetFilter().IsInitialized())
            {
                Model.GetInstance().GetFilter().InitializeFilter();
            }

            setContentView(mapsInt);
        }
    }

    public void Register(View view)
    {
        String host = ((TextView) findViewById(R.id.serverHost)).getText().toString();
        String port = ((TextView) findViewById(R.id.serverPort)).getText().toString();
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();
        String email = ((TextView) findViewById(R.id.email)).getText().toString();
        String firstName = ((TextView) findViewById(R.id.firstName)).getText().toString();
        String lastName = ((TextView) findViewById(R.id.lastName)).getText().toString();
        Boolean male = ((RadioButton) findViewById(R.id.male)).isChecked();
        String gender;

        if (male)
        {
            gender = "m";
        }
        else
        {
            gender = "f";
        }

        ServerProxy serverProxy = new ServerProxy(host, port, this);
        ServerProxy.RegisterUser registerUser = serverProxy.new RegisterUser();

        registerUser.execute(username, password, email, firstName, lastName, gender);
    }

    public void SignIn(View view)
    {
        String host = ((TextView) findViewById(R.id.serverHost)).getText().toString();
        String port = ((TextView) findViewById(R.id.serverPort)).getText().toString();
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();

        ServerProxy serverProxy = new ServerProxy(host, port, this);

        ServerProxy.SignIn signIn = serverProxy.new SignIn();

        signIn.execute(username, password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (Model.IsReady())
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.settings_button)
        {
            GoToActivity(SettingsActivity.class);
            return true;
        }
        else if (item.getItemId() == R.id.search_button)
        {
            GoToActivity(SearchActivity.class);
            return true;
        }
        else if (item.getItemId() == R.id.filter_button)
        {
            GoToActivity(FilterActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void GoToActivity(Class c)
    {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}
