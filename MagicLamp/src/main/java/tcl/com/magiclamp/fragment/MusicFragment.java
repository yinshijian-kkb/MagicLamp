package tcl.com.magiclamp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import tcl.com.magiclamp.MyActivity;
import tcl.com.magiclamp.R;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.ToastUtils;
import tcl.com.magiclamp.utils.UIUtils;

/**
 * Created by sjyin on 10/9/15.
 */
public class MusicFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private MyActivity mContext;
    private TextView tv_header, tv_singer;
    private SeekBar seek_bar;
    private Button btn_retreat, btn_forward;
    private CheckBox cb_play;
    private boolean mIsPlaying;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (MyActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init header
        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_header_back);
        ivBack.setImageResource(R.drawable.arrow_down_selector);
        ivBack.setOnClickListener(this);
        view.findViewById(R.id.cb_checker).setVisibility(View.GONE);
        view.findViewById(R.id.rl_header).setBackgroundColor(UIUtils.getColor(R.color.white));
        tv_header = (TextView) view.findViewById(R.id.tv_header_title);
        tv_singer = (TextView) view.findViewById(R.id.tv_singer);
        String _song = "怒放的生命";
        String _singer = "汪峰";
        //音乐
        if (ConfigData.curLamp.getMusic() != null && !TextUtils.isEmpty(ConfigData.curLamp.getMusic().getSong().toString().trim())) {
            _song = ConfigData.curLamp.getMusic().getSong().toString().trim();
        }
        tv_header.setText(_song);
        //歌手
        if (ConfigData.curLamp.getMusic() != null && !TextUtils.isEmpty(ConfigData.curLamp.getMusic().getSinger().toString().trim())) {
            _singer = ConfigData.curLamp.getMusic().getSinger().toString().trim();
        }
        tv_singer.setText(_singer);
        //init progress
        seek_bar = (SeekBar) view.findViewById(R.id.progress_seek_bar);
        seek_bar.setProgressDrawable(UIUtils.getDrawable(R.drawable.progress_drawable4volume));
        seek_bar.setOnSeekBarChangeListener(this);
        //init player
        btn_retreat = (Button) view.findViewById(R.id.btn_retreat);
        btn_forward = (Button) view.findViewById(R.id.btn_forward);
        cb_play = (CheckBox) view.findViewById(R.id.cb_play);
        btn_retreat.setOnClickListener(this);
        btn_forward.setOnClickListener(this);
        cb_play.setOnClickListener(this);
        //播放 true:play false:suspended
        mIsPlaying = ConfigData.curLamp.getMusic() == null ? false : ConfigData.curLamp.getMusic().isPlaying();
        cb_play.setChecked(mIsPlaying);
        checkBox(mIsPlaying);

        invalidate();
    }

    private void invalidate() {

    }

    /**
     * 设置CheckBox的选中状态
     *
     * @param checked
     */
    private void checkBox(boolean checked){
        cb_play.setButtonDrawable(checked ? R.drawable.music_main_play_normal : R.drawable.music_main_suspended_normal);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_back:
                mContext.performClickMenu();
                break;
            case R.id.btn_retreat:
                ToastUtils.showShort(mContext, "上一首");
                break;
            case R.id.btn_forward:
                ToastUtils.showShort(mContext, "下一首");
                break;
            case R.id.cb_play:
                mIsPlaying = !mIsPlaying;
                ToastUtils.showShort(mContext, mIsPlaying ? "播放" : "暂停");
                ConfigData.curLamp.getMusic().play(mIsPlaying);
                ConfigData.curLamp.getMusic().hide(!mIsPlaying);
                checkBox(mIsPlaying);
                break;
        }
    }

    /* update volume*/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ToastUtils.showShort(mContext, String.format("音量为:%d", seekBar.getProgress()));
    }

}
