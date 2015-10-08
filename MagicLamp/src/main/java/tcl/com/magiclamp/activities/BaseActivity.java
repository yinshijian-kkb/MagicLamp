package tcl.com.magiclamp.activities;

import android.app.Activity;
import android.content.Intent;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;

/**
 * Created by sjyin on 9/26/15.
 */
public class BaseActivity extends Activity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fade_right, R.anim.fade);
    }

    @Override
    public void finish() {
        setResult(MyActivity.SceneFinishResultCode);
        super.finish();
        overridePendingTransition(R.anim.fade, R.anim.fade_left);
    }
}
