package com.example.familymap.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.familymap.R;

public class LoginFragment extends Fragment
{
    private EditText host;
    private EditText port;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup gender;
    private Button register;
    private Button signIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.login_fragment, container, true);

        ConnectToWidgets(view);

        return view;
    }

    private void ConnectToWidgets(View view)
    {
        register = view.findViewById(R.id.registerButton);
        signIn = view.findViewById(R.id.signInButton);
        host = view.findViewById(R.id.serverHost);
        port = view.findViewById(R.id.serverPort);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);

        register.addTextChangedListener(textWatcher);
        signIn.addTextChangedListener(textWatcher);
        host.addTextChangedListener(textWatcher);
        port.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        firstName.addTextChangedListener(textWatcher);
        lastName.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        gender.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
            //do nothing
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {
            AdjustButtons();
        }
    };

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId)
        {
            AdjustButtons();
        }
    };

    private void AdjustButtons()
    {
        AdjustSignInButton();
        AdjustRegisterButton();
    }

    private void AdjustRegisterButton()
    {
        if (username.getText().toString().equals("") ||
                password.getText().toString().equals("") ||
                firstName.getText().toString().equals("") ||
                lastName.getText().toString().equals("") ||
                email.getText().toString().equals("") ||
                gender.getCheckedRadioButtonId() == -1 ||
                host.getText().toString().equals("") ||
                port.getText().toString().equals(""))
        {
            register.setEnabled(false);
        }
        else
        {
            register.setEnabled(true);
        }
    }

    private void AdjustSignInButton()
    {
        if (username.getText().toString().equals("") ||
            password.getText().toString().equals("") ||
            host.getText().toString().equals("") ||
            port.getText().toString().equals(""))
        {
            signIn.setEnabled(false);
        }
        else
        {
            signIn.setEnabled(true);
        }
    }


}
