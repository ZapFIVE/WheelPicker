package com.zap.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * WheelView滑动基类
 * <p>
 * Created by Will on 2016/10/11.
 */
public class WheelView extends ScrollView {

    public static final int TEXT_NORMAL_SIZE = 20;
    public static final int TEXT_SELECT_SIZE = 25;
    public static final int TEXT_NORMAL_COLOR = 0x7FFFFFFF;
    public static final int TEXT_SELECT_COLOR = 0XFF7D55FF;
    public static final int MASK_COLOR = 0xFFC0C0C0;
    public static final int OFF_SET = 1;
    private static final int POST_DELAY = 50;

    private Context context;
    private LinearLayout views;//包含所有TextView的布局
    private List<String> items = null;
    private int offSet = OFF_SET;//间隔数
    private int showItemCount;//显示个数
    private int selectedIndex;//当前选中下标
    private int scrollY;//竖直方向滚动值
    private int defaultTextHeight = 0;//默认一个TextView的高度
    private Runnable scrollTask = null;//自动滚动线程
    private int itemHeight = 0;//每个Item的高度
    private int itemWidth = 0;
    private int itemMaxWidth = 0;
    private int currSlidDirection = 0;
    private OnWheelViewListener onWheelViewListener;//滚动监听

    private Paint maskPaint = null;//遮罩层画笔
    private int viewWidth = 0;//View宽度
    private int textNormalSize = TEXT_NORMAL_SIZE;
    private int textSelectSize = TEXT_SELECT_SIZE;
    private int textNormalColor = TEXT_NORMAL_COLOR;
    private int textSelectColor = TEXT_SELECT_COLOR;
    private int maskColor = MASK_COLOR;//遮罩层颜色
    private boolean isMaskVisible = false;//遮罩层是否可见
    private boolean isUserScroll = false;//是否用户手动操作

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    /**
     * 初始化
     */
    private void initView(Context context) {
        this.context = context;

        items = new ArrayList<>();
        scrollTask = new ScrollTask();
        defaultTextHeight = ScreenHelper.dp2px(context, 60);
        itemMaxWidth = 3 * ScreenHelper.getScreenPixels(context).widthPixels / 4;

        /** 消除渐变阴影 **/
        setFadingEdgeLength(0);

        if (Build.VERSION.SDK_INT >= 9) {
            /** 消除滚动回弹 **/
            setOverScrollMode(OVER_SCROLL_NEVER);
        }

        /** 消除拖动条 **/
        setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        addView(views);
    }

    @Override
    public void setBackground(Drawable background) {
        if (viewWidth == 0) {
            viewWidth = ScreenHelper.getScreenPixels(context).widthPixels;
        }

        if (!isMaskVisible) return;

        if (maskPaint == null) {
            maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            maskPaint.setStyle(Paint.Style.FILL);
            maskPaint.setColor(maskColor);
        }

        background = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                Rect rect = new Rect(0, itemHeight * offSet, viewWidth, itemHeight * (offSet + 1));
                canvas.drawRect(rect, maskPaint);
            }

            @Override
            public void setAlpha(int alpha) {
                maskPaint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {
                maskPaint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSLUCENT;
            }
        };

