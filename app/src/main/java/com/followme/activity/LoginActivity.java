package com.followme.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.followme.bean.Attraction;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLoginBtn;
    private Button btnRegisterBtn;
    private EditText accountET;
    private EditText passwordET;
    private ProgressDialog progressDialog;
    private Activity thisActivity = this;

    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private static final int flag_fail = Const.handlerFlag.FAIL;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {//3、定义处理消息的方法
            switch (msg.what) {
                case flag_error:
                    Exception e = (Exception) msg.obj;
                    ToastUtil.show(thisActivity, "出现异常，报错信息为：" + e.toString());
                    break;
                case flag_fail:
                    ServerResponse serverResponse = (ServerResponse) msg.obj;
                    ToastUtil.show(thisActivity, serverResponse.getMsg());
                    break;
                case flag_success:
                    User user = (User) msg.obj;
                    ToastUtil.show(thisActivity, "登录成功，欢迎使用，" + user.getUsername());
                    gotoMainActivity(user);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLoginBtn = findViewById(R.id.login_loginButton);
        btnRegisterBtn = findViewById(R.id.login_registerButton);
        accountET = findViewById(R.id.login_account);
        passwordET = findViewById(R.id.login_password);

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登陆...");
        progressDialog.show();

        UserModuleRequest.login(account, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = Const.handlerFlag.ERROR;
                msg.obj = e;
                mHandler.sendMessage(msg);
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

                    Message msg = Message.obtain();
                    msg.what = Const.handlerFlag.SUCCESS;
                    msg.obj = currentUser;
                    mHandler.sendMessage(msg);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Message msg = Message.obtain();
                    msg.what = Const.handlerFlag.FAIL;
                    msg.obj = serverResponse;
                    mHandler.sendMessage(msg);
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

}

