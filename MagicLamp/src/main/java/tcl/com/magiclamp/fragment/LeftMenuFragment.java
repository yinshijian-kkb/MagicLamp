package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.adapter.MenuHolder;
import tcl.com.magiclamp.data.MenuItem;

/**
 * Created by sjyin on 9/21/15.
 */
public class LeftMenuFragment extends Fragment {

    private MyActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_menu, null, true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv = (ListView) view.findViewById(R.id.lv_items);

        lv.setAdapter(new CommonAdapter<MenuItem>(mContext, configMenu()){

            @Override
            protected BaseHolder getHolder(Context context) {
                return new MenuHolder(context);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.setOnMenuClick(parent,view,position,id);
            }
        });

    }

    /**
     * 配置抽屉中的选项
     * @return
     */
    public List<MenuItem> configMenu(){
        List<MenuItem> data = new ArrayList<MenuItem>();
        String[] menus = mContext.getResources().getStringArray(R.array.arr_menu);
        for (int i =0;i < menus.length; i++){
            MenuItem menu = new MenuItem(menus[i]);
            data.add(menu);
        }
        return data;
    }
}
