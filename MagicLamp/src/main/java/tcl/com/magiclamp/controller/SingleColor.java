package tcl.com.magiclamp.controller;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * 灯色
 * Created by sjyin on 10/13/15.
 */
public class SingleColor {
    /**
     * 灯色
     */
    private int mColor;
    /**
     * 灯色是否可操作
     */
    private LampColorState mState;
    /**
     * 灯色对应的View
     */
    private View mTargetView;
    /**
     * 是否被选中：灯色、变化色
     */
    private boolean mChecked;
    public static final int COLOR_DISABLE = 0xff888888;
    public static final int COLOR_EMPTY = 0xffffffff;


    /**
     * 通过状态获取色值
     *
     * @param state
     * @return
     */
    public int setState(LampColorState state) {
        mState = state;
        int _color = mColor;
        if (state == LampColorState.STATE_DISABLE) {
            _color = COLOR_DISABLE;
        }
        updateBg(_color);
        return _color;
    }

    public LampColorState getState() {
        return mState;
    }

    public void check(boolean clickable) {
        this.mChecked = clickable;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public SingleColor(View targetView) {
        mTargetView = targetView;
    }

    public void setTargetView(View mTargetView) {
        this.mTargetView = mTargetView;
    }

    public View getTargetView() {
        return mTargetView;
    }

    public void setColor(int color) {
        this.mColor = color;
        updateBg(color);
    }

    public int getColor() {
        return mColor;
    }

    /**
     * 更新色块的背景色
     *
     * @param color
     */
    private void updateBg(int color) {
        Drawable _bg = mTargetView.getBackground();
        if (_bg instanceof GradientDrawable) {
            ((GradientDrawable) _bg).setColor(color);
        }
    }

    public void setVisibility(int visibility) {
        mTargetView.setVisibility(visibility);
    }

    public void setBackgroundResource(int resId) {
        mTargetView.setBackgroundResource(resId);
    }

    public void setSize(int width, int height) {
        Drawable _bg = mTargetView.getBackground();
        if (_bg instanceof GradientDrawable) {
            _bg.mutate();
            ((GradientDrawable) _bg).setSize(width, height);
        }
    }

    public static final class LampColorState {

        public static final LampColorState STATE_ENABLE = new LampColorState();
        /**
         * 不可操作状态
         */
        public static final LampColorState STATE_DISABLE = new LampColorState();

    }

}
