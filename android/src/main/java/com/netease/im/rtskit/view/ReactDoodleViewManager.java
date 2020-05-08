package com.netease.im.rtskit.view;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.netease.im.rtskit.common.log.LogUtil;
import com.netease.im.rtskit.doodle.DoodleView;
import com.netease.im.rtskit.doodle.ReplayData;
import com.netease.im.rtskit.doodle.ReplayTransaction;
import com.netease.im.rtskit.doodle.Transaction;
import com.netease.im.rtskit.doodle.TransactionCenter;
import com.netease.im.rtskit.doodle.TransactionList;
import com.netease.im.rtskit.doodle.constant.WhiteBoardType;
import com.netease.im.rtskit.util.Util;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.model.RTSTunData;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import okio.BufferedSource;
import okio.Okio;

public class ReactDoodleViewManager extends SimpleViewManager<DoodleContainer> {

    private static final String cmds = "1:0.098940,0.225000;5:3,0;2:0.098482,0.293111;2:0.097020,0.330525;5:4,0;2:0.095704,0.361936;2:0.095406,0.377578;2:0.094100,0.387124;5:5,0;2:0.093538,0.389048;3:0.093538,0.389048;5:6,0;1:0.174323,0.254630;5:7,0;2:0.192568,0.249068;2:0.199831,0.254311;5:8,0;2:0.213550,0.264550;2:0.222826,0.272388;5:9,0;2:0.231272,0.281967;2:0.237842,0.296097;5:10,0;2:0.241250,0.314088;2:0.241658,0.328385;5:11,0;2:0.239612,0.342052;2:0.236626,0.352238;5:12,0;2:0.225329,0.363267;2:0.216005,0.371862;5:13,0;2:0.207337,0.376838;2:0.198308,0.379518;5:14,0;2:0.194658,0.380556;2:0.192469,0.381105;5:15,0;2:0.191138,0.381481;5:16,0;2:0.208632,0.379442;2:0.239281,0.384444;5:17,0;2:0.257678,0.392169;2:0.270289,0.399157;5:18,0;2:0.275959,0.402449;2:0.279446,0.404167;5:19,0;2:0.281311,0.405864;2:0.281802,0.406944;5:20,0;3:0.281802,0.406944;5:21,0;1:0.299764,0.207407;2:0.335026,0.204318;5:22,0;2:0.346175,0.212451;2:0.355063,0.228560;5:23,0;2:0.355401,0.258346;2:0.348441,0.293555;5:24,0;2:0.341160,0.320335;2:0.336729,0.333387;5:25,0;2:0.334256,0.338364;2:0.333628,0.340741;5:26,0;2:0.340838,0.339912;5:27,0;2:0.362114,0.333124;2:0.376500,0.329429;5:28,0;2:0.386967,0.327778;2:0.394350,0.327778;5:29,0;2:0.395278,0.329798;5:30,0;2:0.389761,0.363489;2:0.377902,0.389639;5:31,0;2:0.362301,0.419618;2:0.354262,0.435076;5:32,0;2:0.350239,0.442955;2:0.349262,0.444444;3:0.349262,0.444444;5:33,0;1:0.472909,0.194444;5:34,0;2:0.460505,0.277843;2:0.462214,0.298183;5:35,0;2:0.466021,0.311835;2:0.475645,0.322662;5:36,0;2:0.490027,0.327355;2:0.506071,0.327778;5:37,0;2:0.526954,0.321757;2:0.540186,0.307532;5:38,0;2:0.546461,0.289172;3:0.546461,0.289172;5:39,0;1:0.518257,0.227778;5:40,0;2:0.510887,0.314171;2:0.510601,0.360087;5:41,0;2:0.512624,0.389938;2:0.514840,0.415171;5:42,0;2:0.515901,0.417593;3:0.515901,0.417593;5:43,0;1:0.669022,0.135185;5:44,0;2:0.641291,0.218330;2:0.629342,0.262936;5:45,0;2:0.623224,0.300057;2:0.622497,0.313549;5:46,0;2:0.623867,0.323128;2:0.629779,0.333107;5:47,0;2:0.645544,0.345077;2:0.659198,0.356422;5:48,0;2:0.667231,0.366628;2:0.670807,0.377861;5:49,0;2:0.668709,0.397665;2:0.652692,0.416078;5:50,0;2:0.626822,0.430476;2:0.608622,0.432011;3:0.608622,0.432011;5:51,0;1:0.628975,0.213889;5:52,0;2:0.654222,0.184093;2:0.675456,0.177034;5:53,0;2:0.700887,0.171167;2:0.731507,0.166389;5:54,0;2:0.755853,0.160074;3:0.755853,0.160074;5:55,0;1:0.176090,0.109259;5:56,0;2:0.158989,0.181876;5:57,0;2:0.152918,0.234424;2:0.151618,0.291968;5:58,0;2:0.153256,0.331035;2:0.159538,0.368208;5:59,0;2:0.166225,0.391064;2:0.172905,0.405844;5:60,0;2:0.181628,0.416391;2:0.194304,0.419825;5:61,0;2:0.208837,0.408387;2:0.225828,0.373214;5:62,0;2:0.235119,0.346819;2:0.237851,0.326319;5:63,0;2:0.235032,0.310594;2:0.227631,0.298106;5:64,0;2:0.208295,0.289510;2:0.189603,0.297853;5:65,0;2:0.162029,0.324778;2:0.151595,0.338717;5:66,0;2:0.148410,0.343519;3:0.148410,0.343519;5:67,0;1:0.312721,0.179630;5:68,0;2:0.351706,0.163245;5:69,0;2:0.368789,0.159236;2:0.386324,0.157407;5:70,0;2:0.401714,0.161368;2:0.414873,0.180724;5:71,0;2:0.422969,0.223803;2:0.423218,0.287668;5:72,0;2:0.422850,0.338041;2:0.426735,0.376932;5:73,0;2:0.427562,0.381481;3:0.427562,0.381481;5:74,0;11:https://ppt2h5-1259648581.file.myqcloud.com/g1o54ufrpr7q4madf7qb/index.html,05:75,0;1:0.171378,0.319444;5:76,0;2:0.192641,0.246061;2:0.209737,0.224346;5:77,0;2:0.233098,0.204394;2:0.250113,0.197484;5:78,0;2:0.261230,0.194386;2:0.273992,0.197956;5:79,0;2:0.282774,0.216211;2:0.282359,0.260308;5:80,0;2:0.271425,0.324963;2:0.262361,0.384589;5:81,0;2:0.254357,0.427985;2:0.250766,0.460432;5:82,0;2:0.249706,0.479849;2:0.251223,0.491417;5:83,0;2:0.253407,0.499006;2:0.256814,0.502331;5:84,0;2:0.263544,0.500265;2:0.275260,0.479790;5:85,0;2:0.285417,0.447596;2:0.288530,0.415375;5:86,0;2:0.283902,0.384442;2:0.277013,0.362092;5:87,0;2:0.265523,0.341027;2:0.247343,0.318459;5:88,0;2:0.230570,0.302905;2:0.211551,0.291819;5:89,0;2:0.198672,0.286191;2:0.188446,0.281708;5:90,0;3:0.188446,0.281708;5:91,0;1:0.475265,0.230556;5:92,0;2:0.442939,0.212037;2:0.420320,0.221225;5:93,0;2:0.390358,0.255702;2:0.369427,0.304098;5:94,0;2:0.360037,0.339043;2:0.357829,0.361978;5:95,0;2:0.360601,0.373827;2:0.366766,0.380566;5:96,0;2:0.391722,0.361449;2:0.424348,0.318959;5:97,0;2:0.453131,0.285291;2:0.477033,0.248989;5:98,0;2:0.490377,0.232875;2:0.496362,0.226008;5:99,0;2:0.498394,0.223822;2:0.499509,0.224074;5:100,0;2:0.498772,0.283056;2:0.491571,0.349325;5:101,0;2:0.484258,0.390209;2:0.482921,0.422708;5:102,0;2:0.481743,0.431481;3:0.481743,0.431481;5:103,0;1:0.617786,0.148148;5:104,0;2:0.601112,0.251993;2:0.596849,0.315191;5:105,0;2:0.597397,0.371555;2:0.599851,0.393365;5:106,0;2:0.606586,0.418184;2:0.611328,0.426885;5:107,0;2:0.615782,0.431295;2:0.626247,0.423069;5:108,0;2:0.644295,0.388984;2:0.657731,0.344422;5:109,0;2:0.667533,0.300828;2:0.675035,0.245326;5:110,0;2:0.680382,0.198395;2:0.681357,0.175616;5:111,0;2:0.680935,0.163304;2:0.677182,0.153226;5:112,0;2:0.658205,0.154600;2:0.628912,0.171821;5:113,0;2:0.595107,0.197548;2:0.588339,0.201852;3:0.588339,0.201852;5:114,0;";
    private static final String PROP_SESSION_ID_AND_ACCOUNT = "sessionIdAndAccount";
    private static final String PROP_BOARD_ID = "boardId";
    private static final String PROP_BOARD_TYPE = "boardType";
    private static final String PROP_PAINT_COLOR_TYPE = "paintColorType";
    private static final String PROP_REMOTE_PAINT_COLOR_TYPE = "remotePaintColorType";
    private static final String PROP_LOGIN_TYPE = "loginType";
    private static final String PROP_CANVAS_SIZE = "canvasSize";
    private static final SparseIntArray PAINT_COLOR_SUPPORT;

