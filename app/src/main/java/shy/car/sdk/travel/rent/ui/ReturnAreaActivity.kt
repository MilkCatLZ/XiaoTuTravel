package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.base.ProgressDialog
import com.base.overlay.DrivingRouteOverlay
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.MapUtil
import shy.car.sdk.databinding.ActivityReturnAreaBinding
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.presenter.ReturnAreaPresenter


/**
 * create by Sharon at 2018/06/25
 * 还车区域
 */
@Route(path = RouteMap.ReturnArea)
class ReturnAreaActivity : XTBaseActivity(),
        ReturnAreaPresenter.CallBack {

    override fun onError(e: Throwable) {

    }

    var netWorkList: ArrayList<NearCarPoint> = ArrayList<NearCarPoint>()
    var network = ObservableField<NearCarPoint>()
    var naviInfo = ObservableField<String>("")

    override fun getListSuccess(t: ArrayList<NearCarPoint>) {
        netWorkList = t
        for (i in 0..(t.size - 1)) {
            var it = t[i]
            var marker = binding.mapView.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                    .anchor(0.5f, 1.0f)
                    .snippet(it.address)
                    .title(it.name)
                    .position(LatLng(it.lat, it.lng))
                    .draggable(false))
            if (i == 0) {
                marker.showInfoWindow()
                network.set(it)
                getNaviDetail()
            }
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
        binding.mapView.map.uiSettings.isZoomControlsEnabled = false
        initMap()
    }

    private fun initMap() {
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
        binding.mapView.map.animateCamera(CameraUpdateFactory.zoomTo(13f), 1000, null)


        binding.mapView.map.setInfoWindowAdapter(NearInfoWindowAdapter(this))
        binding.mapView.map.setOnMarkerClickListener(
                {
                    ProgressDialog.showLoadingView(this@ReturnAreaActivity)
                    it.showInfoWindow()

                    network.set(findCarPoint(it.position))
                    getNaviDetail()
                    true
                }
        )


        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(3000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.mapView.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style
        binding.mapView.map.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        presenter.getNetWorkList()
    }

    fun naviToNetWork() {
        calculateRoute()
    }

    private fun calculateRoute() {
        var routeSearch = RouteSearch(this)
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(result: DriveRouteResult, p1: Int) {
                ProgressDialog.hideLoadingView(this@ReturnAreaActivity)
                if (result.paths != null) {
                    if (result.paths.size > 0) {
                        var mDrivingRouteResult = result
                        val drivePath = mDrivingRouteResult!!.paths[0] ?: return
                        val drivingRouteOverlay = DrivingRouteOverlay(
                                this@ReturnAreaActivity, binding.mapView.map, drivePath,
                                mDrivingRouteResult!!.startPos,
                                mDrivingRouteResult!!.targetPos, null)
                        drivingRouteOverlay.setNodeIconVisibility(false)//设置节点marker是否显示
                        drivingRouteOverlay.removeFromMap()
                        drivingRouteOverlay.addToMap()
                        drivingRouteOverlay.zoomToSpan()
                    }
                }
            }

            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

            }

            override fun onWalkRouteSearched(result: WalkRouteResult, p1: Int) {

            }
        })
        val fromAndTo = RouteSearch.FromAndTo(LatLonPoint(app.location.lat, app.location.lng), LatLonPoint(network.get()?.lat!!, network.get()?.lng!!))
        val query = RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "")
        routeSearch.calculateDriveRouteAsyn(query)
    }

    private fun findCarPoint(position: LatLng): NearCarPoint? {
        netWorkList.map {
            if (it.lat == position.latitude && it.lng == position.longitude) {
                return it
            }
        }
        return null
    }

    fun getNaviDetail() {
        MapUtil.getDriveTimeAndDistance(this, NaviLatLng(app.location.lat, app.location.lng), NaviLatLng(network.get()?.lat!!, network.get()?.lng!!), 1, object : MapUtil.GetDetailListener {
            override fun calculateSuccess(allLength: Int?, allTime: Int?) {
                if (allLength != null && allTime != null) {
                    naviInfo.set("全程${LNTextUtil.getPriceText(allLength / 1000.0)}公里 驾车${allTime / 60}分钟")
                }
            }

        })
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