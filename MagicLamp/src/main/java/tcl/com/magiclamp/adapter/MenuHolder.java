package tcl.com.magiclamp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.data.MenuItem;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * Created by sjyin on 9/27/15.
 */
public class MenuHolder extends BaseHolder<MenuItem> {
    private int[] uncheckedIcons = new int[]{
            R.drawable.light, R.drawable.menu_music, R.drawable.radio, R.drawable.scene, R.drawable.set_up
    };
    private int[] checkedIcons = new int[]{
            R.drawable.light_selected, R.drawable.menu_music_selected, R.drawable.radio_selected, R.drawable.scene_selected, R.drawable.set_up_selected
    };
    private TextView tv_mode;
    private ImageView iv_mode;

    public MenuHolder(Context context) {
        super(context);
    }

    @Override
    public void refreshView(MenuItem data) {
        if (data.isChecked){
            iv_mode.setImageResource(checkedIcons[data.getIndex()]);
        }else{
            iv_mode.setImageResource(uncheckedIcons[data.getIndex()]);
        }
        tv_mode.setText(UIUtils.getString(data.getTitle()));
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_mode, null);
        tv_mode = (TextView)view.findViewById(R.id.tv_mode);
        iv_mode = (ImageView)view.findViewById(R.id.iv_mode);
        return view;
    }
}
