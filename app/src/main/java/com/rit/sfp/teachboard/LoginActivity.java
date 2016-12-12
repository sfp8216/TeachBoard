package com.rit.sfp.teachboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by steve on 12/1/2016.
 */

public class LoginActivity extends AppCompatActivity{

    EditText username, password;

    Button loginBtn, createAccountBtn;
    DatabaseHelper myDb;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.loginUsernameET);
        password = (EditText)findViewById(R.id.passwordLoginET);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        createAccountBtn = (Button)findViewById(R.id.createAccountBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A temporary way to store passwords - Final version will be hashed
                boolean valid = myDb.checkLogin(username.getText().toString(),password.getText().toString());
                if(valid) {
                    Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                    intent.putExtra("Username",username.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Username or password is inavalid!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateAccountActivity.class);
                startActivity(intent);
            }
        });


    }


}
