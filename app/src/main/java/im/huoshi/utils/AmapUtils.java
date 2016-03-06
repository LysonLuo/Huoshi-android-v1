package im.huoshi.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public final class AmapUtils {

    private static final String LogTag = AmapUtils.class.getCanonicalName();
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private GetLoc mLoc;

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.d(LogTag, String.format("error %d => %s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo()));

            if (aMapLocation.getErrorCode() == 0) {
                //获取位置信息
                Double geoLat = aMapLocation.getLatitude();
                Double geoLng = aMapLocation.getLongitude();
                Bundle locBundle = aMapLocation.getExtras();
                LogUtils.d(LogTag, aMapLocation.toString());
                if (locBundle != null) {
                    LogUtils.d(LogTag, locBundle.getString("desc"));
                }
                mLoc.onLoc(aMapLocation.getProvince(), aMapLocation.getCity(), geoLat, geoLng);
            } else {
                mLoc.onLocFailure();
            }
        }
    };

    /**
     * 初始化定位
     */
    public AmapUtils(Context context, final GetLoc loc) {
        mLoc = loc;
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    public interface GetLoc {
        void onLoc(String provinceName, String cityName, double latitude, double longitude);

        void onLocFailure();
    }
}
