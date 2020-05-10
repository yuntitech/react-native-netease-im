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

    private byte step;
    //data
    private float x;
    private float y;
    //
    private String dataFirst;
    private String dataSecond;
    private String dataThird;
    private String dataFour;
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
            ActionStep.START_LESSON,
            ActionStep.MODIFY_PRICE,
            ActionStep.END_LESSON
    );
    private static List<Byte> PAINT_OPERATE = Arrays.asList(
            ActionStep.START,
            ActionStep.MOVE,
            ActionStep.ERASER,
            ActionStep.END);

    Transaction(byte step) {
        this.step = step;
    }

    Transaction(byte step, float x, float y) {
        this.step = step;
        this.x = x;
        this.y = y;
    }

    Transaction(byte step, String dataFirst) {
        this.step = step;
        this.dataFirst = dataFirst;
    }

    Transaction(byte step, String dataFirst, String dataSecond) {
        this.step = step;
        this.dataFirst = dataFirst;
        this.dataSecond = dataSecond;
    }

    public Transaction(byte step, String dataFirst, String dataSecond, String dataThird) {
        this.step = step;
        this.dataFirst = dataFirst;
        this.dataSecond = dataSecond;
        this.dataThird = dataThird;
    }

    public Transaction(byte step, String dataFirst, String dataSecond, String dataThird, String dataFour) {
        this.step = step;
        this.dataFirst = dataFirst;
        this.dataSecond = dataSecond;
        this.dataThird = dataThird;
        this.dataFour = dataFour;
    }

    String getDataFirst() {
        return dataFirst;
    }

    String getDataSecond() {
        return dataSecond;
    }

    public String getDataThird() {
        return dataThird;
    }

    public String getDataFour() {
        return dataFour;
    }

    static String pack(Transaction t) {
        switch (t.step) {
            case ActionStep.ADD_BOARD:
            case ActionStep.CHANGE_BOARD:
            case ActionStep.DELETE_BOARD:
            case ActionStep.CHANGE_PAINT_COLOR:
            case ActionStep.PPT_CHANGE_PAGE:
            case ActionStep.VIDEO_SEEK:
            case ActionStep.MODIFY_PRICE:
            case ActionStep.IMAGE_ROTATE:
                return String.format(Locale.CHINA, "%d:%s;", t.step, t.dataFirst);
            case ActionStep.ADD_IMAGE:
            case ActionStep.ADD_PPT:
            case ActionStep.START_LESSON:
                return String.format(Locale.CHINA, "%d:%s,%s", t.step, t.dataFirst, t.dataSecond);
            case ActionStep.ADD_VIDEO:
                return String.format(Locale.CHINA, "%d:%s,%s,%s,%s", t.step, t.dataFirst,
                        t.dataSecond, t.dataThird, t.dataFour);
            case ActionStep.START:
            case ActionStep.MOVE:
            case ActionStep.END:
                return String.format(Locale.CHINA, "%d:%f,%f;", t.step, t.x, t.y);
            default:
                return String.format(Locale.CHINA, "%d:", t.step);
        }

    }


    static Transaction unpack(String data, byte roleType, int currentBoardId) {
//        LogUtil.log("unpack data %s", data);
        int sp1 = data.indexOf(":");
        if (sp1 <= 0) {
            return null;
        }

        String step = data.substring(0, sp1);
        String infoStr = data.substring(sp1 + 1);
        String[] dataInfo = infoStr.split(",");


        try {
            byte p1 = Byte.parseByte(step);
            switch (p1) {
                case ActionStep.ADD_BOARD:
                case ActionStep.DELETE_BOARD:
                case ActionStep.CHANGE_PAINT_COLOR:
                case ActionStep.VIDEO_SEEK:
                case ActionStep.MODIFY_PRICE:
                    return new Transaction(p1, dataInfo[0]);
                case ActionStep.CHANGE_BOARD:
                    return new Transaction(p1, dataInfo[0], String.valueOf(roleType));
                case ActionStep.IMAGE_ROTATE:
                case ActionStep.PPT_CHANGE_PAGE:
                    return new Transaction(p1, dataInfo[0], String.valueOf(currentBoardId));
                case ActionStep.ADD_IMAGE:
                case ActionStep.ADD_PPT:
                case ActionStep.START_LESSON:
                    return new Transaction(p1, dataInfo[0], dataInfo[1]);
                case ActionStep.ADD_VIDEO:
                    return new Transaction(p1, dataInfo[0], dataInfo[1], dataInfo[2], dataInfo[3]);
                case ActionStep.START:
                case ActionStep.MOVE:
                case ActionStep.END:
                    return new Transaction(p1, Float.valueOf(dataInfo[0]), Float.valueOf(dataInfo[1]));
                default:
                    return new Transaction(p1);
            }
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


}
