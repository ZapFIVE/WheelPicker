package com.zap.picker;

import android.app.Activity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zap.picker.base.WheelPicker;
import com.zap.picker.base.WheelView;
import com.zap.picker.utils.ScreenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 单项选择器，label可选，遮罩可选
 * <p>
 * Created by Will on 2016/10/12.
 */
public class SinglePicker extends WheelPicker {

    private List<String> items = new ArrayList<>();
    private OnSinglePickListener onSinglePickListener;
    private String label = "";
    private String selectedText = "";

    public SinglePicker(Activity activity, String[] itemsString) {
        super(activity);
        this.items.addAll(Arrays.asList(itemsString));
        if (items.size() != 0) {
            selectedText = items.get(0);
        }
    }

    public SinglePicker(Activity activity, List<String> itemsList) {
        super(activity);
        this.items.addAll(itemsList);
        if (items.size() != 0) {
            selectedText = items.get(0);
        }
    }

    /**
     * 获取WheelView可提供的最大宽度
     */
    private int getViewWidth() {
        int textWidth = 0;
        TextView tv = new TextView(activity);
        tv.setSingleLine(true);
        tv.setTextSize(textSelectSize);
        TextPaint textPaint = tv.getPaint();
        for (String item : items) {
            if (textWidth < textPaint.measureText(item)) {
                textWidth = (int) textPaint.measureText(item) + 2 * labelPadding;
            }
        }

        if (textWidth > 3 * ScreenHelper.getScreenPixels(activity).widthPixels / 4) {
            textWidth = 3 * ScreenHelper.getScreenPixels(activity).widthPixels / 4;
        }
        return textWidth;
    }

    @Override
    protected View initContentView() {
        if (items.size() == 0) {
            throw new IllegalArgumentException("items can't be empty");
        }

        LinearLayout rootView = new LinearLayout(activity);
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        rootView.setGravity(Gravity.CENTER);

        /** 左边LayoutParams **/
        LinearLayout layout_1 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(getViewWidth(), WRAP_CONTENT);
        layout_1.setLayoutParams(params_1);
        layout_1.setGravity(Gravity.CENTER);

        WheelView wv = new WheelView(activity);
        wv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        wv.setTextSize(textNormalSize, textSelectSize);
        wv.setTextColor(textNormalColor, textSelectColor);
        wv.setOffSet(offSet);
        wv.setMaskColor(maskColor);
        wv.setMaskVisible(isMaskVisible);
        layout_1.addView(wv);
        rootView.addView(layout_1);

        TextView tv = new TextView(activity);
        tv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv.setTextColor(labelColor);
        tv.setTextSize(labelSize);
        tv.setText(label);
        if (!TextUtils.isEmpty(label)) {
            tv.setPadding(labelPadding, 0, 0, 0);
        }
        rootView.addView(tv);


        if (!TextUtils.isEmpty(selectedText)) {
            wv.setItemList(items, selectedText);
        } else {
            wv.setItemList(items);
        }
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectedText = item;
            }
        });

        return rootView;
    }

    @Override
    protected void setContentViewAfter(View contentView) {
        super.setContentViewAfter(contentView);
        super.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (onSinglePickListener != null) {
                    onSinglePickListener.onSelected(selectedText);
                }
            }
        });
    }

    public interface OnSinglePickListener {
        void onSelected(String text);
    }

    public void setSelectedIndex(int index) {
        for (int i = 0; i < items.size(); i++) {
            if (index == i) {
                selectedText = items.get(i);
                break;
            }
        }
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public void setOnSinglePickListener(OnSinglePickListener onSinglePickListener) {
        this.onSinglePickListener = onSinglePickListener;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
