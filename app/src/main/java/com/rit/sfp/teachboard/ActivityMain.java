package com.rit.sfp.teachboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by steve on 12/1/2016.
 */

public class ActivityMain extends AppCompatActivity {

    Button joinBtn, createBtn;
    TextView welcomeUser;
    String username = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        welcomeUser = (TextView) findViewById(R.id.welcomeMessageTV);
        joinBtn = (Button) findViewById(R.id.joinBoardBtn);
        createBtn = (Button) findViewById(R.id.createBoardBtn);
        username = getIntent().getStringExtra("Username");
        if (username != "USER") {
            welcomeUser.setText("Welcome " + username + "!");
        }


        //temp intent holders


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WhiteboardActivity.class);
                //Temp
                intent.putExtra("BoardID", "1");
                //startActivity(intent);
                intent = new Intent(getApplicationContext(), TeachboardListActivity.class);
                startActivity(intent);
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateBoardActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });


    }


}
