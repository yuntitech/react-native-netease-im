package com.netease.im.rtskit.doodle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.netease.im.rtskit.common.log.LogUtil;
import com.netease.im.rtskit.doodle.action.Action;
import com.netease.im.rtskit.doodle.action.MyPath;
import com.netease.im.rtskit.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 涂鸦板控件（基类）
 * <p/>
 * Created by huangjun on 2015/6/24.
 */
public class DoodleView extends View implements TransactionObserver {

    public enum Events {
        EVENT_CLICK("onPress"),
        EVENT_DOUBLE_CLICK("onPressDouble"),
        EVENT_RECEIVE_RTS_DATA("onReceiveRtsData"),
        EVENT_PROGRESS("onProgress"),
        EVENT_ACTION_START("onActionStart");


        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }


    public enum Mode {
        PAINT,
        PLAYBACK,
        BOTH
    }

    private final String TAG = "##";
    private static final String KEY_PAINT = "KEY_PAINT_";
    private static final String KEY_PLAYBACK = "KEY_PLAYBACK_";

    private ArrayMap<String, DoodleChannel> paintChannelMap;

    private DoodleChannel paintChannel; // 绘图通道

    private DoodleChannel playbackChannel; // 回放通道

    private TransactionManager transactionManager; // 数据发送管理器

    private int bgColor = Color.TRANSPARENT; // 背景颜色

    private float zoomX = 1.0f; // 收发数据时缩放倍数（归一化）
    private float zoomY = 1.0f;

    private float lastX = 0.0f;
    private float lastY = 0.0f;
    private RCTEventEmitter mEventEmitter;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private PorterDuffXfermode mEraserMode;
    private boolean mEnableEraser;
    private boolean mRemoteEraser;
    private GestureDetector mGestureDetector;
    private Path mEraserPath;
    private Region mClip;
    private int boardId = 1;
    private int paintColor = 0xFF000000;
    private int remotePaintColor = 0xFF000000;
    private byte loginType = 1; //默认普通用户

    public DoodleView(ThemedReactContext context) {
        super(context);
        mEventEmitter = context.getJSModule(RCTEventEmitter.class);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            mBitmap.eraseColor(Color.TRANSPARENT);
            drawHistoryActions(mCanvas);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        super.onDraw(canvas);
    }

