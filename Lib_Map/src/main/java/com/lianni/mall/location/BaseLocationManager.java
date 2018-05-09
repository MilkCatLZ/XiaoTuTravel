package com.lianni.mall.location;


import android.content.Context;
import android.location.LocationManager;


//import android.content.Context;
//
//


////
////
////import android.app.Application;
////import android.content.Context;
////import android.location.LocationManager;
////import android.support.annotation.Nullable;
////
////import com.baidu.location.BDLocation;
////import com.baidu.location.BDLocationListener;
////import com.baidu.location.LocationClient;
////import com.baidu.location.LocationClientOption;
////import com.baidu.location.Poi;
////import com.base.util.Log;
////
////import java.util.List;
////
////
/////**
//// * Created by LZ on 2016/8/24.
//// * 单实例方式获取定位
//// * 1、在Application中调用{@link #initLocation(Application)}
//// * 2、{@link #getInstance()}获取实例
//// * 3、{@link #startGetLocation(OnReceiveLocationListener)}开始定位
//// * 4、{@link #stop(OnReceiveLocationListener)}停止定位
//// */
public final class BaseLocationManager {

//    private static LocationManager locationManager;
////
////    /**
////     * 当前位置
////     * 存储已经定位成功的位置，通常这个位置每次定位都会变
////     * 所以不要用这个数据绑定界面，用其他形式赋值给页面
////     */
////    private Location location = new Location();
////    private boolean hasLocation;
////
////    private Application application;
////    private OnReceiveLocationListener onReceiveLocationListener;
////    private BDLocationListener locationListener;
////    private LocationClient locationClient;
////
////
////    public interface OnReceiveLocationListener {
////
////        /**
////         * @param bdLocation 百度的定位详情
////         * @param location   当前位置的简单详情
////         */
////        void onReceiveLocation(BDLocation bdLocation, Location location);
////    }
////
////
////    private BDLocationManager(Application application) {
////        this.application = application;
////        locationListener = new BDLocationListener() {
////            @Override
////            public void onReceiveLocation(BDLocation bdLocation) {
////                //判断是否定位成功
////                if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation
////                    || bdLocation.getLocType() == BDLocation.TypeGpsLocation
////                    || bdLocation.getLocType() == BDLocation.TypeOffLineLocation
////                    ) {
////
////                    List<Poi> list1 = bdLocation.getPoiList();// POI数据
////                    String address = bdLocation.getCity() + bdLocation.getDistrict() + bdLocation.getStreet() + bdLocation.getStreetNumber();
////
////                    if (location == null) {
////                        location = new Location();
////                    }
////
////
////                    location.setLatitude(bdLocation.getLatitude());
////                    location.setLongitude(bdLocation.getLongitude());
////                    if (list1 != null && list1.size() > 0)
////                        location.setName(list1.get(0).getName());
////                    location.setAddress(address);
////                    location.setCity(bdLocation.getCity());
////
////                    if (BDLocationManager.this.onReceiveLocationListener != null) {
////                        BDLocationManager.this.onReceiveLocationListener.onReceiveLocation(bdLocation, location);
////                    }
////
////                    hasLocation = true;
////                }
////                Log.d("BDLocationManager", "Location has goted!!!" + "/longitude=" + bdLocation.getLongitude() + "/latitude=" + bdLocation.getLatitude());
////                stop(onReceiveLocationListener);
////                //region debug
//////            //Receive Location
//////            StringBuffer sb = new StringBuffer(256);
//////            sb.append("time : ");
//////            sb.append(bdLocationListener.getTime());
//////            sb.append("\nerror code : ");
//////            sb.append(bdLocationListener.getLocType());
//////            sb.append("\nlatitude : ");
//////            sb.append(bdLocationListener.getLatitude());
//////            sb.append("\nlontitude : ");
//////            sb.append(bdLocationListener.getLongitude());
//////            sb.append("\nradius : ");
//////            sb.append(bdLocationListener.getRadius());
//////            if (bdLocationListener.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//////                sb.append("\nspeed : ");
//////                sb.append(bdLocationListener.getSpeed());// 单位：公里每小时
//////                sb.append("\nsatellite : ");
//////                sb.append(bdLocationListener.getSatelliteNumber());
//////                sb.append("\nheight : ");
//////                sb.append(bdLocationListener.getAltitude());// 单位：米
//////                sb.append("\ndirection : ");
//////                sb.append(bdLocationListener.getDirection());// 单位度
//////                sb.append("\naddr : ");
//////                sb.append(bdLocationListener.getAddrStr());
//////                sb.append("\ndescribe : ");
//////                sb.append("gps定位成功");
//////
//////            } else if (bdLocationListener.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//////                sb.append("\naddr : ");
//////                sb.append(bdLocationListener.getAddrStr());
//////                //运营商信息
//////                sb.append("\noperationers : ");
//////                sb.append(bdLocationListener.getOperators());
//////                sb.append("\ndescribe : ");
//////                sb.append("网络定位成功");
//////            } else if (bdLocationListener.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//////                sb.append("\ndescribe : ");
//////                sb.append("离线定位成功，离线定位结果也是有效的");
//////            } else if (bdLocationListener.getLocType() == BDLocation.TypeServerError) {
//////                sb.append("\ndescribe : ");
//////                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//////            } else if (bdLocationListener.getLocType() == BDLocation.TypeNetWorkException) {
//////                sb.append("\ndescribe : ");
//////                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//////            } else if (bdLocationListener.getLocType() == BDLocation.TypeCriteriaException) {
//////                sb.append("\ndescribe : ");
//////                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//////            }
//////            sb.append("\nlocationdescribe : ");
//////            sb.append(bdLocationListener.getLocationDescribe());// 位置语义化信息
//////            List<Poi> list = bdLocationListener.getPoiList();// POI数据
//////            if (list != null) {
//////                sb.append("\npoilist size = : ");
//////                sb.append(list.size());
//////                for(Poi p : list) {
//////                    sb.append("\npoi= : ");
//////                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//////                }
//////            }
//////            Log.i("idebug", sb.toString());
////                //endregion
////            }
////
////            @Override
////            public void onConnectHotSpotMessage(String s, int i) {
////
////            }
////        };
////    }
////
////    public static BDLocationManager getInstance() {
////        return locationManager;
////    }
////
////    public static BDLocationManager initLocation(Application application) {
////        if (locationManager == null) {
////            locationManager = new BDLocationManager(application);
////        }
////        return locationManager;
////    }
////
////    /**
////     * 开始定位定位
////     *
////     * @param onReceiveLocationListener 不允许空，
////     */
////    public void startGetLocation(@Nullable OnReceiveLocationListener onReceiveLocationListener) {
////        this.onReceiveLocationListener = null;
////        this.onReceiveLocationListener = onReceiveLocationListener;
////        if (locationClient != null && locationClient.isStarted()) {
////            locationClient.stop();
////        }
////        /**
////         * 第一次或者已经成功获取过定位
////         * 再次定位时需要初始化，才能进行下一次定位
////         */
////        if (hasLocation() || locationClient == null) {
////            locationClient = new LocationClient(application);
////            initLocationClient();
////        }
////        locationClient.registerLocationListener(locationListener);
////        locationClient.start();
////    }
////
////    public void stop(OnReceiveLocationListener onReceiveLocationListener) {
////        this.onReceiveLocationListener = onReceiveLocationListener;
////        locationClient.stop();
////    }
////
////
////    private void initLocationClient() {
////
////
////        LocationClientOption option = new LocationClientOption();
////        option.setLocationMode(
////            LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
////        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
////        int span = 1000;
////        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
////        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
////        option.setOpenGps(true);//可选，默认false,设置是否使用gps
////        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
////        option.setIsNeedLocationDescribe(
////            true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
////        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
////        option.setIgnoreKillProcess(
////            false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
////        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
////        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
////        locationClient.setLocOption(option);
////
////
////    }
////
//
    
    /**
     * 检查定位服务是否开启
     *
     * @param context
     *
     * @return false:没开启，true:已开启
     */
    public static final boolean isOpen(final Context context) {
//        if (locationManager == null) {
//            locationManager = new BDLocationManager(application);
//        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        
        return false;
    }
////
////    public Location getLocation() {
////        return location;
////    }
////
////    public LocationClient getLocationClient() {
////        return locationClient;
////    }
////
////    public boolean hasLocation() {
////        return hasLocation;
////    }
}