package com.netease.im.rtskit.doodle;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.netease.im.rtskit.common.log.LogUtil;
import com.netease.nim.util.Util;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSTunData;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okio.BufferedSource;
import okio.Okio;

/**
 * 白板数据收发中心
 * <p/>
 * Created by huangjun on 2015/6/29.
 */
public class TransactionCenter {

    private int index = 0;

    private final String TAG = "TransactionCenter";

    // sessionId to TransactionObserver
    private Map<String, TransactionObserver> observers = new HashMap<>(2);
    String externalDirectory;

    private TransactionCenter() {
        this.externalDirectory = DemoCache.getContext().getExternalFilesDir(null).getAbsolutePath();
    }

    public static TransactionCenter getInstance() {
        return TransactionCenterHolder.instance;
    }

    private static class TransactionCenterHolder {
        public static final TransactionCenter instance = new TransactionCenter();
    }

    public void registerObserver(String sessionId, TransactionObserver o) {
        this.observers.put(sessionId, o);
    }

    /**
     * 数据发送
     */
    public void sendToRemote(String sessionId, String toAccount, byte loginType, int boardId, List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return;
        }

        String data = pack(transactions, loginType, boardId);
//        String data = "1:6731,3150;2:6731,3150;2:6731,3297;2:6731,3480;2:6703,3736;2:6676,4212;2:6621,4469;2:6593,4945;2:6566,5311;2:6511,5641;3:6374,6813;";
        try {
            RTSTunData channelData = new RTSTunData(sessionId, toAccount, data.getBytes
                    ("UTF-8"), data.getBytes().length);
            RTSManager.getInstance().sendData(channelData);
//            saveToLocal(data);
            Log.i(TAG, "SEND DATA = " + index + ", BYTES = " + data.getBytes().length);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("Transaction", "send to remote, getBytes exception : " + data);
        }
    }

    public void sendToRemote(String sessionId, String toAccount, byte loginType, int boardId, Transaction tran) {
        String data = Util.strFormat("%d:%d;%s", loginType, boardId, Transaction.pack(tran));
        try {
            RTSTunData channelData = new RTSTunData(sessionId, toAccount, data.getBytes
                    ("UTF-8"), data.getBytes().length);
            RTSManager.getInstance().sendData(channelData);
            Log.i(TAG, "SEND DATA = " + index + ", BYTES = " + data.getBytes().length);
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("Transaction", "send to remote, getBytes exception : " + data);
        }
    }

    private String pack(List<Transaction> transactions, byte roleType, int boardId) {
        StringBuilder buf = new StringBuilder();
        //header 角色与当前白板id
        buf.append(Util.strFormat("%d:%d;", roleType, boardId));
        for (Transaction t : transactions) {
            buf.append(Transaction.pack(t));
        }

//        LogUtil.log("pack %s", sb.toString());
        // 打入序号
//        buf.append(Transaction.packIndex(++index));

        return buf.toString();
    }

    /**
     * 数据接收
     */
    public void onReceive(String sessionId, TransactionList tranList) {
        if (observers.containsKey(sessionId)) {
            observers.get(sessionId).onTransaction(tranList);
        }
    }

    public TransactionList unpack(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }

        TransactionList tranList = new TransactionList();
        String[] pieces = data.split(";");
        String[] header = pieces[0].split(":");
        tranList.roleType = Byte.valueOf(header[0]);
        tranList.boardId = Integer.valueOf(header[1]);
        String[] newPieces = new String[pieces.length - 1];
        System.arraycopy(pieces, 1, newPieces, 0, newPieces.length);
        for (String p : newPieces) {
            Transaction t = Transaction.unpack(p, tranList.roleType, tranList.boardId);
            if (t != null) {
                tranList.trans.add(t);
            }
        }

        return tranList;
    }

    public ReplayData getReplayDataTest(String replayData) {
        if (TextUtils.isEmpty(replayData)) {
            return null;
        }

        List<ReplayTransaction> transactions = new ArrayList<>();
        String[] pieceGroup = replayData.split("\n");
        long duration = Long.valueOf(pieceGroup[pieceGroup.length - 2]);
        for (int i = 1; i < pieceGroup.length; i += 2) {
            String[] pieces = pieceGroup[i].split(";");
            long time = Long.valueOf(pieceGroup[i - 1]);
            for (String p : pieces) {
                Transaction t = Transaction.unpack(p, (byte) 1, 1);
                if (t != null) {
                    transactions.add(new ReplayTransaction(time, t));
                }
            }
        }

        return new ReplayData(duration, transactions);
    }

    public ReplayData getReplayDataFromGz(Context context, String gzName) {
        long duration = 0;
        List<ReplayTransaction> transactions = new ArrayList<>();
        try {
            InputStream inputStream = context.getAssets().open(gzName);
            BufferedSource source = Okio.buffer(Okio.source(inputStream));
            int totalLen = 1024 * 1024 * 4;
            totalLen = 3587;
            int readLen = 0;
            LogUtil.log("生成回放数据开始");
            long startTime = System.currentTimeMillis();
            while (readLen < totalLen) {
                int dataLen = source.readIntLe();
                if (dataLen == 0) {
                    LogUtil.log("dataLen is 0");
                    break;
                }
                long time = source.readIntLe();
                String data = source.readString((long) (dataLen - 8), Charset.forName("utf-8"));
                String[] pieces = data.split(";");
                for (String p : pieces) {
                    Transaction t = Transaction.unpack(p, (byte) 1, 1);
                    if (t != null) {
                        transactions.add(new ReplayTransaction(time, t));
                    }
                }
                duration += time;
                readLen += dataLen;
                LogUtil.log("dataLen %d readLen is %d time is %d , data is %s ", dataLen, readLen, time, data);
            }
            LogUtil.log("读取完成耗时 %d", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            //ignore
        }
        return new ReplayData(duration, transactions);
    }

    private long startTime = 0;

//    private void saveToLocal(String data) {
//        try {
//            if (startTime == 0) {
//                startTime = System.currentTimeMillis();
//            }
//            File file = new File(String.format("%s/%s.txt", externalDirectory, "cmds"));
//            FileUtils.write(file, (System.currentTimeMillis() - startTime) + "\n", "utf-8", true);
//            FileUtils.write(file,
//                    data + "\n", "utf-8", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
