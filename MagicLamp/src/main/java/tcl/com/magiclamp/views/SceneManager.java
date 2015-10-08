package tcl.com.magiclamp.views;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.SceneHolder;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.data.SceneItem;
import tcl.com.magiclamp.utils.ConfigData;

/**
 * 场景管理器
 *
 * Created by sjyin on 10/8/15.
 */
public class SceneManager {

    private Context mContext;
    private LampMode mMode;
    /**
     * 默认情况下是默认模式
     */
    private int mLastModePos = 3;
    private ArrayList<SceneItem> items;
    private ListView lv;
    private CommonAdapter<SceneItem> modeAdapter;
    private List<SceneItem> mSceneItems;

    public SceneManager(Context pContext){
        mContext = pContext;
        mMode = ConfigData.curLampMode;
        initSceneView();
    }

    public int getLastModePos(){
        return mLastModePos;
    }

    public void setLastModePos(int pLastModePos){
        mLastModePos = pLastModePos;
    }

    public View getSceneView(){
        return lv;
    }

    public BaseAdapter getAdapter() {
        return modeAdapter;
    }

    public List<SceneItem> getData(){
        return mSceneItems;
    }

    public void setOnItemClickListener(OnItemClickListener pListener){
        if (lv != null)
            lv.setOnItemClickListener(pListener);
    }

    private View initSceneView() {
        lv = new ListView(mContext);
        mSceneItems = configScene();
        modeAdapter = new CommonAdapter<SceneItem>(mContext, mSceneItems) {
            @Override
            protected BaseHolder getHolder(Context context) {
                return new SceneHolder(mContext);
            }
        };
        lv.setAdapter(modeAdapter);
        lv.setDividerHeight(2);
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

}
