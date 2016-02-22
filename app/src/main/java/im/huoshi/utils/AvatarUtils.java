package im.huoshi.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;

import java.io.File;

import im.huoshi.base.BaseActivity;

/**
 * Created by Lyson on 16/2/1.
 */
public class AvatarUtils {
    public static final int GALLERY_REQUEST_CODE = 0;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int ACTION_IMAGE_CROP = 2;

    public static Uri showEditAvatarDialog(final BaseActivity activity) {
        File newAvatarFile = createAvatarFile();
        final Uri uri = Uri.fromFile(newAvatarFile);
        new AlertDialog.Builder(activity).setTitle("修改头像").setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        intent.putExtra("return-data", true);
                        activity.startActivityForResult(Intent.createChooser(intent, "请选择"), CAMERA_REQUEST_CODE);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
                        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                        activity.startActivityForResult(Intent.createChooser(intent, "请选择"), GALLERY_REQUEST_CODE);
                        break;
                    }
                }
            }
        }).show();
        return uri;
    }

    public static File createAvatarFile() {
        File newAvatarDir = new File(Environment.getExternalStorageDirectory(), "/huoshi");
        if (!newAvatarDir.exists()) {
            newAvatarDir.mkdirs();
        }
        File newAvatarFile = new File(Environment.getExternalStorageDirectory(), "Avatar.png");
        return newAvatarFile;
    }

    public static Uri cropImage(Activity activity, Uri uri) {
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);// 输出图片大小
        intent.putExtra("outputY", 200);
        File newAvatarFile = createAvatarFile();
        uri = Uri.fromFile(newAvatarFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, ACTION_IMAGE_CROP);
        return uri;
    }
}
