package com.netease.im.rtskit.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;

import androidx.annotation.RequiresApi;

/**
 * 路径
 * <p/>
 * Created by Administrator on 2015/6/24.
 */
public class MyPath extends Action {
    private Path path;

    public MyPath(Float x, Float y, Integer color, Integer size, boolean enableEraser) {
        super(x, y, color, size, enableEraser);
        path = new Path();
        path.moveTo(x, y);
        path.lineTo(x, y);
    }

    @Override
    public boolean isSequentialAction() {
        return true;
    }

    public void onDraw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawPath(path, paint);
    }


    public void onMove(float mx, float my) {
        path.lineTo(mx, my);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean contains(Path eraserPath, Region clip) {
        Region pathRegion = new Region();
        pathRegion.setPath(this.path, clip);
        Region eraser = new Region();
        eraser.setPath(eraserPath, clip);
        return eraser.op(pathRegion, Region.Op.INTERSECT);
    }

}
