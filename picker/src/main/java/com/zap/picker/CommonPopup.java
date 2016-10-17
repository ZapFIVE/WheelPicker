package com.zap.picker;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * CommonPopup通用弹窗
 * <p>
 * Created by Will on 2016/10/11.
 */
public abstract class CommonPopup<V extends View> extends BottomPopup<View> implements View.OnClickListener {
    private static final String TAG_CONFIRM = "confirm";

    private int rootBackgroundColor = 0xFF191923;//跟布局背景颜色
    private int topBackgroundColor = 0xFF7D55FF;//顶部布局背景颜色
    private int topViewHeight;//顶部布局高度
    private boolean isTitleVisible = true;//title是否可见
    private CharSequence textTitle = "Select";//title
    private int titleTextColor = Color.WHITE;//title文本颜色
    private int titleTextSize;//title文本字体大小
    private int confirmImageViewRsId = R.drawable.bg_dialog_confirm;//确认图片背景
    private boolean isLineVisible = true;//间隔线是否可见
    private int dividingLineColor = Color.BLUE;//间隔线颜色
    private OnConfirmListener onConfirmListener;//确认监听

    public CommonPopup(Activity activity) {
        super(activity);
        titleTextSize = ScreenHelper.sp2px(activity, 20);
        topViewHeight = ScreenHelper.dp2px(activity, 50);
    }

    @Override
    protected View getView() {

        /** 根布局 **/
        LinearLayout rootView = createContentView();

        /** 顶部View **/
        createTopView(rootView);

        /** 间隔线 **/
        if (isLineVisible) {
            View lineView = new View(activity);
            lineView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, 1));
            lineView.setBackgroundColor(dividingLineColor);
            rootView.addView(lineView);
        }

        /** ContentView **/
        rootView.addView(initContentView());
        return rootView;
    }

    private LinearLayout createContentView() {
        LinearLayout rootView = new LinearLayout(activity);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        rootView.setBackgroundColor(rootBackgroundColor);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setGravity(Gravity.CENTER);
        rootView.setPadding(0, 0, 0, 0);
        rootView.setClipToPadding(false);

        return rootView;
    }

    private void createTopView(LinearLayout rootView) {
        RelativeLayout topView = new RelativeLayout(activity);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(MATCH_PARENT, topViewHeight));
        topView.setBackgroundColor(topBackgroundColor);
        topView.setGravity(Gravity.CENTER_VERTICAL);

        /** 创建Title **/
        TextView tv_title = new TextView(activity);
        tv_title.setVisibility(isTitleVisible ? View.VISIBLE : View.GONE);
        RelativeLayout.LayoutParams params_title = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params_title.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params_title.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params_title.setMargins(50, 0, 0, 0);
        tv_title.setLayoutParams(params_title);
        tv_title.setGravity(Gravity.CENTER);
        if (!TextUtils.isEmpty(textTitle)) {
            tv_title.setText(textTitle);
        }
        tv_title.setTextColor(titleTextColor);
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        topView.addView(tv_title);

        /** 创建Confirm的ImageVIew **/
        ImageView iv_confirm = new ImageView(activity);
        RelativeLayout.LayoutParams params_confirm = new RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        params_confirm.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params_confirm.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params_confirm.setMargins(0, 0, 0, 0);
        iv_confirm.setLayoutParams(params_confirm);
        iv_confirm.setScaleType(ImageView.ScaleType.FIT_XY);
        if (confirmImageViewRsId != 0) {
            iv_confirm.setImageResource(confirmImageViewRsId);
        }
        iv_confirm.setTag(TAG_CONFIRM);
        iv_confirm.setOnClickListener(this);
        topView.addView(iv_confirm);

        /** topView添加到rootView **/
        rootView.addView(topView);
    }

    protected abstract V initContentView();

    @Override
    public void onClick(View v) {
        if (onConfirmListener == null) return;
        String tag = v.getTag().toString();
        if (tag.equals(TAG_CONFIRM)) {
            onConfirmListener.onConfirm();
        }
        dismiss();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public void setConfirmImageViewRsId(int confirmImageViewRsId) {
        this.confirmImageViewRsId = confirmImageViewRsId;
    }

    public void setDividingLineColor(int dividingLineColor) {
        this.dividingLineColor = dividingLineColor;
    }

    public void setLineVisible(boolean lineVisible) {
        isLineVisible = lineVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        isTitleVisible = titleVisible;
    }

    public void setRootBackgroundColor(int rootBackgroundColor) {
        this.rootBackgroundColor = rootBackgroundColor;
    }

    public void setTextTitle(CharSequence textTitle) {
        this.textTitle = textTitle;
    }

    public void setTitleTextColor(int titleTextColor) {
        this.titleTextColor = titleTextColor;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public void setTopBackgroundColor(int topBackgroundColor) {
        this.topBackgroundColor = topBackgroundColor;
    }

    public void setTopViewHeight(int topViewHeight) {
        this.topViewHeight = topViewHeight;
    }
}
