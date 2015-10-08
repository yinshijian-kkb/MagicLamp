package tcl.com.magiclamp.activities;

import android.os.Bundle;

import tcl.com.magiclamp.utils.ViewHelper;

/**
 * 设置
 * Created by sjyin on 9/26/15.
 */
public class SettingActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewHelper.getTextWithTitle("设置"));
    }
}
