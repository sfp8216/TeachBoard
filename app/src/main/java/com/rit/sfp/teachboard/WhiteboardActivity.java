package com.rit.sfp.teachboard;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by steve on 12/1/2016.
 */

public class WhiteboardActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    Button showToolsBtn, showChatBtn;
    TeachboardView teachBoardView;
    String boardId = "";
    String username = "";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteboard);
        boardId =  getIntent().getStringExtra("BoardId");
        username = getIntent().getStringExtra("Username");


        myDb = new DatabaseHelper(this);
        showChatBtn = (Button)findViewById(R.id.showChatBtn);
        showToolsBtn = (Button) findViewById(R.id.showToolsBtn);
        teachBoardView = (TeachboardView) findViewById(R.id.teachBoardView);
        teachBoardView.setBoardId(boardId);
        teachBoardView.setUserId(username);



        //Set Fragments
        showChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager _fm = getFragmentManager();
                FragmentTransaction _ft = _fm.beginTransaction();
                ChatFragment chatFragment = new ChatFragment();
                _ft.add(R.id.chatFragment, chatFragment);
                _ft.addToBackStack("chatFragment");
                _ft.commit();
            }
        });

        showToolsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager _fm = getFragmentManager();
                FragmentTransaction _ft = _fm.beginTransaction();
                ToolsFragment toolsFragment = new ToolsFragment();

                _ft.add(R.id.toolsFragment, toolsFragment);
                _ft.addToBackStack("toolsFragment");
                _ft.commit();
            }
        });


        //Load image now
        loadImageFromDB(boardId);
    }

    //Dev Ops
    public void saveImage(){
        Bitmap signature = null;
        ByteArrayOutputStream stream = null;
        teachBoardView.buildDrawingCache();
        signature = teachBoardView.getDrawingCache();
        stream = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG,100,stream);
        signature.getByteCount();
        boolean saved = myDb.saveBoardAsImage(boardId, username, stream.toByteArray());
        signature.recycle();
        teachBoardView.destroyDrawingCache();
        if(saved){
            Toast.makeText(WhiteboardActivity.this,"Board Saved Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(WhiteboardActivity.this,"Error saving board",Toast.LENGTH_SHORT).show();
        }
    }


    public void loadImageFromDB(String boardId) {
        //load the image from the db
        Cursor res = myDb.getImageData(boardId);
        while (res.moveToNext()) {
            if(res.isNull(0)){
            }
            byte[] byteArray = res.getBlob(0);
            if(byteArray != null) {
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                teachBoardView.setImageFromDatabase(bm);
                //Display image loaded
                Toast.makeText(WhiteboardActivity.this, boardId+" Board loaded", Toast.LENGTH_SHORT).show();
            }else{
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bm = Bitmap.createBitmap(100,100,conf);
                Toast.makeText(WhiteboardActivity.this, "New board loaded", Toast.LENGTH_SHORT).show();
            }
        }
        res.close();
    }

    public void showBoardData() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            showMessage("Error with Image", "Nothing found");
            res.close();
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("BOARDID: " + res.getString(0) + "\n\n");
                buffer.append("BOARDNAME: " + res.getString(1) + "\n\n");
                buffer.append("USERID: " + res.getString(2) + "\n\n");
                if(res.getBlob(3) != null){
                    buffer.append("BOARDDATA: " + res.getBlob(3).toString() + "\n\n");
                }else{
                    buffer.append("BOARDDATA: NULL \n\n");
                }
            }
            showMessage("Data", buffer.toString());
            res.close();
            return;
        }
    }
    //Helper function

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void clearBoard(String boardId){
        myDb.clearBoard(boardId);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()){
            case R.id.clearBoardBtn:
                clearBoard(boardId);
                return true;
            case R.id.settingsBtn:
                return true;
            case R.id.loadImgBtn:
                loadImageFromDB(boardId);
                return true;
            case R.id.saveImgBtn:
                saveImage();
                return true;
            case R.id.checkDataBtn:
                showBoardData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
