package com.netease.im.rtskit.doodle;

import android.view.MotionEvent;

import androidx.core.view.GestureDetectorCompat;

import com.facebook.react.bridge.ReactContext;
import com.netease.im.rtskit.common.log.LogUtil;
import com.netease.im.rtskit.doodle.constant.WhiteBoardType;
import com.ugee.pentabletinterfacelibrary.IBleUsbDataReturnInterface;
import com.yunti.ble.UGBleModule;

public class GestureDetector extends android.view.GestureDetector.SimpleOnGestureListener {

    private GestureDetectorCompat mDetectorCompat;
    private GestureDetectorDelegate mDelegate;
    private boolean isDown;
    private boolean isMove;
    private byte boardType = WhiteBoardType.BOARD;
    private int mCanvasWidth;
    private int mCanvasHeight;
    private int mBleUsbMaxX = 1;
    private int mBleUsbMaxY = 1;
    private PenView mPenView;

    public void setCanvasWidth(int canvasWidth) {
        mCanvasWidth = canvasWidth;
    }

    public void setCanvasHeight(int canvasHeight) {
        mCanvasHeight = canvasHeight;
    }


    GestureDetector(ReactContext context, GestureDetectorDelegate delegate, PenView penView) {
        mDetectorCompat = new GestureDetectorCompat(context, this);
        mDetectorCompat.setIsLongpressEnabled(false);
        mDelegate = delegate;
        this.mPenView = penView;
        UGBleModule ugBleModule = context.getNativeModule(UGBleModule.class);
        if (ugBleModule != null) {
            ugBleModule.addIBleUsbDataReturnInterface(new IBleUsbDataReturnInterfaceDelegate(delegate));
        }
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

    class IBleUsbDataReturnInterfaceDelegate implements IBleUsbDataReturnInterface {
        private GestureDetectorDelegate mDelegate;

        IBleUsbDataReturnInterfaceDelegate(GestureDetectorDelegate delegate) {
            mDelegate = delegate;
        }

        @Override
        public void onGetBleUsbDataReturn(final byte bleButton, final int bleX, final int bleY, final short blePressure) {
            //包含悬浮(-96)和按下(-95)，以及笔板分离（-64）
            float touchX = bleX * 1f / GestureDetector.this.mBleUsbMaxX * mCanvasWidth;
            float touchY = bleY * 1f / GestureDetector.this.mBleUsbMaxY * mCanvasHeight;
            LogUtil.log("bleButton is %d touchX is %f touchY is %f", bleButton, touchX, touchY);
            LogUtil.log("onGetBleUsbDataReturn %s", Thread.currentThread().getName());
            switch (bleButton) {
                case -96:
                    if (isMove) {
                        isDown = false;
                        isMove = false;
                        mDelegate.onActionEnd();
                    }
                    mPenView.renderPen(touchX, touchY);
                    break;
                case -64:
                    if (isMove) {
                        isDown = false;
                        isMove = false;
                        mDelegate.onActionEnd();
                    }
                    break;
                case -95:
                    if (isDown) {
                        isMove = true;
                        mDelegate.onActionMove(touchX, touchY);
                    } else {
                        isDown = true;
                        mDelegate.onActionStart(touchX, touchY);
                    }
                    mPenView.renderPen(touchX, touchY);
                    break;
            }
        }

        @Override
        public void onGetBleUsbSolfKeyBroad(byte bleHardButton, int bleHardX, int bleHardY) {
            LogUtil.log("villa", "bleHardButton : " + bleHardButton + ",bleHardX : " + bleHardX + ",bleHardY : " + bleHardY);
        }

        @Override
        public void onGetBleUsbHardKeyBroad(final byte bleSolfButton, final int bleSolfX, final int bleSolfY) {
            LogUtil.log("villa", "bleSolfButton : " + bleSolfButton + ",bleSolfX : " + bleSolfX + ",bleSolfY : " + bleSolfY);
        }

        @Override
        public void onGetBleUsbScreenMax(int rc, int maxX, int maxY, int maxButton, int maxPressure) {
            LogUtil.log("onGetBleUsbScreenMax maxX %d maxY %d", maxX, maxY);
            GestureDetector.this.mBleUsbMaxX = Math.max(maxX, 1);
            GestureDetector.this.mBleUsbMaxY = Math.max(maxY, 1);
        }

        @Override
        public void onGetBleUsbConnectType(final int type) {
            LogUtil.log("onGetBleUsbConnectType %d", type);

        }

        @Override
        public void onGetBleUsbBatteryLevel(String s) {
            LogUtil.log("onGetBleUsbBatteryLevel %s", s);
        }
    }
}
