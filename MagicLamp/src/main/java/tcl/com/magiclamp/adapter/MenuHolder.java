package tcl.com.magiclamp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.data.MenuItem;

/**
 * Created by sjyin on 9/27/15.
 */
public class MenuHolder extends BaseHolder<MenuItem> {
    private TextView tv_mode;

    public MenuHolder(Context context) {
        super(context);
    }

    @Override
    public void refreshView(MenuItem data) {
        tv_mode.setText(data.getTitle());
    }

    @Override
    public View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.item_mode, null);
        tv_mode = (TextView)view.findViewById(R.id.tv_mode);
        return view;
    }
}
