package im.huoshi.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;

/**
 * Created by ben on 14-8-14.
 * 高德地图
 */
public final class AmapUtils {

    public static final String LOCATION_CHANGED_ACTION = "us.bestapp.biketicket.location.changed";

    private static final String LogTag = AmapLoc.class.getCanonicalName();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
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
                BikeLog.d(LogTag, aMapLocation.toString());
                if (locBundle != null) {
                    BikeLog.d(LogTag, locBundle.getString("desc"));
                }

                mLoc.onLoc(aMapLocation.getCity(), geoLat, geoLng);
            } else {
                mLoc.onLocFailure();
            }
        }
    };

    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private GetLoc mLoc;

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

//        LocationManagerProxy mLocationManagerProxy = LocationManagerProxy.getInstance(context);
//
//        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
//        //在定位结束后，在合适的生命周期调用destroy()方法
//        //其中如果间隔时间为-1，则定位只定一次
//        mLocationManagerProxy.requestLocationData(
//                LocationProviderProxy.AMapNetwork, -1, 15,
//                new AMapLocationListener() {
//                    @Override
//                    public void onLocationChanged(AMapLocation aMapLocation) {
//                        Log.d(LogTag, String.format("error %d => %s", aMapLocation.getAMapException().getErrorCode(), aMapLocation.getAMapException().getErrorMessage()));
//
//                        if (aMapLocation.getAMapException().getErrorCode() == 0) {
//                            //获取位置信息
//                            Double geoLat = aMapLocation.getLatitude();
//                            Double geoLng = aMapLocation.getLongitude();
//                            Bundle locBundle = aMapLocation.getExtras();
//                            BikeLog.d(LogTag, aMapLocation.toString());
//                            if (locBundle != null) {
//                                BikeLog.d(LogTag, locBundle.getString("desc"));
//                            }
//
//                            loc.onLoc(aMapLocation.getCity(), geoLat, geoLng);
//                        } else {
//                            loc.onLocFailure();
//                        }
//
//                    }
//
//                    @Override
//                    public void onLocationChanged(Location location) {
//
//                    }
//
//                    @Override
//                    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                    }
//
//                    @Override
//                    public void onProviderEnabled(String provider) {
//
//                    }
//
//                    @Override
//                    public void onProviderDisabled(String provider) {
//
//                    }
//                });
//
//        mLocationManagerProxy.setGpsEnable(false);
    }

    public interface GetLoc {
        void onLoc(String cityName, double latitude, double longitude);

        void onLocFailure();
    }
}
