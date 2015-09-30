package tcl.com.magiclamp.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    private BaseHolder mHolder;

    public CommonAdapter(Context context, List<T> mDatas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null && convertView.getTag() instanceof BaseHolder) {
            mHolder = (BaseHolder) convertView.getTag();
        } else {
            mHolder = getHolder(mContext);//1
        }
        mHolder.setData(mDatas.get(position));//2
        return mHolder.getRootView();
    }

    protected abstract BaseHolder getHolder(Context context);

}
