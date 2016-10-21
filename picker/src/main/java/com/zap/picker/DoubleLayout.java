package com.zap.picker;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zap.picker.base.BottomPopup;
import com.zap.picker.base.WheelLayout;
import com.zap.picker.base.WheelView;

import java.util.List;

/**
 * 双项选择器控件（非窗口式）
 * <p>
 * Created by Will on 2016/10/18.
 */
public class DoubleLayout extends WheelLayout {

    private WheelView firstWheel, secondWheel;
    private TextView firstLabelView, secondLabelView;
    private boolean isSupportRelevance = false;
    private int relevanceType;
    private SparseArray<List<String>> relevanceRule = null;//联动规则
    private OnDoubleLayoutListener onDoubleLayoutListener;
    private String firstSelectValue = "", secondSelectValue = "";
    private String firstLabel = "", secondLabel = "";

    public DoubleLayout(Context context) {
        this(context, null);
    }

    public DoubleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WheelLayout, defStyleAttr, 0);
        if (a != null) {
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.WheelLayout_first_label) {
                    firstLabel = a.getString(attr);
                } else if (attr == R.styleable.WheelLayout_second_label) {
                    secondLabel = a.getString(attr);
                } else if (attr == R.styleable.WheelLayout_relevance_type) {
                    relevanceType = a.getInt(attr, 0);
                }
            }
            a.recycle();
        }

        initView();
    }

    private void initView() {
        if (isInEditMode()) return;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BottomPopup.WRAP_CONTENT, BottomPopup.WRAP_CONTENT);

        /** 左边LayoutParams **/
        LinearLayout layout_1 = new LinearLayout(getContext());
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(BottomPopup.MATCH_PARENT, BottomPopup.WRAP_CONTENT, 1);
        layout_1.setLayoutParams(params_1);
        layout_1.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        firstWheel = new WheelView(this.getContext());
        firstWheel.setLayoutParams(params);
        firstWheel.setTextSize(textNormalSize, textSelectSize);
        firstWheel.setTextColor(textNormalColor, textSelectColor);
        firstWheel.setOffSet(offSet);
        firstWheel.setMaskColor(maskColor);
        firstWheel.setMaskVisible(isMaskVisible);
        layout_1.addView(firstWheel);
        addView(layout_1);

        firstLabelView = new TextView(getContext());
        firstLabelView.setLayoutParams(params);
        firstLabelView.setTextColor(labelColor);
        firstLabelView.setTextSize(labelSize);
        firstLabelView.setText(firstLabel);
        if (!TextUtils.isEmpty(firstLabel)) {
            firstLabelView.setPadding(labelPadding, 0, labelPadding, 0);
        }
        addView(firstLabelView);

        /** 右边LayoutParams **/
        LinearLayout layout_2 = new LinearLayout(getContext());
        LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(BottomPopup.MATCH_PARENT, BottomPopup.WRAP_CONTENT, 1);
        layout_2.setLayoutParams(params_2);
        layout_2.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        secondWheel = new WheelView(this.getContext());
        secondWheel.setLayoutParams(params);
        secondWheel.setTextSize(textNormalSize, textSelectSize);
        secondWheel.setTextColor(textNormalColor, textSelectColor);
        secondWheel.setOffSet(offSet);
        secondWheel.setMaskColor(maskColor);
        secondWheel.setMaskVisible(isMaskVisible);
        layout_2.addView(secondWheel);
        addView(layout_2);

        secondLabelView = new TextView(getContext());
        secondLabelView.setLayoutParams(params);
        secondLabelView.setTextColor(labelColor);
        secondLabelView.setTextSize(labelSize);
        secondLabelView.setText(secondLabel);
        if (!TextUtils.isEmpty(secondLabel)) {
            secondLabelView.setPadding(labelPadding, 0, 0, 0);
        }
        addView(secondLabelView);

        firstWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                firstSelectValue = item;
                if (isSupportRelevance) {
                    if (relevanceRule != null && relevanceRule.size() > 0) {
                        if (isUserScroll) {
                            if (relevanceType == 0) {
                                secondWheel.setItemList(relevanceRule.get(selectedIndex - offSet), 0);
                            } else if (relevanceType == 1) {
                                secondWheel.setItemList(relevanceRule.get(selectedIndex - offSet), secondWheel.getSelectedIndex());
                            }
                        } else {
                            secondWheel.setItemList(relevanceRule.get(selectedIndex - offSet), secondSelectValue);
                        }
                    } else {
                        throw new IllegalArgumentException("Current RelevanceRule is null or size is 0");
                    }
                } else {
                    if (onDoubleLayoutListener != null) {
                        onDoubleLayoutListener.onSelected(firstSelectValue, secondSelectValue);
                    }
                }
            }
        });

        secondWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                secondSelectValue = item;
                if (onDoubleLayoutListener != null) {
                    onDoubleLayoutListener.onSelected(firstSelectValue, secondSelectValue);
                }
            }
        });
    }

    public void setOnDoubleLayoutListener(OnDoubleLayoutListener onDoubleLayoutListener) {
        this.onDoubleLayoutListener = onDoubleLayoutListener;
    }

    /**
     * 一定在设定当前WheelView的items之后执行该方法，否则无效
     */
    public void setSelectValue(String firstSelectValue, String secondSelectValue) {
        this.firstSelectValue = firstSelectValue;
        this.secondSelectValue = secondSelectValue;
        firstWheel.setSelectedItem(firstSelectValue);
        secondWheel.setSelectedItem(secondSelectValue);
    }

    public void setMaskVisible(boolean maskVisible) {
        isMaskVisible = maskVisible;
        firstWheel.setMaskVisible(maskVisible);
        secondWheel.setMaskVisible(maskVisible);
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        firstLabelView.setTextColor(labelColor);
        secondLabelView.setTextColor(labelColor);
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
        firstLabelView.setPadding(labelPadding, 0, labelPadding, 0);
        secondLabelView.setPadding(labelPadding, 0, 0, 0);
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
        firstLabelView.setTextSize(labelSize);
        secondLabelView.setTextSize(labelSize);
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        firstWheel.setMaskColor(maskColor);
        secondWheel.setMaskColor(maskColor);
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
        firstWheel.setOffSet(offSet);
        secondWheel.setOffSet(offSet);
    }

    public void setLabel(String firstLabel, String secondLabel) {
        this.firstLabelView.setText(firstLabel);
        this.secondLabelView.setText(secondLabel);
    }

    public void setItems(List<String> firstList, List<String> secondList) {
        firstSelectValue = firstList.get(0);
        secondSelectValue = secondList.get(0);
        firstWheel.setItemList(firstList, 0);
        secondWheel.setItemList(secondList, 0);
    }

    public void setItems(List<String> firstList, List<String> secondList, String firstValue, String secondValue) {
        this.firstSelectValue = firstValue;
        this.secondSelectValue = secondValue;
        firstWheel.setItemList(secondList, firstValue);
        secondWheel.setItemList(secondList, secondValue);
    }

    public void setListAndRelevanceRule(List<String> firstList, SparseArray<List<String>> relevanceRule) {
        isSupportRelevance = true;
        this.relevanceRule = relevanceRule;

        setItems(firstList, relevanceRule.get(0));
    }

    public void setListAndRelevanceRule(List<String> firstList,
                                        SparseArray<List<String>> relevanceRule,
                                        String firstValue, String secondValue) {
        isSupportRelevance = true;
        this.relevanceRule = relevanceRule;

        setItems(firstList, relevanceRule.get(0), firstValue, secondValue);
    }


    public interface OnDoubleLayoutListener {
        void onSelected(String firstSelectValue, String secondFirstValue);
    }
}
