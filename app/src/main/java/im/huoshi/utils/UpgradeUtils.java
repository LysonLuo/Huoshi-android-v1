package im.huoshi.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import im.huoshi.BuildConfig;
import im.huoshi.base.BaseActivity;
import im.huoshi.model.AppVersion;

public class UpgradeUtils {


    public static void showAppUpgradeDialog(final AppVersion version, final BaseActivity activity) {
        if (version == null) {
            return;
        }
        final int newestVersion = Integer.valueOf(version.getLatestVersion().replaceAll("[^0-9]", ""));
        int appVersion = Integer.valueOf(BuildConfig.VERSION_NAME.replaceAll("[^0-9]", ""));
        if (newestVersion - appVersion <= 0) {
            return;
        }

        int lastCancelVersion = activity.mLocalUser.getAppUpdateCancelVersion();
        long lastCancelTime = activity.mLocalUser.getAppUpdateCancelTime();

        if (lastCancelVersion != 0 && lastCancelTime != 0) {
            int calcleDayBetween = DateUtils.getDayBetween(lastCancelTime);
            if (newestVersion == lastCancelVersion && calcleDayBetween <= 0) {
                return;
            }
        }

        StringBuffer desc = new StringBuffer();
        if (!TextUtils.isEmpty(version.getDescription()) && !TextUtils.isEmpty(version.getDescription())) {
            desc.append("版本号：" + version.getLatestVersion() + "\n" + version.getDescription() + "\n\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("有新版本升级");
        builder.setMessage(desc.toString());
        builder.setPositiveButton("马上升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openBrowser(activity, version.getDownloadUrl());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.mLocalUser.setAppUpdateCancelConfig(newestVersion);
                dialog.dismiss();
            }
        });
        builder.setCancelable(false).create().show();
    }

    /**
     * 调用浏览器
     *
     * @param url
     */
    public static void openBrowser(BaseActivity activity, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }
}
