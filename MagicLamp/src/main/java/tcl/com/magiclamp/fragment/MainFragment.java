package tcl.com.magiclamp.fragment;

import com.nineoldandroids.animation.Animator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.TreeSet;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.data.LampAffection;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampColor;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.picker.ColorPickerView;
import tcl.com.magiclamp.picker.OnColorChangedListener;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.utils.UIUtils;
import tcl.com.magiclamp.views.SceneManager;

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

    private MyActivity mContext;
    /**
     * 颜色选择器
     */
    private ColorPickerView colorPicker2;
    private PopupWindow modPop;
    private TextView tv_header;
    private ImageView iv_lamp_color;
    private View viewError, viewLoading, fl_cover, colorPickerCover, compoundColorContainer, view_lamp_bg;
    private Button panelConfirm;
    /**
     * 首页底部音乐面板，可隐藏
     */
    private View music_panel;
    private CheckBox music_arrow, music_play;
    private RelativeLayout music_content;
    /**
     * 变化色
     */
    private View panel_2,panel_3,panel_4,panel_5,panel_6;
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
     * 灯光变化总共可选的个数
     */
    private final int compoundColorSize = 5;
    /**
     * 变化色中当前选中位置
     */
    private int mCompoundColorPosition;
    /**
     * 变化色中上一个历史选中位置
     */
    private int mLastCompoundColorPos;
    /**
     * 小神灯支持的模式集合
     */
    private ArrayList<LampMode> lampModes;
    /**
     * 变化色中选中取消的位置
     */
    private TreeSet<Integer> cancelColorPos;//if there are more than one mode can change compound color ,this should be map

    /**
     * 模拟延时操作
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    viewLoading.setVisibility(View.GONE);
                    viewError.setVisibility(View.GONE);
                    break;
            }
        }
    };
    /**
     * 变化色是否折叠
     */
    private boolean mIsExpanded;
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
    private LampColor mLampColor;
    /**
     * 当前变化色
     */
    private ArrayList<LampColor> mCompundColorWithState;
    /**
     * 变化色的初始大小和选中后大小
     */
    private int colorBgOldSize, colorCheckedBgSize;

    private HashMap<LampMode, TreeSet<Integer>> cancelColorPosMap;
    private HashMap<LampMode, LampColor> lampColorMap;
    private HashMap<LampMode, ArrayList<LampColor>> lampCompoundColorMap;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lampModes = new ArrayList<LampMode>();
        HashMap<LampMode, LampBean> lamps = ConfigData.lamps;
        for (LampMode mode : lamps.keySet()) {
            lampModes.add(mode);
        }
        cancelColorPosMap = new HashMap<LampMode, TreeSet<Integer>>();
        lampColorMap = new HashMap<LampMode, LampColor>();
        lampCompoundColorMap = new HashMap<LampMode, ArrayList<LampColor>>();

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

        iv_lamp_color = (ImageView) view.findViewById(R.id.iv_lamp_color);
        view_lamp_bg = view.findViewById(R.id.view_lamp_bg);
        iv_lamp_color.setOnClickListener(this);

        colorPicker2 = (ColorPickerView) view.findViewById(R.id.color_picker);
        colorPickerCover = view.findViewById(R.id.picker_cover);
        colorPicker2.setOnColorChangedListener(this);
        colorPickerCover.setOnClickListener(this);

        compoundColorContainer = view.findViewById(R.id.rl_panel);
        panel_2 = view.findViewById(R.id.panel_2);
        panel_3 = view.findViewById(R.id.panel_3);
        panel_4 = view.findViewById(R.id.panel_4);
        panel_5 = view.findViewById(R.id.panel_5);
        panel_6 = view.findViewById(R.id.panel_6);
        panel_2.setOnClickListener(this);
        panel_3.setOnClickListener(this);
        panel_4.setOnClickListener(this);
        panel_5.setOnClickListener(this);
        panel_6.setOnClickListener(this);
        panelConfirm = (Button) view.findViewById(R.id.btn_confirm);
        panelConfirm.setOnClickListener(this);

        brightnessBar = (SeekBar) view.findViewById(R.id.progress_seek_bar);
        brightnessBar.setOnSeekBarChangeListener(this);

        cb_sync_music_mode = (RadioButton) view.findViewById(R.id.sync_music_mode);
        blink_mode = (RadioButton) view.findViewById(R.id.blink_mode);
        candy_light_mode = (RadioButton) view.findViewById(R.id.candy_light_mode);
        default_mode = (RadioButton) view.findViewById(R.id.default_mode);

        viewError = view.findViewById(R.id.error_view);
        viewLoading = view.findViewById(R.id.loading_view);
        fl_cover = view.findViewById(R.id.fl_cover);
        fl_cover.setOnClickListener(this);


        //add music panel at footer
        music_panel = view.findViewById(R.id.music_panel);
        music_arrow = ((CheckBox) view.findViewById(R.id.cb_arrow));
        music_play = ((CheckBox) view.findViewById(R.id.cb_play));
        music_content = ((RelativeLayout) view.findViewById(R.id.rl_music_content));
        music_content.setOnClickListener(this);
        music_arrow.setOnCheckedChangeListener(this);
        music_play.setOnCheckedChangeListener(this);

        //measure music content height
        musicContentHeight = UIUtils.getDimens(R.dimen.music_content_height);
        mkeyTime = System.currentTimeMillis();

        //measure compound color bg size
        colorBgOldSize = LampColor.getCompoundColorBgSize();
        colorCheckedBgSize = LampColor.getCheckedCompoundColorBgSize();

        lampBeanInvalidate();
    }

    /**
     * 切换不同的模式
     */
    public void lampBeanInvalidate() {
        if (mMode != null && mMode == ConfigData.curLampMode) {
            return;
        }

        mMode = ConfigData.curLampMode;
        mLampData = ConfigData.curLamp;

        mCompoundColorPosition = 0;
        mLastCompoundColorPos = 0;
        mIsExpanded = true;
        mPowerOff = false;

        //init lamp color data
        if (lampColorMap.get(mMode) == null){
            mLampColor = new LampColor(iv_lamp_color);
            lampColorMap.put(mMode,mLampColor);
        }else{
            mLampColor = lampColorMap.get(mMode);
        }
        //init lamp compound color
        if (lampCompoundColorMap.get(mMode) == null){
            mCompundColorWithState = new ArrayList<LampColor>();
            mCompundColorWithState.add(new LampColor(panel_2));
            mCompundColorWithState.add(new LampColor(panel_3));
            mCompundColorWithState.add(new LampColor(panel_4));
            mCompundColorWithState.add(new LampColor(panel_5));
            mCompundColorWithState.add(new LampColor(panel_6));

            lampCompoundColorMap.put(mMode,mCompundColorWithState);
        }else{
            mCompundColorWithState = lampCompoundColorMap.get(mMode);
        }
        //lamp compound color cancel position
        if (cancelColorPosMap.get(mMode) == null){
            cancelColorPos = new TreeSet<Integer>();
            cancelColorPosMap.put(mMode,cancelColorPos);
        }else{
            cancelColorPos = cancelColorPosMap.get(mMode);
        }

        //是否显示音乐面板
        music_panel.setVisibility(mLampData.isCanAdjustedMusic() ? View.VISIBLE : View.INVISIBLE);
        if (mLampData.isCanAdjustedMusic()) {
            mMusicPanelShowing = true;
        }
        //更新标题
        tv_header.setText(mMode.toString());
        //灯色
        if (mLampData.getColor() != 0) {
            mLampColor.setColor(mLampData.getColor());
        } else {
            mLampColor.setColor(LampColor.COLOR_EMPTY);
        }

        //变化色
        for (int i = 0; i < compoundColorSize; i++) {
            LampColor _colorHolder = mCompundColorWithState.get(i);
            if (mLampData.getCompoundColor() != null && mLampData.getCompoundColor()[i] != 0) {
                _colorHolder.setColor(mLampData.getCompoundColor()[i]);
                mCompoundColorPosition ++;
            } else {
                cancelColorPos.add(i);
                _colorHolder.setColor(LampColor.COLOR_EMPTY);
            }
        }

        //亮度
        brightnessBar.setProgress(mLampData.getBrightness());

        //灯效
        LampAffection affection = mLampData.getAffection();
        if (affection == null)
            affection = LampAffection.Default;
        showLampAffection(affection);

        //关灯时选中灯色、变化色收起
        checkedLampColor(!mPowerOff);
        expandCompoundColor(mPowerOff);

        setLampEnable(!mPowerOff);
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
     * 开关机更新状态
     */
    private void setLampEnable(boolean enabled) {
        fl_cover.setVisibility(enabled ? View.GONE : View.VISIBLE);
        if (!enabled) {
            //默认情况下选中灯的颜色
            disableLampController();
            return;
        }
        //灯色
        mLampColor.setColor(mLampData.isCanAdjustedColor() ?
                mLampColor.getColor()
                : mLampColor.setState(LampColor.LampColorState.STATE_DISABLE));

        //变化色
        for (int i = 0; i < compoundColorSize; i++) {
            LampColor _colorHolder = mCompundColorWithState.get(i);
            _colorHolder.setColor(mLampData.isCanAdjustedComposedColor() ? _colorHolder.getColor()
                    : _colorHolder.setState(LampColor.LampColorState.STATE_DISABLE));
        }
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
        mLampColor.setColor(mLampColor.setState(LampColor.LampColorState.STATE_DISABLE));
        //变化色
        for (int i = 0; i < compoundColorSize; i++) {
            LampColor _compoundColor = mCompundColorWithState.get(i);
            _compoundColor.setColor(mLampColor.setState(LampColor.LampColorState.STATE_DISABLE));
        }
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
     * 变化色是否可点击
     *
     * @param canAdjustedColor
     * @param canAdjustedComposedColor
     */
    private void setPanelState(boolean canAdjustedColor, boolean canAdjustedComposedColor) {
        iv_lamp_color.setEnabled(canAdjustedColor ? true : false);
        panelConfirm.setEnabled(canAdjustedComposedColor ? true : false);

        //update compound color state
        for (int i = 0; i < compoundColorSize; i++) {
            LampColor _compoundColor = mCompundColorWithState.get(i);
            _compoundColor.getTargetView().setEnabled(canAdjustedComposedColor ? true : false);
        }
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
            case R.id.iv_lamp_color://编辑灯色
                checkedLampColor(true);
                //灯色
                if (mLampData.getColor() != 0) {
                    mLampColor.setColor(LampColor.COLOR_EMPTY);
                } else {
                    ToastUtils.showShort(mContext, "请选择灯色");
                }
                break;
            case R.id.panel_2://编辑变化色
                checkedLampColor(false);
                if (!mIsExpanded) {
                    expandCompoundColor(true);
                } else {
                    cancelPanelColor(v, 0);
                }
                break;
            case R.id.panel_3:
                cancelPanelColor(v, 1);
                break;
            case R.id.panel_4:
                cancelPanelColor(v, 2);
                break;
            case R.id.panel_5:
                cancelPanelColor(v, 3);
                break;
            case R.id.panel_6:
                cancelPanelColor(v, 4);
                break;
            case R.id.btn_confirm:
                ToastUtils.showShort(mContext, "同步变化色");
                expandCompoundColor(false);
                break;
            //touch the music panel
            case R.id.rl_music_content:
                if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                    mContext.performChange2Music();
                }
                mkeyTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }

    /**
     * 变化色和灯色互斥，通过修改背景来表示当前的选中状态
     *
     * @param checked
     */
    private void checkedLampColor(boolean checked) {
        if (mLampColor.isChecked() == checked) return;//这是为什么把选中状态放在对象中的原因
        if (checked && mIsExpanded) {
            expandCompoundColor(false);
        }
        view_lamp_bg.setBackgroundResource(checked ? R.drawable.changable_panel_bg : R.color.transparent);
        compoundColorContainer.setBackgroundResource(!checked ? R.drawable.changable_panel_bg : R.color.transparent);
        mLampColor.setChecked(checked);
    }

    /**
     * 选中变化色
     *
     * @param pos
     */
    private void checkedCompoundColor(int pos) {
        if (mLastCompoundColorPos == pos) return;

        LampColor _compoundColor = mCompundColorWithState.get(pos);
        _compoundColor.setSize(colorCheckedBgSize, colorCheckedBgSize);
        LampColor _lastCompoundColor = mCompundColorWithState.get(mLastCompoundColorPos);
        if (_lastCompoundColor != null)
            _lastCompoundColor.setSize(colorCheckedBgSize, colorCheckedBgSize);
        mLastCompoundColorPos = pos;
    }

    /**
     * 展开、折叠变化色
     *
     * @param isExpanded
     */
    private void expandCompoundColor(boolean isExpanded) {
        mIsExpanded = isExpanded;
        panelConfirm.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        for (int i = 0; i < compoundColorSize; i++) {
            LampColor _compoundColor = mCompundColorWithState.get(i);
            if (i == 0) continue;
            /*//when expanded set the color button size
            if (i == 0) {
                if (mIsExpanded) {
                    _compoundColor.setSize(colorCheckedBgSize, colorCheckedBgSize);
                } else {
                    _compoundColor.setSize(colorBgOldSize, colorBgOldSize);
                }
                continue;
            }*/
            _compoundColor.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 取消填充板上的色值
     *
     * @param v
     * @param pos
     */
    private void cancelPanelColor(View v, int pos) {
        LampColor _curPanel = mCompundColorWithState.get(pos);
        if (_curPanel.getColor() == LampColor.COLOR_EMPTY) {
            //无法取消填色值，因为没有填充色值
            ToastUtils.showShort(mContext, "没有填充色值");
        } else {
            cancelColorPos.add(pos);
            _curPanel.setColor(LampColor.COLOR_EMPTY);
        }
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
    public void showLoading() {
        viewLoading.setVisibility(View.VISIBLE);
        viewError.setVisibility(View.GONE);
        mHandler.sendEmptyMessageDelayed(1, 1000);
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
        ToastUtils.showShort(mContext, "当前的亮度是：" + seekBar.getProgress());
    }

    /* ColorChangeListener */
    @Override
    public void colorChanged(int color) {
        //ColorPicker cannot click
        if (!(mLampData.isCanAdjustedColor() || mLampData.isCanAdjustedComposedColor())) {
            return;
        }

        if (mLampColor.isChecked()) {
            //编辑灯颜色
            fillPanel(4, color);
        } else {
            //编辑变化色
            if (!cancelColorPos.isEmpty()) {
                int mPos = cancelColorPos.first();
                fillCompoundColorPanel(mPos, color);
                if (cancelColorPos.remove(mPos)){
                    mCompoundColorPosition++;
                    mLastCompoundColorPos = mCompoundColorPosition;
                }
            } else {
                if (mCompoundColorPosition >= compoundColorSize) {
                    ToastUtils.showShort(mContext, "灯色面板已经填充满");
                } else {
                    fillCompoundColorPanel(++mCompoundColorPosition, color);
//            checkedCompoundColor(++mCompoundColorPosition);
                }
            }
        }
    }

    /**
     * 填充填色板
     */
    private void fillCompoundColorPanel(int pos, int color) {
        LampColor _compoundColor = mCompundColorWithState.get(pos);
        if (_compoundColor.getColor() == LampColor.COLOR_EMPTY ){
            mCompundColorWithState.get(pos).setColor(color);
        }
    }

    /**
     * 填充灯色
     */
    private void fillPanel(int pos, int color) {
        mLampColor.setColor(color);
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
                if (mIsExpanded){
                    checkedLampColor(true);
                    expandCompoundColor(true);
                }
                setLampEnable(mPowerOff);
                ToastUtils.showShort(mContext, mPowerOff ? "关机" : "开机");
                break;
            // add music panel at footer
            case R.id.cb_arrow:
                mMusicPanelShowing = isChecked;
                if (mMusicPanelShowing) {
                    musicPanelUp();
                } else {
                    musicPanelDown();
                }
                break;
            case R.id.cb_play:

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
