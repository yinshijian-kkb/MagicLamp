package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.adapter.base.BaseHolder;
import tcl.com.magiclamp.adapter.base.CommonAdapter;
import tcl.com.magiclamp.adapter.MenuHolder;
import tcl.com.magiclamp.data.MenuItem;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * Created by sjyin on 9/21/15.
 */
public class LeftMenuFragment extends Fragment {

    private MyActivity mContext;
    /**
     * 抽屉中默认被选中的条目，默认是0
     */
    private int mPrePos = 0;
    private List<MenuItem> mdata;
    private CommonAdapter<MenuItem> mAdapter;

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

        mdata = configMenu();
        mAdapter = new CommonAdapter<MenuItem>(mContext, mdata) {

            @Override
            protected BaseHolder getHolder(Context context) {
                return new MenuHolder(context);
            }
        };
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mContext.setOnMenuClick(parent, view, position, id);
            }
        });
    }

    /**
     * 修改菜单的选中状态
     *
     * @param position
     */
    public void updateMenuState(int position){
        if (mPrePos == position)    return;
        mdata.get(mPrePos).isChecked = false;
        mdata.get(position).isChecked = true;
        mPrePos = position;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 配置抽屉中的选项
     *
     * @return
     */
    public List<MenuItem> configMenu() {
        List<MenuItem> data = new ArrayList<MenuItem>();
        int[] names = new int[]{
                R.string.light, R.string.music, R.string.radio, R.string.scene, R.string.set_up
        };
        for (int i = 0; i < names.length; i++) {
            MenuItem menu = new MenuItem(i, names[i]);
            if (i == mPrePos){
                menu.isChecked = true;
            }
            data.add(menu);
        }
        return data;
    }
}
