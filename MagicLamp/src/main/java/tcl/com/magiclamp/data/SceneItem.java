package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 9/27/15.
 */
public class SceneItem {
    private boolean checked;
    private LampMode mode;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public LampMode getMode() {
        return mode;
    }

    public void setMode(LampMode mode) {
        this.mode = mode;
    }
}
