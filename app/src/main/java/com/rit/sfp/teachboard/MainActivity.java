package com.rit.sfp.teachboard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    TabHost tabHost;
    EditText boardName, boardOwner, boardStatus;
    Button createBoardBtn, getDataBtn, getBoardDataBtn, colorsBtn, shapesBtn;
    ImageView testView;
    WhiteboardView whiteboardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Chat");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tools");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Create Board");
        host.addTab(spec);

       myDb = new DatabaseHelper(this);

        boardName = (EditText) findViewById(R.id.boardName);
        boardOwner = (EditText) findViewById(R.id.boardOwner);
        boardStatus = (EditText) findViewById(R.id.boardStatus);
        colorsBtn = (Button) findViewById(R.id.ColorsBtn);
        createBoardBtn = (Button) findViewById(R.id.createBoardBtn);
        getDataBtn = (Button) findViewById(R.id.showDatabase);
        getBoardDataBtn = (Button) findViewById(R.id.showData);
        testView = (ImageView) findViewById(R.id.testImageView);
        whiteboardView = (WhiteboardView) findViewById(R.id.whiteBoardView);
        shapesBtn =(Button)findViewById(R.id.Shapes);
        createBoard();
        showAllDatabase();
        showBoardData();
        changeImage();
        saveImage();
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
        shapesBtn.setOnClickListener(
                new View.OnClickListener(){
                    Bitmap signature = null;
                    ByteArrayOutputStream stream = null;
                    @Override
                    public void onClick(View v){
                        whiteboardView.buildDrawingCache();
                        signature = whiteboardView.getDrawingCache();
                        stream = new ByteArrayOutputStream();
                        signature.compress(Bitmap.CompressFormat.PNG,100,stream);
                        signature.getByteCount();
                        Log.i("Byte array length", "Counter" + signature.getByteCount());
                        myDb.saveBoardAsImage(1, 1, stream.toByteArray());
                        signature.recycle();
                        whiteboardView.destroyDrawingCache();
                    }
                }
        );
    }


    public void changeImage() {
        colorsBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable drawable = null;
                        //load the image from the db
                        Cursor res = myDb.getImageData();
                        while (res.moveToNext()) {
                            byte[] byteArray = res.getBlob(0);
                            Log.i("Image length","Length-->" + byteArray.length);
                            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            testView.setImageBitmap(bm);
                             drawable = new BitmapDrawable(getResources(),bm);
                            whiteboardView.setBackground(drawable);

                        }
                        res.close();
                    }
                }
        );
    }

    public void showBoardData(){
        getBoardDataBtn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Cursor res = myDb.getBoardData();
                        Log.i("RES COUNTER", "Counter " + res.getCount());
                        if(res.getCount() == 0){
                            showMessage("Error with Image", "Nothing found");
                            res.close();
                        return;
                        }else{
                            StringBuffer buffer = new StringBuffer();
                            while(res.moveToNext()){
                                buffer.append("BOARDID: " + res.getString(0)+"\n\n");
                                buffer.append("USERID: " + res.getString(1)+"\n\n");
                                buffer.append("BOARDDATA: " + res.getBlob(2).toString()+"\n\n");
                            }
                            showMessage("Data", buffer.toString());
                            res.close();
                            return;
                        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}