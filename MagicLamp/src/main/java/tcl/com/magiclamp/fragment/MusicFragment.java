package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.utils.ViewHelper;

/**
 * Created by sjyin on 10/9/15.
 */
public class MusicFragment extends Fragment{

    private MyActivity mContext;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return ViewHelper.getTextWithTitle("音乐");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
