package com.zap.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private int textLabelSize = textNormalSize;
    private int textLabelColor = 0xFFFFFFFF;
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

        if (!TextUtils.isEmpty(firstLabel)) {
            TextView tv = new TextView(activity);
            tv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            tv.setTextColor(textLabelColor);
            tv.setTextSize(textLabelSize);
            tv.setPadding(20, 0, 0, 0);
            tv.setText(firstLabel);
            rootView.addView(tv);
        }

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

        if (!TextUtils.isEmpty(secondLabel)) {
            TextView tv = new TextView(activity);
            tv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            tv.setTextColor(textLabelColor);
            tv.setTextSize(textLabelSize);
            tv.setPadding(20, 0, 20, 0);
            tv.setText(secondLabel);
            rootView.addView(tv);
        }

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

        if (!TextUtils.isEmpty(threeLabel)) {
            TextView tv = new TextView(activity);
            tv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            tv.setTextColor(textLabelColor);
            tv.setTextSize(textLabelSize);
            tv.setPadding(20, 0, 20, 0);
            tv.setText(threeLabel);
            rootView.addView(tv);
        }

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
                    onThreePickListener.onThreePicked(firstSelectValue, secondSelectValue, threeSelectValue);
                }
            }
        });
    }

    public void setLabel(String firstLabel, String secondLabel, String threeLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
        this.threeLabel = threeLabel;
    }

    public void setSecletedItem(String firstValue, String secondValue, String threeValue) {
        this.firstSelectValue = firstValue;
        this.secondSelectValue = secondValue;
        this.threeSelectValue = threeValue;
    }

    public void setItemList(List<String> firstList, List<String> secondList, List<String> threeList) {
        this.firstList = firstList;
        this.secondList = secondList;
        this.threeList = threeList;
    }

    public void setFirstList(List<String> firstList) {
        this.firstList = firstList;
    }

    public void setSupportRelevance(boolean isSupportRelevance) {
        this.oneSupportRelevance = isSupportRelevance;
        this.twoSupportRelevance = isSupportRelevance;
    }

    public void setRelevanceRule(SparseArray<List<String>> oneRelevanceRule, SparseArray<List<String>> twoRelevanceRule) {
        this.oneRelevanceRule = oneRelevanceRule;
        this.twoRelevanceRule = twoRelevanceRule;
    }

    public void setOnThreePickListener(OnThreePickListener onThreePickListener) {
        this.onThreePickListener = onThreePickListener;
    }

    public void setTextLabelColor(int textLabelColor) {
        this.textLabelColor = textLabelColor;
    }

    public void setTextLabelSize(int textLabelSize) {
        this.textLabelSize = textLabelSize;
    }

    public void setOneSupportRelevance(boolean oneSupportRelevance) {
        this.oneSupportRelevance = oneSupportRelevance;
    }

    public void setTwoSupportRelevance(boolean twoSupportRelevance) {
        this.twoSupportRelevance = twoSupportRelevance;
    }

    public void setOneRelevanceRule(SparseArray<List<String>> oneRelevanceRule) {
        this.oneRelevanceRule = oneRelevanceRule;
    }

    public void setTwoRelevanceRule(SparseArray<List<String>> twoRelevanceRule) {
        this.twoRelevanceRule = twoRelevanceRule;
    }

    public void setRelevanceType(RelevanceType relevanceType) {
        this.relevanceType = relevanceType;
    }

    public interface OnThreePickListener {
        void onThreePicked(String firstSelectValue, String secondSelectValue, String threeSelectValue);
    }
}
