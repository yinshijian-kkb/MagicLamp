package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 9/27/15.
 */
public class LampBean {

    /* 灯光颜色、亮度和灯效 */
    private int color;
    private int brightness;
    private int[] compoundColor;
    private LampAffection affection;

    /* 开始和结束时间、重复时间、背景音 */
    private String startTime;
    private String endTime;
    private String repeatDuration;
    private MusicBean music;

    private boolean canAdjustedColor;
    private boolean canAdjustedBrightness;
    private boolean canAdjustedComposedColor;
    private boolean canAdjustedAffection;
    private boolean canAdjustedStartTime;
    private boolean canAdjustedRepeatDuration;
    private boolean canAdjustedMusic;

    private LampMode scene;

    public LampBean() {

    }

    /**
     * @param canAdjustedMusic 音乐
     * @param canAdjustedColor 灯色
     * @param canAdjustedBrightness 亮度
     * @param canAdjustedComposedColor 组合色
     * @param canAdjustedAffection 灯效
     * @param canAdjustedStartTime 开始时间
     * @param canAdjustedRepeatDuration 重复日期
     */
    public LampBean(boolean canAdjustedMusic,
                    boolean canAdjustedColor,
                    boolean canAdjustedBrightness,
                    boolean canAdjustedComposedColor,
                    boolean canAdjustedAffection,
                    boolean canAdjustedStartTime,
                    boolean canAdjustedRepeatDuration) {
        this.canAdjustedMusic = canAdjustedMusic;
        this.canAdjustedColor = canAdjustedColor;
        this.canAdjustedBrightness = canAdjustedBrightness;
        this.canAdjustedComposedColor = canAdjustedComposedColor;
        this.canAdjustedAffection = canAdjustedAffection;
        this.canAdjustedStartTime = canAdjustedStartTime;
        this.canAdjustedRepeatDuration = canAdjustedRepeatDuration;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public LampAffection getAffection() {
        return affection;
    }

    public void setAffection(LampAffection affection) {
        this.affection = affection;
    }

    public int[] getCompoundColor() {
        return compoundColor;
    }

    public void setCompoundColor(int[] compoundColor) {
        this.compoundColor = compoundColor;
    }

    public MusicBean getMusic() {
        return music;
    }

    public void setMusic(MusicBean music) {
        this.music = music;
    }

    public LampMode getScene() {
        return scene;
    }

    public void setScene(LampMode scene) {
        this.scene = scene;
    }

    public boolean isCanAdjustedBrightness() {
        return canAdjustedBrightness;
    }

    public void setCanAdjustedBrightness(boolean canAdjustedBrightness) {
        this.canAdjustedBrightness = canAdjustedBrightness;
    }

    public boolean isCanAdjustedColor() {
        return canAdjustedColor;
    }

    public void setCanAdjustedColor(boolean canAdjustedColor) {
        this.canAdjustedColor = canAdjustedColor;
    }

    public boolean isCanAdjustedComposedColor() {
        return canAdjustedComposedColor;
    }

    public void setCanAdjustedComposedColor(boolean canAdjustedComposedColor) {
        this.canAdjustedComposedColor = canAdjustedComposedColor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeatDuration() {
        return repeatDuration;
    }

    public void setRepeatDuration(String repeatDuration) {
        this.repeatDuration = repeatDuration;
    }

    public boolean isCanAdjustedAffection() {
        return canAdjustedAffection;
    }

    public boolean isCanAdjustedStartTime() {
        return canAdjustedStartTime;
    }

    public boolean isCanAdjustedRepeatDuration() {
        return canAdjustedRepeatDuration;
    }

    public boolean isCanAdjustedMusic() {
        return canAdjustedMusic;
    }
}
