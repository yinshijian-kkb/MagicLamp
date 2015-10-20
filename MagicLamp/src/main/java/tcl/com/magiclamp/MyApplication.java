package tcl.com.magiclamp;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import tcl.com.magiclamp.data.LampMode;
import tcl.com.magiclamp.utils.ConfigData;
import tcl.com.magiclamp.utils.CrashHandler;

/**
 * Created by sjyin on 9/21/15.
 */
public class MyApplication extends Application {

    private static Handler mMainThreadHandler;
    private static Thread mMainThread;
    private static int mMainThreadID;
    private static MyApplication mContext;
    private Looper mMianThreadLooper;

    public MyApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadID = android.os.Process.myTid();
        this.mMianThreadLooper = getMainLooper();

        initImageLoader();
        initLamp();
        collectCrash();
    }

    private void collectCrash() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    /**
     * 初始化ImageLoader 配置
     */
    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,
                "magic_lamp/.Picture");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // default
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)
                        //.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                        //.memoryCacheSize(2 * 1024 * 1024)
                        //.memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                        // .diskCacheSize(50 * 1024 * 1024)
                        // .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //
                        // default
                .imageDownloader(new BaseImageDownloader(mContext)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 初始化不同模式下的灯参数
     */
    private void initLamp() {
        ConfigData.lamps = ConfigData.init();
        ConfigData.curLampMode = LampMode.Normal;
        ConfigData.curLamp = ConfigData.lamps.get(ConfigData.curLampMode);
    }

    public static MyApplication getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadID() {
        return mMainThreadID;
    }

    public void ExitApp() {
        System.exit(0);
    }
}
