package com.followme.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.followme.bean.User;
import com.followme.common.ServerResponse;
import com.followme.exchange.UserModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button registerBtn;
    private EditText userNameET;
    private EditText passwordET;
    private EditText emailET;
    private EditText phoneET;
    private EditText questionET;
    private EditText answerET;
    private ProgressDialog progressDialog;


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

                User user = new User();
                user.setUsername(userName);
                user.setPassword(password);
                user.setEmail(email);
                user.setPhone(phone);
                user.setQuestion(question);
                user.setAnswer(answer);

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("正在注册...");
                progressDialog.show();

                UserModuleRequest.register(user, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("注册出现异常", e.toString());
                        progressDialog.setMessage("注册出现异常");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson json = new Gson();
                        String jsonStr = response.body().string();
                        ServerResponse serverResponse = json.fromJson(jsonStr, ServerResponse.class);
                        Log.d("注册返回信息", serverResponse.toString());
                        if (serverResponse.isSuccess()) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            finish();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
