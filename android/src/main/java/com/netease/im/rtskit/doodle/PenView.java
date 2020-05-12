package com.netease.im.rtskit.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

import com.netease.im.R;

public class PenView extends View {

    private Bitmap mPenBitmap;
    private float mPenX;
    private float mPenY;
    private Paint mPaint;

    public PenView(Context context) {
        super(context);
        init(context);
    }

    public void renderPen(float x, float y) {
        this.mPenX = x;
        this.mPenY = y;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.mPenBitmap, mPenX, mPenY - this.mPenBitmap.getHeight(), mPaint);
    }


    private void init(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pen_blue);
        //scale
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f);
        this.mPenBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        // 画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }
}
