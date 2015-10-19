package tcl.com.magiclamp.fragment;

import com.nineoldandroids.animation.Animator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.HashMap;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.controller.CompoundColorController;
import tcl.com.magiclamp.data.LampAffection;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.controller.SingleColor;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.data.MusicBean;
import tcl.com.magiclamp.picker.ColorPickerView;
import tcl.com.magiclamp.picker.OnColorChangedListener;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.MyToast;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.utils.UIUtils;
import tcl.com.magiclamp.controller.SceneManager;

import static tcl.com.magiclamp.data.LampAffection.Blink;
import static tcl.com.magiclamp.data.LampAffection.Candy;
import static tcl.com.magiclamp.data.LampAffection.Default;
import static tcl.com.magiclamp.data.LampAffection.SyncMusic;

/**
 * Created by sjyin on 9/21/15.
 */
public class MainFragment extends Fragment implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener,
        OnColorChangedListener,
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener,
        Animator.AnimatorListener {

    public static final int CHANGE_COLOR = 0x0001;
    public static final int CHANGE_COMPOUND_COLOR = 0x0002;
    private static final int CHANGE_LIGHT_LEVEL = 0x003;

    private MyActivity mContext;
    /**
     * 颜色选择器
     */
    private ColorPickerView colorPicker2;
    private PopupWindow modPop;
    private TextView tv_header, tv_song, tv_singer;
    private View viewError, viewLoading, fl_cover, colorPickerCover, view_lamp_bg, iv_lamp_color;
    private FrameLayout compoundColorContainer;
    /**
     * 首页底部音乐面板，可隐藏
     */
    private View music_panel;
    private Button music_arrow;
    private CheckBox music_play;
    private RelativeLayout music_content;
    /**
     * 灯效
     */
    private RadioButton cb_sync_music_mode, blink_mode, candy_light_mode, default_mode;
    /**
     * 灯光亮度调节器
     */
    private SeekBar brightnessBar;

    /**
     * 当前的模式
     */
    private LampMode mMode;
    /**
     * 当前模式下，对应的数据
     */
    private LampBean mLampData;
    /**
     * 小神灯支持的模式集合
     */
    private ArrayList<LampMode> lampModes;

    /**
     * 模拟延时操作
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLoading.dismiss();
            switch (msg.what) {
                case CHANGE_COLOR:
                    mLampData.setColor(singleColor.getColor());
                    break;
                case CHANGE_COMPOUND_COLOR:
                    mLampData.setCompoundColor(compoundColorController.getCompoundColor());
                    break;
                case CHANGE_LIGHT_LEVEL:
                    mLampData.setBrightness(curBrightness);
                    break;
            }
        }
    };
    /**
     * 是否显示音乐面板
     */
    private boolean mMusicPanelShowing;
    /**
     * 是否关机
     */
    private boolean mPowerOff;
    private SceneManager mSceneManger;

    /**
     * 音乐面板的高度
     */
    private int musicContentHeight;
    private long mkeyTime;
    /**
     * 当前灯色
     */
    private SingleColor singleColor;

    private HashMap<LampMode, SingleColor> singleColorMap;

    private CompoundColorController compoundColorController;
    /**
     * 当前的亮度
     */
    private int curBrightness;
    /**
     * Loading view
     */
    private MyToast mLoading;
    /**
     * 播放背景音乐
     */
    private boolean mIsPlaying;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            lampModes = (ArrayList<LampMode>) savedInstanceState.getSerializable("lampModes");
            singleColorMap = (HashMap<LampMode, SingleColor>) savedInstanceState.getSerializable("singleColorMap");
            compoundColorController = (CompoundColorController) savedInstanceState.getSerializable("compoundColorController");
        }
        if (lampModes == null) {
            lampModes = new ArrayList<LampMode>();
            HashMap<LampMode, LampBean> lamps = ConfigData.lamps;
            for (LampMode mode : lamps.keySet()) {
                lampModes.add(mode);
            }
        }

        if (singleColorMap == null) {
            singleColorMap = new HashMap<LampMode, SingleColor>();
        }

        if (compoundColorController == null) {
            compoundColorController = new CompoundColorController(mContext, this);
        }

        view.findViewById(R.id.header).setBackgroundColor(
                UIUtils.getColor(R.color.info_sreen_bg));

        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_header_back);
        ivBack.setImageResource(R.drawable.menu_selector);
        ivBack.setOnClickListener(this);

        CheckBox powerView = (CheckBox) view.findViewById(R.id.cb_checker);
        powerView.setButtonDrawable(R.drawable.on);
        powerView.setOnCheckedChangeListener(this);

        tv_header = (TextView) view.findViewById(R.id.tv_header_title);
        tv_header.setOnClickListener(this);

        iv_lamp_color = view.findViewById(R.id.iv_lamp_color);
        view.findViewById(R.id.tv_panel).setOnClickListener(this);
        view.findViewById(R.id.tv_lamp_color).setOnClickListener(this);
        iv_lamp_color.setOnClickListener(this);
        view_lamp_bg = view.findViewById(R.id.view_lamp_bg);

        colorPicker2 = (ColorPickerView) view.findViewById(R.id.color_picker);
        colorPickerCover = view.findViewById(R.id.picker_cover);
        colorPicker2.setOnColorChangedListener(this);
        colorPickerCover.setOnClickListener(this);

        compoundColorContainer = (FrameLayout) view.findViewById(R.id.rl_panel);
        View _view = compoundColorController.getView();
        ViewParent _parentView = _view.getParent();
        if (_parentView != null) {
            ((ViewGroup) _parentView).removeView(_view);
        }
        compoundColorContainer.addView(_view);

        brightnessBar = (SeekBar) view.findViewById(R.id.progress_seek_bar);
        brightnessBar.setOnSeekBarChangeListener(this);

        cb_sync_music_mode = (RadioButton) view.findViewById(R.id.sync_music_mode);
        blink_mode = (RadioButton) view.findViewById(R.id.blink_mode);
        candy_light_mode = (RadioButton) view.findViewById(R.id.candy_light_mode);
        default_mode = (RadioButton) view.findViewById(R.id.default_mode);

