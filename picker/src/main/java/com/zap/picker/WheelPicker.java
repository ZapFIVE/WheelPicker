package com.zap.picker;

import android.app.Activity;
import android.view.View;

/**
 * 选择器基类
 * <p>
 * Created by Will on 2016/10/11.
 */
public abstract class WheelPicker extends CommonPopup<View> {
    protected int textNormalSize = WheelView.TEXT_NORMAL_SIZE;
    protected int textSelectSize = WheelView.TEXT_SELECT_SIZE;
    protected int textNormalColor = WheelView.TEXT_NORMAL_COLOR;
    protected int textSelectColor = WheelView.TEXT_SELECT_COLOR;
    protected int maskColor = WheelView.MASK_COLOR;
    protected boolean isMaskVisible = true;
    protected int offSet = WheelView.OFF_SET;

    public enum RelevanceType {
        BACK_TO_TOP,
        STAY_IN_PLACE
    }

    public WheelPicker(Activity activity) {
        super(activity);
    }

    public void setMaskVisible(boolean maskVisible) {
        isMaskVisible = maskVisible;
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public void setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
    }

    public void setTextNormalSize(int textNormalSize) {
        this.textNormalSize = textNormalSize;
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }

    public void setTextSelectSize(int textSelectSize) {
        this.textSelectSize = textSelectSize;
    }

    public void setTextSize(int textNormalSize, int textSelectSize) {
        this.textNormalSize = textNormalSize;
        this.textSelectSize = textSelectSize;
    }

    public void setTextColor(int textNormalColor, int textSelectColor) {
        this.textNormalColor = textNormalColor;
        this.textSelectColor = textSelectColor;
    }
}
