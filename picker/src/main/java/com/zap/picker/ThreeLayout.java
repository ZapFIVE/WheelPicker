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
 * 三项选择器控件（非窗口式）
 * <p>
 * Created by Will on 2016/10/19.
 */
public class ThreeLayout extends WheelLayout {

    private WheelView firstWheel, secondWheel, threeWheel;
    private TextView firstLabelView, secondLabelView, threeLabelView;
    private int relevanceType;
    private OnThreeLayoutListener onThreeLayoutListener;
    private String firstSelectValue = "", secondSelectValue = "", threeSelectValue = "";
    private String firstLabel = "", secondLabel = "", threeLabel = "";
    private boolean oneSupportRelevance = false, twoSupportRelevance = false;//一，二是否支持联动
    private SparseArray<List<String>> oneRelevanceRule = null, twoRelevanceRule = null;//联动规则

    public ThreeLayout(Context context) {
        this(context, null);
    }

    public ThreeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WheelLayout, defStyleAttr, 0);
        if (a != null) {
            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.WheelLayout_first_label) {
                    firstLabel = a.getString(attr);
                } else if (attr == R.styleable.WheelLayout_second_label) {
                    secondLabel = a.getString(attr);
                } else if (attr == R.styleable.WheelLayout_three_label) {
                    threeLabel = a.getString(attr);
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

        LayoutParams params = new LayoutParams(BottomPopup.WRAP_CONTENT, BottomPopup.WRAP_CONTENT);

        /** 左边LayoutParams **/
        LinearLayout layout_1 = new LinearLayout(getContext());
        LayoutParams params_1 = new LayoutParams(BottomPopup.MATCH_PARENT, BottomPopup.WRAP_CONTENT, 1);
        layout_1.setLayoutParams(params_1);
        layout_1.setGravity(Gravity.CENTER);
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
        LayoutParams params_2 = new LayoutParams(BottomPopup.MATCH_PARENT, BottomPopup.WRAP_CONTENT, 1);
        layout_2.setLayoutParams(params_2);
        layout_2.setGravity(Gravity.CENTER);
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
            secondLabelView.setPadding(labelPadding, 0, labelPadding, 0);
        }
        addView(secondLabelView);

        /** 右边LayoutParams **/
        LinearLayout layout_3 = new LinearLayout(getContext());
        LayoutParams params_3 = new LayoutParams(BottomPopup.MATCH_PARENT, BottomPopup.WRAP_CONTENT, 1);
        layout_3.setLayoutParams(params_3);
        layout_3.setGravity(Gravity.CENTER);
        threeWheel = new WheelView(this.getContext());
        threeWheel.setLayoutParams(params);
        threeWheel.setTextSize(textNormalSize, textSelectSize);
        threeWheel.setTextColor(textNormalColor, textSelectColor);
        threeWheel.setOffSet(offSet);
        threeWheel.setMaskColor(maskColor);
        threeWheel.setMaskVisible(isMaskVisible);
        layout_3.addView(threeWheel);
        addView(layout_3);

        threeLabelView = new TextView(getContext());
        threeLabelView.setLayoutParams(params);
        threeLabelView.setTextColor(labelColor);
        threeLabelView.setTextSize(labelSize);
        threeLabelView.setText(threeLabel);
        if (!TextUtils.isEmpty(threeLabel)) {
            threeLabelView.setPadding(labelPadding, 0, 0, 0);
        }
        addView(threeLabelView);

        firstWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                firstSelectValue = item;
                if (oneSupportRelevance) {
                    if (oneRelevanceRule != null && oneRelevanceRule.size() > 0) {
                        if (isUserScroll) {
                            if (relevanceType == 0) {
                                secondWheel.setItemList(oneRelevanceRule.get(selectedIndex - offSet), 0);
                            } else if (relevanceType == 1) {
                                secondWheel.setItemList(oneRelevanceRule.get(selectedIndex - offSet), secondWheel.getSelectedIndex());
                            }
                        } else {
                            secondWheel.setItemList(oneRelevanceRule.get(selectedIndex - offSet), secondSelectValue);
                        }
                    } else {
                        throw new IllegalArgumentException("Current RelevanceRule is null or size is 0");
                    }
                } else {
                    if (onThreeLayoutListener != null) {
                        onThreeLayoutListener.onSelected(firstSelectValue, secondSelectValue, threeSelectValue);
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
                        if (isUserScroll) {
                            if (relevanceType == 0) {
                                threeWheel.setItemList(twoRelevanceRule.get(selectedIndex - offSet), 0);
                            } else if (relevanceType == 1) {
                                threeWheel.setItemList(twoRelevanceRule.get(selectedIndex - offSet), threeWheel.getSelectedIndex());
                            }
                        } else {
                            threeWheel.setItemList(twoRelevanceRule.get(selectedIndex - offSet), threeSelectValue);
                        }
                    } else {
                        throw new IllegalArgumentException("Current RelevanceRule is null or size is 0");
                    }
                } else {
                    if (onThreeLayoutListener != null) {
                        onThreeLayoutListener.onSelected(firstSelectValue, secondSelectValue, threeSelectValue);
                    }
                }
            }
        });
        threeWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                threeSelectValue = item;
                if (onThreeLayoutListener != null) {
                    onThreeLayoutListener.onSelected(firstSelectValue, secondSelectValue, threeSelectValue);
                }
            }
        });
    }

    public void setMaskVisible(boolean maskVisible) {
        this.isMaskVisible = maskVisible;
        this.firstWheel.setMaskVisible(maskVisible);
        this.secondWheel.setMaskVisible(maskVisible);
        this.threeWheel.setMaskVisible(maskVisible);
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        this.firstLabelView.setTextColor(labelColor);
        this.secondLabelView.setTextColor(labelColor);
        this.threeLabelView.setTextColor(labelColor);
    }

    public void setLabelPadding(int labelPadding) {
        this.labelPadding = labelPadding;
        this.firstLabelView.setPadding(labelPadding, 0, labelPadding, 0);
        this.secondLabelView.setPadding(labelPadding, 0, 0, 0);
        this.threeLabelView.setPadding(labelPadding, 0, 0, 0);
    }

    public void setLabelSize(int labelSize) {
        this.labelSize = labelSize;
        this.firstLabelView.setTextSize(labelSize);
        this.secondLabelView.setTextSize(labelSize);
        this.threeLabelView.setTextSize(labelSize);
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
        this.firstWheel.setMaskColor(maskColor);
        this.secondWheel.setMaskColor(maskColor);
        this.threeWheel.setMaskColor(maskColor);
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
        this.firstWheel.setOffSet(offSet);
        this.secondWheel.setOffSet(offSet);
        this.threeWheel.setOffSet(offSet);
    }

    public void setLabel(String firstLabel, String secondLabel) {
        this.firstLabelView.setText(firstLabel);
        this.secondLabelView.setText(secondLabel);
        this.threeLabelView.setText(secondLabel);
    }

    public void setItems(List<String> firstList, List<String> secondList, List<String> threeList) {
        firstSelectValue = firstList.get(0);
        secondSelectValue = secondList.get(0);
        threeSelectValue = threeList.get(0);
        firstWheel.setItemList(firstList, firstSelectValue);
        secondWheel.setItemList(secondList, secondSelectValue);
        threeWheel.setItemList(threeList, threeSelectValue);
    }

    public void setItems(List<String> firstList, List<String> secondList, List<String> threeList,
                         String firstValue, String secondValue, String threeValue) {
        this.firstSelectValue = firstValue;
        this.secondSelectValue = secondValue;
        this.threeSelectValue = threeValue;
        firstWheel.setItemList(firstList, firstSelectValue);
        secondWheel.setItemList(secondList, secondSelectValue);
        threeWheel.setItemList(threeList, threeSelectValue);
    }

    public void setListAndRelevanceRule(List<String> firstList,
                                        SparseArray<List<String>> oneRelevanceRule,
                                        SparseArray<List<String>> twoRelevanceRule) {
        oneSupportRelevance = true;
        twoSupportRelevance = true;
        this.oneRelevanceRule = oneRelevanceRule;
        this.twoRelevanceRule = twoRelevanceRule;

        setItems(firstList, oneRelevanceRule.get(0), twoRelevanceRule.get(0));
    }

    public void setListAndRelevanceRule(List<String> firstList,
                                        SparseArray<List<String>> oneRelevanceRule,
                                        SparseArray<List<String>> twoRelevanceRule,
                                        String firstValue, String secondValue, String threeValue) {
        oneSupportRelevance = true;
        twoSupportRelevance = true;
        this.oneRelevanceRule = oneRelevanceRule;
        this.twoRelevanceRule = twoRelevanceRule;

        setItems(firstList, oneRelevanceRule.get(0), twoRelevanceRule.get(0), firstValue, secondValue, threeValue);
    }

    public void setOnThreeLayoutListener(OnThreeLayoutListener onThreeLayoutListener) {
        this.onThreeLayoutListener = onThreeLayoutListener;
    }

    public interface OnThreeLayoutListener {
        void onSelected(String firstSelectValue, String secondFirstValue, String threeSelectValue);
    }
}
