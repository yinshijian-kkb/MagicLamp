package tcl.com.magiclamp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tcl.com.magiclamp.OnModeCheckedListener;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.adapter.SceneHolder;
import tcl.com.magiclamp.data.SceneItem;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.ToastUtils;

/**
 * Created by sjyin on 9/26/15.
 */
public class SceneActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener,OnModeCheckedListener<SceneItem> {
    private ListView lv;
    private ArrayList<SceneItem> items;
    private LampMode tempCurMode;
    private CommonAdapter<SceneItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        initView();
    }

    private void initView() {
        findViewById(R.id.cb_checker).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_header_title)).setText("场景");
        findViewById(R.id.iv_header_back).setOnClickListener(this);
        lv = (ListView)findViewById(R.id.lv);
        mAdapter = new CommonAdapter<SceneItem>(this, configScene()) {
            @Override
            protected BaseHolder getHolder(Context context) {
                return new SceneHolder(SceneActivity.this);
            }
        };
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(this);

        tempCurMode = ConfigData.curLampMode;
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
        for (LampMode mode : scenes.keySet()) {
            SceneItem item = new SceneItem();
            if (mode == tempCurMode) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
            item.setMode(mode);
            items.add(item);
        }
        return items;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back://退出当前页 保存场景模式
                if (tempCurMode != null && ConfigData.curLampMode!=tempCurMode)
                    ConfigData.curLampMode = tempCurMode;
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SceneItem item = items.get(position);
        ToastUtils.showShort(this, "设置" + item.getMode().toString());

        /*LampMode curMode = item.getMode();
        tempCurMode = null;
        if (LampMode.Normal.equals(curMode)) {
            tempCurMode = LampMode.Normal;
        } else if (LampMode.Awaked.equals(curMode)) {
            tempCurMode = LampMode.Awaked;
        } else if (LampMode.Reading.equals(curMode)) {
            tempCurMode = LampMode.Reading;
        } else if (LampMode.Romantic.equals(curMode)) {
            tempCurMode = LampMode.Romantic;
        } else if (LampMode.Asleep.equals(curMode)) {
            tempCurMode = LampMode.Asleep;
        }*/

        /*Intent i = null;
        if (i != null)
            startActivityForResult(i, ConfigData.SceneRequestCode);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConfigData.SceneRequestCode) {

        }
    }

    /**
     * 切换场景模式
     *
     * @param isChecked
     * @param data
     */
    public void changeMode(boolean isChecked, SceneItem data) {
        if (isChecked){
            if (data.getMode() != tempCurMode){
                tempCurMode = data.getMode();
                configScene();
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
