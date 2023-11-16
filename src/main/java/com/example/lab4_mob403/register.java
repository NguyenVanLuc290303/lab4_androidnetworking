package com.example.lab4_mob403;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4_mob403.API.RequestInterface;
import com.example.lab4_mob403.model.ServerRequest;
import com.example.lab4_mob403.model.ServerResponse;
import com.example.lab4_mob403.model.User;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class register extends AppCompatActivity {
    EditText ed_name,ed_email,ed_password;
    Button btn_register;
    TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        tv_login = findViewById(R.id.tv_login);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString();
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                ServerRequest request = new ServerRequest();
                request.setOperation(Constants.REGISTER_OPERATION);
                request.setUser(user);
                Call<ServerResponse> response = requestInterface.operation(request);
                response.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        ServerResponse resp = response.body();
                        Toast.makeText(getApplicationContext(),resp.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Log.d(Constants.TAG,"failed");
                    }
                });

            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}