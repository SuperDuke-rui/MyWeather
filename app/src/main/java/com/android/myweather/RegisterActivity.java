package com.android.myweather;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.myweather.utils.HttpPostRequest;

import okhttp3.*;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    /** 注册页面的账户名 **/
    EditText etUsernameRgt;

    /** 注册页面的密码 **/
    EditText etPasswordRgt;

    /** 密码确认 **/
    EditText etPasswordAgain;

    /** 注册按钮 **/
    Button btnRegister;

    /** 返回按钮 **/
    Button btnRegisterBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    public void initView() {
        etUsernameRgt = findViewById(R.id.et_username_register);
        etPasswordRgt = findViewById(R.id.et_password_register);
        etPasswordAgain = findViewById(R.id.et_password_again_register);
        btnRegister = findViewById(R.id.btn_register);
        btnRegisterBack = findViewById(R.id.btn_register_back);

        btnRegister.setOnClickListener(v -> {
            // 1.1 判断是否为空
            if (etUsernameRgt.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "账户名不能为空", Toast.LENGTH_SHORT).show();
            } else if (etPasswordRgt.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (etPasswordAgain.getText().toString().equals("")){
                Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
            } else {

                //1.2判断两次输入的密码是否一致
                if (!etPasswordRgt.getText().toString().equals(etPasswordAgain.getText().toString())) {

                    Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    etPasswordRgt.setText("");
                    etPasswordAgain.setText("");

                } else { //两次密码一致
                    //判断该账号是否已经被注册
                    String getUsernameUrl = "http://" + this.getResources().getString(R.string.request_url) + ":8080/user/getByUsername";
                    String registerUrl = "http://" + this.getResources().getString(R.string.request_url) + ":8080/user/register";
                    //请求传入的参数
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", etUsernameRgt.getText().toString())
                            .build();
                    
                    HttpPostRequest.okhttpPost(getUsernameUrl, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseDate = response.body().string();

                            System.out.println(responseDate);

                            if ("true".equals(responseDate)){
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this, "该账户已经注册", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                RequestBody requestBody1 = new FormBody.Builder()
                                        .add("username", etUsernameRgt.getText().toString())
                                        .add("password", etPasswordRgt.getText().toString())
                                        .build();

                                HttpPostRequest.okhttpPost(registerUrl, requestBody1, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Looper.prepare();
                                        Toast.makeText(RegisterActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        final String responseData = response.body().string();

                                        if ("true".equals(responseData)) {
                                            Looper.prepare();
                                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }
                                });

                            }
                        }
                    });
                    

                }

            }


        });

        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭当前窗口
                finish();
            }
        });
    }
}