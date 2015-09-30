package tcl.com.magiclamp;

/**
 * Created by sjyin on 9/29/15.
 */
public interface OnModeCheckedListener<T> {
    void changeMode(boolean isChecked, T data);
}
