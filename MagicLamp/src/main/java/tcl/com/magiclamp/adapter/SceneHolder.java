package tcl.com.magiclamp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import tcl.com.magiclamp.OnModeCheckedListener;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.activities.SceneActivity;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.data.SceneItem;

/**
 * Created by sjyin on 9/27/15.
 */
public class SceneHolder extends BaseHolder<SceneItem> {

    private TextView tv_scene;
    private RadioButton ic_scene;
    private SceneItem mData;
    private Context mContext;

    public SceneHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void refreshView(SceneItem data) {
        this.mData = data;
        tv_scene.setText(data.getMode().toString());
        ic_scene.setChecked(data.isChecked());
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_scene, null);
        tv_scene = (TextView)view.findViewById(R.id.tv_scene);
        ic_scene = (RadioButton)view.findViewById(R.id.ic_scene);
        ic_scene.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ((OnModeCheckedListener)mContext).changeMode(isChecked, mData);
            }
        });
        return view;
    }

}
