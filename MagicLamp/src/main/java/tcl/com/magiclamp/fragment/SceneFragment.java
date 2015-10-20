package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.data.SceneItem;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * 场景Fragment
 * Created by sjyin on 10/12/15.
 */
public class SceneFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private MyActivity mContext;
    private int mLastModePos;
    private CommonAdapter<SceneItem> mAdapter;
    private List<SceneItem> mData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scene, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.iv_header_back).setOnClickListener(this);
        view.findViewById(R.id.rl_header).setBackgroundColor(UIUtils.getColor(R.color.c_1f263f));
        view.findViewById(R.id.cb_checker).setVisibility(View.GONE);
        TextView tv_header_title = ((TextView) view.findViewById(R.id.tv_header_title));
        tv_header_title.setTextColor(Color.WHITE);
        tv_header_title.setText("场景");
        View _optionView = view.findViewById(R.id.iv_header_option);
        _optionView.setVisibility(View.GONE);

        FrameLayout container = (FrameLayout) view.findViewById(R.id.container);
        ListView _lv = new ListView(mContext);
        _lv.setSelector(R.color.white);
        _lv.setCacheColorHint(UIUtils.getColor(R.color.white));
        _lv.setDividerHeight(1);
        _lv.setDivider(UIUtils.getDrawable(R.drawable.gray_divider));
        mData = configScene();
        mAdapter = new CommonAdapter<SceneItem>(mContext, mData) {
            @Override
            protected BaseHolder getHolder(Context context) {
                return new SceneHolder(mContext);
            }
        };
        _lv.setAdapter(mAdapter);
        _lv.setOnItemClickListener(this);
        container.addView(_lv);
    }

    /**
     * 配置场景
     *
     * @return
     */
    private List<SceneItem> configScene() {
        List<SceneItem> items = new ArrayList<SceneItem>();
        LampMode mMode = ConfigData.curLampMode;
        Map<LampMode, LampBean> scenes = ConfigData.lamps;
        int index = 0;
        for (LampMode mode : scenes.keySet()) {
            SceneItem item = new SceneItem();
            if (mode == mMode) {
                mLastModePos = index;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
                mContext.performClickMenu();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mLastModePos) return;
        SceneItem _oldItem = mData.get(mLastModePos);
        SceneItem _newItem = mData.get(position);
        _oldItem.setChecked(false);
        _newItem.setChecked(true);
        mAdapter.notifyDataSetChanged();
        mLastModePos = position;
    }

    static class SceneHolder extends BaseHolder<SceneItem> {

        private TextView tv_scene;
        private ImageView ic_scene;

        public SceneHolder(Context context) {
            super(context);
        }

        @Override
        public void refreshView(SceneItem data) {
            tv_scene.setText(data.getMode().toString());
            ic_scene.setImageDrawable(data.isChecked() ? UIUtils.getDrawable(R.drawable.switch_on) :
                    UIUtils.getDrawable(R.drawable.switch_off));
        }

        @Override
        public View initView(LayoutInflater inflater) {
            View _view = inflater.inflate(R.layout.item_scene4_menu_scene, null, true);
            tv_scene = (TextView) _view.findViewById(R.id.tv_scene);
            ic_scene = (ImageView) _view.findViewById(R.id.ic_scene);
            return _view;
        }
    }
}
