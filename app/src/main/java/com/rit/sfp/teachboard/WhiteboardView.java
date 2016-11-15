package com.rit.sfp.teachboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DepthCharge on 11/15/2016.
 */

public class WhiteboardView extends View {
    private Path path = new Path();

    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    // Store circles to draw each time the user touches down
    private List<Point> circlePoints;

    public WhiteboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint(); // same as before
        circlePoints = new ArrayList<Point>();
    }

    // Draw each circle onto the view
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,drawPaint);

        //   for (Point p : circlePoints) {
        //      canvas.drawCircle(p.x, p.y, 5, drawPaint);
        //  }
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
                path.lineTo(touchX,touchY);
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
