package tcl.com.magiclamp.data;

/**
 * <item>普通模式</item>
 * <item>唤醒模式</item>
 * <item>阅读模式</item>
 * <item>浪漫模式</item>
 * <item>睡眠模式</item>
 * Created by sjyin on 9/27/15.
 */
public enum LampMode {

    Normal("普通模式"),
    Awaked("唤醒模式"),
    Reading("阅读模式"),
    Romantic("浪漫模式"),
    Asleep("睡眠模式");

    private final String mName;

    LampMode(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return mName;
    }
}
