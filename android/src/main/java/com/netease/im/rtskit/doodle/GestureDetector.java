package com.netease.im.rtskit.doodle;

import android.content.Context;
import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import com.netease.im.rtskit.doodle.constant.WhiteBoardType;

public class GestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {

    private GestureDetectorCompat mDetectorCompat;
    private GestureDetectorDelegate mDelegate;
    private boolean isDown;
    private boolean isMove;
    private byte boardType = WhiteBoardType.BOARD;

    void setBoardType(byte boardType) {
        this.boardType = boardType;
    }

    GestureDetector(Context context, GestureDetectorDelegate delegate) {
        mDetectorCompat = new GestureDetectorCompat(context, this);
        mDetectorCompat.setIsLongpressEnabled(false);
        mDelegate = delegate;
    }

    void onTouchEvent(MotionEvent event) {
        mDetectorCompat.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isMove) {
                    mDelegate.onActionEnd();
                }
                isDown = false;
                isMove = false;
                break;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        LogUtil.log("onScroll x %f y %f", e2.getX(), e2.getY());
        if (isDown) {
            isMove = true;
            mDelegate.onActionMove(e2.getX(), e2.getY());
        } else {
            isDown = true;
            mDelegate.onActionStart(e1.getX(), e1.getY());
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (!this.isVideoType()) {
            mDelegate.onActionClick();
        }
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (isVideoType()) {
            mDelegate.onActionClick();
        }
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (isVideoType()) {
            mDelegate.onActionDoubleClick();
        }
        return super.onDoubleTap(e);
    }


    private boolean isVideoType() {
        return WhiteBoardType.VIDEO == boardType;
    }

    public interface GestureDetectorDelegate {
        void onActionStart(float touchX, float touchY);

        void onActionMove(float touchX, float touchY);

        void onActionEnd();

        void onActionClick();

        void onActionDoubleClick();

    }
}
