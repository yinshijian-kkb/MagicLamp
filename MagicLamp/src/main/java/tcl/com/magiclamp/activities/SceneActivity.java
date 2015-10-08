package tcl.com.magiclamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.MyApplication;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.data.SceneItem;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.views.SceneManager;

/**
 * Created by sjyin on 9/26/15.
 */
public class SceneActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SceneManager sm;

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
        View _optionView = findViewById(R.id.iv_header_option);
        _optionView.setVisibility(View.VISIBLE);
        _optionView.setOnClickListener(this);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        sm = new SceneManager(this);
        sm.setOnItemClickListener(this);
        container.addView(sm.getSceneView());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateGlobeData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_option:
            case R.id.iv_header_back://退出当前页 保存场景模式
                setResult(MyActivity.SceneFinishResultCode);
                updateGlobeData();
                finish();
                break;
        }
    }

    /**
     * 更新全局的模式
     */
    private void updateGlobeData(){
        LampMode _curMode = sm.getData().get(sm.getLastModePos()).getMode();
        if (ConfigData.curLampMode != _curMode) {
            ConfigData.curLampMode = _curMode;
            ConfigData.curLamp = ConfigData.lamps.get(ConfigData.curLampMode);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SceneItem item = sm.getData().get(position);
        ToastUtils.showShort(this, "设置" + item.getMode().toString());

        if (sm.getLastModePos() != position) {
            sm.getData().get(position).setChecked(true);
            sm.getData().get(sm.getLastModePos()).setChecked(false);
            sm.getAdapter().notifyDataSetChanged();
            sm.setLastModePos(position);
        }
    }
}
