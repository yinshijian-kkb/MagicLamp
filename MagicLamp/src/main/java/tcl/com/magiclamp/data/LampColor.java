package tcl.com.magiclamp.data;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import tcl.com.magiclamp.R;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * 灯色
 * Created by sjyin on 10/13/15.
 */
public class LampColor {
    /**
     * 小神等的灯色
     */
    private int mColor;
    private LampColorState mState;

    public static final int COLOR_DISABLE = 0xff888888;
    public static final int COLOR_EMPTY = 0xff000000;
    private boolean mChecked;

    public int getColor() {
        return mColor;
    }

    public int getCompoundColorBgSize() {
        return UIUtils.getDimens(R.dimen.lamp_color_size);
    }

    public int getCheckedCompoundColorBgSize() {
        return UIUtils.getDimens(R.dimen.lamp_color_checked_size);
    }

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
        return _color;
    }

    public LampColorState getState() {
        return mState;
    }

    public void setChecked(boolean clickable) {
        this.mChecked = clickable;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public static final class LampColorState {
        private int mColor;

        private LampColorState(int color) {
            mColor = color;
        }

        public static final LampColorState STATE_ENABLE = new LampColorState(0x000002);
        public static final LampColorState STATE_DISABLE = new LampColorState(0x000003);

    }

    //====
    private View mTargetView;

    public LampColor(View targetView) {
        mTargetView = targetView;
    }

    /*public void setBackgroundResource(int resId) {
        if (mTargetView instanceof ImageView) {
            mTargetView.setBackgroundResource(resId);
        }
    }*/

    public void setSize(int width, int height) {
        if (mTargetView instanceof ImageView) {
            Drawable _bg = mTargetView.getBackground();
            if (_bg instanceof GradientDrawable) {
                _bg.mutate();
                ((GradientDrawable) _bg).setSize(width, height);
            }

        }
    }

    public void setColor(int color) {
        this.mColor = color;
        if (mTargetView instanceof ImageView) {
            Drawable _bg = mTargetView.getBackground();
            if (_bg instanceof GradientDrawable) {
                ((GradientDrawable) _bg).setColor(color);
            }

        }
    }

    public void setVisibility(int visibility) {
        mTargetView.setVisibility(visibility);
    }
}
