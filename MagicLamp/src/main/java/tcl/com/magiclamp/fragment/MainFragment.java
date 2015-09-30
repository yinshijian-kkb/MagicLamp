package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.SceneHolder;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.data.LampAffection;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.data.SceneItem;
import tcl.com.magiclamp.picker.ColorPickerView2;
import tcl.com.magiclamp.picker.OnColorChangedListener;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.utils.UIUtils;

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
        AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private MyActivity mContext;
    private ColorPickerView2 colorPicker2;
    private TextView panel_1, tv_header;
    private PopupWindow modPop;
    private View viewError, viewLoading, fl_cover;

    private LampMode mMode;
    private LampBean mLampData;
    private SeekBar brightnessBar;
    private Button panel2, panel3, panel4, panel5;
    private Drawable panelEmpty;
    private int compoundColorSize = 4;
    private int[] mCompundColors = new int[]{0,0,0,0,0};
    private int mCompundColorPosition = 0;
    private RadioButton cb_sync_music_mode, blink_mode, candy_light_mode, default_mode;
    private ArrayList<LampMode> lampModes;
    private ArrayList<SceneItem> items;
    private CommonAdapter<SceneItem> modeAdapter;
    private int lastModePos;
    private TreeSet<Integer> cancelColorPos;//if there are more than one mode can change compound color ,this should be map

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

        cancelColorPos = new TreeSet<Integer>();
        view.findViewById(R.id.header).setBackgroundColor(
                UIUtils.getColor(R.color.info_sreen_bg));

        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_header_back);
        ivBack.setImageResource(R.drawable.menu);
        ivBack.setOnClickListener(this);

        CheckBox cb = (CheckBox) view.findViewById(R.id.cb_checker);
        cb.setButtonDrawable(R.drawable.on);
        cb.setOnCheckedChangeListener(this);

        tv_header = (TextView) view.findViewById(R.id.tv_header_title);
        tv_header.setOnClickListener(this);

        panelEmpty = UIUtils.getDrawable(R.drawable.panel_empty);
        panel_1 = (TextView) view.findViewById(R.id.panel_1);
        GradientDrawable panelDefault = (GradientDrawable) UIUtils.getDrawable(R.drawable.panel_default);
        panelDefault.setBounds(0, 0, panelDefault.getMinimumWidth(), panelDefault.getMinimumHeight());
        panelEmpty.setBounds(0, 0, panelEmpty.getMinimumWidth(), panelEmpty.getMinimumHeight());

        colorPicker2 = (ColorPickerView2) view.findViewById(R.id.color_picker);
        colorPicker2.setOnColorChangedListener(this);

        panel2 = (Button) view.findViewById(R.id.panel_2);
        panel3 = (Button) view.findViewById(R.id.panel_3);
        panel4 = (Button) view.findViewById(R.id.panel_4);
        panel5 = (Button) view.findViewById(R.id.panel_5);
        panel2.setOnClickListener(this);
        panel3.setOnClickListener(this);
        panel4.setOnClickListener(this);
        panel5.setOnClickListener(this);

        brightnessBar = (SeekBar) view.findViewById(R.id.progress_seek_bar);
        brightnessBar.setOnSeekBarChangeListener(this);

        cb_sync_music_mode = (RadioButton) view.findViewById(R.id.sync_music_mode);
        blink_mode = (RadioButton) view.findViewById(R.id.blink_mode);
        candy_light_mode = (RadioButton) view.findViewById(R.id.candy_light_mode);
        default_mode = (RadioButton) view.findViewById(R.id.default_mode);

        viewError = view.findViewById(R.id.error_view);
        viewLoading = view.findViewById(R.id.loading_view);
        fl_cover = view.findViewById(R.id.fl_cover);

        lampBeanInvalidate();

    }

    /**
     * 切换不同的模式
     */
    private void lampBeanInvalidate() {
        mMode = ConfigData.curLampMode;
        mLampData = ConfigData.curLamp;

        //更新标题
        tv_header.setText(mMode.toString());
        //灯色
        GradientDrawable panelBg = (GradientDrawable) panel_1.getCompoundDrawables()[0];
        if (mLampData.getColor() != 0) {
            panelBg.setColor(mLampData.getColor());
        } else {
            panelBg.setColor(Color.BLACK);
        }
        //变化色
        for (int i = 0; i < compoundColorSize; i++) {//TODO:has bug
            String tag = String.format("panel_%d", i + 2);
            GradientDrawable gd = (GradientDrawable) getView().findViewWithTag(tag).getBackground();
            if (mLampData.getCompoundColor() != null && mLampData.getCompoundColor()[i] != 0) {
                gd.setColor(mLampData.getCompoundColor()[i]);
            } else {
                gd.setColor(UIUtils.getColor(R.color.black));
            }
        }

        //亮度
        brightnessBar.setProgress(mLampData.getBrightness());
        //灯效
        boolean canAdjustedAffection = mLampData.isCanAdjustedAffection();
        LampAffection affection = mLampData.getAffection();
        cb_sync_music_mode.setEnabled(canAdjustedAffection ? true : false);
        blink_mode.setEnabled(canAdjustedAffection ? true : false);
        candy_light_mode.setEnabled(canAdjustedAffection ? true : false);
        default_mode.setEnabled(canAdjustedAffection ? true : false);
        if (canAdjustedAffection && mLampData.getAffection() != null) {
            cb_sync_music_mode.setChecked(affection == SyncMusic);
            blink_mode.setChecked(affection == Blink);
            candy_light_mode.setChecked(affection == Candy);
            default_mode.setChecked(affection == Default);
        }

        //是否可编辑
        //灯色
        //亮度
        setPanelState(mLampData.isCanAdjustedComposedColor());

    }

    /**
     * 变化色是否可点击
     *
     * @param canAdjustedComposedColor
     */
    private void setPanelState(boolean canAdjustedComposedColor) {
        panel2.setEnabled(canAdjustedComposedColor ? true : false);
        panel3.setEnabled(canAdjustedComposedColor ? true : false);
        panel4.setEnabled(canAdjustedComposedColor ? true : false);
        panel5.setEnabled(canAdjustedComposedColor ? true : false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showLoading();
    }

    private boolean flag = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
//                mContext.performClick();//back::showContent
//                group.setEnabled(flag ? true : false);
                flag = !flag;
                break;
            case R.id.tv_header_title:
                showPop();
                break;
            case R.id.panel_2:
                cancelPanelColor(v, 0);
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
            default:
                break;
        }
    }

    /**
     * 将色值填充到色值面板上
     *
     * @param v
     * @param pos
     */
    private void cancelPanelColor(View v, int pos) {
        if(mCompundColors[pos] == 0){
            ToastUtils.showShort(mContext, "没有填充色值");
        }else{
            cancelColorPos.add(pos);
            ((GradientDrawable) v.getBackground()).setColor(UIUtils.getColor(R.color.black));
            mCompundColors[pos] = 0;
        }
    }

    /**
     * 选择模式
     */
    private void showPop() {
        if (modPop == null) {
            modPop = new PopupWindow(getModePopView(),
                    200,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            modPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//or can not dismiss when touch outside
            modPop.setFocusable(true);
            modPop.setOutsideTouchable(true);
        }
        if (!modPop.isShowing()) {
            int[] location = new int[2];
            tv_header.getLocationOnScreen(location);
            modPop.showAsDropDown(tv_header,
//                    location[0]-(modPop.getWidth() - tv_header.getWidth())/2,
                    0,
                    0);
        } else {
            modPop.dismiss();
        }
    }

    private View getModePopView() {
        ListView lv = new ListView(mContext);
        modeAdapter = new CommonAdapter<SceneItem>(mContext, configScene()) {
            @Override
            protected BaseHolder getHolder(Context context) {
                return new SceneHolder(mContext);
            }
        };
        lv.setAdapter(modeAdapter);
        lv.setDividerHeight(2);
        lv.setOnItemClickListener(this);
        lv.setBackgroundResource(R.drawable.pop_bg);
        return lv;
    }

    /**
     * 配置场景
     *
     * @return
     */
    private List<SceneItem> configScene() {
        if (items == null)
            items = new ArrayList<SceneItem>();
        else
            items.clear();
        Map<LampMode, LampBean> scenes = ConfigData.lamps;
        int index = 0;
        for (LampMode mode : scenes.keySet()) {
            SceneItem item = new SceneItem();
            if (mode == mMode) {
                lastModePos = index;
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
            item.setMode(mode);
            items.add(index, item);
            index++;
        }
        return items;
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
        //不可以编辑灯色
        if (!mLampData.isCanAdjustedComposedColor()) {
            ToastUtils.showShort(mContext, "不可编辑灯色");
            return;
        }

        if (!cancelColorPos.isEmpty()) {
            int mPos = cancelColorPos.first();
            fillPanel(mPos, color);
            cancelColorPos.remove(mPos);
            return;
        } else {
            if (mCompundColorPosition >= compoundColorSize) {
                ToastUtils.showShort(mContext, "灯色面板已经填充满");
                return;
            }
        }
        fillPanel(mCompundColorPosition, color);
        mCompundColorPosition++;
    }

    /**
     * 填充填色板
     * @param color
     */
    private void fillPanel(int pos,int color){
        mCompundColors[pos] = color;
        String tag = String.format("panel_%d", pos + 2);
        GradientDrawable gd = (GradientDrawable) getView().findViewWithTag(tag).getBackground();
        gd.setColor(color);
    }

    /**
     * 检查变换色列表是否有空的
     *
     * @return
     */
    private boolean checkCompoundColorPanel() {
        for (int i = 0; i < compoundColorSize; i++) {
            if (mCompundColors[i] == 0) {
                return true;
            }
        }
        return false;
    }

    /*  OnItemClickListener  */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showPop();
        if (lastModePos != position) {
            SceneItem item = items.get(position);
            changeMode(true, item);
        }
    }

    private void changeMode(boolean isChecked, SceneItem data) {
        if (!isChecked) return;

        ConfigData.curLampMode = data.getMode();
        ConfigData.curLamp = ConfigData.lamps.get(ConfigData.curLampMode);
        lampBeanInvalidate();
        configScene();
        modeAdapter.notifyDataSetChanged();
    }

    /* OnCheckedChangeListener */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_checker:
                fl_cover.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                tv_header.setEnabled(!isChecked);
                break;
        }
    }
}
