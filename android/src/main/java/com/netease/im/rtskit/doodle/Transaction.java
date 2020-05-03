package com.netease.im.rtskit.doodle;

import com.netease.im.rtskit.common.log.LogUtil;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by huangjun on 2015/6/24.
 */
public class Transaction implements Serializable {

    private byte roleType;
    private int currentBoardId;
    private byte step;
    //data
    private int boardId;
    private float x;
    private float y;
    private String url;
    private byte paintColorType;
    private int pptIndex;
    private double seekTo;
    private long startLessonTime;
    private int startLessonPrice;
    private static List<Byte> COMMAND_OPERATE = Arrays.asList(
            ActionStep.ADD_BOARD,
            ActionStep.ADD_IMAGE,
            ActionStep.ADD_PPT,
            ActionStep.ADD_VIDEO,
            ActionStep.CHANGE_BOARD,
            ActionStep.DELETE_BOARD,
            ActionStep.CHANGE_PAINT_COLOR,
            ActionStep.PPT_START_PLAY,
            ActionStep.PPT_END_PLAY,
            ActionStep.PPT_NEXT_FRAME,
            ActionStep.PPT_PREV_FRAME,
            ActionStep.PPT_CHANGE_PAGE,
            ActionStep.VIDEO_PLAY,
            ActionStep.VIDEO_PAUSE,
            ActionStep.VIDEO_SEEK,
            ActionStep.START_LESSON
    );
    private static List<Byte> PAINT_OPERATE = Arrays.asList(
            ActionStep.START,
            ActionStep.MOVE,
            ActionStep.ERASER,
            ActionStep.END);


    Transaction(Builder builder) {
        this.roleType = builder.roleType;
        this.currentBoardId = builder.currentBoardId;
        this.step = builder.step;
        this.boardId = builder.boardId;
        this.x = builder.x;
        this.y = builder.y;
        this.url = builder.url;
        this.paintColorType = builder.paintColorType;
        this.pptIndex = builder.pptIndex;
        this.seekTo = builder.seekTo;
        this.startLessonTime = builder.startLessonTime;
        this.startLessonPrice = builder.startLessonPrice;
    }


    static String pack(Transaction t) {
        switch (t.step) {
            case ActionStep.ADD_IMAGE:
            case ActionStep.ADD_PPT:
            case ActionStep.ADD_VIDEO:
                return String.format(Locale.CHINA, "%d:%d,%s;", t.step, t.boardId, t.url);
            case ActionStep.ADD_BOARD:
            case ActionStep.CHANGE_BOARD:
            case ActionStep.DELETE_BOARD:
                return String.format(Locale.CHINA, "%d:%d;", t.step, t.boardId);
            case ActionStep.CHANGE_PAINT_COLOR:
                return String.format(Locale.CHINA, "%d:%d;", t.step, t.paintColorType);
            case ActionStep.PPT_CHANGE_PAGE:
                return String.format(Locale.CHINA, "%d:%d;", t.step, t.pptIndex);
            case ActionStep.PPT_START_PLAY:
            case ActionStep.PPT_END_PLAY:
            case ActionStep.PPT_NEXT_FRAME:
            case ActionStep.PPT_PREV_FRAME:
            case ActionStep.VIDEO_PLAY:
            case ActionStep.VIDEO_PAUSE:
            case ActionStep.ERASER:
                return String.format(Locale.CHINA, "%d", t.step);
            case ActionStep.VIDEO_SEEK:
                return String.format(Locale.CHINA, "%d:%f;", t.step, t.seekTo);
            case ActionStep.START_LESSON:
                return String.format(Locale.CHINA, "%d:%d,%d", t.step, t.startLessonTime, t.startLessonPrice);
            default:
                return String.format(Locale.CHINA, "%d:%f,%f;", t.step, t.x, t.y);
        }

    }

    static String packIndex(int index) {
        return String.format("5:%d,0;", index);
    }

