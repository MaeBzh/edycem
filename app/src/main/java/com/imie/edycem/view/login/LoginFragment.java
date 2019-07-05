package com.imie.edycem.view.login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.imie.edycem.R;
import com.imie.edycem.view.workingtime.WorkingTimeActivity;

public class LoginFragment extends Fragment {

    private EditText editEmail;
    private Button submitButton;

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.initComponents(view);
        return view;
    }

    public void initComponents(View view) {
        this.editEmail = (EditText) view.findViewById(R.id.edit_email);
        this.submitButton = (Button) view.findViewById(R.id.button_submit);
        this.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginFragment.this.getContext(), WorkingTimeActivity.class);
                startActivity(intent)
                ;
            }
        });
    }
}
