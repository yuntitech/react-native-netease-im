package com.netease.im.rtskit.doodle;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Transaction发包管理器
 * <p/>
 * Created by huangjun on 2015/6/24.
 */
public class TransactionManager {

    private static TransactionManager instance;
    private final int TIMER_TASK_PERIOD = 30;

    private String sessionId;

    private String toAccount;

    private Handler handler;

    private List<Transaction> cache = new ArrayList<>(1000);
    private int mBoardId = 1;
    private byte mLoginType = 1;//默认普通用户

    public static TransactionManager get(String sessionId, String account, Context context) {
        if (instance == null) {
            synchronized (TransactionManager.class) {
                if (instance == null) {
                    instance = new TransactionManager(sessionId, account, context);
                }
            }
        }
        return instance;
    }

    public TransactionManager(String sessionId, String toAccount, Context context) {
        this.sessionId = sessionId;
        this.toAccount = toAccount;
        this.handler = new Handler(context.getMainLooper());
        this.handler.postDelayed(timerTask, TIMER_TASK_PERIOD); // 立即开启定时器
    }

    public void end() {
        this.handler.removeCallbacks(timerTask);
        cache.clear();
        instance = null;
    }

    public void registerTransactionObserver(TransactionObserver o) {
        TransactionCenter.getInstance().registerObserver(sessionId, o);
    }

    public void sendStartTransaction(float x, float y) {
        cache.add(new Transaction(ActionStep.START, x, y));
    }

    public void sendMoveTransaction(float x, float y) {
        cache.add(new Transaction(ActionStep.MOVE, x, y));
    }

    public void sendEndTransaction(float x, float y) {
        cache.add(new Transaction(ActionStep.END, x, y));
    }

    public void sendRevokeTransaction() {
        cache.add(new Transaction(ActionStep.REVOKE));
    }

    public void sendEraserTransaction() {
        cache.add(new Transaction(ActionStep.ERASER));
    }

    public void sendClearSelfTransaction() {
        cache.add(new Transaction(ActionStep.CLEAR_SELF));
    }

    public void sendClearAckTransaction() {
        cache.add(new Transaction(ActionStep.CLEAR_ACK));
    }

    public void sendUpdateBoard(byte step, int boardId, String url) {
        sendToRemote(new Transaction(step, String.valueOf(boardId), url));
    }

    public void sendUpdateBoard(byte step, int boardId, String url, String videoTitle, String videoThumb) {
        sendToRemote(new Transaction(step, String.valueOf(boardId), url, videoTitle, videoThumb));
    }

    public void sendUpdateBoard(byte step, int boardId) {
        sendToRemote(new Transaction(step, String.valueOf(boardId)));
    }

    public void sendChangePaintColor(byte step, int paintColorType) {
        sendToRemote(new Transaction(step, String.valueOf(paintColorType)));
    }

    public void sendChangePptPage(byte step, int pptIndex) {
        sendToRemote(new Transaction(step, String.valueOf(pptIndex)));
    }

    public void sendActionStep(byte step) {
        sendToRemote(new Transaction(step));
    }

    public void sendVideoSeek(byte step, double seekTo) {
        sendToRemote(new Transaction(step, String.valueOf(seekTo)));
    }

    public void sendStartLesson(byte step, long startLessonTime, int lessonPrice) {
        sendToRemote(new Transaction(step, String.valueOf(startLessonTime), String.valueOf(lessonPrice)));
    }

    public void sendModifyPrice(byte step, int lessonPrice) {
        sendToRemote(new Transaction(step, String.valueOf(lessonPrice)));
    }

    public void sendImageRotate(byte step, int imgRotateIndex) {
        sendToRemote(new Transaction(step, String.valueOf(imgRotateIndex)));
    }


    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(timerTask);
            {
                if (cache.size() > 0) {
                    sendCacheTransaction();
                }
            }
            handler.postDelayed(timerTask, TIMER_TASK_PERIOD);
        }
    };

    private void sendCacheTransaction() {
        TransactionCenter.getInstance().sendToRemote(sessionId, toAccount, mLoginType, mBoardId, this.cache);
        cache.clear();
    }

    private void sendToRemote(Transaction tran) {
        TransactionCenter.getInstance().sendToRemote(sessionId, toAccount, mLoginType, mBoardId, tran);
    }

    public void changeBoard(int boardId) {
        this.mBoardId = boardId;
    }

    public void setLoginType(byte loginType) {
        this.mLoginType = loginType;
    }

}
