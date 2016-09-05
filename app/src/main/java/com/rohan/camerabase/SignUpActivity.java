package com.rohan.camerabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername, etName, etAge, etEmail, etPassword, editTextConfirmPassoword;
    private Button btnSignUp;
    Context context;
    DataBaseAdapter dataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dataBaseAdapter = new DataBaseAdapter(this);
        dataBaseAdapter = dataBaseAdapter.open();
        etUsername = (EditText) findViewById(R.id.et_su_usermane);
        etPassword = (EditText) findViewById(R.id.et_su_password);
        etAge = (EditText) findViewById(R.id.et_su_age);
        etEmail = (EditText) findViewById(R.id.et_su_email);
        etName = (EditText) findViewById(R.id.et_su_name);
        editTextConfirmPassoword = (EditText) findViewById(R.id.et_su_password);

        btnSignUp = (Button) findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String name = etName.getText().toString();
                String age = etAge.getText().toString();
                String email = etEmail.getText().toString();

                String confirmPassword = etPassword.getText().toString();
                if(username.equals("")||password.equals("")||confirmPassword.equals("")){
                    Toast.makeText(getApplicationContext(),"Field Vacant",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    byte[] image = DefaultImage();
                    dataBaseAdapter.InsertEntry(username,password,name,email,age,image);
                    Toast.makeText(getApplicationContext(),"Account Successfully Created",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        dataBaseAdapter.close();
    }

    public byte[] DefaultImage(){
        Drawable myDrawable = getResources().getDrawable(R.drawable.defaulticon);
        Bitmap bitmap = ((BitmapDrawable) myDrawable).getBitmap();
//        Bitmap bitmap= BitmapFactory.decodeResource(getApplicationContext().getResources() , R.drawable.ic_account_box_black_24dp);
        byte[] image = DbBitmapUtility.getBytes(bitmap);
        return image;
    }
}

