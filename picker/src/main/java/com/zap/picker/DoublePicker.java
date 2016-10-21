package com.zap.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zap.picker.base.WheelPicker;
import com.zap.picker.base.WheelView;
import com.zap.picker.utils.RelevanceType;
import com.zap.picker.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 双项选择器，遮罩可选，label可选，联动可选
 * <p>
 * Created by Will on 2016/10/14.
 */
public class DoublePicker extends WheelPicker {

    private String firstLabel = "", secondLabel = "";
    private String firstSelectValue = "", secondSelectValue = "";
    private List<String> firstList = null, secondList = null;
    private OnDoublePickListener onDoublePickListener;//确认监听
    private boolean isSupportRelevance = false;//是否支持联动
    private SparseArray<List<String>> relevanceRule = null;//联动规则
    private RelevanceType relevanceType = RelevanceType.BACK_TO_TOP;

    public DoublePicker(Activity activity) {
        super(activity);
        this.firstSelectValue = StringHelper.getCurrHour();
        this.secondSelectValue = StringHelper.getCurrMinute();
        firstList = new ArrayList<>();
        secondList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            firstList.add(String.format(Locale.getDefault(), "%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            secondList.add(String.format(Locale.getDefault(), "%02d", i));
        }
    }

    @Override
    protected View initContentView() {
        /** 根部局 **/
        LinearLayout rootView = new LinearLayout(activity);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        rootView.setGravity(Gravity.CENTER);

        /** 左边LayoutParams **/
        LinearLayout layout_1 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        layout_1.setLayoutParams(params_1);
        layout_1.setGravity(Gravity.CENTER);
        /** 左边WheelView **/
        WheelView firstWheel = new WheelView(activity);
        firstWheel.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        firstWheel.setTextSize(textNormalSize, textSelectSize);
        firstWheel.setTextColor(textNormalColor, textSelectColor);
        firstWheel.setOffSet(offSet);
        firstWheel.setMaskColor(maskColor);
        firstWheel.setMaskVisible(isMaskVisible);
        layout_1.addView(firstWheel);
        rootView.addView(layout_1);
        /** 左边Label **/

        TextView tv_1 = new TextView(activity);
        tv_1.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv_1.setTextColor(labelColor);
        tv_1.setTextSize(labelSize);
        tv_1.setText(firstLabel);
        if (!TextUtils.isEmpty(firstLabel)) {
            tv_1.setPadding(labelPadding, 0, labelPadding, 0);
        }
        rootView.addView(tv_1);

        /** 右边LayoutParams **/
        LinearLayout layout_2 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        layout_2.setLayoutParams(params_2);
        layout_2.setGravity(Gravity.CENTER);
        /** 右边WheelView **/
        final WheelView secondWheel = new WheelView(activity);
        secondWheel.setLayoutParams(params_1);
        secondWheel.setTextSize(textNormalSize, textSelectSize);
        secondWheel.setTextColor(textNormalColor, textSelectColor);
        secondWheel.setOffSet(offSet);
        secondWheel.setMaskColor(maskColor);
        secondWheel.setMaskVisible(isMaskVisible);
        layout_2.addView(secondWheel);
        rootView.addView(layout_2);
        /** 右边Label **/

        TextView tv_2 = new TextView(activity);
        tv_2.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv_2.setTextColor(labelColor);
        tv_2.setTextSize(labelSize);
        tv_2.setText(secondLabel);
        if (!TextUtils.isEmpty(secondLabel)) {
            tv_2.setPadding(labelPadding, 0, 0, 0);
        }
        rootView.addView(tv_2);


        firstWheel.setItemList(firstList, firstSelectValue);
        /** 如果设置了联动效果，添加联动集和默认选择值**/
        if (isSupportRelevance && relevanceRule != null && relevanceRule.size() > 0) {
            secondSelectValue = relevanceRule.get(0).get(0);
            secondList = relevanceRule.get(0);
        }
        secondWheel.setItemList(secondList, secondSelectValue);

        firstWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                firstSelectValue = item;
                if (isSupportRelevance) {
                    if (relevanceRule != null && relevanceRule.size() > 0) {
                        if (relevanceType == RelevanceType.BACK_TO_TOP) {
                            secondWheel.setItemList(relevanceRule.get(selectedIndex - offSet), 0);
                        } else if (relevanceType == RelevanceType.STAY_IN_PLACE) {
                            secondWheel.setItemList(relevanceRule.get(selectedIndex - offSet), secondWheel.getSelectedIndex());
                        }
                    } else {
                        throw new IllegalArgumentException("Current RelevanceRule is null or size is 0");
                    }
                }
            }
        });
        secondWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                secondSelectValue = item;
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
                if (onDoublePickListener != null) {
                    onDoublePickListener.onSelected(firstSelectValue, secondSelectValue);
                }
            }
        });
    }

    public void setLabel(String firstLabel, String secondLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
    }

    public void setRelevanceType(RelevanceType relevanceType) {
        this.relevanceType = relevanceType;
    }

    /**
     * 设定选中文本（一定要在setItems或setListAndRelevanceRule之后执行）
     */
    public void setSecletedItem(String firstValue, String secondValue) {
        this.firstSelectValue = firstValue;
        this.secondSelectValue = secondValue;
    }

    public void setItems(List<String> firstList, List<String> secondList) {
        firstSelectValue = firstList.get(0);
        secondSelectValue = secondList.get(0);
        this.firstList = firstList;
        this.secondList = secondList;
    }

    public void setOnDoublePickListener(OnDoublePickListener listener) {
        this.onDoublePickListener = listener;
    }

    /**
     * 设定第一项List和关联规则（默认设定联动为true）
     */
    public void setListAndRelevanceRule(List<String> firstList, SparseArray<List<String>> rules) {
        isSupportRelevance = true;
        this.relevanceRule = rules;

        setItems(firstList, rules.get(0));
    }

    public interface OnDoublePickListener {
        void onSelected(String firstSelectValue, String secondSelectValue);
    }


}
