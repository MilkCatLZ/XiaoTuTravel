package shy.car.sdk.app.util

import android.content.Context
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.enums.PathPlanningStrategy
import com.amap.api.navi.model.*
import com.autonavi.tbt.TrafficFacilityInfo

class MapUtil {

    interface GetDetailListener {
        fun calculateSuccess(allLength: Int?, allTime: Int?)
    }


    companion object : AMapNaviListener {
        override fun onNaviInfoUpdate(p0: NaviInfo?) {

        }

        override fun onCalculateRouteSuccess(p0: IntArray?) {

        }

        override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
            callBack?.calculateSuccess(mAMapNavi?.naviPath?.allLength, mAMapNavi?.naviPath?.allTime)
            callBack = null
        }

        override fun onCalculateRouteFailure(p0: Int) {
        }

        override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
        }

        override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
        }

        override fun onEndEmulatorNavi() {
        }

        override fun onArrivedWayPoint(p0: Int) {
        }

        override fun onArriveDestination() {
        }

        override fun onPlayRing(p0: Int) {
        }

        override fun onTrafficStatusUpdate() {
        }

        override fun onGpsOpenStatus(p0: Boolean) {
        }

        override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
        }

        override fun showCross(p0: AMapNaviCross?) {
        }

        override fun onGetNavigationText(p0: Int, p1: String?) {
        }

        override fun onGetNavigationText(p0: String?) {
        }

        override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
        }

        override fun hideCross() {
        }

        override fun onInitNaviFailure() {
        }

        override fun onInitNaviSuccess() {
            if (type == 1) {
                mAMapNavi?.calculateDriveRoute(startList, endList, null, PathPlanningStrategy.DRIVING_DEFAULT)
            } else {
                mAMapNavi?.calculateWalkRoute(startList[0], endList[0])
            }
        }

        override fun onReCalculateRouteForTrafficJam() {
        }

        override fun updateIntervalCameraInfo(p0: AMapNaviCameraInfo?, p1: AMapNaviCameraInfo?, p2: Int) {
        }

        override fun hideLaneInfo() {
        }

        override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {
        }

        override fun showModeCross(p0: AMapModelCross?) {
        }

        override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
        }

        override fun hideModeCross() {
        }

        override fun onLocationChange(p0: AMapNaviLocation?) {
        }

        override fun onReCalculateRouteForYaw() {
        }

        override fun onStartNavi(p0: Int) {
        }

        override fun notifyParallelRoad(p0: Int) {
        }

        override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
        }

        override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
        }

        override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {
        }

        override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
        }

        override fun showLaneInfo(p0: AMapLaneInfo?) {
        }

        var mAMapNavi: AMapNavi? = null
        var startList = ArrayList<NaviLatLng>()
        var endList = ArrayList<NaviLatLng>()
        var type: Int = 0

        var callBack: GetDetailListener? = null

        open fun getDriveTimeAndDistance(applicationContext: Context, startPoint: NaviLatLng, endPoint: NaviLatLng, type: Int, callBack: GetDetailListener) {
            startList.add(startPoint)
            endList.add(endPoint)
            this.type = type
            this.callBack = callBack
            if (mAMapNavi == null) {
                mAMapNavi = AMapNavi.getInstance(applicationContext)
            } else {
                //添加监听回调，用于处理算路成功
                mAMapNavi?.addAMapNaviListener(this)
                if (type == 1) {
                    mAMapNavi?.calculateDriveRoute(startList, endList, null, PathPlanningStrategy.DRIVING_DEFAULT)
                } else {
                    mAMapNavi?.calculateWalkRoute(startList[0], endList[0])
                }
            }


        }
    }
}