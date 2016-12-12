package com.rit.sfp.teachboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by steve on 12/1/2016.
 */

public class CreateBoardActivity extends AppCompatActivity {
    EditText boardName;
    RadioGroup privacyOptions;
    Button confirmBoardCreateBtn;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.activity_create_whiteboard);


        boardName = (EditText) findViewById(R.id.whiteboardName);
        confirmBoardCreateBtn = (Button) findViewById(R.id.confirmBoardCreateBtn);


        confirmBoardCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get userId
                String username = getIntent().getStringExtra("Username");
                if (!boardName.getText().toString().matches("")) {
                    boolean created = myDb.createTeachboard(boardName.getText().toString(), username, "public");
                    if (created) {
                        Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error creating board", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
}
