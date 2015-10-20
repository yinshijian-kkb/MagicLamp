package tcl.com.magiclamp.utils;

import java.util.HashMap;

import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampAffection;
import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.data.MusicBean;

/**
 * Created by sjyin on 9/27/15.
 */
public class ConfigData {

    public static int SceneRequestCode = 0x0001;
    /**
     * 当前灯的模式
     */
    public static LampMode curLampMode;

    /**
     * 当前模式下等的参数
     */
    public static LampBean curLamp;

    public static HashMap<LampMode, LampBean> lamps;

    public static HashMap<LampMode, LampBean> init() {
        if (lamps != null)
            return lamps;

        lamps = new HashMap<LampMode, LampBean>();
        LampBean normalLamp = new LampBean(
                true, true, true, true,
                true, true, true
        );//all true
        int[] composeColor = new int[5];
        composeColor[0] = 0xffff0000;
        composeColor[1] = 0xff0000ff;
        composeColor[2] = 0xffffff00;
        composeColor[3] = 0xffff0000;

        normalLamp.setColor(0xffffffff);
        normalLamp.setCompoundColor(composeColor);
        normalLamp.setAffection(LampAffection.Default);
        normalLamp.setBrightness(50);
        //for Test ,can delete
        MusicBean _bean = new MusicBean("怒放的生命","汪峰");
        _bean.play(true);
        normalLamp.setMusic(_bean);
        //
        lamps.put(LampMode.Normal, normalLamp);

        /*LampBean awakedLamp = new LampBean(true,
                false,false,false,false,
                true,true);//开始时间、重复时间、音乐*/
        LampBean awakedLamp = new LampBean();
        awakedLamp.setColor(0xffffffff);
        awakedLamp.setAffection(LampAffection.Default);
        awakedLamp.setBrightness(80);
        awakedLamp.setMusic(new MusicBean("鸟叫",null));
        lamps.put(LampMode.Awaked, awakedLamp);

        /*LampBean readingLamp = new LampBean(true,false,true,
                false,false,false,false);//亮度、音乐*/
        LampBean readingLamp = new LampBean();
        readingLamp.setColor(0xffffffff);
        readingLamp.setAffection(LampAffection.Default);
        readingLamp.setBrightness(100);
        lamps.put(LampMode.Reading, readingLamp);

        /*LampBean romanticLamp = new LampBean(true,true,
                false,true,
                true,false,false);//音乐、灯色、灯效*/
        LampBean romanticLamp = new LampBean();
        romanticLamp.setColor(0xff9932cd);
        romanticLamp.setAffection(LampAffection.Candy);
        romanticLamp.setMusic(new MusicBean("爵士音乐",null));
        romanticLamp.setBrightness(30);
        lamps.put(LampMode.Romantic, romanticLamp);

        /*LampBean asleepLamp = new LampBean(true,
                false,false,false,false,
                true,false);//开始时间、音乐*/
        LampBean asleepLamp = new LampBean();
        asleepLamp.setColor(0xffc0d9d9);
        asleepLamp.setAffection(LampAffection.Default);
        asleepLamp.setMusic(new MusicBean("催眠曲",null));
        asleepLamp.setBrightness(50);
        lamps.put(LampMode.Asleep, asleepLamp);

        return lamps;
    }
}
