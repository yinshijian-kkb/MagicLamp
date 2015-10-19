package tcl.com.magiclamp.data;

/**
 * Created by sjyin on 10/19/15.
 */
public class MusicBean {

    private boolean isPlaying;
    private boolean isHided;
    private String song;
    private String singer;

    public MusicBean(String song, String singer) {
        this.song = song;
        this.singer = singer;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getSong() {
        return song;
    }

    public String getSinger() {
        return singer;
    }

    public boolean isHided() {
        return isHided;
    }

    public void hide(boolean hided) {
        this.isHided = hided;
    }
}
