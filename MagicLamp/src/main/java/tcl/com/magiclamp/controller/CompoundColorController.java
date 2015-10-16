package tcl.com.magiclamp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import tcl.com.magiclamp.R;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.fragment.MainFragment;
import tcl.com.magiclamp.utils.ToastUtils;

/**
 * 变化色管理器
 *
 * Created by sjyin on 10/15/15.
 */
public class CompoundColorController implements View.OnClickListener ,Serializable {

    private MainFragment mFragment;
    private Context mContext;
    private View mView;
    /**
     * 变化色
     */
    private ArrayList<View> viewSet;
    /**
     * 灯光变化总共可选的个数
     */
    private int compoundColorSize;
    /**
     * 变化色的初始大小和选中后大小
     */
    private int colorBgOldSize, colorCheckedBgSize;

    private HashMap<LampMode, CompoundLampColor> lampCompoundColorMap;
    private Button panelConfirm;
    private CompoundLampColor mCompoundLampColor;

    public CompoundColorController(Context context, MainFragment fragment) {
        this.mContext = context;
        this.mFragment = fragment;
        mView = LayoutInflater.from(context).inflate(R.layout.layout_panel, null);
        initView();
    }

    private void initView() {
        viewSet = new ArrayList<View>();
        lampCompoundColorMap = new HashMap<LampMode, CompoundLampColor>();
        View panel_2 = mView.findViewById(R.id.panel_2);
        View panel_3 = mView.findViewById(R.id.panel_3);
        View panel_4 = mView.findViewById(R.id.panel_4);
        View panel_5 = mView.findViewById(R.id.panel_5);
        View panel_6 = mView.findViewById(R.id.panel_6);
        panelConfirm = (Button) mView.findViewById(R.id.btn_confirm);
        panelConfirm.setOnClickListener(this);
        panel_2.setOnClickListener(this);
        panel_3.setOnClickListener(this);
        panel_4.setOnClickListener(this);
        panel_5.setOnClickListener(this);
        panel_6.setOnClickListener(this);
        viewSet.add(panel_2);
        viewSet.add(panel_3);
        viewSet.add(panel_4);
        viewSet.add(panel_5);
        viewSet.add(panel_6);
        compoundColorSize = viewSet.size();
    }

    @Override
    public void onClick(View v) {
        int _pos = 0;
        switch (v.getId()) {
            case R.id.panel_2://编辑变化色
                mFragment.checkSingleColor(false);
                if (isExpanded()) {
                    cancelPanelColor(0);
                }else{
                    mFragment.lightLampColor(false);
                }
                break;
            case R.id.panel_3:
                _pos = 1;
                break;
            case R.id.panel_4:
                _pos = 2;
                break;
            case R.id.panel_5:
                _pos = 3;
                break;
            case R.id.panel_6:
                _pos = 4;
                break;
            case R.id.btn_confirm:
                ToastUtils.showShort(mContext, "同步变化色");
                expandCompoundColor(false);
                break;

        }
        if (_pos != 0)
            cancelPanelColor(_pos);
    }

    /**
     * 通过模式获取变化色
     * @param mode
     * @return
     */
    private CompoundLampColor getCompoundColor(LampMode mode) {
        if (lampCompoundColorMap.get(mode) == null) {
            lampCompoundColorMap.put(mode, new CompoundLampColor(viewSet));
        }
        return lampCompoundColorMap.get(mode);
    }

    /**
     * 重置变化色
     * @param lampMode
     * @param lampBean
     */
    public void resetCompoundColor(LampMode lampMode, LampBean lampBean) {
        mCompoundLampColor = getCompoundColor(lampMode);
        ArrayList<SingleColor> _LampColor = mCompoundLampColor.getCompoundColor();
        TreeSet<Integer> _canceledPosSet = mCompoundLampColor.getCancelColorPosSet();
        mCompoundLampColor.setExpanded(true);
        for (int i = 0; i < compoundColorSize; i++) {
            SingleColor _colorHolder = _LampColor.get(i);
            if (lampBean.getCompoundColor() != null && lampBean.getCompoundColor()[i] != 0) {
                _colorHolder.setColor(lampBean.getCompoundColor()[i]);
            } else {
                _canceledPosSet.add(i);
                _colorHolder.setColor(SingleColor.COLOR_EMPTY);
            }
        }
    }

