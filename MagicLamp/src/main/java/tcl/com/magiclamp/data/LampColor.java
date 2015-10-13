package tcl.com.magiclamp.data;

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


    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
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

    public static final class LampColorState {
        private int mColor;

        private LampColorState(int color) {
            mColor = color;
        }

        public static final LampColorState STATE_ENABLE = new LampColorState(0x000002);
        public static final LampColorState STATE_DISABLE = new LampColorState(0x000003);

    }
}
