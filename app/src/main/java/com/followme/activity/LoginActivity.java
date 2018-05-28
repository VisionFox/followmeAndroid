package com.followme.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.followme.bean.User;
import com.followme.common.Const;
import com.followme.common.ServerResponse;
import com.followme.exchange.Exchange;
import com.followme.exchange.UserModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLoginBtn;
    private Button btnRegisterBtn;
    private EditText accountET;
    private EditText passwordET;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLoginBtn = (Button) findViewById(R.id.login_loginButton);
        btnRegisterBtn = (Button) findViewById(R.id.login_registerButton);
        accountET = (EditText) findViewById(R.id.login_account);
        passwordET = (EditText) findViewById(R.id.login_password);

        btnLoginBtn.setOnClickListener(this);
        btnRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_loginButton:
                String account = accountET.getText().toString();
                String password = passwordET.getText().toString();
                Log.d(account, password);
                login(account, password);
                break;
            case R.id.login_registerButton:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void login(String account, String password) {
        String url = Const.SERVER_URL;
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在登陆...");
        progressDialog.show();

        UserModuleRequest.login(account, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("登录出现异常", e.toString());
                progressDialog.setMessage("登录出现异常");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson json = new Gson();
                String jsonStr = response.body().string();
                ServerResponse serverResponse = json.fromJson(jsonStr, ServerResponse.class);
                Log.d("登录返回信息", serverResponse.toString());
                if (serverResponse.isSuccess()){
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }
}

