package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 9/23/15.
 */
public class MenuItem {
    private int title;
    private int index;
    public boolean isChecked;

    public MenuItem(int pIndex, int pTitle) {
        this.index = pIndex;
        this.title = pTitle;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
