package tcl.com.magiclamp.activities;

import android.app.Activity;
import android.os.Bundle;

import tcl.com.magiclamp.utils.ViewHelper;

/**
 * 音乐
 * Created by sjyin on 9/26/15.
 */
public class MusicActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ViewHelper.getTextWithTitle("音乐"));
    }
}
