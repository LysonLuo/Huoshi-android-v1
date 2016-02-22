package im.huoshi.model;

/**
 * Created by Lyson on 16/1/27.
 */
public class Gender {
    public int key;
    public String value;

    public Gender(int key, String value) {
        this.key = key;
        this.value = value;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
    public String getPickerViewText() {
        //这里还可以判断文字超长截断再提供显示
        return value;
    }
}
