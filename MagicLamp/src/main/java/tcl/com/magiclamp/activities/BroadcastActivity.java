package tcl.com.magiclamp.activities;

import android.app.Activity;
import android.os.Bundle;

import tcl.com.magiclamp.utils.ViewHelper;

/**
 * 广播
 * Created by sjyin on 9/26/15.
 */
public class BroadcastActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewHelper.getTextWithTitle("广播"));
    }
}
