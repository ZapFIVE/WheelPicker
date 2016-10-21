package com.zap.picker.base;

import android.app.Activity;
import android.view.View;

import com.zap.picker.utils.ScreenHelper;

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
    protected int labelSize = WheelView.TEXT_SELECT_SIZE;
    protected int labelColor = WheelView.TEXT_SELECT_COLOR;
    protected int maskColor = WheelView.MASK_COLOR;
    protected boolean isMaskVisible = false;
    protected int offSet = WheelView.OFF_SET;
    protected int labelPadding = ScreenHelper.dp2px(activity, 10);

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

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
    }

    public void setTextSize(int textNormalSize, int textSelectSize) {
        this.textNormalSize = textNormalSize;
        this.textSelectSize = textSelectSize;
    }

    public void setTextColor(int textNormalColor, int textSelectColor) {
        this.textNormalColor = textNormalColor;
        this.textSelectColor = textSelectColor;
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
    }
}