    /**
     * 是否设置为不可点击的背景色
     * @param enable
     */
    public void setColorEnable(boolean enable) {
        for (int i = 0; i < compoundColorSize; i++) {
            SingleColor _colorHolder = mCompoundLampColor.getCompoundColor().get(i);
            if (enable) {
                _colorHolder.setColor(_colorHolder.getColor());
            } else {
                _colorHolder.setState(SingleColor.LampColorState.STATE_DISABLE);
            }
        }
    }

    /**
     * 是否为不可点击状态
     * @param enable
     */
    public void setEnable(boolean enable) {
        panelConfirm.setEnabled(enable ? true : false);
        for (int i = 0; i < compoundColorSize; i++) {
            SingleColor _compoundColor = mCompoundLampColor.getCompoundColor().get(i);
            _compoundColor.getTargetView().setEnabled(enable ? true : false);
        }
    }

    /**
     * 获取变化色的View
     * @return
     */
    public View getView() {
        return mView;
    }

    /**
     * 当前变化色是否为展开状态
     * @return
     */
    public boolean isExpanded(){
        return mCompoundLampColor.isExpanded();
    }

    /**
     * 设置变化色的背景
     * @param resId
     */
    public void setBackgroundResource(int resId) {
        mView.setBackgroundResource(resId);
    }

    /**
     * 取消填充板上的色值
     *
     * @param pos
     */
    private void cancelPanelColor(int pos) {
        SingleColor _curPanel = mCompoundLampColor.getCompoundColor().get(pos);
        if (_curPanel.getColor() == SingleColor.COLOR_EMPTY) {
            //无法取消填色值，因为没有填充色值
            ToastUtils.showShort(mContext, "没有填充色值");
        } else {
            mCompoundLampColor.getCancelColorPosSet().add(pos);
            _curPanel.setColor(SingleColor.COLOR_EMPTY);
        }
    }

    /**
     * 是否展开变化色
     *
     * @param isExpanded
     */
    public void expandCompoundColor(boolean isExpanded) {
        mCompoundLampColor.setExpanded(isExpanded);
        panelConfirm.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        for (int i = 0; i < compoundColorSize; i++) {
            SingleColor _compoundColor = mCompoundLampColor.getCompoundColor().get(i);
            if (i == 0) continue;
            _compoundColor.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 填充填色板
     */
    public void fillCompoundColorPanel(int color) {
        if (!mCompoundLampColor.getCancelColorPosSet().isEmpty()) {
            int _pos = mCompoundLampColor.getCancelColorPosSet().first();
            fillCompoundColorPanel(_pos, color);
            mCompoundLampColor.getCancelColorPosSet().remove(_pos);
        } else {
            ToastUtils.showShort(mContext, "灯色面板已经填充满");
        }
    }

    /**
     * 填充填色板
     */
    private void fillCompoundColorPanel(int pos, int color) {
        SingleColor _compoundColor = mCompoundLampColor.getCompoundColor().get(pos);
        if (_compoundColor.getColor() == SingleColor.COLOR_EMPTY) {
            mCompoundLampColor.getCompoundColor().get(pos).setColor(color);
        }
    }

    /**
     * 选中变化色
     */
    /*private void checkedCompoundColor(int pos) {
        if (mLastPos == pos) return;

        //measure compound color bg size
        if (colorBgOldSize == 0 || colorCheckedBgSize == 0) {
            colorBgOldSize = LampColorController.getCompoundColorBgSize();
            colorCheckedBgSize = LampColorController.getCheckedCompoundColorBgSize();
        }

        LampColorController _compoundColor = mCurLampColor.get(pos);
        _compoundColor.setSize(colorCheckedBgSize, colorCheckedBgSize);
        LampColorController _lastCompoundColor = mCurLampColor.get(mLastPos);
        if (_lastCompoundColor != null)
            _lastCompoundColor.setSize(colorCheckedBgSize, colorCheckedBgSize);
        mLastPos = pos;
    }*/

}
