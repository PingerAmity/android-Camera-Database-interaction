package com.rohan.camerabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private TextView tvSignup;
    private Button login;
//    PrefManager prefManager;
    DataBaseAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        prefManager = new PrefManager(this);
//        if(prefManager.isLoggedIn()){
//            Intent intent = new Intent(WelcomeActivity.this, WelcomeActivity.class);
//            startActivity(intent);
//        }
//        else {
//            Toast.makeText(this,"Not Logged in",Toast.LENGTH_SHORT).show();
//        }

        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter = dataBaseAdapter.open();

        login = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_create_account);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(SignUp);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername = (EditText) findViewById(R.id.et_w_username);
                etPassword = (EditText) findViewById(R.id.et_w_password);
                String UserName = etUsername.getText().toString();
                String Password = etPassword.getText().toString();
                String storedPassword = dataBaseAdapter.getPasswordEntry(UserName);
                if(Password.equals(storedPassword)){
                    Toast.makeText(WelcomeActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                    Intent main = new Intent(WelcomeActivity.this,ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Username",UserName);
                    main.putExtras(bundle);
                    startActivity(main);
                    //prefManager.setLoggedIn(true);

                }
                else {
                    Toast.makeText(WelcomeActivity.this,"Username or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    protected void onDestroy(){
        super.onDestroy();
        dataBaseAdapter.close();
    }
}
