package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.PolygonOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.WalkRouteQuery
import com.base.base.ProgressDialog
import com.base.overlay.WalkRouteOverlay
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityReturnAreaBinding
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.presenter.ReturnAreaPresenter


/**
 * create by Sharon at 2018/06/25
 * 还车区域
 */
@Route(path = RouteMap.ReturnArea)
class ReturnAreaActivity : XTBaseActivity(), ReturnAreaPresenter.CallBack {
    override fun onError(e: Throwable) {

    }

    var netWorkList: ArrayList<NearCarPoint> = ArrayList<NearCarPoint>()
    var network = ObservableField<NearCarPoint>()

    override fun getListSuccess(t: ArrayList<NearCarPoint>) {
        netWorkList = t
        t.map {
            binding.mapView.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                    .anchor(0.5f, 1.0f)
                    .snippet(it.address)
                    .title(it.name)
                    .position(LatLng(it.lat, it.lng))
                    .draggable(false))
            drawPointAngel(it)
        }
        moveCameraAndShowLocation(LatLng(t[0].lat, t[0].lng))
    }

    private fun moveCameraAndShowLocation(latLng: LatLng) {
        binding.mapView.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))

    }

    lateinit var binding: ActivityReturnAreaBinding
    lateinit var presenter: ReturnAreaPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_return_area)
        binding.ac = this
        presenter = ReturnAreaPresenter(this, this)
        binding.mapView.onCreate(savedInstanceState)


        binding.mapView.map.animateCamera(CameraUpdateFactory.zoomTo(13f), 1000, null)
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))

        binding.mapView.map.setOnMarkerClickListener(
                {
                    ProgressDialog.showLoadingView(this@ReturnAreaActivity)
                    network.set(findCarPoint(it.position))
                    it.showInfoWindow()
                    calculateTime()
                    true
                }
        )

        presenter.getNetWorkList()
    }

    var mWalkRouteResult: WalkRouteResult? = null

    private fun calculateTime() {
        var routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {

            }

            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

            }

            override fun onWalkRouteSearched(result: WalkRouteResult, p1: Int) {
                ProgressDialog.hideLoadingView(this@ReturnAreaActivity)
                if (result.paths != null) {
                    if (result.paths.size > 0) {
                        mWalkRouteResult = result
                        val drivePath = mWalkRouteResult!!.paths[0] ?: return
                        val drivingRouteOverlay = WalkRouteOverlay(
                                this@ReturnAreaActivity, binding.mapView.map, drivePath,
                                mWalkRouteResult!!.startPos,
                                mWalkRouteResult!!.targetPos)
                        drivingRouteOverlay.setNodeIconVisibility(false)//设置节点marker是否显示
                        drivingRouteOverlay.removeFromMap()
                        drivingRouteOverlay.addToMap()
                        drivingRouteOverlay.zoomToSpan()
                    } else if (result.paths == null) {

                    }

                }
            }
        })
        val fromAndTo = RouteSearch.FromAndTo(LatLonPoint(app.location.lat, app.location.lng), LatLonPoint(network.get()?.lat!!, network.get()?.lng!!))
        val query = WalkRouteQuery(fromAndTo)
        routeSearch.calculateWalkRouteAsyn(query)
    }

    private fun findCarPoint(position: LatLng): NearCarPoint? {
        netWorkList.map {
            if (it.lat == position.latitude && it.lng == position.longitude) {
                return it
            }
        }
        return null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


    /**
     * 在地图上画区域
     */
    private fun drawPointAngel(network: NearCarPoint) {
        // 定义多边形的5个点点坐标
        var polygonOptions = PolygonOptions()
        network.range?.map {
            val latLng = LatLng(it.lat, it.lng)
            polygonOptions.add(latLng)
        }
        // 添加 多边形的每个顶点（顺序添加）
        polygonOptions.strokeWidth(1f) // 多边形的边框
                .strokeColor(Color.argb(0, 0, 0, 0)) // 边框颜色
                .fillColor(Color.argb(70, 0, 179, 138))   // 多边形的填充色
        binding.mapView.map.addPolygon(polygonOptions)
    }
}