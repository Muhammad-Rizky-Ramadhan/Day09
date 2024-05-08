package com.example.day09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.day09.data.ApiClient;
import com.example.day09.data.ApiInterface;
import com.example.day09.databinding.ActivityLoginBinding;
import com.example.day09.model.login.Login;
import com.example.day09.model.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    ApiInterface apiInterface;
    String Username, Password;
    SessionManager sessionManager;
//    EditText etUsername, etPassword;
//    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        etUsername = findViewById(R.id.logUsername);
//        etPassword = findViewById(R.id.logPassword);
//        btnLogin = findViewById(R.id.btnLogin);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(this);
        binding.tvCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin){
            Username = binding.logUsername.getText().toString();
            Password = binding.logPassword.getText().toString();
            login(Username,Password);
        } else if (v.getId() == R.id.tvCreate) {
            Intent intent = new Intent(LoginActivity.this ,RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void login(String username, String password) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username,password);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                sessionManager = new SessionManager(LoginActivity.this);
                LoginData loginData = response.body().getData();
                sessionManager.createLoginSession(loginData);

                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    Toast.makeText(LoginActivity.this, response.body().getData().getName(),  Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this ,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}