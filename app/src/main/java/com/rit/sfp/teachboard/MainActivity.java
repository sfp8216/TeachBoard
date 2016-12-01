package com.rit.sfp.teachboard;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//TODO
//Work on implementing a larger screen to draw, changing brush widths etc, and other stuff
public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    TabHost tabHost;
    EditText boardName, boardOwner, boardStatus;
    Button createBoardBtn, getDataBtn, getBoardDataBtn, colorsBtn, shapesBtn, showToolsBtn, showChatBtn, closeToolsBtn,closeChatViewBtn;
    ImageView testView;
    WhiteboardView whiteboardView;

    ListView chatListView;


    ///Temp

    View toolsView, chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playground);

       myDb = new DatabaseHelper(this);
        createBoardBtn = (Button) findViewById(R.id.createBoardBtn);
        getDataBtn = (Button) findViewById(R.id.showDatabase);
        getBoardDataBtn = (Button) findViewById(R.id.showData);
        whiteboardView = (WhiteboardView) findViewById(R.id.whiteBoardView);

        showChatBtn = (Button)findViewById(R.id.showChatBtn);
        showToolsBtn = (Button) findViewById(R.id.showToolsBtn);

        chatListView = (ListView)findViewById(R.id.chatListView);

   /*     createBoard();
        showAllDatabase();
        showBoardData();
        saveImage();*/
        changeImage();

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



    }

    public void createBoard(){
        createBoardBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean status = myDb.createTeachboard(boardName.getText().toString(),boardOwner.getText().toString(),boardStatus.getText().toString());
                        if(status = true){
                            Toast.makeText(MainActivity.this,"Board Created", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

public void saveImage(){
    Bitmap signature = null;
    ByteArrayOutputStream stream = null;
        whiteboardView.buildDrawingCache();
        signature = whiteboardView.getDrawingCache();
        stream = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG,100,stream);
        signature.getByteCount();
        Log.i("Byte array length", "Counter" + signature.getByteCount());
        boolean saved = myDb.saveBoardAsImage(1, 1, stream.toByteArray());
        signature.recycle();
        whiteboardView.destroyDrawingCache();
    if(saved){
        Toast.makeText(MainActivity.this,"Board Saved Successfully",Toast.LENGTH_SHORT).show();
    }else{
        Toast.makeText(MainActivity.this,"Error saving board",Toast.LENGTH_LONG).show();
    }
    }


    public void changeImage() {
        Drawable drawable = null;
        //load the image from the db
        Cursor res = myDb.getImageData();
        while (res.moveToNext()) {
            byte[] byteArray = res.getBlob(0);
            Log.i("Image length","Length-->" + byteArray.length);
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //testView.setImageBitmap(bm);
            drawable = new BitmapDrawable(getResources(),bm);
            whiteboardView.setBackground(drawable);


            //Display image loaded
            Toast.makeText(MainActivity.this,"Board loaded",Toast.LENGTH_LONG).show();
        }
        res.close();
    }

    public void showBoardData() {
        Cursor res = myDb.getBoardData();

        if (res.getCount() == 0) {
            showMessage("Error with Image", "Nothing found");
            res.close();
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("BOARDID: " + res.getString(0) + "\n\n");
                buffer.append("USERID: " + res.getString(1) + "\n\n");
                buffer.append("BOARDDATA: " + res.getBlob(2).toString() + "\n\n");
            }
            showMessage("Data", buffer.toString());
            res.close();
            return;
        }
    }

    public void closeTools(){
        closeToolsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toolsView.setVisibility(View.GONE);
                    }
                }
        );
    }

    public void showAllDatabase(){
        getDataBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0){
                            //No data found
                            showMessage("Error", "No data found");
                            res.close();
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("ID : " + res.getString(0)+"\n\n");
                            buffer.append("NAME : " + res.getString(1)+"\n\n");
                            buffer.append("OWNER : " + res.getString(2)+"\n\n");
                            buffer.append("PUBLIC : " + res.getString(3)+"\n\n");
                        }
                        showMessage("Data", buffer.toString());
                        res.close();
                    }
                }
        );
    }
    //Maybe turn into fragment?


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()){
            case R.id.clearBoardBtn:
                return true;
            case R.id.settingsBtn:
                return true;
            case R.id.loadImgBtn:
                changeImage();
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