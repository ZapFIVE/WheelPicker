package com.zap.picker.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.zap.picker.R;
import com.zap.picker.utils.ScreenHelper;

/**
 * 非窗口式选择器基类
 * <p>
 * Created by Will on 2016/10/18.
 */
public abstract class WheelLayout extends LinearLayout {

    protected int textNormalSize = WheelView.TEXT_NORMAL_SIZE;
    protected int textSelectSize = WheelView.TEXT_SELECT_SIZE;
    protected int textNormalColor = WheelView.TEXT_NORMAL_COLOR;
    protected int textSelectColor = WheelView.TEXT_SELECT_COLOR;
    protected int labelSize = WheelView.TEXT_SELECT_SIZE;
    protected int labelColor = WheelView.TEXT_SELECT_COLOR;
    protected int maskColor = WheelView.MASK_COLOR;
    protected boolean isMaskVisible = true;
    protected int offSet = WheelView.OFF_SET;
    protected int labelPadding;

    public WheelLayout(Context context) {
        this(context, null);
    }

    public WheelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WheelLayout, defStyleAttr, 0);
        if (a != null) {
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.WheelLayout_normal_size) {
                    textNormalSize = a.getDimensionPixelSize(attr, WheelView.TEXT_NORMAL_SIZE);
                } else if (attr == R.styleable.WheelLayout_select_size) {
                    textSelectSize = a.getDimensionPixelSize(attr, WheelView.TEXT_SELECT_SIZE);
                } else if (attr == R.styleable.WheelLayout_normal_color) {
                    textNormalColor = a.getColor(attr, WheelView.TEXT_NORMAL_COLOR);
                } else if (attr == R.styleable.WheelLayout_select_color) {
                    textSelectColor = a.getColor(attr, WheelView.TEXT_SELECT_COLOR);
                } else if (attr == R.styleable.WheelLayout_label_size) {
                    labelSize = a.getDimensionPixelSize(attr, WheelView.TEXT_NORMAL_SIZE);
                } else if (attr == R.styleable.WheelLayout_label_color) {
                    labelColor = a.getColor(attr, WheelView.TEXT_SELECT_COLOR);
                } else if (attr == R.styleable.WheelLayout_mask_Color) {
                    maskColor = a.getColor(attr, WheelView.MASK_COLOR);
                } else if (attr == R.styleable.WheelLayout_offSet) {
                    offSet = a.getInteger(attr, WheelView.OFF_SET);
                } else if (attr == R.styleable.WheelLayout_mask_visible) {
                    isMaskVisible = a.getBoolean(attr, false);
                } else if (attr == R.styleable.WheelLayout_label_padding) {
                    labelPadding = a.getDimensionPixelSize(attr, ScreenHelper.dp2px(context, 20));
                }
            }
            a.recycle();
        }

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }
}
