package tcl.com.magiclamp.utils;

import java.util.HashMap;

import tcl.com.magiclamp.data.LampBean;
import tcl.com.magiclamp.data.LampAffection;
import tcl.com.magiclamp.data.LampMode;

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

    public static HashMap<LampMode,LampBean> lamps;
    private static HashMap<LampMode, LampBean> map;

    public static HashMap<LampMode,LampBean> init(){
        if (map != null)
            return lamps;

        map = new HashMap<LampMode, LampBean>();
        LampBean normalLamp = new LampBean();//all false
        int[] composeColor = new int[5];
        composeColor[0] = 0xffff0000;
        composeColor[1] = 0xffffffff;
        composeColor[2] = 0xffffff00;
//        composeColor[3] = 0xff0000ff;
        composeColor[3] = 0xffff0000;

        normalLamp.setCompoundColor(composeColor);
        normalLamp.setAffection(LampAffection.Default);
        normalLamp.setBrightness(50);
        map.put(LampMode.Normal, normalLamp);

        LampBean awakedLamp = new LampBean(true,
                false,false,false,false,
                true,true);//开始时间、重复时间、音乐
        awakedLamp.setColor(0xffffffff);
        awakedLamp.setAffection(LampAffection.Default);
        awakedLamp.setBrightness(80);
        awakedLamp.setMusic("鸟叫");
        map.put(LampMode.Awaked, awakedLamp);

        LampBean readingLamp = new LampBean(true,false,true,
                false,false,false,false);//亮度、音乐
        readingLamp.setColor(0xffffffff);
        readingLamp.setAffection(LampAffection.Default);
        readingLamp.setBrightness(100);
        map.put(LampMode.Reading, readingLamp);

        LampBean romanticLamp = new LampBean(true,true,
                false,true,
                true,false,false);//音乐、灯色、灯效
        romanticLamp.setColor(0xff9932cd);
        romanticLamp.setAffection(LampAffection.Candy);
        romanticLamp.setMusic("爵士音乐");
        romanticLamp.setBrightness(30);
        map.put(LampMode.Romantic, romanticLamp);

        LampBean asleepLamp = new LampBean(true,
                false,false,false,false,
                true,false);//开始时间、音乐
        asleepLamp.setColor(0xffc0d9d9);
        asleepLamp.setAffection(LampAffection.Default);
        asleepLamp.setMusic("催眠曲");
        asleepLamp.setBrightness(50);
        map.put(LampMode.Asleep, asleepLamp);

        return map;
    }
}
