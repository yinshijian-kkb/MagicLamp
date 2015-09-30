package tcl.com.magiclamp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sjyin on 9/29/15.
 */
public class RoundedTextView extends TextView {
    private RectF rectF;
    private Paint mPaint;

    public RoundedTextView(Context context) {
        this(context, null);
    }

    public RoundedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
        super.setBackgroundColor(color);
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (rectF == null)
            rectF = new RectF(left, top, right, bottom);
        else
            rectF.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        canvas.drawRoundRect(rectF, mWidth / 2, mHeight / 2, mPaint);
        super.onDraw(canvas);
    }
}