    private void init() {
        this.setFocusable(true);
        paintChannelMap = new ArrayMap<>();
        // 画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF0091EA);
        mPaint.setStrokeWidth(Util.dp2px(2, getContext()));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mEraserMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        //
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.GestureDetectorDelegate() {
            @Override
            public void onActionStart(float touchX, float touchY) {
                if (mEnableEraser) {
                    onEraserPath(true, touchX, touchY);
                } else {
                    onPaintActionStart(touchX, touchY);
                }
                emitActionStart();
            }

            @Override
            public void onActionMove(float touchX, float touchY) {
                if (mEnableEraser) {
                    onEraserPath(false, touchX, touchY);
                } else {
                    onPaintActionMove(touchX, touchY);
                }

            }

            @Override
            public void onActionEnd() {
                if (!mEnableEraser) {
                    onPaintActionEnd();
                }
            }

            @Override
            public void onActionClick() {
                emitClick();
            }

            @Override
            public void onActionDoubleClick() {
                emitDoubleClick();
            }
        });
        this.paintChannel = getPaintChannel(KEY_PAINT + boardId);
        // 橡皮檫路径
        this.mEraserPath = new Path();
    }

    /**
     * 初始化（必须调用）
     *
     * @param mode    设置板书模式
     * @param bgColor 设置板书的背景颜色
     */
    public void initTransactionManager(String sessionId, String account, Mode mode, int bgColor) {
        this.transactionManager = TransactionManager.get(sessionId, account, getContext());
        this.transactionManager.setLoginType(this.loginType);

        if (mode == Mode.PLAYBACK || mode == Mode.BOTH) {
            initPlaybackChannel();
            this.transactionManager.registerTransactionObserver(this);
        }

        this.bgColor = bgColor;
    }

    public void setCanvasSize(int widthDp, int heightDp) {
        float density = getResources().getDisplayMetrics().density;
        int width = (int) (widthDp * density);
        int height = (int) (heightDp * density);
        // 初始化画板
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        zoomX = width;
        zoomY = height;
        this.mClip = new Region(0, 0, width, height);
    }

    public void initPlaybackChannel() {
        if (this.playbackChannel == null) {
            this.playbackChannel = this.getPaintChannel(KEY_PLAYBACK + 1);
        }
    }

    /**
     * 退出涂鸦板时调用
     */
    public void end() {
        if (transactionManager != null) {
            transactionManager.end();
        }
    }

    /**
     * ******************************* 绘图板 ****************************
     */

    /**
     * 设置绘制时的画笔颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        this.paintColor = color;
        setPaintColor(KEY_PAINT, color);
    }

    public void setBoardId(int boardId) {
        if (this.boardId != boardId) {
            this.boardId = boardId;
            this.paintChannel = this.getPaintChannel(KEY_PAINT + boardId);
            this.playbackChannel = this.getPaintChannel(KEY_PLAYBACK + boardId);
            invalidate();
            if (transactionManager != null) {
                transactionManager.changeBoard(boardId);
            }
        }
    }

    public void setBoardType(byte boardType) {
        mGestureDetector.setBoardType(boardType);
    }

    /**
     * 设置回放时的画笔颜色
     *
     * @param color
     */
    public void setPlaybackColor(int color) {
        this.remotePaintColor = color;
        setPaintColor(KEY_PLAYBACK, color);
    }

    public void setLoginType(byte loginType) {
        this.loginType = loginType;
    }

    /**
     * 设置画笔的粗细
     *
     * @param size
     */
    public void setPaintSize(int size) {
        if (size > 0) {
            this.paintChannel.paintSize = size;
            this.playbackChannel.paintSize = size;
        }
    }

    /**
     * 设置当前画笔的形状
     *
     * @param type
     */
    public void setPaintType(int type) {
        this.paintChannel.setType(type);
        this.playbackChannel.setType(type);
    }

    /**
     * 设置当前画笔为橡皮擦
     *
     * @param size 橡皮擦的大小（画笔的粗细)
     */
    public void setEraseType(int size) {
        this.paintChannel.setEraseType(this.bgColor, size);
    }

    /**
     * 撤销一步
     *
     * @return 撤销是否成功
     */
    public synchronized boolean paintBack() {
        if (paintChannel == null) {
            return false;
        }

        boolean res = back(true);
        if (transactionManager != null) {
            transactionManager.sendRevokeTransaction();
        }
        return res;
    }

    /**
     * 橡皮檫
     *
     * @param enable 是否开启
     */
    public synchronized void enableEraser(boolean enable) {
        mEnableEraser = enable;
        if (transactionManager != null) {
            transactionManager.sendEraserTransaction();
        }

    }

    /**
     * 清除
     */
    public synchronized void clear() {
        clearAll();
        if (transactionManager != null) {
            transactionManager.sendClearSelfTransaction();
        }
    }

    public void changeBoardPage(int boardId, byte boardType) {
        this.boardId = boardId;
        mGestureDetector.setBoardType(boardType);
        this.paintChannel = this.getPaintChannel(KEY_PAINT + boardId);
        this.playbackChannel = this.getPaintChannel(KEY_PLAYBACK + boardId);
        invalidate();
        if (transactionManager != null) {
            transactionManager.changeBoard(boardId);
        }
    }

    public void deleteBoard(int boardId) {
        if (boardId > 0) {
            DoodleChannel doodleChannel = this.paintChannelMap.remove(KEY_PAINT + boardId);
            if (doodleChannel != null) {
                doodleChannel.actions.clear();
            }
            DoodleChannel playbackChannel = this.paintChannelMap.remove(KEY_PLAYBACK + boardId);
            if (playbackChannel != null) {
                playbackChannel.actions.clear();
            }
        }
    }

    /**
     * 触摸绘图
     *
     * @param event
     * @return
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private void onPaintActionStart(float x, float y) {
        if (paintChannel == null) {
            return;
        }

        onActionStart(x, y);
        if (transactionManager != null) {
            transactionManager.sendStartTransaction(x / zoomX, y / zoomY);
        }
    }

    private void onPaintActionMove(float x, float y) {
        if (paintChannel == null) {
            return;
        }

        if (!isNewPoint(x, y)) {
            return;
        }

        onActionMove(x, y);
        if (transactionManager != null) {
            transactionManager.sendMoveTransaction(x / zoomX, y / zoomY);
        }
    }

    private void onPaintActionEnd() {
        if (paintChannel == null) {
            return;
        }

        onActionEnd();
        if (transactionManager != null) {
            transactionManager.sendEndTransaction(lastX / zoomX, lastY / zoomY);
        }
    }

    private void onEraserPath(boolean isStart, float touchX, float touchY) {
        if (isStart) {
            mEraserPath.reset();
            mEraserPath.moveTo(touchX, touchY);
        }
        mEraserPath.lineTo(touchX, touchY);
        for (Action action : paintChannel.actions) {
            LogUtil.log("onEraserPath x %f y %f", touchX, touchX);
            if (action.contains(mEraserPath, mClip)) {
                paintChannel.actions.remove(action);
                invalidate();
                break;
            }
        }

    }

    /**
     * ******************************* 回放板 ****************************
     */

    @Override
    public synchronized void onTransaction(TransactionList tranList) {
//        Log.i(TAG, "onTransaction, size =" + transactions.size());

        if (playbackChannel == null) {
            return;
        }

        //老师的指令 & 白板不一致
        if (tranList.roleType == 3 && this.boardId != tranList.boardId) {
            emitReceiveEvent(new Transaction(ActionStep.CHANGE_BOARD,
                    String.valueOf(tranList.boardId),
                    String.valueOf(tranList.roleType)));
        }
        List<Transaction> cache = new ArrayList<>(tranList.trans.size());
        for (Transaction t : tranList.trans) {
            if (t == null) {
                continue;
            }
//            LogUtil.log("onTransaction step %d isAddOperate %b", t.getStep(), t.isAddOperate());
            if (t.isPaint()) {
                // 正常画笔
                cache.add(t);
            } else if (t.isCommand()) {
                emitReceiveEvent(t);
            } else {
                onMultiTransactionsDraw(cache, tranList.boardId);
                cache.clear();
                if (t.isRevoke()) {
                    back(false);
                } else if (t.isClearSelf()) {
                    //收到对方clear 数据的请求
                    clearAll();
                    if (transactionManager != null) {
                        transactionManager.sendClearAckTransaction();
                    }
                } else if (t.isClearAck()) {
                    //对方收到了你的 clear 请求
//                    clearAll();
                }
            }
        }

        if (cache.size() > 0) {
            onMultiTransactionsDraw(cache, tranList.boardId);
            cache.clear();
        }
    }

    private void setPlaybackEraseType(int size) {
        this.playbackChannel.setEraseType(this.bgColor, size);
    }

    /**
     * ******************************* 基础绘图封装 ****************************
     */

    private void onActionStart(float x, float y) {
        DoodleChannel channel = paintChannel;
        if (channel == null) {
            return;
        }

        channel.action = new MyPath(x, y, channel.paintColor, channel.paintSize, mEnableEraser);
    }

    private void onActionMove(float x, float y) {
        DoodleChannel channel = paintChannel;
        if (channel == null) {
            return;
        }

        if (channel.action == null) {
            // 有可能action被清空，此时收到move，重新补个start
            onPaintActionStart(x, y);
        }
        // 绘制当前Action
        channel.action.onMove(x, y);
        invalidate();
    }

    private void onActionEnd() {
        DoodleChannel channel = paintChannel;
        if (channel == null || channel.action == null) {
            return;
        }

        channel.actions.add(channel.action);
        channel.action = null;
    }

    private void onMultiTransactionsDraw(List<Transaction> transactions, int boardId) {
        if (transactions == null || transactions.size() == 0) {
            return;
        }

        DoodleChannel playbackChannel = getPaintChannel(KEY_PLAYBACK + boardId);
        // 绘制新的数据
        for (Transaction t : transactions) {
            switch (t.getStep()) {
                case ActionStep.START:
                    if (playbackChannel.action != null) {
                        // 如果没有收到end包，在这里补提交
                        playbackChannel.actions.add(playbackChannel.action);
                    }

                    playbackChannel.action = new MyPath(t.getX() * zoomX, t.getY() * zoomY, playbackChannel
                            .paintColor, playbackChannel.paintSize, this.mRemoteEraser);
//                    playbackChannel.action.onStart(mCanvas);
                    break;
                case ActionStep.MOVE:
                    if (playbackChannel.action != null) {
                        playbackChannel.action.onMove(t.getX() * zoomX, t.getY() * zoomY);
//                        setPaintAttr(playbackChannel.action.isEnableEraser(), mPaint);
//                        playbackChannel.action.onDraw(mCanvas, mPaint);
                    }
                    break;
                case ActionStep.END:
                    if (playbackChannel.action != null) {
                        playbackChannel.actions.add(playbackChannel.action);
                        playbackChannel.action = null;
                    }
                    break;
                case ActionStep.ERASER:
                    this.mRemoteEraser = !this.mRemoteEraser;
                    break;
                default:
                    break;
            }
        }
        if (this.boardId == boardId) {
            postInvalidate();
        }
    }

    private void drawHistoryActions(Canvas canvas) {
        // 绘制背景
        canvas.drawColor(bgColor);

        if (playbackChannel != null && playbackChannel.actions != null) {
            for (Action a : playbackChannel.actions) {
                setPaintAttr(a.isEnableEraser(), mPaint);
                a.onDraw(canvas, mPaint);
            }

            // 绘制当前
            if (playbackChannel.action != null) {
                setPaintAttr(playbackChannel.action.isEnableEraser(), mPaint);
                playbackChannel.action.onDraw(canvas, mPaint);
            }
        }

        // 绘制所有历史Action
        if (paintChannel != null && paintChannel.actions != null) {
            for (Action a : paintChannel.actions) {
                setPaintAttr(a.isEnableEraser(), mPaint);
                a.onDraw(canvas, mPaint);
            }

            // 绘制当前
            if (paintChannel.action != null) {
                setPaintAttr(paintChannel.action.isEnableEraser(), mPaint);
                paintChannel.action.onDraw(canvas, mPaint);
            }
        }
    }

    private boolean back(boolean isPaintView) {
        DoodleChannel channel = isPaintView ? paintChannel : playbackChannel;
        if (channel == null) {
            return false;
        }

        if (channel.actions != null && channel.actions.size() > 0) {
            channel.actions.remove(channel.actions.size() - 1);
            invalidate();
            return true;
        }
        return false;
    }

    private void clearAll() {
        clear(false);
        clear(true);
    }

    private void clear(boolean isPaintView) {
        DoodleChannel channel = isPaintView ? paintChannel : playbackChannel;
        if (channel == null) {
            return;
        }

        if (channel.actions != null) {
            channel.actions.clear();
        }
        channel.action = null;
        invalidate();
    }

    private boolean isNewPoint(float x, float y) {
        if (Math.abs(x - lastX) <= 0.1f && Math.abs(y - lastY) <= 0.1f) {
            return false;
        }

        lastX = x;
        lastY = y;

        return true;
    }

    private void setPaintAttr(boolean enableEraser, Paint paint) {
//        if (enableEraser) {
//            paint.setStrokeWidth(Util.dp2px(30, getContext()));
//            paint.setXfermode(mEraserMode);
//        } else {
//            paint.setStrokeWidth(Util.dp2px(2, getContext()));
//            paint.setXfermode(null);
//        }
    }

    private void emitReceiveEvent(Transaction t) {
        WritableMap event = Arguments.createMap();
        event.putInt("type", t.getStep());
        switch (t.getStep()) {
            case ActionStep.ADD_IMAGE:
            case ActionStep.ADD_PPT:
            case ActionStep.ADD_VIDEO:
                event.putInt("id", Integer.valueOf(t.getDataFirst()));
                event.putString("url", t.getDataSecond());
                break;
            case ActionStep.ADD_BOARD:
            case ActionStep.DELETE_BOARD:
                event.putInt("id", Integer.valueOf(t.getDataFirst()));
                break;
            case ActionStep.CHANGE_BOARD:
                event.putInt("id", Integer.valueOf(t.getDataFirst()));
                event.putInt("roleType", Integer.valueOf(t.getDataSecond()));
                break;
            case ActionStep.START_LESSON:
                event.putDouble("startLessonTime", Double.valueOf(t.getDataFirst()));
                event.putInt("lessonPrice", Integer.valueOf(t.getDataSecond()));
                break;
            case ActionStep.CHANGE_PAINT_COLOR:
                event.putInt("paintColorType", Integer.valueOf(t.getDataFirst()));
                break;
            case ActionStep.VIDEO_SEEK:
                event.putDouble("seekTo", Double.valueOf(t.getDataFirst()));
                break;
            case ActionStep.PPT_CHANGE_PAGE:
                event.putInt("pptIndex", Integer.valueOf(t.getDataFirst()));
                event.putInt("id", Integer.valueOf(t.getDataSecond()));
                break;
            case ActionStep.MODIFY_PRICE:
                event.putInt("lessonPrice", Integer.valueOf(t.getDataFirst()));
                break;
        }
        mEventEmitter.receiveEvent(getParentId(), Events.EVENT_RECEIVE_RTS_DATA.toString(), event);
    }

    public void emitProgressEvent(long currentTime, long duration) {
        WritableMap data = Arguments.createMap();
        data.putDouble("currentTime", currentTime);
        data.putDouble("duration", duration);
        mEventEmitter.receiveEvent(getParentId(), Events.EVENT_PROGRESS.toString(), data);
    }

    public void emitActionStart() {
        mEventEmitter.receiveEvent(getParentId(), Events.EVENT_ACTION_START.toString(),
                Arguments.createMap());
    }

    private void emitClick() {
        mEventEmitter.receiveEvent(getParentId(), Events.EVENT_CLICK.toString(),
                Arguments.createMap());
    }

    private void emitDoubleClick() {
        mEventEmitter.receiveEvent(getParentId(), Events.EVENT_DOUBLE_CLICK.toString(),
                Arguments.createMap());
    }

    private int getParentId() {
        return ((View) getParent()).getId();
    }

    private DoodleChannel getPaintChannel(String key) {
        DoodleChannel doodleChannel = paintChannelMap.get(key);
        if (doodleChannel == null) {
            doodleChannel = new DoodleChannel();
            doodleChannel.setColor(isPaintChannelKey(key) ? paintColor : remotePaintColor);
            paintChannelMap.put(key, doodleChannel);
        }
        return doodleChannel;
    }

    private void setPaintColor(String paintChannelKey, int color) {
        for (Map.Entry<String, DoodleChannel> entry : paintChannelMap.entrySet()) {
            if (entry.getKey().contains(paintChannelKey)) {
                entry.getValue().setColor(color);
            }
        }
    }

    private boolean isPaintChannelKey(String key) {
        return key.contains(KEY_PAINT);
    }
}
