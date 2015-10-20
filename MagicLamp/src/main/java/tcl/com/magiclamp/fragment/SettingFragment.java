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

import java.util.Arrays;
import java.util.List;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * 设置Fragment
 * Created by sjyin on 10/12/15.
 */
public class SettingFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private MyActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.iv_header_back).setOnClickListener(this);
        view.findViewById(R.id.rl_header).setBackgroundColor(UIUtils.getColor(R.color.c_1f263f));
        view.findViewById(R.id.cb_checker).setVisibility(View.GONE);
        TextView tv_header_title = ((TextView) view.findViewById(R.id.tv_header_title));
        tv_header_title.setTextColor(Color.WHITE);
        tv_header_title.setText("设置");
        View _optionView = view.findViewById(R.id.iv_header_option);
        _optionView.setVisibility(View.GONE);

        FrameLayout container = (FrameLayout) view.findViewById(R.id.container);
        ListView _lv = new ListView(mContext);
        _lv.setSelector(R.color.white);
        _lv.setCacheColorHint(UIUtils.getColor(R.color.white));
        _lv.setDividerHeight(1);
        _lv.setDivider(UIUtils.getDrawable(R.drawable.gray_divider));
        _lv.setAdapter(new CommonAdapter<String>(mContext, configSetting()) {
            @Override
            protected BaseHolder getHolder(Context context) {
                return new SettingHolder(mContext);
            }
        });
        _lv.setOnItemClickListener(this);
        container.addView(_lv);
    }

    /**
     * 配置场景
     *
     * @return
     */
    private List<String> configSetting() {
        return Arrays.asList(getResources().getStringArray(R.array.setting_items));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showShort(mContext,"开发中...");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
                mContext.performClickMenu();
                break;
        }
    }

    static class SettingHolder extends BaseHolder<String> {

        private TextView tv_scene;
        private ImageView ic_scene;

        public SettingHolder(Context context) {
            super(context);
        }

        @Override
        public void refreshView(String data) {
            tv_scene.setText(data);
            if ("检查更新".equals(data)) {
                ic_scene.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public View initView(LayoutInflater inflater) {
            View _view = inflater.inflate(R.layout.item_setting, null);
            tv_scene = (TextView) _view.findViewById(R.id.tv_scene);
            ic_scene = (ImageView) _view.findViewById(R.id.ic_scene);
            return _view;
        }
    }
}
