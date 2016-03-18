package im.huoshi.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;

/**
 * 控件注入工具类
 * <p>
 * Created by Lyson on 15/12/24.
 */
public class ViewUtils {
    public static void inject(Activity activity) {
        Field[] fields = activity.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                ViewInject _id = field.getAnnotation(ViewInject.class);
                if (null != _id) {
                    View view = activity.findViewById(_id.value());
                    if (null != view) {
                        field.setAccessible(true);
                        try {
                            field.set(activity, view);
                        } catch (IllegalAccessException e) {
                            LogUtils.d("ViewUtils", e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public static void inject(Dialog dialog) {
        Field[] fields = dialog.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                ViewInject _id = field.getAnnotation(ViewInject.class);
                if (null != _id) {
                    View view = dialog.findViewById(_id.value());
                    if (null != view) {
                        field.setAccessible(true);
                        try {
                            field.set(dialog, view);
                        } catch (IllegalAccessException e) {
                            LogUtils.d("ViewUtils", e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public static void inject(Object _class, View _view) {
        Field[] fields = _class.getClass().getDeclaredFields();
        if (null != fields && fields.length > 0) {
            for (Field field : fields) {
                ViewInject _id = field.getAnnotation(ViewInject.class);
                if (null != _id) {
                    View view = _view.findViewById(_id.value());
                    if (null != view) {
                        field.setAccessible(true);
                        try {
                            field.set(_class, view);
                        } catch (IllegalAccessException e) {
                            LogUtils.d("ViewUtils", e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
