package com.example.lab4_mob403;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    EditText ed_email,ed_password;
    TextView tv_register;
    private SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        pref = getPreferences(0);

        btn_login = findViewById(R.id.btn_login);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        tv_register = findViewById(R.id.tv_register);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestInterface requestInterface =
                        retrofit.create(RequestInterface.class);
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                ServerRequest request = new ServerRequest();
                request.setOperation(Constants.LOGIN_OPERATION);
                request.setUser(user);
                Call<ServerResponse> response = requestInterface.operation(request);
                response.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        ServerResponse resp = response.body();
                        Toast.makeText(getApplicationContext(),resp.getMessage(),Toast.LENGTH_SHORT).show();
                        if(resp.getResult().equals(Constants.SUCCESS)){
                            SharedPreferences sharedPreferences = getSharedPreferences("myref", Context.MODE_PRIVATE);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(Constants.IS_LOGGED_IN,true);

                            editor.putString(Constants.EMAIL,resp.getUser().getEmail());
                            editor.putString(Constants.NAME,
                                    resp.getUser().getName());

                            editor.putString(Constants.UNIQUE_ID,resp.getUser().getUnique_id());
                            editor.apply();
                            goToProfile();
                        }

                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Log.d(Constants.TAG,"failed");

                    }
                });
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void goToProfile() {
        Intent intent = new Intent(MainActivity.this,profile.class);
        startActivity(intent);
        finish();
    }
}