    static {
        PAINT_COLOR_SUPPORT = new SparseIntArray();
        PAINT_COLOR_SUPPORT.put(0, 0xff000000);
        PAINT_COLOR_SUPPORT.put(1, 0xffffffff);
        PAINT_COLOR_SUPPORT.put(2, 0xffE02020);
        PAINT_COLOR_SUPPORT.put(3, 0xffff6f00);
        PAINT_COLOR_SUPPORT.put(4, 0xfffdd834);
        PAINT_COLOR_SUPPORT.put(5, 0xff19a619);
        PAINT_COLOR_SUPPORT.put(6, 0xff00c6c5);
        PAINT_COLOR_SUPPORT.put(7, 0xff00a3ff);
        PAINT_COLOR_SUPPORT.put(8, 0xffbe00ff);

    }

    // 数据接收处理 放到子线程中，防止阻塞数据发送通道 ， 待sdk 优化
    private HandlerThread receiveThread;
    private Handler receiveDataHandler;
    private String mSessionId;
    private static final int COMMAND_REVOKE = 1;
    private static final int COMMAND_ERASER = 2;
    private static final int COMMAND_CHANGE_PAINT_COLOR = 3;
    private static final int COMMAND_CLEAR = 4;
    private static final int COMMAND_CHANGE_BOARD = 5;
    private static final int COMMAND_REPLAY = 7;
    private static final int COMMAND_SEEK_TO = 8;
    private static final int COMMAND_DELETE_BOARD = 9;
    private boolean isViewDropd = false;
    /**
     * 监听收到对方发送的通道数据
     */
    private Observer<RTSTunData> receiveDataObserver = new Observer<RTSTunData>() {
        @Override
        public void onEvent(final RTSTunData rtsTunData) {
            // 放到子线程中，防止阻塞数据发送通道 ， 待sdk 优化
            String data = "[parse bytes error] , thread = " + Thread.currentThread().getName();
            try {
                data = new String(rtsTunData.getData(), 0, rtsTunData.getLength(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final TransactionList tranList = TransactionCenter.getInstance().unpack(data);
            if (tranList.trans.size() == 0) {
                return;
            }
            // 如果是清屏动作，将之前的都remove 掉， 直接执行清屏动作
            if (tranList.trans.get(0).isClearSelf()) {
                Log.i("RTSActivity", "clear self");
                receiveDataHandler.removeCallbacks(null);
            }

            receiveDataHandler.post(new Runnable() {
                @Override
                public void run() {
                    TransactionCenter.getInstance().onReceive(mSessionId, tranList);
                }
            });
        }
    };

    @Nonnull
    @Override
    public String getName() {
        return "RCTDoodleView";
    }

    @Nonnull
    @Override
    protected DoodleContainer createViewInstance(@Nonnull ThemedReactContext reactContext) {
        this.isViewDropd = false;
        receiveThread = new HandlerThread("receive_data_thread");
        receiveThread.start();
        receiveDataHandler = new Handler(receiveThread.getLooper());
        return new DoodleContainer(reactContext);
    }

    @Override
    public void onDropViewInstance(@Nonnull DoodleContainer doodleContainer) {
        super.onDropViewInstance(doodleContainer);
        this.isViewDropd = true;
        doodleContainer.end();
        if (receiveThread != null) {
            receiveThread.quit();
            receiveThread = null;
        }
        registerReceiveData(false);
    }

    @ReactProp(name = PROP_SESSION_ID_AND_ACCOUNT)
    public void setSessionIdAndAccount(final DoodleContainer doodleContainer, ReadableMap params) {
        mSessionId = Util.strFromReadableMap(params, "sessionId");
        String account = Util.strFromReadableMap(params, "account");
        if (mSessionId != null && account != null) {
            registerReceiveData(true);
            LogUtil.log("setSessionIdAndAccount %s %s", mSessionId, account);
            doodleContainer.initDoodleView(mSessionId, account);
        }
    }

    @ReactProp(name = PROP_BOARD_ID)
    public void setBoardId(final DoodleContainer doodleContainer, int boardId) {
        doodleContainer.getDoodleView().setBoardId(boardId);
    }

    @ReactProp(name = PROP_BOARD_TYPE)
    public void setBoardType(final DoodleContainer doodleContainer, int boardType) {
        doodleContainer.getDoodleView().setBoardType((byte) boardType);
    }

    @ReactProp(name = PROP_PAINT_COLOR_TYPE)
    public void setPaintColorType(final DoodleContainer doodleContainer, int paintColorType) {
        doodleContainer.getDoodleView().setPaintColor(PAINT_COLOR_SUPPORT.get(paintColorType));
    }

    @ReactProp(name = PROP_REMOTE_PAINT_COLOR_TYPE)
    public void setRemotePaintColorType(final DoodleContainer doodleContainer, int remotePaintColorType) {
        doodleContainer.getDoodleView().setPlaybackColor(PAINT_COLOR_SUPPORT.get(remotePaintColorType));
    }

    @ReactProp(name = PROP_LOGIN_TYPE)
    public void setLoginType(final DoodleContainer doodleContainer, int loginType) {
        doodleContainer.getDoodleView().setLoginType((byte) loginType);
    }

    @ReactProp(name = PROP_CANVAS_SIZE)
    public void setCanvasSize(final DoodleContainer doodleContainer, ReadableMap size) {
        doodleContainer.getDoodleView().setCanvasSize(size.getInt("width"), size.getInt("height"));
    }


    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (DoodleView.Events event : DoodleView.Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.<String, Integer>builder()
                .put("doRevoke", COMMAND_REVOKE)
                .put("doEraser", COMMAND_ERASER)
                .put("doChangePaintColor", COMMAND_CHANGE_PAINT_COLOR)
                .put("doClear", COMMAND_CLEAR)
                .put("doChangeBoard", COMMAND_CHANGE_BOARD)
                .put("doReplay", COMMAND_REPLAY)
                .put("doSeekTo", COMMAND_SEEK_TO)
                .put("doDeleteBoard", COMMAND_DELETE_BOARD)
                .build();
    }


    @Override
    public void receiveCommand(@NonNull final DoodleContainer root, int commandId, @Nullable ReadableArray args) {
        DoodleView doodleView = root.getDoodleView();
        switch (commandId) {
            case COMMAND_REVOKE:
                doodleView.paintBack();
                break;
            case COMMAND_ERASER:
                doodleView.enableEraser(args != null && args.getBoolean(0));
                break;
            case COMMAND_CHANGE_PAINT_COLOR:
                ReadableMap params = args != null ? args.getMap(0) : null;
                if (params != null) {
                    byte paintColorType = (byte) params.getInt("paintColorType");
                    int color = PAINT_COLOR_SUPPORT.get(paintColorType);
                    if (getBoolean(params, "self")) {
                        doodleView.setPaintColor(color);
                    } else if (getBoolean(params, "remote")) {
                        doodleView.setPlaybackColor(color);
                    }
                }
                break;
            case COMMAND_CLEAR:
                doodleView.clear();
                break;
            case COMMAND_CHANGE_BOARD:
                int boardId = args != null ? args.getInt(0) : 1;
                int boardType = args != null ? args.getInt(1) : WhiteBoardType.BOARD;
                doodleView.changeBoardPage(boardId, (byte) boardType);
                break;
            case COMMAND_REPLAY:
                doReplay(doodleView);
                break;
            case COMMAND_SEEK_TO:
                doSeekTo(args != null ? args.getInt(0) : 0);
                break;
            case COMMAND_DELETE_BOARD:
                doodleView.deleteBoard(args != null ? args.getInt(0) : 0);
                break;

        }
    }

    private void registerReceiveData(boolean register) {
        if (mSessionId != null) {
            RTSManager.getInstance().observeReceiveData(mSessionId, receiveDataObserver, register);
        }
    }


    private static final int INTERVAL_DRAW = 30;
    private long offsetTime;
    private int mSeekTo;

    private void doReplay(final DoodleView doodleView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                doodleView.initPlaybackChannel();
                ReplayData replayData = TransactionCenter.getInstance().getReplayDataTest(ReplayData.cmds);
//                ReplayData replayData = TransactionCenter.getInstance().getReplayDataFromGz(
//                        doodleView.getContext(), "wb_data");
                final List<ReplayTransaction> trans = replayData.getReplayList();
                if (trans == null || trans.size() == 0) {
                    return;
                }
                final int[] startIndex = {0};
                long startTime = System.currentTimeMillis();
                long prevCurrentTime = 0;
                long currentTime;
                final List<Transaction> replayList = new ArrayList<>();
                while (startIndex[0] < trans.size()) {
                    if (isViewDropd) {
                        break;
                    }
                    if (replayList.size() == 0) {
                        if (mSeekTo > 0) {
                            fillSeekReplayList(trans, replayList, startIndex, startTime, doodleView);
                        } else {
                            fillReplayList(trans, replayList, startIndex[0], startTime);
                        }
//                        LogUtil.log("待回放列表 size is %d", replayList.size());
                        if (replayList.size() > 0) {
                            // 如果是清屏动作，将之前的都remove 掉， 直接执行清屏动作
                            if (replayList.get(0).isClearSelf()) {
                                Log.i("RTSActivity", "clear self");
                                receiveDataHandler.removeCallbacks(null);
                            }

                            receiveDataHandler.post(new Runnable() {
                                @Override
                                public void run() {
//                                    LogUtil.log("已回放列表 size %d", replayList.size());
                                    if (startIndex[0] == 0)
                                        doodleView.clear();
//                                    doodleView.onTransaction(replayList);
                                    startIndex[0] += replayList.size();
                                    replayList.clear();
//                                    LogUtil.log("回放列表清空");
                                }
                            });
                        }
                    }
                    currentTime = (long) ((System.currentTimeMillis() - startTime + offsetTime) / 1000f);
                    if (currentTime > 0 && prevCurrentTime != currentTime) {
                        prevCurrentTime = currentTime;
                        doodleView.emitProgressEvent(currentTime * 1000, replayData.getDuration());
                    }
                    try {
                        Thread.sleep(INTERVAL_DRAW);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mSeekTo = 0;
                offsetTime = 0;
                LogUtil.log("回放结束");
            }
        }).start();

    }


    private void doSeekTo(int time) {
        this.mSeekTo = time;
        LogUtil.log("doSeekTo %d ", time);
    }

    private void fillSeekReplayList(List<ReplayTransaction> trans, List<Transaction> replayList,
                                    int[] startIndex, long startTime, final DoodleView doodleView) {
        //find data before seekTime
        for (int i = 0; i < trans.size(); i++) {
            long tranTime = trans.get(i).getTime();
            if (tranTime >= this.mSeekTo)
                break;
            replayList.add(trans.get(i).getTransaction());
        }
        startIndex[0] = 0;
        long currentTime = System.currentTimeMillis() - startTime;
        this.offsetTime = mSeekTo - currentTime;
        this.mSeekTo = 0;
    }

    private void fillReplayList(List<ReplayTransaction> trans, List<Transaction> replayList, int startIndex, long startTime) {
        for (int i = startIndex; i < trans.size(); i++) {
            long currentTime = System.currentTimeMillis() - startTime + offsetTime;
            if (trans.get(i).getTime() < currentTime) {
                replayList.add(trans.get(i).getTransaction());
            }
        }
    }

    private void decodeWbData(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = context.getAssets().open("wb_data_1");
                    BufferedSource source = Okio.buffer(Okio.source(inputStream));

                    int totalLen = 1024 * 1024 * 4;
                    totalLen = 3587;
                    int readLen = 0;

                    while (readLen < totalLen) {
                        int dataLen = source.readIntLe();
                        if (dataLen == 0) {
                            LogUtil.log("dataLen is 0");
                            break;
                        }
                        long time = source.readIntLe();
                        String data = source.readString((long) (dataLen - 8), Charset.forName("utf-8"));
                        readLen += dataLen;
                        LogUtil.log("dataLen %d readLen is %d time is %d , data is %s ", dataLen, readLen, time, data);
                    }
                    LogUtil.log("读取完成 ... ");


                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.log("error %s", e.getMessage());
                }
            }
        }).start();

    }

    private boolean getBoolean(ReadableMap params, String key) {
        return params.hasKey(key) && params.getBoolean(key);
    }
}
