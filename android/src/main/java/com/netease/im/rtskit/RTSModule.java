package com.netease.im.rtskit;

import android.content.Context;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.netease.im.rtskit.common.log.LogUtil;
import com.netease.im.rtskit.doodle.ActionStep;
import com.netease.im.rtskit.doodle.TransactionManager;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.rts.RTSCallback;
import com.netease.nimlib.sdk.rts.RTSChannelStateObserver;
import com.netease.nimlib.sdk.rts.RTSManager;
import com.netease.nimlib.sdk.rts.constant.RTSEventType;
import com.netease.nimlib.sdk.rts.constant.RTSTimeOutEvent;
import com.netease.nimlib.sdk.rts.constant.RTSTunnelType;
import com.netease.nimlib.sdk.rts.model.RTSCalleeAckEvent;
import com.netease.nimlib.sdk.rts.model.RTSCommonEvent;
import com.netease.nimlib.sdk.rts.model.RTSControlEvent;
import com.netease.nimlib.sdk.rts.model.RTSNotifyOption;
import com.netease.nimlib.sdk.rts.model.RTSOnlineAckEvent;
import com.netease.nimlib.sdk.rts.model.RTSOptions;

import java.util.ArrayList;
import java.util.List;

public class RTSModule {

    private static final String EVENT_CALLEE_ACK = "nimCalleeAckEvent";
    private static final String EVENT_ACCEPT_SESSION = "nimAcceptSession";
    // 数据接收处理 放到子线程中，防止阻塞数据发送通道 ， 待sdk 优化
    private String mAccount;      // 对方帐号
    private String mSessionId;    // 会话的唯一标识
    private DeviceEventManagerModule.RCTDeviceEventEmitter mEventEmitter;

    public String getAccount() {
        return mAccount;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public RTSModule(ReactApplicationContext context) {
        mEventEmitter = context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);

    }

    public boolean canSend() {
        return mSessionId != null && mAccount != null;
    }

    public void startSession(final String account, RTSCallback callback) {
        List<RTSTunnelType> types = new ArrayList<>(1);
//        types.add(RTSTunnelType.AUDIO); 不用云信音频通道
        types.add(RTSTunnelType.DATA);
        String pushContent = account + "发起一个会话";
        String extra = "extra_data";
        RTSOptions options = new RTSOptions().setRecordAudioTun(false)
                .setRecordDataTun(true);
        RTSNotifyOption notifyOption = new RTSNotifyOption();
        notifyOption.apnsContent = pushContent;
        notifyOption.extendMessage = extra;
        mAccount = account;
        mSessionId = RTSManager.getInstance().start(account, types, options, notifyOption, callback);
        if (mSessionId == null) {
//            Toast.makeText(RTSActivity.this, "发起会话失败!", Toast.LENGTH_SHORT).show();
//            onFinish();
        }
    }

