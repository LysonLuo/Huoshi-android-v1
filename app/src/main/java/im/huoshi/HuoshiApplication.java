package im.huoshi;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import im.huoshi.common.Constants;

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
        copyDataBase();
        startBugtags();
    }

    private void startBugtags() {
        BugtagsOptions bugtagsOptions = new BugtagsOptions.Builder().trackingLocation(false).build();
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug")) {
            Bugtags.start("8634c2abf3eb2f584688b083fdbb5b98", this, Bugtags.BTGInvocationEventBubble, bugtagsOptions);
        } else {
            Bugtags.start("8634c2abf3eb2f584688b083fdbb5b98", this, Bugtags.BTGInvocationEventNone, bugtagsOptions);
        }
    }

    private void initAll() {
        //微信 appid appsecret
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    private void copyDataBase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            try {
                File dir = new File(Constants.DATA_BASE_DIR);
                dir.mkdirs();
                InputStream inputStream = getAssets().open(Constants.DATA_BASE_NAME);
                String outPutFileName = Constants.DATA_BASE_DIR + Constants.DATA_BASE_NAME;
                OutputStream fileOutputStream = new FileOutputStream(outPutFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDatabase() {
        String dbPath = Constants.DATA_BASE_DIR + Constants.DATA_BASE_NAME;
        File dbFile = new File(dbPath);
        return dbFile.exists();
    }
}
