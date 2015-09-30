package tcl.com.magiclamp.utils;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import tcl.com.magiclamp.MyApplication;

/**
 * Created by sjyin on 9/21/15.
 */
public class ViewHelper {

    private static boolean bol;
    public static View getTextWithTitle(String title){
        TextView tv = new TextView(MyApplication.getApplication());
        if (TextUtils.isEmpty(title)){
            title = "灯光";
        }
        tv.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        tv.setGravity(Gravity.CENTER);
        tv.setText(title);
        tv.setTextSize(20);
        tv.setTextColor(Color.BLACK);
        if (bol){
            tv.setBackgroundColor(Color.parseColor("#FFFFFF00"));
        }else {
            tv.setBackgroundColor(Color.parseColor("#FFFF9F00"));
        }
        bol = !bol;
        return tv;
    }
}
