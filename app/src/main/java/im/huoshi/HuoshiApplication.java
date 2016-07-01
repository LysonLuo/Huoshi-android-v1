package im.huoshi;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.mcxiaoke.packer.helper.PackerNg;
import com.umeng.analytics.MobclickAgent;
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
            Bugtags.start(BuildConfig.BUGTAGS_ID, this, Bugtags.BTGInvocationEventBubble, bugtagsOptions);
        } else {
            Bugtags.start(BuildConfig.BUGTAGS_ID, this, Bugtags.BTGInvocationEventNone, bugtagsOptions);
        }
    }

    private void initAll() {
        //微信 appid appsecret
        PlatformConfig.setWeixin(BuildConfig.WECHAT_ID, BuildConfig.WECHAT_SECRET);
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo(BuildConfig.WEIBO_ID, BuildConfig.WEIBO_SECRET);
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone(BuildConfig.QQ_ID, BuildConfig.QQ_SECRET);
        initFeedBack();
        initChannels();
    }

    private void initFeedBack() {
        FeedbackAPI.initAnnoy(this, BuildConfig.FEED_BACK_KEY);
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

    private void initChannels() {
        String market = PackerNg.getMarket(this);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, "569ef0fc67e58e17180015bb", market);
        MobclickAgent.startWithConfigure(config);

    }
}
