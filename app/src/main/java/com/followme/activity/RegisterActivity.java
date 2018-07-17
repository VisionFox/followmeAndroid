package com.followme.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.followme.bean.User;
import com.followme.common.Const;
import com.followme.common.MyApplication;
import com.followme.common.ServerResponse;
import com.followme.exchange.UserModuleRequest;
import com.followme.lusir.followmeandroid.R;
import com.followme.util.ToastUtil;
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
    private Activity thisActivity = this;


    private static final int flag_error = Const.handlerFlag.ERROR;
    private static final int flag_success = Const.handlerFlag.SUCCESS;
    private static final int flag_fail = Const.handlerFlag.FAIL;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
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
                    String succ = (String) msg.obj;
                    ToastUtil.show(thisActivity, succ);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = findViewById(R.id.register_registerButton);
        userNameET = findViewById(R.id.register_userName);
        passwordET = findViewById(R.id.register_password);
        emailET = findViewById(R.id.register_email);
        phoneET = findViewById(R.id.register_phone);
        questionET = findViewById(R.id.register_question);
        answerET = findViewById(R.id.register_answer);

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

                //使用自己封装好的request工具类，发送注册信息，并完成相应的回调处理
                UserModuleRequest.register(user, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Message msg = Message.obtain();
                        msg.what = Const.handlerFlag.ERROR;
                        msg.obj = e;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson json = new Gson();
                        String jsonStr = response.body().string();
                        ServerResponse serverResponse = json.fromJson(jsonStr, ServerResponse.class);
                        Log.d("注册返回信息", serverResponse.toString());
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (serverResponse.isSuccess()) {
                            Message msg = Message.obtain();
                            msg.what = Const.handlerFlag.SUCCESS;
                            msg.obj = "注册成功！！welcome！！";
                            mHandler.sendMessage(msg);
                            finish();
                        } else {
                            Message msg = Message.obtain();
                            msg.what = Const.handlerFlag.FAIL;
                            msg.obj = serverResponse;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
