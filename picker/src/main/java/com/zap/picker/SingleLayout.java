package com.zap.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zap.picker.base.BottomPopup;
import com.zap.picker.base.WheelLayout;
import com.zap.picker.base.WheelView;
import com.zap.picker.utils.ScreenHelper;

import java.util.List;

/**
 * 单项选择器控件（非窗口式）
 * <p>
 * Created by Will on 2016/10/18.
 */
public class SingleLayout extends WheelLayout {

    private String firstLabel = "";
    private OnSingleLayoutListener onSingleLayoutListener;
    private String selectValue = "";
    private WheelView firstWheel;
    private TextView firstLabelView;

    public SingleLayout(Context context) {
        this(context, null);
    }

    public SingleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WheelLayout, defStyleAttr, 0);
        if (a != null) {
            for (int i = 0; i < a.getIndexCount(); i++) {
                if (a.getIndex(i) == R.styleable.WheelLayout_first_label) {
                    firstLabel = a.getString(R.styleable.WheelLayout_first_label);
                    break;
                }
            }
            a.recycle();
        }
        initView();
    }

    private void initView() {
        if (isInEditMode()) return;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BottomPopup.WRAP_CONTENT, BottomPopup.WRAP_CONTENT);

        firstWheel = new WheelView(this.getContext());
        firstWheel.setLayoutParams(params);
        firstWheel.setTextSize(textNormalSize, textSelectSize);
        firstWheel.setTextColor(textNormalColor, textSelectColor);
        firstWheel.setOffSet(offSet);
        firstWheel.setMaskColor(maskColor);
        firstWheel.setMaskVisible(isMaskVisible);
        addView(firstWheel);

        firstLabelView = new TextView(getContext());
        firstLabelView.setLayoutParams(params);
        firstLabelView.setTextColor(labelColor);
        firstLabelView.setTextSize(labelSize);
        firstLabelView.setText(firstLabel);
        if (!TextUtils.isEmpty(firstLabel)) {
            firstLabelView.setPadding(labelPadding, 0, 0, 0);
        }
        addView(firstLabelView);

        firstWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectValue = item;
                if (onSingleLayoutListener != null) {
                    onSingleLayoutListener.onSelected(item);
                }
            }
        });
    }

    /**
     * 获取WheelView可提供的最大宽度
     */
    private int getViewWidth(List<String> items) {
        int textWidth = 0;
        TextView tv = new TextView(getContext());
        tv.setSingleLine(true);
        tv.setTextSize(textSelectSize);
        TextPaint textPaint = tv.getPaint();
        for (String item : items) {
            if (textWidth < textPaint.measureText(item)) {
                textWidth = (int) textPaint.measureText(item) + 2 * labelPadding;
            }
        }

        if (textWidth > 3 * ScreenHelper.getScreenPixels(getContext()).widthPixels / 4) {
            textWidth = 3 * ScreenHelper.getScreenPixels(getContext()).widthPixels / 4;
        }
        return textWidth;
    }

    public void setOnSingleLayoutListener(OnSingleLayoutListener onSingleLayoutListener) {
        this.onSingleLayoutListener = onSingleLayoutListener;
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
        firstLabelView.setPadding(labelPadding, 0, 0, 0);
    }

    public String getSelectValue() {
        return selectValue;
    }

    public void setMaskVisible(boolean maskVisible) {
        this.isMaskVisible = maskVisible;
        firstWheel.setMaskVisible(maskVisible);
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        firstLabelView.setTextColor(labelColor);
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
        firstLabelView.setTextSize(labelSize);
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        firstWheel.setMaskColor(maskColor);
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
        firstWheel.setOffSet(offSet);
    }

    public void setLabel(String label) {
        this.firstLabel = label;
    }

    public void setItems(List<String> items, String selectValue) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(selectValue)) {
                this.selectValue = selectValue;
                firstWheel.setLayoutParams(new LinearLayout.LayoutParams(getViewWidth(items), BottomPopup.WRAP_CONTENT));
                firstWheel.setItemList(items, selectValue);
                break;
            }
        }
    }

    public void setItems(List<String> items) {
        setItems(items, items.get(0));
    }

    public interface OnSingleLayoutListener {
        void onSelected(String item);
    }
}
