package im.huoshi;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by Lyson on 15/12/24.
 */
public class HuoshiApplication extends Application {
    private static Context mInstance;

    public static Context getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        initAll();
    }

    private void initAll() {
        initFresco();
    }

    private void initFresco() {

        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder()
                .setBaseDirectoryName("HuoshiV1")
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())
                .setMaxCacheSize(100 * 1024 * 1024)
                .build();
        ImagePipelineConfig Frescoconfig = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();
        Fresco.initialize(this, Frescoconfig);
    }
}
