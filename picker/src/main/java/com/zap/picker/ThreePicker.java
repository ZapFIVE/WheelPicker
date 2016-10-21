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
 * 三项选择器，遮罩可选，label可选，联动可选
 * <p>
 * Created by Will on 2016/10/17.
 */
public class ThreePicker extends WheelPicker {
    private String firstLabel = "", secondLabel = "", threeLabel = "";
    private String firstSelectValue = "", secondSelectValue = "", threeSelectValue = "";
    private List<String> firstList = null, secondList = null, threeList = null;
    private OnThreePickListener onThreePickListener;//确认监听
    private boolean oneSupportRelevance = false, twoSupportRelevance = false;//一，二是否支持联动
    private SparseArray<List<String>> oneRelevanceRule = null, twoRelevanceRule = null;//联动规则
    private RelevanceType relevanceType = RelevanceType.BACK_TO_TOP;

    public ThreePicker(Activity activity) {
        super(activity);
        this.firstSelectValue = StringHelper.getCurrHour();
        this.secondSelectValue = StringHelper.getCurrMinute();
        this.threeSelectValue = StringHelper.getCurrSecond();
        firstList = new ArrayList<>();
        secondList = new ArrayList<>();
        threeList = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            firstList.add(String.format(Locale.getDefault(), "%02d", i));
        }
        for (int i = 0; i < 60; i++) {
            secondList.add(String.format(Locale.getDefault(), "%02d", i));
            threeList.add(String.format(Locale.getDefault(), "%02d", i));
        }
    }

    @Override
    protected View initContentView() {
        LinearLayout rootView = new LinearLayout(activity);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        rootView.setOrientation(LinearLayout.HORIZONTAL);
        rootView.setGravity(Gravity.CENTER);

        LinearLayout layout_1 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        layout_1.setLayoutParams(params_1);
        layout_1.setGravity(Gravity.CENTER);

        WheelView firstWheel = new WheelView(activity);
        firstWheel.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        firstWheel.setTextSize(textNormalSize, textSelectSize);
        firstWheel.setTextColor(textNormalColor, textSelectColor);
        firstWheel.setOffSet(offSet);
        firstWheel.setMaskColor(maskColor);
        firstWheel.setMaskVisible(isMaskVisible);
        layout_1.addView(firstWheel);
        rootView.addView(layout_1);

        TextView tv_1 = new TextView(activity);
        tv_1.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv_1.setTextColor(labelColor);
        tv_1.setTextSize(labelSize);
        tv_1.setText(firstLabel);
        if (!TextUtils.isEmpty(firstLabel)) {
            tv_1.setPadding(labelPadding, 0, labelPadding, 0);
        }
        rootView.addView(tv_1);

        LinearLayout layout_2 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        layout_2.setLayoutParams(params_2);
        layout_2.setGravity(Gravity.CENTER);

        final WheelView secondWheel = new WheelView(activity);
        secondWheel.setLayoutParams(params_1);
        secondWheel.setTextSize(textNormalSize, textSelectSize);
        secondWheel.setTextColor(textNormalColor, textSelectColor);
        secondWheel.setOffSet(offSet);
        secondWheel.setMaskColor(maskColor);
        secondWheel.setMaskVisible(isMaskVisible);
        layout_2.addView(secondWheel);
        rootView.addView(layout_2);

        TextView tv_2 = new TextView(activity);
        tv_2.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv_2.setTextColor(labelColor);
        tv_2.setTextSize(labelSize);
        tv_2.setText(secondLabel);
        if (!TextUtils.isEmpty(secondLabel)) {
            tv_2.setPadding(labelPadding, 0, labelPadding, 0);
        }
        rootView.addView(tv_2);

        LinearLayout layout_3 = new LinearLayout(activity);
        LinearLayout.LayoutParams params_3 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, 1);
        layout_3.setLayoutParams(params_3);
        layout_3.setGravity(Gravity.CENTER);

        final WheelView threeWheel = new WheelView(activity);
        threeWheel.setLayoutParams(params_1);
        threeWheel.setTextSize(textNormalSize, textSelectSize);
        threeWheel.setTextColor(textNormalColor, textSelectColor);
        threeWheel.setOffSet(offSet);
        threeWheel.setMaskColor(maskColor);
        threeWheel.setMaskVisible(isMaskVisible);
        layout_3.addView(threeWheel);
        rootView.addView(layout_3);

        TextView tv_3 = new TextView(activity);
        tv_3.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        tv_3.setTextColor(labelColor);
        tv_3.setTextSize(labelSize);
        tv_3.setText(threeLabel);
        if (!TextUtils.isEmpty(threeLabel)) {
            tv_3.setPadding(labelPadding, 0, 0, 0);
        }
        rootView.addView(tv_3);

        firstWheel.setItemList(firstList, firstSelectValue);
        if (oneSupportRelevance && oneRelevanceRule != null && oneRelevanceRule.size() > 0) {
            secondSelectValue = oneRelevanceRule.get(0).get(0);
            secondList = oneRelevanceRule.get(0);
        }
        secondWheel.setItemList(secondList, secondSelectValue);
        if (twoSupportRelevance && twoRelevanceRule != null && twoRelevanceRule.size() > 0) {
            threeSelectValue = twoRelevanceRule.get(0).get(0);
            threeList = twoRelevanceRule.get(0);
        }
        threeWheel.setItemList(threeList, threeSelectValue);

        firstWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                firstSelectValue = item;
                if (oneSupportRelevance) {
                    if (oneRelevanceRule != null && oneRelevanceRule.size() > 0) {
                        if (relevanceType == RelevanceType.BACK_TO_TOP) {
                            secondWheel.setItemList(oneRelevanceRule.get(selectedIndex - offSet), 0);
                        } else if (relevanceType == RelevanceType.STAY_IN_PLACE) {
                            secondWheel.setItemList(oneRelevanceRule.get(selectedIndex - offSet), secondWheel.getSelectedIndex());
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
                if (twoSupportRelevance) {
                    if (twoRelevanceRule != null && twoRelevanceRule.size() > 0) {
                        if (relevanceType == RelevanceType.BACK_TO_TOP) {
                            threeWheel.setItemList(twoRelevanceRule.get(selectedIndex - offSet), 0);
                        } else if (relevanceType == RelevanceType.STAY_IN_PLACE) {
                            threeWheel.setItemList(twoRelevanceRule.get(selectedIndex - offSet), threeWheel.getSelectedIndex());
                        }
                    } else {
                        throw new IllegalArgumentException("Current RelevanceRule is null or size is 0");
                    }
                }
            }
        });
        threeWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                threeSelectValue = item;
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
                if (onThreePickListener != null) {
                    onThreePickListener.onSelected(firstSelectValue, secondSelectValue, threeSelectValue);
                }
            }
        });
    }

    public void setLabel(String firstLabel, String secondLabel, String threeLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
        this.threeLabel = threeLabel;
    }

    public void setRelevanceType(RelevanceType relevanceType) {
        this.relevanceType = relevanceType;
    }

    /**
     * 设定选中文本（一定要在setItems或setListAndRelevanceRule之后执行）
     */
    public void setSecletedItem(String firstValue, String secondValue, String threeValue) {
        this.firstSelectValue = firstValue;
        this.secondSelectValue = secondValue;
        this.threeSelectValue = threeValue;
    }

    public void setItems(List<String> firstList, List<String> secondList, List<String> threeList) {
        firstSelectValue = firstList.get(0);
        secondSelectValue = secondList.get(0);
        threeSelectValue = threeList.get(0);
        this.firstList = firstList;
        this.secondList = secondList;
        this.threeList = threeList;
    }

    /**
     * 设定第一项List和关联规则（默认设定联动为true）
     */
    public void setListAndRelevanceRule(List<String> firstList, SparseArray<List<String>> oneRelevanceRule, SparseArray<List<String>> twoRelevanceRule) {
        oneSupportRelevance = true;
        twoSupportRelevance = true;
        this.oneRelevanceRule = oneRelevanceRule;
        this.twoRelevanceRule = twoRelevanceRule;

        setItems(firstList, oneRelevanceRule.get(0), twoRelevanceRule.get(0));
    }

    public void setOnThreePickListener(OnThreePickListener onThreePickListener) {
        this.onThreePickListener = onThreePickListener;
    }

    public interface OnThreePickListener {
        void onSelected(String firstSelectValue, String secondSelectValue, String threeSelectValue);
    }
}
