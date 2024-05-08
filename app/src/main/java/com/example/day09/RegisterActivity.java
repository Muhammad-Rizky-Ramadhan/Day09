package com.example.day09;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.day09.data.ApiClient;
import com.example.day09.data.ApiInterface;
import com.example.day09.model.register.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private com.example.day09.databinding.ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = com.example.day09.databinding.ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvRegister.setOnClickListener(this);
        binding.btnRegis.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            if (v.getId() == R.id.tvRegister) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);}

            String Username = binding.regUsername.getText().toString();
            String Name = binding.regName.getText().toString();
            String Password = binding.regPassword.getText().toString();

            register(Username,Name,Password);
    }
    private void register(String username, String name, String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Register> call = apiInterface.registerResponse(username, name, password);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.isSuccessful() && response.body() != null){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Toast.makeText(RegisterActivity.this, "Akun berhasil dibuat", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}