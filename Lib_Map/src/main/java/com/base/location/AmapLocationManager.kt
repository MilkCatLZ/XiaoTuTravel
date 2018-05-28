package com.base.location

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.base.util.Log
import io.reactivex.Observable
import java.util.*

/**
 * Created by LZ on 2017/11/13.
 * 高德地图
 */
class AmapLocationManager {

    /**
     * 单实例
     */
    companion object {
        private val amap by lazy { AmapLocationManager() }

        fun getInstance(): AmapLocationManager {
            return amap
        }

    }

    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        ampLocationClient = AMapLocationClient(context)
        initOption()
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

    lateinit var ampLocationClient: AMapLocationClient
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

    fun getAddress(lat: Double, lng: Double): Observable<RegeocodeResult> {
        return Observable.create<RegeocodeResult> {

            var geocoderSearch = GeocodeSearch(context)
            val query = RegeocodeQuery(LatLonPoint(lat, lng), 200f, GeocodeSearch.AMAP)
            geocoderSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                    if (p0 != null) {
                        it.onNext(p0)
                    } else {
                        it.onComplete()
                    }
                }

                override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

                }
            })
            geocoderSearch.getFromLocationAsyn(query)
        }

    }

    fun searchPoiList(keyWord: String, cityCode: String, pageIndex: Int): Observable<ArrayList<PoiItem>> {
        return Observable.create<ArrayList<PoiItem>> {
            var query = PoiSearch.Query(keyWord, "", cityCode)
            query.pageSize = 10;// 设置每页最多返回多少条poiitem
            query.pageNum = pageIndex;//设置查询页码

            val poiSearch = PoiSearch(context, query)
            poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
                override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

                }

                override fun onPoiSearched(p0: PoiResult, p1: Int) {
                    it.onNext(p0.pois)
                }
            })
            poiSearch.searchPOIAsyn()
        }
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
            location.district = amapLocation.district
            location.city = amapLocation.city
            location.cityCode = amapLocation.cityCode
            location.name = amapLocation.poiName
            amapLocationReceive?.onLocationReceive(amapLocation, location)
        }
    }
}