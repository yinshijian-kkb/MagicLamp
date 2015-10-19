package tcl.com.magiclamp.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sjyin on 9/23/15.
 */
public class ColorPickerView extends View {//颜色选择器自定义View
    private Paint mPaint;//渐变色环画笔 
    private int[] mColors;//渐变色环颜色
    private int[] mDisableColors;
    private OnColorChangedListener mListener;//颜色改变回调


    //内圆的参数
    private static final int CENTER_X = 200;
    private static final int CENTER_Y = 200;
    private static final int CENTER_RADIUS = 200;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mColors = new int[]{//渐变色数组
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000
        };
        mDisableColors = new int[]{//渐变色数组
                0xFFFFFFFF, 0x00000000
        };
        Shader s = new SweepGradient(0, 0, mColors, null);
        //初始化渐变色画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setEnabled(boolean enabled) {
        Shader s = new SweepGradient(0, 0, enabled ? mColors : mDisableColors, null);
        mPaint.setShader(s);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float r = CENTER_X - mPaint.getStrokeWidth() * 0.5f;

        //移动中心
        canvas.translate(CENTER_X, CENTER_X);
        //画出色环
        canvas.drawOval(new RectF(-r, -r, r, r), mPaint);//new RectF(LeftTopPoint,RightBottomPoint)
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(CENTER_X * 2, CENTER_Y * 2);
    }

    private int floatToByte(float x) {
        int n = java.lang.Math.round(x);
        return n;
    }

    private int pinToByte(int n) {
        if (n < 0) {
            n = 0;
        } else if (n > 255) {
            n = 255;
        }
        return n;
    }

    private int ave(int s, int d, float p) {
        return s + java.lang.Math.round(p * (d - s));
    }

    private int interpColor(int colors[], float unit) {
        if (unit <= 0) {
            return colors[0];
        }
        if (unit >= 1) {
            return colors[colors.length - 1];
        }

        float p = unit * (colors.length - 1);
        int i = (int) p;
        p -= i;

        // now p is just the fractional part [0...1) and i is the index
        int c0 = colors[i];
        int c1 = colors[i + 1];
//        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int a = mPaint.getAlpha();
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    private int rotateColor(int color, float rad) {
        float deg = rad * 180 / 3.1415927f;
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        ColorMatrix cm = new ColorMatrix();
        ColorMatrix tmp = new ColorMatrix();

        cm.setRGB2YUV();
        tmp.setRotate(0, deg);
        cm.postConcat(tmp);
        tmp.setYUV2RGB();
        cm.postConcat(tmp);

        final float[] a = cm.getArray();

        int ir = floatToByte(a[0] * r + a[1] * g + a[2] * b);
        int ig = floatToByte(a[5] * r + a[6] * g + a[7] * b);
        int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);

        return Color.argb(Color.alpha(color), pinToByte(ir),
                pinToByte(ig), pinToByte(ib));
    }

    private static final float PI = 3.1415926f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - CENTER_X;
        float y = event.getY() - CENTER_Y;
        boolean _InCenter = java.lang.Math.sqrt(x*x + y*y) <= CENTER_RADIUS;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (_InCenter) {//是否是中心圆
                    float angle = (float) java.lang.Math.atan2(y, x);
                    // need to turn angle [-PI ... PI] into unit [0....1]
                    float unit = angle / (2 * PI);
                    if (unit < 0) {
                        unit += 1;
                    }
                    if (isEnabled()){
                        mPaint.setColor(interpColor(mColors, unit));
                    }
//                invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (_InCenter){
                    mListener.colorChanged(mPaint.getColor());
                    invalidate();
                }
                break;
        }
        return true;
    }


    public void setOpacity(int alpha) {
        if (alpha < 0 ){
            alpha = 0;
        }else if (alpha > 255){
            alpha = 255;
        }
        mPaint.setAlpha(alpha);
        invalidate();
    }

    public void setOnColorChangedListener(OnColorChangedListener l){
        this.mListener = l;
    }
}
