package com.zap.picker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/**
 * 自定义Popup基类
 * <p>
 * Created by Will on 2016/10/11.
 */
public class Popup {

    private int animRes = R.style.Animation_Bottom;//弹窗的出入动画
    private Dialog dialog;
    private FrameLayout contentLayout;//内容布局

    public Popup(Context context) {
        init(context);
    }

    private void init(Context context) {
        /** contentLayout **/
        contentLayout = new FrameLayout(context);
        contentLayout.setFocusable(true);
        contentLayout.setFocusableInTouchMode(true);

        /** Dialog **/
        dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        if (window == null) {
            throw new IllegalArgumentException("current dialog.getWindow() is null");
        }
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(animRes);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(contentLayout);
    }

    /**
     * 重新设置ContentView
     *
     * @param view content
     */
    public void setContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }

    /**
     * 获取ContentView
     *
     * @return View
     */
    public View getContentView() {
        return contentLayout.getChildAt(0);
    }

    /**
     * 设定当前窗口大小
     *
     * @param width  宽
     * @param height 高
     */
    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = contentLayout.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        contentLayout.setLayoutParams(params);
    }

    /**
     * 设定进出动画
     *
     * @param animRes Animation Rs
     */
    public void setAnimRes(int animRes) {
        this.animRes = animRes;
    }

    /**
     * 手否已经显示
     *
     * @return true 显示
     */
    public boolean isShowing() {
        return dialog.isShowing();
    }

    /**
     * 显示
     */
    public void show() {
        dialog.show();
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * 设定隐藏监听
     *
     * @param listener OnDismissListener
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (listener == null) return;
        dialog.setOnDismissListener(listener);
    }
}
