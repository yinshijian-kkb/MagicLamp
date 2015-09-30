package tcl.com.magiclamp.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseHolder<T> {
    private View mRootView;

    public BaseHolder(Context context) {
        mRootView = initView(LayoutInflater.from(context));
        mRootView.setTag(this);
    }

    /**
     * 设置数据 并展示在holder上
     *
     * @param data
     */
    public void setData(T data) {
        refreshView(data);
    }

    public View getRootView() {
        return mRootView;
    }

    public abstract void refreshView(T data);

    /**
     * 保留！
     * 给View添加监听事件
     *
     * @param inflater
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /* NO USE */

    private int mposition = -1;

    /**
     * 记录条目位置  解决imageloader 图片错位问题
     *
     * @param data
     * @param position
     */
    public void setData(T data, int position) {
        mposition = position;
        setData(data);
    }

    /* NO USE */
}
