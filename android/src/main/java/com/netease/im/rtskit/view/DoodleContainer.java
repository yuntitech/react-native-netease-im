package com.netease.im.rtskit.view;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.ThemedReactContext;
import com.netease.im.rtskit.doodle.DoodleView;
import com.netease.im.rtskit.doodle.PenView;
import com.netease.im.rtskit.doodle.constant.ActionTypeEnum;

public class DoodleContainer extends FrameLayout {

    private DoodleView mDoodleView;

    public DoodleView getDoodleView() {
        return mDoodleView;
    }

    public DoodleContainer(@NonNull ThemedReactContext context) {
        super(context);
        init(context);
    }

    private void init(ThemedReactContext context) {
        addDoodleView(context);
    }

    public void initDoodleView(String sessionId, String account) {
        mDoodleView.initTransactionManager(sessionId, account, DoodleView.Mode.BOTH, Color.TRANSPARENT);
        mDoodleView.setPaintSize(10);
        mDoodleView.setPaintType(ActionTypeEnum.Path.getValue());
    }

    public void end() {
        if (mDoodleView != null) {
            mDoodleView.end();
            removeView(mDoodleView);
            mDoodleView = null;
        }

    }

    private void addDoodleView(ThemedReactContext context) {
        PenView penView = new PenView(context);
        mDoodleView = new DoodleView(context, penView);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mDoodleView.setLayoutParams(params);
        addView(mDoodleView);
        addView(penView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


}
