package com.zap.picker.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;

import com.zap.picker.utils.ScreenHelper;

/**
 * BottomPopup底部弹出窗口基类
 * <p>
 * Created by Will on 2016/10/11.
 */
public abstract class BottomPopup<V extends View> {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    private Popup popup;

    public BottomPopup(Activity activity) {
        this.activity = activity;
        popup = new Popup(activity);
        popup.setSize(ScreenHelper.getScreenPixels(activity).widthPixels, WRAP_CONTENT);
    }

    /**
     * 设定尺寸
     *
     * @param width  宽
     * @param height 高
     */
    public void setSize(int width, int height) {
        popup.setSize(width, height);
    }

    /**
     * 是否正在显示
     *
     * @return true 显示
     */
    public boolean isShowing() {
        return popup.isShowing();
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        popup.dismiss();
    }

    /**
     * 显示
     */
    public void show() {
        onShowPrepare();

        popup.show();
    }

    protected abstract V getView();

    /**
     * show之前加载View
     */
    private void onShowPrepare() {
        setContentViewBefore();

        V view = getView();

        popup.setContentView(view);

        setContentViewAfter(view);
    }

    /**
     * 设定内容布局之前
     */
    protected void setContentViewBefore() {
    }

    /**
     * 设定内容布局之火
     *
     * @param contentView 内容布局
     */
    protected void setContentViewAfter(View contentView) {
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        popup.setOnDismissListener(listener);
    }
}