    public void acceptNimSession(String account, final Promise promise) {
        this.mAccount = account;
        RTSOptions options = new RTSOptions().setRecordAudioTun(false).setRecordDataTun(true);
        RTSManager.getInstance().accept(mSessionId, options, new RTSCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // 判断开启通道是否成功
                promise.resolve(success);
            }

            @Override
            public void onFailed(int code) {
                promise.reject(String.valueOf(code), code == -1 ?
                        "接受会话失败,音频通道同时只能有一个会话开启" : "接受会话失败");
            }

            @Override
            public void onException(Throwable exception) {
                promise.reject(exception);
            }
        });
    }


    public void endNimSession(final Promise promise) {
        LogUtil.log("endNimSession " + this.mSessionId);
        if (mSessionId != null) {
            RTSManager.getInstance().close(mSessionId, new RTSCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    promise.resolve(true);
                }

                @Override
                public void onFailed(int code) {
                    promise.reject(String.valueOf(code), "rts close failed");
                }

                @Override
                public void onException(Throwable exception) {
                    promise.reject(exception);
                }
            });
        }
    }


    public void registerOutgoingObserver(boolean register) {
        if (mSessionId != null) {
            RTSManager.getInstance().observeCalleeAckNotification(mSessionId, calleeAckEventObserver, register);
        }
    }

    public void registerInComingObserver(boolean register, String sessionId) {
        if (sessionId != null) {
            mSessionId = sessionId;
            RTSManager.getInstance().observeOnlineAckNotification(mSessionId, onlineAckObserver, register);
        }
    }

    public void registerCommonObserver(boolean register) {
        if (mSessionId != null) {
            RTSManager.getInstance().observeChannelState(mSessionId, channelStateObserver, register);
            RTSManager.getInstance().observeHangUpNotification(mSessionId, endSessionObserver, register);
            RTSManager.getInstance().observeTimeoutNotification(mSessionId, timeoutObserver, register);
            RTSManager.getInstance().observeControlNotification(mSessionId, controlObserver, register);
        }
    }

    public void sendBoardCommand(ReadableMap params, Context context) {
        if (canSend()) {
            byte type = getDouble(params, "type").byteValue();
            int id;
            switch (type) {
                case ActionStep.ADD_IMAGE:
                case ActionStep.ADD_PPT:
                case ActionStep.ADD_VIDEO:
                    id = getDouble(params, "id").intValue();
                    String url = getString(params, "url");
                    getTransactionManager(context).sendUpdateBoard(type, id, url);
                    break;
                case ActionStep.ADD_BOARD:
                case ActionStep.CHANGE_BOARD:
                case ActionStep.DELETE_BOARD:
                    id = getDouble(params, "id").intValue();
                    getTransactionManager(context).sendUpdateBoard(type, id);
                    break;
                case ActionStep.CHANGE_PAINT_COLOR:
                    getTransactionManager(context).sendChangePaintColor(type, getInt(params, "paintColorType"));
                    break;
                case ActionStep.PPT_CHANGE_PAGE:
                    getTransactionManager(context).sendChangePptPage(type, getInt(params, "pptIndex"));
                    break;
                case ActionStep.VIDEO_SEEK:
                    getTransactionManager(context).sendVideoSeek(type, getDouble(params, "seekTo"));
                    break;
                case ActionStep.START_LESSON:
                case ActionStep.START_TRAIL:
                    long startLessonTime = getDouble(params, "startLessonTime").longValue();
                    int lessonPrice = getInt(params, "lessonPrice");
                    getTransactionManager(context).sendStartLesson(type, startLessonTime, lessonPrice);
                    break;
                case ActionStep.MODIFY_PRICE:
                    getTransactionManager(context).sendModifyPrice(type, getInt(params, "lessonPrice"));
                    break;
                default:
                    getTransactionManager(context).sendActionStep(type);
                    break;
            }
        }
    }

    /**
     * 主叫方监听被叫方的接受or拒绝会话的响应
     */
    private Observer<RTSCalleeAckEvent> calleeAckEventObserver = new Observer<RTSCalleeAckEvent>() {
        @Override
        public void onEvent(RTSCalleeAckEvent rtsCalleeAckEvent) {
            if (rtsCalleeAckEvent.getEvent() == RTSEventType.CALLEE_ACK_AGREE) {
                LogUtil.log("calleeAckEventObserver isTunReady %b", rtsCalleeAckEvent.isTunReady());
                // 判断SDK自动开启通道是否成功
                if (!rtsCalleeAckEvent.isTunReady()) {
//                    Toast.makeText(RTSActivity.this, "通道开启失败!请查看LOG", Toast.LENGTH_SHORT).show();
//                    return;
                }
//                acceptView(); // 进入会话界面
            } else if (rtsCalleeAckEvent.getEvent() == RTSEventType.CALLEE_ACK_REJECT) {
//                Toast.makeText(RTSActivity.this, R.string.callee_reject, Toast.LENGTH_SHORT).show();
//                onFinish(false);
            }
            emitCalleeAckEvent(rtsCalleeAckEvent);
        }
    };

    /**
     * 监听对方挂断
     */
    private Observer<RTSCommonEvent> endSessionObserver = new Observer<RTSCommonEvent>() {
        @Override
        public void onEvent(RTSCommonEvent rtsCommonEvent) {
//            Toast.makeText(RTSActivity.this, R.string.target_has_end_session, Toast.LENGTH_SHORT).show();
//            onFinish(false);
            LogUtil.log("endSessionObserver ... ");
            remoteEndSession();
        }
    };


    /**
     * 被叫方监听在线其他端的接听响应
     */
    private Observer<RTSOnlineAckEvent> onlineAckObserver = new Observer<RTSOnlineAckEvent>() {
        @Override
        public void onEvent(RTSOnlineAckEvent rtsOnlineAckEvent) {
            if (rtsOnlineAckEvent.getClientType() != ClientType.Android) {
                String client = null;
                switch (rtsOnlineAckEvent.getClientType()) {
                    case ClientType.Web:
                        client = "Web";
                        break;
                    case ClientType.Windows:
                        client = "Windows";
                        break;
                    case ClientType.MAC:
                        client = "Mac";
                        break;
                    default:
                        break;
                }
                if (client != null) {
                    String option = rtsOnlineAckEvent.getEvent() == RTSEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE ?
                            "接受" : "拒绝";
//                    Toast.makeText(RTSActivity.this, "白板演示已在" + client + "端被" + option, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(RTSActivity.this, "白板演示已在其他端处理", Toast.LENGTH_SHORT).show();
                }
//                onFinish();
            }
        }
    };

    /**
     * 监听控制消息
     */
    private Observer<RTSControlEvent> controlObserver = new Observer<RTSControlEvent>() {
        @Override
        public void onEvent(RTSControlEvent rtsControlEvent) {
//            Toast.makeText(RTSActivity.this, rtsControlEvent.getCommandInfo(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 监听当前会话的状态
     */
    private RTSChannelStateObserver channelStateObserver = new RTSChannelStateObserver() {

        @Override
        public void onConnectResult(String sessionId, RTSTunnelType tunType, long channelId, int code, String file) {
            try {
//                Toast.makeText(RTSActivity.this, "onConnectResult, tunType=" + tunType.toString() +
//                        ", channelId=" + channelId +
//                        ", code=" + code + ", file=" + file, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onChannelEstablished(String sessionId, RTSTunnelType tunType) {
            try {
//                Toast.makeText(RTSActivity.this, "onCallEstablished,tunType=" + tunType.toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (tunType == RTSTunnelType.AUDIO) {
                RTSManager.getInstance().setSpeaker(sessionId, true); // 默认开启扬声器
            }
        }

        @Override
        public void onUserJoin(String sessionId, RTSTunnelType tunType, String account) {

        }

        @Override
        public void onUserLeave(String sessionId, RTSTunnelType tunType, String account, int event) {
            LogUtil.log("onUserLeave  " + sessionId);
            //双人直接关闭会话
            remoteEndSession();
        }

        @Override
        public void onDisconnectServer(String sessionId, RTSTunnelType tunType) {
//            Toast.makeText(RTSActivity.this, "onDisconnectServer, tunType=" + tunType.toString(), Toast
//                    .LENGTH_SHORT).show();
            if (tunType == RTSTunnelType.DATA) {
                // 如果数据通道断了，那么关闭会话
//                Toast.makeText(RTSActivity.this, "TCP通道断开，自动结束会话", Toast.LENGTH_SHORT).show();
                LogUtil.log("onDisconnectServer .. ");
                remoteEndSession();
            } else if (tunType == RTSTunnelType.AUDIO) {
                // 如果音频通道断了，那么UI变换
//                if (audioOpen) {
//                    audioSwitch();
//                }
            }
        }

        @Override
        public void onError(String sessionId, RTSTunnelType tunType, int code) {
//            Toast.makeText(RTSActivity.this, "onError, tunType=" + tunType.toString() + ", error=" + code,
//                    Toast.LENGTH_LONG).show();
            LogUtil.log("onError .. ");
            remoteEndSession();
        }

        @Override
        public void onNetworkStatusChange(String sessionId, RTSTunnelType tunType, int value) {
            // 网络信号强弱
            LogUtil.log("onNetworkStatusChange %d", value);
        }
    };
    private Observer<RTSTimeOutEvent> timeoutObserver = new Observer<RTSTimeOutEvent>() {
        @Override
        public void onEvent(RTSTimeOutEvent rtsTimeOutEvent) {
//            Toast.makeText(RTSActivity.this,
//                    (rtsTimeOutEvent == RTSTimeOutEvent.OUTGOING_TIMEOUT) ? getString(R.string.callee_ack_timeout) :
//                            "超时未处理，自动结束", Toast.LENGTH_SHORT).show();
//            onFinish();
            remoteEndSession();
        }
    };

    private void remoteEndSession() {
        mEventEmitter.emit("remoteEndSession", Arguments.createMap());
    }


    private void emitCalleeAckEvent(RTSCalleeAckEvent rtsCalleeAckEvent) {
        WritableMap data = Arguments.createMap();
        data.putString("calleeAck", rtsCalleeAckEvent.getEvent().toString());
        data.putBoolean("isTunReady", rtsCalleeAckEvent.isTunReady());
        mEventEmitter.emit(EVENT_CALLEE_ACK, data);
    }

    private TransactionManager getTransactionManager(Context context) {
        return TransactionManager.get(mSessionId, mAccount, context);
    }

    private Double getDouble(ReadableMap params, String key) {
        return params.hasKey(key) ? params.getDouble(key) : -1;
    }

    private String getString(ReadableMap params, String key) {
        return params.hasKey(key) ? params.getString(key) : null;
    }

    private int getInt(ReadableMap params, String key) {
        return params.hasKey(key) ? params.getInt(key) : 0;
    }

}
