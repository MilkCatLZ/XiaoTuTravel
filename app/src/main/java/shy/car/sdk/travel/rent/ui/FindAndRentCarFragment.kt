package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.overlay.DrivingRouteOverlay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentFindAndRentCarBinding
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.presenter.FindAndRentCarPresenter

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
class FindAndRentCarFragment : XTBaseFragment(),
        FindAndRentCarPresenter.CallBack {
    override fun onRingError(e: Throwable) {

    }


    lateinit var binding: FragmentFindAndRentCarBinding

    private var mStartPoint = LatLonPoint(0.0, 0.0)//起点，108.300745,22.823181
    private var mEndPoint = LatLonPoint(0.0, 0.0)//终点，108.275277,22.873487

    lateinit var presenter: FindAndRentCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = FindAndRentCarPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_and_rent_car, null, false)
        binding.fragment = this

        binding.mapView.onCreate(savedInstanceState)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.map.animateCamera(CameraUpdateFactory.zoomTo(13f), 1000, null)
        initMap()
    }

    var mDriveRouteResult: DriveRouteResult? = null
    var carInfo = CarInfo()
        set(value) {
            field = value
            initValue(value)
            setupMap()

        }
    var orderID: String = ""
        set(value) {
            field = value
            presenter.orderID=orderID
        }

    private fun setupMap() {

        mStartPoint = LatLonPoint(app.location.lat, app.location.lng)
        mEndPoint = LatLonPoint(carInfo.lat, carInfo.lng)

        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
        getRoute()
    }

    private fun initValue(value: CarInfo) {
        presenter.carInfo = carInfo
        binding.car = value
    }

    private fun getRoute() {
        val routeSearch = RouteSearch(activity)

        val query = RouteSearch.DriveRouteQuery(RouteSearch.FromAndTo(mStartPoint, mEndPoint), RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "")



        Observable.create<DriveRouteResult> {
            routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
                override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
                    if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result != null) {
                            it.onNext(result)
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
            routeSearch.calculateDriveRouteAsyn(query)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<DriveRouteResult> {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(result: DriveRouteResult) {
                        binding.mapView.map.clear()// 清理地图上的所有覆盖物


                        activity?.let {

                            if (result.paths != null) {
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
                                } else if (result.paths == null) {
                                }

                            }
                        }

                    }


                })


    }

    private fun initMap() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(15000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
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

    fun ringCar() {
        presenter.carRing()
    }

    fun unLockCar() {
        presenter.unLockCar()
    }

    private fun moveCameraAndShowLocation(latLng: LatLng) {
        binding.mapView.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))
    }
}