        super.setBackground(background);

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t - oldt > 0) {
            currSlidDirection = 1;//up
        } else if (t - oldt < 0) {
            currSlidDirection = 2;//down
        } else {
            currSlidDirection = 0;//default
        }
        refreshItemView(t);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        setBackground(null);
    }

    @Override
    public void fling(int velocityY) {
        /** 降低滑动速度 **/
        super.fling(velocityY / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            isUserScroll = true;
            startScrollTask();
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 获取View的宽高
     *
     * @param v 指定的TextView
     */
    private void getViewMeasured(View v) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpecHeight = MeasureSpec.makeMeasureSpec(defaultTextHeight, MeasureSpec.EXACTLY);
        v.measure(width, expandSpecHeight);
        itemHeight = v.getMeasuredHeight();
        if (itemWidth < v.getMeasuredWidth() && itemWidth != itemMaxWidth) {
            itemWidth = v.getMeasuredWidth();
            if (itemWidth > itemMaxWidth) {
                itemWidth = itemMaxWidth;
            }
        }
    }

    /**
     * 创建TextView
     *
     * @param text 文本
     * @return TextView
     */
    private TextView createTextView(String text) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(BottomPopup.MATCH_PARENT, defaultTextHeight));
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setText(text);
        tv.setTextSize(textSelectSize);
        tv.setGravity(Gravity.CENTER);
        int padding = ScreenHelper.dp2px(context, 10);
        tv.setPadding(padding, padding, padding, padding);

        getViewMeasured(tv);

        return tv;
    }

    /**
     * 初始化数据
     *
     * @param list 显示文本集
     */
    private void initData(List<String> list) {
        items.clear();
        items.addAll(list);

        /** 根据前后位置补填 **/
        for (int i = 0; i < offSet; i++) {
            items.add(0, "");
            items.add("");
        }

        /** 显示Item个数 **/
        showItemCount = offSet * 2 + 1;

        /** 添加需要显示的TextView **/
        views.removeAllViews();
        for (String item : items) {
            views.addView(createTextView(item));
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.getLayoutParams();
        itemWidth = Math.max(lp.width, itemWidth);//保持当前View大小适中，最终由外层父Layout决定ScrollView的宽度
        views.setLayoutParams(new LayoutParams(itemWidth, itemHeight * showItemCount));
        this.setLayoutParams(new LinearLayout.LayoutParams(BottomPopup.WRAP_CONTENT, itemHeight * showItemCount));

        /** 刷新当前显示 **/
        refreshItemView(getScrollY());
    }

    /**
     * 设置选中项
     *
     * @param index 位置
     */
    private void setSelectedIndex(final int index) {
        isUserScroll = false;
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(0, index * itemHeight);

                selectedIndex = index + offSet;

                onSelectedCallBack();
            }
        });
    }

    /**
     * 根据文本设定选中项
     *
     * @param text 被选中的文本
     */
    private void setSelectedItem(String text) {
        for (int i = 0; i < items.size(); i++) {
            if (text.equals(items.get(i))) {
                setSelectedIndex(i - offSet);
            }
        }
    }

    /**
     * 设定Item文本集
     *
     * @param list 文本集
     */
    public void setItemList(List<String> list) {
        setItemList(list, selectedIndex - offSet);
    }

    /**
     * 设定Item文本集并且设定显示第几个item
     *
     * @param list  文本集
     * @param index 下标
     */
    public void setItemList(List<String> list, int index) {
        initData(list);

        setSelectedIndex(index);
    }

    /**
     * 设定Item文本集且设定当前显示Item为text
     *
     * @param list 文本集
     * @param text 显示文本
     */
    public void setItemList(List<String> list, String text) {
        initData(list);

        setSelectedItem(text);
    }

    /**
     * 开启滚动线程
     */
    private void startScrollTask() {
        scrollY = getScrollY();
        postDelayed(scrollTask, POST_DELAY);
    }

    private void refreshItemView1(int y) {
        y += itemHeight * offSet;

        int remainder = y % itemHeight;
        int dTextSize = textSelectSize - textNormalSize;

        for (int i = 0; i < views.getChildCount(); i++) {
            TextView tv = (TextView) views.getChildAt(i);
            Log.e("getChildCount", "remainder  " + remainder);
            if (tv.getY() == (y - remainder) && (remainder <= itemHeight / 2)) {
                tv.setTextSize(textSelectSize - (dTextSize * remainder / itemHeight));
                tv.setTextColor(textSelectColor);
                TextView preTv = (TextView) views.getChildAt(i - 1);
                TextView nextTv = (TextView) views.getChildAt(i + 1);
                if (currSlidDirection == 1) {
                    nextTv.setTextSize(textNormalSize + (dTextSize * remainder / itemHeight));
                    preTv.setTextSize(textNormalSize);
                } else if (currSlidDirection == 2) {
                    preTv.setTextSize(textNormalSize + (dTextSize * remainder / itemHeight));
                    nextTv.setTextSize(textNormalSize);
                }
                preTv.setTextColor(textNormalColor);
                nextTv.setTextColor(textNormalColor);

                for (int j = 0; j < views.getChildCount(); j++) {
                    if (j != i - 1 && j != i && j != i + 1) {
                        TextView tvJ = (TextView) views.getChildAt(j);
                        if (tvJ != null) {
                            tvJ.setTextSize(textNormalSize);
                            tvJ.setTextColor(textNormalColor);
                        }
                    }
                }
                break;
            }
        }

    }

    /**
     * 刷新当前Item
     *
     * @param y 滑动距离
     */
    private void refreshItemView(int y) {
        int position = y / itemHeight + offSet;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder != 0 && remainder > itemHeight / 2) {
            position = divided + offSet + 1;
        }

        int childCount = views.getChildCount();
        for (int i = 0; i < childCount; i++) {
            TextView currTv = (TextView) views.getChildAt(i);

            if (currTv == null) return;

            if (position == i) {
                currTv.setTextSize(textSelectSize);
                currTv.setTextColor(textSelectColor);
            } else {
                currTv.setTextSize(textNormalSize);
                currTv.setTextColor(textNormalColor);
            }
        }
    }

    /**
     * 选中之后执行的回调
     */
    private void onSelectedCallBack() {
        if (onWheelViewListener != null) {
            onWheelViewListener.onSelected(isUserScroll, selectedIndex, items.get(selectedIndex));
        }
    }

    /**
     * 设置监听
     *
     * @param listener OnWheelViewListener
     */
    public void setOnWheelViewListener(OnWheelViewListener listener) {
        this.onWheelViewListener = listener;
    }

    public interface OnWheelViewListener {
        void onSelected(boolean isUserScroll, int selectedIndex, String item);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            if (itemHeight == 0) {
                throw new IllegalArgumentException("current itemHeight is 0");
            }

            int currY = getScrollY();
            if (scrollY - currY == 0) {
                final int divided = scrollY / itemHeight;
                final int remainder = scrollY % itemHeight;

                if (remainder == 0) {//刚好处于item间隔位置
                    selectedIndex = divided + offSet;
                    onSelectedCallBack();
                } else if (remainder > itemHeight / 2) {//处于超过一半Item位置
                    post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, scrollY - remainder + itemHeight);
                            selectedIndex = divided + offSet + 1;
                            onSelectedCallBack();
                        }
                    });
                } else {//处于小于Item一半位置
                    post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, scrollY - remainder);
                            selectedIndex = divided + offSet;
                            onSelectedCallBack();
                        }
                    });
                }
            } else {
                startScrollTask();
            }
        }
    }

    public int getTextSelectSize() {
        return textSelectSize;
    }

    public void setTextSelectSize(int textSelectSize) {
        this.textSelectSize = textSelectSize;
    }

    public int getTextSelectColor() {
        return textSelectColor;
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }

    public int getTextNormalSize() {
        return textNormalSize;
    }

    public void setTextNormalSize(int textNormalSize) {
        this.textNormalSize = textNormalSize;
    }

    public int getTextNormalColor() {
        return textNormalColor;
    }

    public void setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
    }

    public int getShowItemCount() {
        return showItemCount;
    }

    public void setShowItemCount(int showItemCount) {
        this.showItemCount = showItemCount;
    }

    public int getMaskColor() {
        return maskColor;
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public boolean isMaskVisible() {
        return isMaskVisible;
    }

    public void setMaskVisible(boolean maskVisible) {
        isMaskVisible = maskVisible;
    }

    public void setDefaultTextHeight(int defaultTextHeight) {
        this.defaultTextHeight = defaultTextHeight;
    }

    public void setTextSize(int normalSize, int selectSize) {
        this.textNormalSize = normalSize;
        this.textSelectSize = selectSize;
    }

    public void setTextColor(int normalColor, int selectColor) {
        this.textNormalColor = normalColor;
        this.textSelectColor = selectColor;
    }

    public int getSelectedIndex() {
        return selectedIndex - offSet;
    }
}