    static Transaction unpack(String data, byte roleType, int currentBoardId) {
//        LogUtil.log("unpack data %s", data);
        LogUtil.log("##", "unpack " + data);
        int sp1 = data.indexOf(":");
        if (sp1 <= 0) {
            return null;
        }

        String step = data.substring(0, sp1);
        String infoStr = data.substring(sp1 + 1);
        String[] dataInfo = infoStr.split(",");

//        if (dataInfo.length < 2) {
//            return null;
//        }

        try {
            byte p1 = Byte.parseByte(step);
//            if (p1 == 5) {
//                return null;
//            }
            Builder builder = new Builder().step(p1);
            int boardId;
            switch (p1) {
                case ActionStep.ADD_IMAGE:
                case ActionStep.ADD_PPT:
                case ActionStep.ADD_VIDEO:
                    boardId = Integer.parseInt(dataInfo[0]);
                    builder.boardId(boardId).url(dataInfo[1]);
                    break;
                case ActionStep.ADD_BOARD:
                case ActionStep.CHANGE_BOARD:
                case ActionStep.DELETE_BOARD:
                    boardId = Integer.parseInt(dataInfo[0]);
                    builder.roleType(roleType).boardId(boardId);
                    LogUtil.log("##", "command " + p1 + " boardId  " + +boardId);
                    break;
                case ActionStep.CHANGE_PAINT_COLOR:
                    builder.paintColorType(Byte.valueOf(dataInfo[0]));
                    break;
                case ActionStep.PPT_CHANGE_PAGE:
                    builder.boardId(currentBoardId);
                    builder.pptIndex(Integer.valueOf(dataInfo[0]));
                    break;
                case ActionStep.PPT_START_PLAY:
                case ActionStep.PPT_END_PLAY:
                case ActionStep.PPT_NEXT_FRAME:
                case ActionStep.PPT_PREV_FRAME:
                case ActionStep.VIDEO_PLAY:
                case ActionStep.VIDEO_PAUSE:
                case ActionStep.ERASER:
                    break;
                case ActionStep.VIDEO_SEEK:
                    builder.seekTo(Double.valueOf(dataInfo[0]));
                    break;
                case ActionStep.START_LESSON:
                    builder.startLessonTime(Long.valueOf(dataInfo[0]));
                    builder.startLessonPrice(Integer.valueOf(dataInfo[1]));
                    break;
                default:
                    builder.coordinate(Float.valueOf(dataInfo[0]),
                            Float.valueOf(dataInfo[1]));
                    break;
            }
            return builder.build();
        } catch (Exception e) {
            LogUtil.log("error %s", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    int getStep() {
        return step;
    }

    float getX() {
        return x;
    }

    float getY() {
        return y;
    }

    int getBoardId() {
        return boardId;
    }

    byte getRoleType() {
        return roleType;
    }

    int getCurrentBoardId() {
        return currentBoardId;
    }


    String getUrl() {
        return url;
    }

    byte getPaintColorType() {
        return paintColorType;
    }

    int getPptIndex() {
        return pptIndex;
    }

    double getSeekTo() {
        return seekTo;
    }

    long getStartLessonTime() {
        return startLessonTime;
    }

    int getStartLessonPrice() {
        return startLessonPrice;
    }

    boolean isPaint() {
//        return !isRevoke() && !isClearSelf() && !isClearAck();
        return PAINT_OPERATE.contains(step);
    }

    boolean isCommand() {
        return COMMAND_OPERATE.contains(step);
    }

    boolean isRevoke() {
        return step == ActionStep.REVOKE;
    }

    public boolean isClearSelf() {
        return step == ActionStep.CLEAR_SELF;
    }

    boolean isClearAck() {
        return step == ActionStep.CLEAR_ACK;
    }


    public static class Builder {
        private byte roleType;
        private int currentBoardId;
        private int boardId;
        private byte step;
        private float x;
        private float y;
        private String url;
        private byte paintColorType;
        private int pptIndex;
        private double seekTo;
        private long startLessonTime;
        private int startLessonPrice;

        Builder roleType(byte roleType) {
            this.roleType = roleType;
            return this;
        }

        public Builder currentBoardId(int currentBoardId) {
            this.currentBoardId = currentBoardId;
            return this;
        }


        Builder step(byte step) {
            this.step = step;
            return this;
        }


        Builder boardId(int boardId) {
            this.boardId = boardId;
            return this;
        }

        Builder coordinate(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        Builder url(String url) {
            this.url = url;
            return this;
        }

        Builder paintColorType(byte paintColorType) {
            this.paintColorType = paintColorType;
            return this;
        }

        Builder pptIndex(int pptIndex) {
            this.pptIndex = pptIndex;
            return this;
        }

        Builder seekTo(double seekTo) {
            this.seekTo = seekTo;
            return this;
        }

        Builder startLessonTime(long startLessonTime) {
            this.startLessonTime = startLessonTime;
            return this;
        }

        Builder startLessonPrice(int startLessonPrice) {
            this.startLessonPrice = startLessonPrice;
            return this;
        }

        Transaction build() {
            return new Transaction(this);
        }

    }


}
