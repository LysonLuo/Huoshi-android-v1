package im.huoshi.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;

import im.huoshi.R;
import im.huoshi.asynapi.callback.RestApiCallback;
import im.huoshi.asynapi.request.ShareRequest;
import im.huoshi.base.BaseActivity;
import im.huoshi.data.ReadPreference;
import im.huoshi.data.UserPreference;
import im.huoshi.model.Share;
import im.huoshi.model.event.ShareEvent;

/**
 * 分享工具类
 * <p>
 * Created by Lyson on 16/3/16.
 */
public class ShareUtils {

    public interface UmengShareListener {
        void onSuccess();
    }

    public static void init(final BaseActivity activity) {
       init(activity, null);
    }

    public static void init(final BaseActivity activity, final UmengShareListener listener) {
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
        new ShareAction(activity).setDisplayList(displaylist).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                share(activity, share_media, listener);
            }
        }).open();
    }

    public static void share(final BaseActivity activity, SHARE_MEDIA share_media, final UmengShareListener listener) {
        if (share_media.equals(SHARE_MEDIA.SMS)) {
            smsShare(activity);
        } else {
            new ShareAction(activity).setPlatform(share_media)
                    .withText("籍着你的分享，更多的弟兄姊妹有机会拥抱移动互联网下的信仰生活。")
                    .withTitle("我来自活石app")
                    .withTargetUrl("http://www.baidu.com")
                    .withMedia(new UMImage(activity, R.mipmap.ic_launcher))
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            activity.showShortToast(share_media.name() + "分享成功");
                            updateShareRecord(activity);
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                            activity.showShortToast(share_media.name() + "分享出错");
                            LogUtils.d("huoshi_umeng_share", throwable.getMessage());
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {
                            activity.showShortToast(share_media.name() + "分享取消");
                        }
                    }).share();
        }
    }

    public static void shareOnlyText(){

    }

    public static void updateShareRecord(BaseActivity activity) {
        ShareRequest.shareRecord(activity, UserPreference.getInstance().getUser().getUserId(), new RestApiCallback() {
            @Override
            public void onSuccess(String responseString) {
                Share share = new Gson().fromJson(responseString, new TypeToken<Share>() {
                }.getType());
                ReadPreference.getInstance().updateTotalShareTimes(share.getTotalShareTimes());
                EventBus.getDefault().post(new ShareEvent());
            }

            @Override
            public void onFailure() {
                LogUtils.d("shareutils", "failure");
            }
        });
    }

    public static void smsShare(final BaseActivity activity) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SMS)
                .withText("我发现一个主内工具类APP，不但可以读圣经看注释，最酷的是能手机上代祷，强烈推荐！我已经下载了，你快下载试试！地址：http://www.fir.im/huoshi;（放心，这不是病毒，哈哈哈）")
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }).share();
    }
}
