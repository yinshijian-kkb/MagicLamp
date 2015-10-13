package tcl.com.magiclamp.data;

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

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public int getCompoundColorBgSize(){
        return UIUtils.getDimens(R.dimen.lamp_color_size);
    }

    public int getCheckedCompoundColorBgSize(){
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

    public boolean isChecked(){
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
}
