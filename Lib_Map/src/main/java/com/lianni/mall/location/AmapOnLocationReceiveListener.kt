package com.lianni.mall.location

import com.amap.api.location.AMapLocation

/**
 * Created by LZ on 2017/11/13.
 */
interface AmapOnLocationReceiveListener {
    fun onLocationReceive(ampLocation: AMapLocation, location: Location)
}