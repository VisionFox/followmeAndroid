package com.followme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.followme.lusir.followmeandroid.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerBtn;
    private EditText userNameET;
    private EditText passwordET;
    private EditText emailET;
    private EditText phoneET;
    private EditText questionET;
    private EditText answerET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = (Button) findViewById(R.id.register_registerButton);
        userNameET = (EditText) findViewById(R.id.register_userName);
        passwordET = (EditText) findViewById(R.id.register_password);
        emailET = (EditText) findViewById(R.id.register_email);
        phoneET = (EditText) findViewById(R.id.register_phone);
        questionET = (EditText) findViewById(R.id.register_question);
        answerET = (EditText) findViewById(R.id.register_answer);

        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_registerButton:
                String userName = userNameET.getText().toString();
                String password = passwordET.getText().toString();
                String email = emailET.getText().toString();
                String phone = phoneET.getText().toString();
                String question = questionET.getText().toString();
                String answer = answerET.getText().toString();
                Log.d(userName, password);
                break;
            default:
                break;
        }
    }
}
