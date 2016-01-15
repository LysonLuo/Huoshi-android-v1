package im.huoshi;

import android.app.Application;
import android.content.Context;

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

    }

}
