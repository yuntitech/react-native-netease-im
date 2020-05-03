package com.netease.im.rtskit.doodle;

import com.netease.im.rtskit.doodle.action.Action;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 涂鸦板通道（输入通道，输出通道）
 * <p/>
 * Created by huangjun on 2015/6/29.
 */
class DoodleChannel {
    /**
     * 当前所选的画笔
     */
    public int type = 0; // 当前的形状类型

    public Action action; // 当前的形状对象

    public int paintColor = 0xFF32C5FF;

    public int paintSize = 5;

    public int lastPaintColor = paintColor; // 上一次使用的画笔颜色（橡皮擦切换回形状时，恢复上次的颜色）

    public int lastPaintSize = paintSize; // 上一次使用的画笔粗细（橡皮擦切换回形状时，恢复上次的粗细）

    /**
     * 记录所有形状的列表
     */
    public List<Action> actions = new CopyOnWriteArrayList<>();
    public String name = "";

    /**
     * 设置当前画笔的形状
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 设置当前画笔为橡皮擦
     */
    public void setEraseType(int bgColor, int size) {
        this.lastPaintColor = this.paintColor; // 备份当前的画笔颜色
        this.lastPaintSize = this.paintSize; // 备份当前的画笔粗细
        this.paintColor = bgColor;
        if (size > 0) {
            paintSize = size;
        }
    }

    /**
     * 设置当前画笔的颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.paintColor = color;
    }

    /**
     * 设置画笔的粗细
     *
     * @param size
     */
    public void setSize(int size) {
        if (size > 0) {
            this.paintSize = size;
        }
    }
}
