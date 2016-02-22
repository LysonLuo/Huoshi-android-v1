package im.huoshi.utils;

import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

/**
 * Created by Lyson on 16/1/22.
 */
public class QiNiuUtils {

    public static void upLoadFile(String filePath, String uploadToken, UpCompletionHandler handler) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(filePath, null, uploadToken, handler, null);
    }
}
