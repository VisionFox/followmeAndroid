package com.followme.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.followme.bean.User;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.common.ServerResponse;
import com.followme.exchange.Exchange;
import com.followme.exchange.UserModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.JsonTransform;
import com.followme.util.ToastUtil;
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

    private static final String PREFS = "prefs";
    private static final String PREF_account = "account";
    private static final String PREF_password = "password";
    SharedPreferences mSharedPreferences;

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

//        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
//        checkLoginState();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkLoginState();
//    }
//
//    private void checkLoginState() {
//        String account = mSharedPreferences.getString(PREF_account, "");
//        String password = mSharedPreferences.getString(PREF_password, "");
//
//        if (account == null || account.length() == 0) {
//            return;
//        } else {
//            login(account, password);
//        }
//    }

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登陆...");
        progressDialog.show();

        UserModuleRequest.login(account, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("登录出现异常", e.toString());
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson json = new Gson();
                String jsonStr = response.body().string();
                ServerResponse serverResponse = JsonTransform.jsonToServerResponse(jsonStr);
                Log.d("登录返回信息", serverResponse.toString());
                if (serverResponse.isSuccess()) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    String temp = json.toJson(serverResponse.getData());
                    User currentUser = json.fromJson(temp.toString(), User.class);
                    Log.d("当前用户：", currentUser.toString());
//                    displayWelcome();
                    gotoMainActivity(currentUser);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Log.d("登录失败：", serverResponse.getMsg());
                }
            }
        });

    }

    private void gotoMainActivity(User currentUser) {
        finish();
        MyApplication.bindCurrentUser(currentUser);
        Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
        startActivity(intent);
    }

//    private void displayWelcome() {
//        String account = mSharedPreferences.getString(PREF_account, "");
//
//        if (account.length() > 0) {
////            Toast.makeText(MyApplication.getContext(), "Welcome, " + account + "!", Toast.LENGTH_LONG).show();
////            ToastUtil.show(this, "Welcome back, " + account + "!");
//            Log.d("test","test");
//        } else {
//            SharedPreferences.Editor e = mSharedPreferences.edit();
//
//            String userName = accountET.getText().toString();
//            e.putString(PREF_password, userName);
//            e.commit();
//
//            String password = passwordET.getText().toString();
//            e.putString(PREF_password, password);
//            e.commit();
//
//            Log.d("test","test");
////            Toast.makeText(MyApplication.getContext(), "Welcome, " + account + "!", Toast.LENGTH_LONG).show();
//        }
//    }
}

