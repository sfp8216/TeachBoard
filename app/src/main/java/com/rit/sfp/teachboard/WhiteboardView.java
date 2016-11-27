package com.rit.sfp.teachboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by DepthCharge on 11/15/2016.
 */

public class WhiteboardView extends ImageView{
    private Path path = new Path();
    DatabaseHelper myDb;

    private ImageView whiteBoardView = (ImageView) findViewById(R.id.whiteBoardView);


    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    // Store circles to draw each time the user touches down
    private List<String> circlePoints;

    private ArrayList<String> pathPoints = new ArrayList<String>();


    public WhiteboardView(Context context, AttributeSet attrs) {
        super(context, attrs);




        





        ImageView testView = (ImageView) findViewById(R.id.testImageView);

          setupPaint(); // same as before
            circlePoints = new ArrayList<String>();
        if(!isInEditMode()) {
            myDb = new DatabaseHelper(getContext());
        }

    }

    // Draw each circle onto the view
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPath(path,drawPaint);

        //   for (Point p : circlePoints) {
        //      canvas.drawCircle(p.x, p.y, 5, drawPaint);
        //  }
    }
public void saveImageToDatabase(int boardId, int userId, byte[] byteArray){
try {
    boolean result = myDb.saveBoardAsImage(1, 1, byteArray);
    if (result = true) {
        Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_SHORT).show();
    } else {
        Toast.makeText(getContext(), "ERROR", Toast.LENGTH_LONG).show();
    }
}catch(NullPointerException e){
    e.printStackTrace();
}
}
    // Append new circle each time user presses on screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                pathPoints.add(touchX+","+touchY);
                path.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                whiteBoardView.buildDrawingCache();
                Bitmap signature = whiteBoardView.getDrawingCache();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                signature.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();
                Log.i("Byte array length", "Counter" + byteArray.length);
                try {
                    saveImageToDatabase(1, 1, byteArray);
                }catch(SQLiteConstraintException e){

                }
                break;
            default: return false;
        }
        //     circlePoints.add(new Point(Math.round(touchX), Math.round(touchY)));
        // indicate view should be redrawn
        postInvalidate();

        return true;
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE); // change to fill
        // ...
    }
}
