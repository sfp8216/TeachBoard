package com.rit.sfp.teachboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DepthCharge on 11/15/2016.
 */
//CAN BE DELETED
public class WhiteboardView {/*
    private Path path = new Path();
    private Paint drawPaint = new Paint();
    private Bitmap mBitmap = Bitmap.createBitmap(10,10, Bitmap.Config.ARGB_8888);
    private Canvas mCanvas = new Canvas(mBitmap);

    Map<Path,Paint> pathMap = new HashMap<Path,Paint>();
    DatabaseHelper myDb;

    private WhiteboardView teachBoardView = (WhiteboardView) findViewById(R.id.teachBoardView);


    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas

    public WhiteboardView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setupPaint();
        if(!isInEditMode()) {
            myDb = new DatabaseHelper(getContext());
        }
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        teachBoardView.setBackground(new BitmapDrawable(getResources(),bitmap));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap,0,0,drawPaint);
        canvas.drawPath(path,drawPaint);
      //  for(Map.Entry<Path,Paint> p : pathMap.entrySet()){
       //     canvas.drawPath(p.getKey(),p.getValue());
       // }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.reset();
                path.moveTo(touchX,touchY);
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX,touchY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Paint newPaint = new Paint();
                newPaint.set(drawPaint);
                pathMap.put(path,newPaint);
                teachBoardView.buildDrawingCache();
                Bitmap signature = teachBoardView.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                signature.compress(Bitmap.CompressFormat.PNG,100,stream);
                signature.getByteCount();
                myDb.saveBoardAsImage(1, 1, stream.toByteArray());
                signature.recycle();
                teachBoardView.destroyDrawingCache();
                path= new Path();
                path.lineTo(touchX,touchY);
                mCanvas.drawPath(path,drawPaint);
                path.reset();
                invalidate();

                break;
            default: return false;
        }
        invalidate();
        return true;
    }



    private void setupPaint() {
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStyle(Paint.Style.STROKE);
    }

    public void increaseStroke(){

        float currentWidth = drawPaint.getStrokeWidth();
        drawPaint.setStrokeWidth(currentWidth+5);
        invalidate();
    }
    public void decreaseStroke(){
        float currentWidth = drawPaint.getStrokeWidth();
        drawPaint.setStrokeWidth(currentWidth-5);
        invalidate();
    }
    public void erase(){
        drawPaint.setColor(Color.WHITE);
        //drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    public void normalDraw(){
        drawPaint.setColor(Color.BLACK);
      //  drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.));
    }*/
}
