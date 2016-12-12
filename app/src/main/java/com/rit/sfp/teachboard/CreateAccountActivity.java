package com.rit.sfp.teachboard;

import android.annotation.TargetApi;
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

public class CreateAccountActivity extends AppCompatActivity{
    EditText email, username, pass,passAgain;
    Button submitBtn;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        myDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_account_creation);

        email = (EditText) findViewById(R.id.emailET);
        username = (EditText) findViewById(R.id.usernameET);
        pass = (EditText) findViewById(R.id.passwordET1);
        passAgain = (EditText) findViewById(R.id.passwordET2);
        submitBtn = (Button) findViewById(R.id.confirmCreateBtn);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().toString().equals(passAgain.getText().toString())){
                    boolean created = myDb.createAccount(username.getText().toString(),passAgain.getText().toString(),email.getText().toString());
                    if(created){
                        Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
                        intent.putExtra("Username",username.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"There was a problem creating your account, please try again!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}
