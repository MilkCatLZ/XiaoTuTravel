package com.lianni.mall.location

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.base.util.Log

/**
 * Created by LZ on 2017/11/13.
 * 高德地图
 */
class AmapLocationManager private constructor(context: Context) {

    /**
     * 单实例
     */
    companion object {
        @JvmStatic
        fun init(context: Context) {
            instance = AmapLocationManager(context)
            instance.initOption()
        }

        @Volatile
        @JvmStatic
        lateinit var instance: AmapLocationManager
    }

    /**
     * 初始化设置
     */
    private fun initOption() {
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //单次定位
        option.isOnceLocation = true
        //3秒内最精确的定位
        option.isOnceLocationLatest = true
        //缓存
        option.isLocationCacheEnable = false

        ampLocationClient.setLocationOption(option)
        ampLocationClient.setLocationListener(listener)
    }

    private val ampLocationClient = AMapLocationClient(context)
    var listener = Listener()
    var amapLocationReceive: AmapOnLocationReceiveListener? = null
    private val option: AMapLocationClientOption = AMapLocationClientOption()

    /**
     * 获取位置
     */
    fun getLocation(listener: AmapOnLocationReceiveListener) {
        amapLocationReceive = listener
        ampLocationClient.startLocation()
    }

    val tag = "AmapLocationManager"

    inner class Listener : AMapLocationListener {
        override fun onLocationChanged(amapLocation: AMapLocation?) {
            if (amapLocation != null) {
                if (amapLocation.errorCode == 0) {
                    setUpLocation(amapLocation)
                    Log.d(tag, "高德定位获取成功，lat=${amapLocation.latitude}/log=${amapLocation.longitude}")
                } else {
                    Log.e(tag, "高德获取定位失败，errorCode=${amapLocation.errorCode}")
                }
            } else {
                Log.e(tag, "高德获取定位失败，amapLocation=null!!")
            }
        }

        fun setUpLocation(amapLocation: AMapLocation) {
            val location = Location()
            location.latitude = amapLocation.latitude
            location.longitude = amapLocation.longitude
            location.address = amapLocation.address
            location.city = amapLocation.city
            location.name = amapLocation.poiName
            amapLocationReceive?.onLocationReceive(amapLocation, location)
        }
    }
}