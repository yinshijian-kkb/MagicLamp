package tcl.com.magiclamp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import tcl.com.magiclamp.fragment.BroadcastFragment;
import tcl.com.magiclamp.fragment.MainFragment;
import tcl.com.magiclamp.fragment.LeftMenuFragment;
import tcl.com.magiclamp.fragment.MusicFragment;
import tcl.com.magiclamp.fragment.SceneFragment;
import tcl.com.magiclamp.fragment.SettingFragment;
import tcl.com.magiclamp.utils.JLog;

public class MyActivity extends SlidingFragmentActivity{

    private SlidingMenu mSlidingMenu;
    private long mkeyTime;
    private MainFragment mainFragment;
    private LeftMenuFragment mLeftFragment;

    public static final int RequestCode = 0x001;
    public static final int SceneFinishResultCode = 0x002;
    private MusicFragment musicFragment;
    /**
     * 上一Fragment的标记
     */
    private int mLastFragmentPos;
    private BroadcastFragment broadcastFragment;
    private SceneFragment sceneFragment;
    private SettingFragment settingFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);//1
        setBehindContentView(R.layout.left_menu);//2

        // customize the SlidingMenu
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setMode(SlidingMenu.LEFT);//设置左右都可以划出SlidingMenu菜单 //3

//        mSlidingMenu.setShadowWidth(5);
//        mSlidingMenu.setBehindOffset(30);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width); //设置阴影图片的宽度
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset); //SlidingMenu划出时主页面显示的剩余宽度
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        //设置 SlidingMenu 内容
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragment = new MainFragment();
        mLeftFragment = new LeftMenuFragment();
        fragmentTransaction.replace(R.id.left_menu, mLeftFragment);
        fragmentTransaction.replace(R.id.content, mainFragment);
        fragmentTransaction.commit();
    }

    /**
     * 切换Fragment
     *
     * @param fragmentPos
     */
    public void replaceContentFragment(int fragmentPos){
        Fragment _fragment = null;
        switch (fragmentPos){
            case 0://灯光
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                _fragment = mainFragment;
                break;
            case 1://音乐
                if (musicFragment == null){
                    musicFragment = new MusicFragment();
                }
                _fragment = musicFragment;
                break;
            case 2://广播
                if (broadcastFragment == null){
                    broadcastFragment = new BroadcastFragment();
                }
                _fragment = broadcastFragment;
                break;
            case 3://场景
                if (sceneFragment == null){
                    sceneFragment = new SceneFragment();
                }
                _fragment = sceneFragment;
                break;
            case 4://设置
                if (settingFragment == null){
                    settingFragment = new SettingFragment();
                }
                _fragment = settingFragment;
                break;
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentPos > mLastFragmentPos){
            fragmentTransaction.setCustomAnimations(R.anim.fade_up_enter, R.anim.fade_up_exit);
        }else{
            fragmentTransaction.setCustomAnimations(R.anim.fade_down_enter, R.anim.fade_down_exit);
        }
        fragmentTransaction.replace(R.id.content, _fragment);
        fragmentTransaction.commit();
        mLastFragmentPos = fragmentPos;
    }

    /**
     * 选中灯光
     */
    public void performClickMenu(){
        toggle();
    }

    /**
     * 选中音乐
     */
    public void performChange2Music(){
        //update content fragment
        replaceContentFragment(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RequestCode:
                switch (resultCode){
                    case SceneFinishResultCode:
                        mLeftFragment.updateMenuState(0);
                        setOnMenuClick(null,null,0,0);
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mkeyTime) > 2000) {
            mkeyTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
        } else {
//            finish();
            super.onBackPressed();
        }
    }

    public void setOnMenuClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent();
        switch (position){
            case 0://灯光
                toggle();
                replaceContentFragment(0);
                i = null;
                break;
            case 1://音乐
                toggle();
                replaceContentFragment(1);
                i = null;
                break;
            case 2://广播
                toggle();
                replaceContentFragment(2);
                i = null;
//                i.setClass(this,BroadcastActivity.class);
                break;
            case 3://场景
                toggle();
                replaceContentFragment(3);
                i = null;
//                i.setClass(this,SceneActivity.class);
                break;
            case 4://设置
                toggle();
                replaceContentFragment(4);
                i = null;
//                i.setClass(this,SettingActivity.class);
                break;
            default:
                i = null;
                break;
        }
        if (i != null){
            startActivityForResult(i, RequestCode);
        }
    }
}
