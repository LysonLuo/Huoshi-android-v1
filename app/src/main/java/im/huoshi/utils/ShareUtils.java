package im.huoshi.utils;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import im.huoshi.R;
import im.huoshi.base.BaseActivity;

/**
 * 分享工具类
 * <p>
 * Created by Lyson on 16/3/16.
 */
public class ShareUtils {

    public static void init(final BaseActivity activity) {
        SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SMS, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
        new ShareAction(activity).setDisplayList(displaylist).setShareboardclickCallback(new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                share(activity, share_media);
            }
        }).open();
    }

    public static void share(final BaseActivity activity, SHARE_MEDIA share_media) {
        if (share_media.equals(SHARE_MEDIA.SMS)) {
            smsShare(activity);
        } else {
            new ShareAction(activity).setPlatform(share_media)
                    .withText("籍着你的分享，更多的弟兄姊妹有机会拥抱移动互联网下的信仰生活。")
                    .withTitle("我来自活石app")
                    .withTargetUrl("http://www.baidu.com")
                    .withMedia(new UMImage(activity, R.mipmap.icon_logo))
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            activity.showShortToast(share_media.name() + "分享成功");
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

    public static void smsShare(final BaseActivity activity) {
        new ShareAction(activity).setPlatform(SHARE_MEDIA.SMS)
                .withText("哈喽，忍不住想给你推荐个基督生活类的APP【活石】，这里下载：http://www.huoshi.im；我已经在用了，淡定绝对不是病毒，你赶紧试试。")
                .setCallback(new UMShareListener() {
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
//                        activity.showShortToast(share_media.name() + "分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                        activity.showShortToast(share_media.name() + "分享出错");
//                        LogUtils.d("huoshi_umeng_share", throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
//                        activity.showShortToast(share_media.name() + "分享取消");
                    }
                }).share();
    }
}
