package com.rit.sfp.teachboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by steve on 12/6/2016.
 */

public class TeachboardView extends View {
    private Paint tbPaint = new Paint();
    private Bitmap tbBitmap;
    public Canvas tbCanvas;
    private Path drawPath;
    private Paint tbBitmapPaint;
    private float myX, myY;
    private DatabaseHelper myDb;
    private Canvas saveCanvas;
    private Bitmap saveBitmap;

    public String boardId = "";
    public String userId = "";

    public TeachboardView(Context context) {
        super(context);
        boardPaintConfig();
        drawPath = new Path();
        tbBitmapPaint = new Paint(Paint.DITHER_FLAG);
        myDb = new DatabaseHelper(context);
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String newBoardId) {
        boardId = newBoardId;
    }

    public String getuserId() {
        return userId;
    }

    public void setUserId(String newUserId) {
        userId = newUserId;
    }

    public TeachboardView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //When changing orientation, make sure to load the last Bitmap drawable or else it will clear
        if (saveBitmap != null && saveCanvas != null) {
            tbBitmap = saveBitmap;
            tbCanvas = saveCanvas;
        } else {
            tbBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            tbCanvas = new Canvas(tbBitmap);
        }
        boardPaintConfig();
        drawPath = new Path();
        myDb = new DatabaseHelper(getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xffffff);
        canvas.drawBitmap(tbBitmap, 0, 0, tbBitmapPaint);
        canvas.drawPath(drawPath, tbPaint);
    }


    //Cleaner Functions
    private void clean_start(float x, float y) {
        drawPath.reset();
        drawPath.moveTo(x, y);
        myX = x;
        myY = y;
    }

    private void clean_move(float x, float y) {
        myX = x;
        myY = y;
        drawPath.lineTo(x, y);
    }

    private void clean_up() {
        drawPath.lineTo(myX, myY);
        tbCanvas.drawPath(drawPath, tbPaint);
        drawPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clean_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                clean_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                clean_up();
                saveBoard(boardId, userId);
                invalidate();
                break;
        }
        return true;
    }

    private void boardPaintConfig() {
        tbPaint = new Paint();
        tbPaint.setAntiAlias(true);
        tbPaint.setStrokeWidth(5);
        tbPaint.setStrokeJoin(Paint.Join.ROUND);
        tbPaint.setStrokeCap(Paint.Cap.ROUND);
        tbPaint.setStyle(Paint.Style.STROKE);
        tbPaint.setColor(Color.BLACK);
    }

    public void turnBlack() {
        tbPaint.setColor(Color.BLACK);
    }

    public void turnWhite() {
        tbPaint.setColor(Color.WHITE);
    }

    public void erase() {
        Toast.makeText(getContext(), "Erase", Toast.LENGTH_SHORT).show();
        tbPaint.setColor(Color.WHITE);
        tbPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void unErase() {
        Toast.makeText(getContext(), "Draw", Toast.LENGTH_SHORT).show();
        tbPaint.setXfermode(null);
        tbPaint.setAlpha(0x1231231);
        tbPaint.setColor(Color.BLACK);
    }


    public void saveBoard(String boardId, String userId) {
        this.buildDrawingCache();
        Bitmap signature = this.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 100, stream);
        signature.getByteCount();
        myDb.saveBoardAsImage(boardId, userId, stream.toByteArray());
        signature.recycle();
        this.destroyDrawingCache();
    }

    public void setImageFromDatabase(Bitmap bitmap) {
        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        //If tbCanvas is null (On first startup//
        if (tbCanvas == null) {
            tbCanvas = new Canvas(newBitmap);
        }
        tbCanvas.drawBitmap(bitmap.copy(Bitmap.Config.ARGB_8888, true), 0, 0, null);
        saveBitmap = newBitmap;
        saveCanvas = tbCanvas;
    }

    public void increaseBrush() {
        float currentWidth = tbPaint.getStrokeWidth();
        float newWidth = currentWidth + 5;
        //Max size 50
        if (newWidth < 50) {
            tbPaint.setStrokeWidth(newWidth);
            Toast.makeText(getContext(), "Brush size " + newWidth, Toast.LENGTH_SHORT).show();
        }
    }

    public void decreaseBrush() {
        float currentWidth = tbPaint.getStrokeWidth();
        float newWidth = currentWidth - 5;
        //Max size 50
        if (newWidth > 1) {
            tbPaint.setStrokeWidth(newWidth);
            Toast.makeText(getContext(), "Brush size " + newWidth, Toast.LENGTH_SHORT).show();
        }
    }

    public void changeColor(int color) {
        tbPaint.setColor(color);
    }

}
