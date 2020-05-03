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
        cache.add(new Transaction.Builder().step(ActionStep.START).coordinate(x, y).build());
    }

    public void sendMoveTransaction(float x, float y) {
        cache.add(new Transaction.Builder().step(ActionStep.MOVE).coordinate(x, y).build());
    }

    public void sendEndTransaction(float x, float y) {
        cache.add(new Transaction.Builder().step(ActionStep.END).coordinate(x, y).build());
    }

    public void sendRevokeTransaction() {
        cache.add(new Transaction.Builder().step(ActionStep.REVOKE).build());
    }

    public void sendEraserTransaction() {
        cache.add(new Transaction.Builder().step(ActionStep.ERASER).build());
    }

    public void sendClearSelfTransaction() {
        cache.add(new Transaction.Builder().step(ActionStep.CLEAR_SELF).build());
    }

    public void sendClearAckTransaction() {
        cache.add(new Transaction.Builder().step(ActionStep.CLEAR_ACK).build());
    }

    public void sendUpdateBoard(byte step, int boardId, String url) {
        sendToRemote(new Transaction.Builder().step(step).boardId(boardId).url(url).build());
    }

    public void sendUpdateBoard(byte step, int boardId) {
        sendToRemote(new Transaction.Builder().step(step).boardId(boardId).build());
    }

    public void sendChangePaintColor(byte paintColorType) {
        sendToRemote(new Transaction.Builder().step(ActionStep.CHANGE_PAINT_COLOR)
                .paintColorType(paintColorType).build());
    }

    public void sendChangePptPage(int pptIndex) {
        sendToRemote(new Transaction.Builder().step(ActionStep.PPT_CHANGE_PAGE).pptIndex(pptIndex).build());
    }

    public void sendActionStep(byte step) {
        sendToRemote(new Transaction.Builder().step(step).build());
    }

    public void sendVideoSeek(byte step, double seekTo) {
        sendToRemote(new Transaction.Builder().step(step).seekTo(seekTo).build());
    }

    public void sendStartLesson(byte step, long startLessonTime, int startLessonPrice) {
        sendToRemote(new Transaction.Builder()
                .step(step)
                .startLessonTime(startLessonTime)
                .startLessonPrice(startLessonPrice)
                .build());
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
