package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 9/23/15.
 */
public class MenuItem {
    private String iconUrl;
    private String title;

    public MenuItem(String title) {
        this.title = title;
    }

    public MenuItem(String iconUrl, String title) {
        this.iconUrl = iconUrl;
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
