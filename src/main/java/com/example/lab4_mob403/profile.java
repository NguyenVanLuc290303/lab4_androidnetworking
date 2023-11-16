package com.example.lab4_mob403;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab4_mob403.API.RequestInterface;
import com.example.lab4_mob403.model.ServerRequest;
import com.example.lab4_mob403.model.ServerResponse;
import com.example.lab4_mob403.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profile extends AppCompatActivity {
    Button btn_changePassword,btn_logout;
    AlertDialog dialog;
    TextView tv_name,tv_email , tv_message;
    private EditText ed_old_password, ed_new_password;
    private SharedPreferences pref;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("myref", Context.MODE_PRIVATE);
        btn_changePassword = findViewById(R.id.btn_changePassword);
        btn_logout = findViewById(R.id.btn_logout);
        tv_name = findViewById(R.id.tv_name);
        tv_email =findViewById(R.id.tv_email);
        tv_name.setText("Welcome : " + sharedPreferences.getString(Constants.NAME, ""));
        tv_email.setText(sharedPreferences.getString(Constants.EMAIL, ""));
        btn_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(profile.this);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profile.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new
                AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_changepassword, null);
        ed_old_password = (EditText)
                view.findViewById(R.id.ed_old_password);
        ed_new_password = (EditText)
                view.findViewById(R.id.ed_new_password);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String old_password = ed_old_password.getText().toString();
                String new_password = ed_new_password.getText().toString();
                changePasswordProcess(tv_email.getText().toString(),
                        old_password, new_password);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
         builder.create();
        builder.show();

    }

    private void changePasswordProcess(String toString, String old_password, String new_password) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface requestInterface =
                retrofit.create(RequestInterface.class);
        User user = new User();
        user.setEmail(toString);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().equals(Constants.SUCCESS)) {
                    tv_message.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),resp.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getMessage());
            }
        });
    }
}