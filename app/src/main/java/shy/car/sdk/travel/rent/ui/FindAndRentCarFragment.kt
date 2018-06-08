package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.overlay.DrivingRouteOverlay
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentFindAndRentCarBinding

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
class FindAndRentCarFragment : XTBaseFragment() {


    lateinit var binding: FragmentFindAndRentCarBinding
    private val mStartPoint = LatLonPoint(39.942295, 116.335891)//起点，39.942295,116.335891
    private val mEndPoint = LatLonPoint(39.995576, 116.481288)//终点，39.995576,116.481288

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_and_rent_car, null, false)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.map.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)
//        binding.mapView.map.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(app.location.lat, app.location.lng)))
        initMap()
//        binding.mapView.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat))
//                .anchor(0.5f, 1.0f)
//                .snippet(app.location.address)
//                .position(LatLng(app.location.lat, app.location.lng))
//                .draggable(false))
        getRoute()
    }

    var mDriveRouteResult: DriveRouteResult? = null
    private fun getRoute() {
        val routeSearch = RouteSearch(activity)

        val query = RouteSearch.DriveRouteQuery(RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "")

        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
                binding.mapView.map.clear()// 清理地图上的所有覆盖物


                activity?.let {
                    if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result?.paths != null) {
                            if (result.paths.size > 0) {
                                mDriveRouteResult = result
                                val drivePath = mDriveRouteResult!!.paths[0] ?: return
                                val drivingRouteOverlay = DrivingRouteOverlay(
                                        it, binding.mapView.map, drivePath,
                                        mDriveRouteResult!!.startPos,
                                        mDriveRouteResult!!.targetPos, null)
                                drivingRouteOverlay.setNodeIconVisibility(false)//设置节点marker是否显示
                                drivingRouteOverlay.setIsColorfulline(true)//是否用颜色展示交通拥堵情况，默认true
                                drivingRouteOverlay.removeFromMap()
                                drivingRouteOverlay.addToMap()
                                drivingRouteOverlay.zoomToSpan()
//                            mBottomLayout.setVisibility(View.VISIBLE)
//                            val dis = drivePath.getDistance()
//                                    .toInt()
//                            val dur = drivePath.getDuration()
//                                    .toInt()
//                            val des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")"
//                            mRotueTimeDes.setText(des)
//                            mRouteDetailDes.setVisibility(View.VISIBLE)
//                            val taxiCost = mDriveRouteResult.getTaxiCost()
//                                    .toInt()
//                            mRouteDetailDes.setText("打车约" + taxiCost + "元")
//                            mBottomLayout.setOnClickListener(View.OnClickListener {
//                                val intent = Intent(it,
//                                        DriveRouteDetailActivity::class.java)
//                                intent.putExtra("drive_path", drivePath)
//                                intent.putExtra("drive_result",
//                                        mDriveRouteResult)
//                                startActivity(intent)
//                            })

                            } else if (result != null && result.getPaths() == null) {
//                            ToastUtil.show(mContext, R.string.no_result)
                            }

                        } else {
//                        ToastUtil.show(mContext, R.string.no_result)
                        }
                    } else {
//                    ToastUtil.showerror(this.getApplicationContext(), errorCode)
                    }
                }

            }

            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

            }

            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

            }

            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {

            }

        })

        routeSearch.calculateDriveRoute(query)
    }

    private fun initMap() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.mapView.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        binding.mapView.map.isMyLocationEnabled = true
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
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}