//        viewError = view.findViewById(R.id.error_view);
//        viewLoading = view.findViewById(R.id.loading_view);
        fl_cover = view.findViewById(R.id.fl_cover);
        fl_cover.setOnClickListener(this);

        //add music panel at footer
        tv_singer = (TextView) view.findViewById(R.id.tv_singer);
        tv_song = (TextView) view.findViewById(R.id.tv_song);
        music_panel = view.findViewById(R.id.music_panel);
        music_arrow = ((Button) view.findViewById(R.id.btn_arrow));
        music_play = ((CheckBox) view.findViewById(R.id.cb_play));
        music_content = ((RelativeLayout) view.findViewById(R.id.rl_music_content));
        music_content.setOnClickListener(this);
        music_arrow.setOnClickListener(this);
        music_play.setOnClickListener(this);

        //measure music content height
        musicContentHeight = UIUtils.getDimens(R.dimen.music_content_height);
        mkeyTime = System.currentTimeMillis();

        lampBeanInvalidate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*outState.putSerializable("lampModes", lampModes);
        outState.putSerializable("singleColorMap",singleColorMap);
        outState.putSerializable("compoundColorController",compoundColorController);*/
    }

    /**
     * 切换不同的模式
     */
    public void lampBeanInvalidate() {

        mMode = ConfigData.curLampMode;
        mLampData = ConfigData.curLamp;
        mPowerOff = false;

        //灯色
        if (singleColorMap.get(mMode) == null) {
            singleColor = new SingleColor(iv_lamp_color);
            singleColorMap.put(mMode, singleColor);
        } else {
            singleColor.setTargetView(iv_lamp_color);
            singleColor = singleColorMap.get(mMode);
        }
        if (mLampData.getColor() != SingleColor.COLOR_EMPTY || mLampData.getColor() != 0) {
            singleColor.check(true);
            singleColor.setColor(mLampData.getColor());
        } else {
            singleColor.setColor(SingleColor.COLOR_EMPTY);
        }

        //变化色
        compoundColorController.resetCompoundColor(mMode, mLampData);

        //是否显示音乐面板
        checkBox(mIsPlaying);
        if (mLampData.isCanAdjustedMusic() && mLampData.getMusic() != null &&
                mLampData.getMusic().isPlaying() && !mLampData.getMusic().isHided()) {
            music_panel.setVisibility(View.VISIBLE);
            initMusicPanel();
        } else {
            music_panel.setVisibility(View.INVISIBLE);
        }
        //更新标题
        tv_header.setText(mMode.toString());

        //亮度
        brightnessBar.setProgress(mLampData.getBrightness());

        //灯效
        LampAffection affection = mLampData.getAffection();
        if (affection == null)
            affection = LampAffection.Default;
        showLampAffection(affection);

        //开灯时选中灯色、变化色收起
        lightLampColor(true);
        //开机时屏幕中的控件是否可操作
        setLampEnable(true);
    }

    /**
     * 初始化音乐面板
     */
    private void initMusicPanel() {
        //tv_song, tv_singer
        String _song = "", _singer = "";
        MusicBean _bean = mLampData.getMusic();
        if (!TextUtils.isEmpty(_bean.getSong().toString().trim())) {
            _song = _bean.getSong();
        }
        if (!TextUtils.isEmpty(_bean.getSinger().toString().trim())) {
            _singer = _bean.getSinger();
        }
        mIsPlaying = !_bean.isPlaying();
        music_play.performClick();
        tv_song.setText(_song);
        tv_singer.setText(_singer);

        musicPanelUp();
    }

    /**
     * 设置灯效
     *
     * @param affection
     */
    private void showLampAffection(LampAffection affection) {
        switch (affection) {
            case Default:
                default_mode.setChecked(affection == Default);
                break;
            case Candy:
                candy_light_mode.setChecked(affection == Candy);
                break;
            case Blink:
                blink_mode.setChecked(affection == Blink);
                break;
            case SyncMusic:
                cb_sync_music_mode.setChecked(affection == SyncMusic);
                break;
        }
    }

    /**
     * 是否禁用屏幕中的控件
     *
     * @param powerOn
     */
    private void setLampEnable(boolean powerOn) {
        fl_cover.setVisibility(powerOn ? View.GONE : View.VISIBLE);
        if (!powerOn) {
            //默认情况下选中灯的颜色
            disableLampController();
            return;
        }
        //灯色
        if (mLampData.isCanAdjustedColor()) {
            singleColor.setColor(singleColor.getColor());
        } else {
            singleColor.setState(SingleColor.LampColorState.STATE_DISABLE);
        }


        //变化色
        compoundColorController.setColorEnable(mLampData.isCanAdjustedColor());
        setPanelState(mLampData.isCanAdjustedColor(), mLampData.isCanAdjustedComposedColor());
        //色盘
        boolean _canClickPicker = mLampData.isCanAdjustedColor() || mLampData.isCanAdjustedComposedColor();
        colorPicker2.setEnabled(_canClickPicker);
        colorPickerCover.setVisibility(_canClickPicker ? View.GONE : View.VISIBLE);
        //亮度
        brightnessBar.setEnabled(mLampData.isCanAdjustedBrightness());
        //音乐
        music_arrow.setEnabled(mLampData.isCanAdjustedMusic());
        //灯效
        setAffectionState(mLampData.isCanAdjustedAffection());
    }

    /**
     * 控制面板可以编辑
     */
    private void disableLampController() {
        //是否可编辑
        //灯色
        singleColor.setState(SingleColor.LampColorState.STATE_DISABLE);
        //变化色
        compoundColorController.setColorEnable(false);
        setPanelState(false, false);
        //色盘
        colorPicker2.setEnabled(false);
        colorPickerCover.setVisibility(View.VISIBLE);
        //亮度
        brightnessBar.setEnabled(false);
        //音乐
        music_arrow.setEnabled(false);
        //灯效
        setAffectionState(false);
    }


    /**
     * 灯色和变化色是否可点击
     *
     * @param canAdjustedColor
     * @param canAdjustedComposedColor
     */
    private void setPanelState(boolean canAdjustedColor, boolean canAdjustedComposedColor) {
        iv_lamp_color.setEnabled(canAdjustedColor ? true : false);
        compoundColorController.setEnable(canAdjustedComposedColor);
    }

    /**
     * 灯效是否可编辑
     *
     * @param canAdjustedAffection
     */
    private void setAffectionState(boolean canAdjustedAffection) {
        cb_sync_music_mode.setEnabled(canAdjustedAffection ? true : false);
        blink_mode.setEnabled(canAdjustedAffection ? true : false);
        candy_light_mode.setEnabled(canAdjustedAffection ? true : false);
        default_mode.setEnabled(canAdjustedAffection ? true : false);
    }

    /**
     * 音乐面板向上弹起动画
     */
    private void musicPanelUp() {
        //should be -musicContentHeight but not -musicContentHeight+1 ,I dont know why
        ObjectAnimator _animator = ObjectAnimator.ofFloat(music_panel, "translationY", 0, -musicContentHeight + 1).setDuration(100);
        _animator.start();
        _animator.addListener(this);
    }

    /**
     * 音乐面板向下收藏动画
     */
    private void musicPanelDown() {
        ObjectAnimator _animator = ObjectAnimator.ofFloat(music_panel, "translationY", -musicContentHeight + 1, 0).setDuration(100);
        _animator.start();
        _animator.addListener(this);
    }

    /**
     * 动画没有结束的时候，点击正在动画的UI无响应
     *
     * @param enable
     */
    private void enableMusicPanel(boolean enable) {
        music_arrow.setEnabled(enable ? true : false);
        music_content.setEnabled(enable ? true : false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
                mContext.performClickMenu();//back::showContent
                break;
            case R.id.tv_header_title:
                showPop();
                break;
            case R.id.tv_lamp_color:
            case R.id.iv_lamp_color://编辑灯色
                checkSingleColor(true);
                lightLampColor(true);
                //灯色
                if (singleColor.getColor() == SingleColor.COLOR_EMPTY) {
                    ToastUtils.showShort(mContext, "请选择灯色");
                } else {
                    singleColor.setColor(SingleColor.COLOR_EMPTY);
                }
                break;
            case R.id.tv_panel://编辑变化色
                checkSingleColor(false);
                lightLampColor(false);
                break;
            //touch the music panel
            case R.id.rl_music_content:
                hideMusicPanel();
                mContext.performChange2Music();
                break;
            case R.id.btn_arrow:
                hideMusicPanel();
                musicPanelDown();
                break;
            case R.id.cb_play://播放
                mIsPlaying = !mIsPlaying;
                checkBox(mIsPlaying);
                ToastUtils.showShort(mContext, mIsPlaying ? "播放" : "暂停");
                mLampData.getMusic().play(mIsPlaying);
                break;
            default:
                break;
        }
    }

    /**
     * 设置CheckBox的选中状态
     *
     * @param checked
     */
    private void checkBox(boolean checked){
        music_play.setButtonDrawable(checked ? R.drawable.music_play_normal : R.drawable.music_suspended_normal);
    }

    /**
     * 隐藏音乐面板
     */
    private void hideMusicPanel() {
        music_panel.setVisibility(View.INVISIBLE);
        mLampData.getMusic().hide(true);
        mLampData.getMusic().play(false);
    }

    /**
     * 变化色和灯色互斥，通过修改背景来表示当前的选中状态
     *
     * @param light
     */
    public void lightLampColor(boolean light) {
        if (singleColor.isChecked()) {
            if (singleColor.isChecked() != light)
                return;
        }
        compoundColorController.expandCompoundColor(!light);
        view_lamp_bg.setBackgroundResource(light ? R.drawable.lamp_color_bg2 : R.color.transparent);
        compoundColorController.setBackgroundResource(!light ? R.drawable.changable_panel_bg : R.color.transparent);
        singleColor.check(light);
    }

    public boolean singleColorIsChecked() {
        return singleColor.isChecked();
    }

    public void checkSingleColor(boolean checked) {
        singleColor.check(checked);
    }

    /**
     * 选择模式
     */
    private void showPop() {
        if (modPop == null) {
            mSceneManger = new SceneManager(mContext);//1
            mSceneManger.setOnItemClickListener(this);//2
            modPop = new PopupWindow(mSceneManger.getSceneView(),//3
                    200,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            modPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//or can not dismiss when touch outside
            modPop.setFocusable(true);
            modPop.setOutsideTouchable(true);
        }
        if (!modPop.isShowing()) {
            int[] _location = new int[2];
            int _tvHeaderWidth = tv_header.getWidth();
            int _modPopWidth = modPop.getWidth();
            tv_header.getLocationOnScreen(_location);
            modPop.showAsDropDown(tv_header,
                    _tvHeaderWidth > _modPopWidth ?
                            -(_tvHeaderWidth - _modPopWidth) / 2 :
                            -(_modPopWidth - _tvHeaderWidth) / 2,
                    0);
        } else {
            modPop.dismiss();
        }
    }

    /**
     * 显示阻塞加载
     */
    public void showLoading(int flag) {
        if (mLoading == null)
            mLoading = new MyToast(mContext, true);
        mLoading.show();
        mHandler.sendEmptyMessageDelayed(flag, 1000);
    }

    @Override
    public void onStop() {
        super.onDetach();
        ConfigData.curLampMode = mMode;
        ConfigData.curLamp = mLampData;
    }

    /*  SeekBarChangeListener  */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        showLoading(CHANGE_LIGHT_LEVEL);
        curBrightness = seekBar.getProgress();
//        ToastUtils.showShort(mContext, "当前的亮度是：" + seekBar.getProgress());
    }

    /* ColorChangeListener */
    @Override
    public void colorChanged(int color) {
        //ColorPicker cannot click
        if (!(mLampData.isCanAdjustedColor() || mLampData.isCanAdjustedComposedColor())) {
            return;
        }

        if (!compoundColorController.isExpanded()) {
            //编辑灯颜色
            fillPanel(4, color);
        } else {
            //编辑变化色
            compoundColorController.fillCompoundColorPanel(color);
        }
    }

    /**
     * 填充灯色
     */
    private void fillPanel(int pos, int color) {
        singleColor.setColor(color);
        showLoading(CHANGE_COLOR);
    }

    /*  OnItemClickListener  */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showPop();
        if (mSceneManger.getLastModePos() != position) {
            mSceneManger.getData().get(position).setChecked(true);
            mSceneManger.getData().get(mSceneManger.getLastModePos()).setChecked(false);
            mSceneManger.getAdapter().notifyDataSetChanged();
            mSceneManger.setLastModePos(position);

            ConfigData.curLampMode = mSceneManger.getData().get(position).getMode();
            ConfigData.curLamp = ConfigData.lamps.get(ConfigData.curLampMode);
            if (ConfigData.curLampMode != mMode)
                lampBeanInvalidate();
        }
    }

    /* OnCheckedChangeListener */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_checker:
                mPowerOff = isChecked;
                tv_header.setEnabled(!mPowerOff);
                if (compoundColorController.isExpanded()) {
                    lightLampColor(true);
                }
                setLampEnable(!mPowerOff);
                ToastUtils.showShort(mContext, mPowerOff ? "关机" : "开机");
                break;
        }
    }

    /*  ObjectAnimator Listener*/
    @Override
    public void onAnimationStart(Animator animation) {
        enableMusicPanel(false);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        enableMusicPanel(true);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        enableMusicPanel(true);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
