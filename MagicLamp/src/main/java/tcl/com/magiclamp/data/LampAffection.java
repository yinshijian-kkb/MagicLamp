package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 9/27/15.
 */
public enum LampAffection {
    SyncMusic("音乐同步"),Blink("闪烁"),Candy("烛光"),Default("默认");

    private final String mName;

    LampAffection(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
