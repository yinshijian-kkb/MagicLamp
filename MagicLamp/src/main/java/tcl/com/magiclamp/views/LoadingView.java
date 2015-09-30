package tcl.com.magiclamp.views;

import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.ImageView;

import tcl.com.magiclamp.R;

public class LoadingView extends ImageView {

    Calendar mCalendar;
    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;
    long mDuration = 100;
    int index = 0;
    int[] resIds;

    public LoadingView(Context context) {
        super(context);
        initCalendar();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCalendar();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCalendar();
    }

    /**
     * 时间对象初始化
     */
    private void initCalendar() {
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        if (resIds == null) {
            resIds = new int[]{R.drawable.recommended_blackloding_a1, R.drawable.recommended_blackloding_a2,
                    R.drawable.recommended_blackloding_a3, R.drawable.recommended_blackloding_a4,
                    R.drawable.recommended_blackloding_a5, R.drawable.recommended_blackloding_a6,
                    R.drawable.recommended_blackloding_a7, R.drawable.recommended_blackloding_a8,
                    R.drawable.recommended_blackloding_a9, R.drawable.recommended_blackloding_a10,
                    R.drawable.recommended_blackloding_a11, R.drawable.recommended_blackloding_a12};
        }
    }

    @Override
    protected void onAttachedToWindow() {// 开始
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                setImageResource(resIds[index++]);
                if (index == resIds.length)
                    index = 0;
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (mDuration - now % mDuration);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow() {// 停止
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }
}
