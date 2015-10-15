package tcl.com.magiclamp.controller;

import android.view.View;

import java.util.ArrayList;
import java.util.TreeSet;

public class CompoundLampColor {
    /**
     * 变化色是否折叠
     */
    private boolean isExpanded;
    /**
     * 当前变化色
     */
    private ArrayList<SingleColor> mCompoundColor;
    /**
     * 当前变化色中选中取消的位置
     */
    private TreeSet<Integer> mCanceledColorPosSet;

    public CompoundLampColor(ArrayList<View> targetViews) {
        if (targetViews == null) return;
        mCanceledColorPosSet = new TreeSet<Integer>();
        mCompoundColor = new ArrayList<SingleColor>();
        for (View view : targetViews) {
            mCompoundColor.add(new SingleColor(view));
        }
    }

    public ArrayList<SingleColor> getCompoundColor() {
        return mCompoundColor;
    }

    public TreeSet<Integer> getCancelColorPosSet() {
        return mCanceledColorPosSet;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }
}