package com.android.myweather;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.myweather.utils.HttpPostRequest;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    /** 注册按钮 **/
    Button btnRegister;

    /** 登录按钮 **/
    Button btnLogin;

    /** 登录界面的用户名 **/
    EditText etLoginUsername;

    /** 登录界面的密码 **/
    EditText etLoginPassword;

    /** 登录返回按钮 **/
    Button btnLoginBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        btnRegister = findViewById(R.id.btn_register_layout);
        btnLogin = findViewById(R.id.btn_login_layout);
        etLoginUsername = findViewById(R.id.et_username);
        etLoginPassword = findViewById(R.id.et_password);
        btnLoginBack = findViewById(R.id.btn_login_back);

        //登录
        btnLogin.setOnClickListener(v -> {
            //判断数据库中是否存在该用户
            String url = "http://" + this.getResources().getString(R.string.request_url) + ":8080/user/login";
            //请求传入的参数
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", etLoginUsername.getText().toString())
                    .add("password", etLoginPassword.getText().toString())
                    .build();
            HttpPostRequest.okhttpPost(url, requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //得到服务器返回的具体内容
                    final String responseData = response.body().string();

                    System.out.println(responseData);


                    //返回false说明账号或者密码输入错误
                    if (responseData.equals("false")){
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "账户或密码错误，请确认后再次尝试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else { //否则说明数据库中已存在该用户信息
                        //跳转回原页面
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, "登录成功！用户：" + etLoginUsername.getText().toString() , Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }
                }


            });

        });

        //注册
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //返回
        btnLoginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭当前页面
                finish();
            }
        });
    }
}
