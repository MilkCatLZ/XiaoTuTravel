package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.base.ProgressDialog
import com.base.overlay.DrivingRouteOverlay
import com.base.util.MapSelectDialogFragment
import com.base.util.StringUtils
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_find_and_rent_car.*
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.dialog.HintDialog
import shy.car.sdk.app.eventbus.TakeCarSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.CountDownThread
import shy.car.sdk.app.util.CountUpThread
import shy.car.sdk.app.util.MapUtil
import shy.car.sdk.databinding.FragmentFindAndRentCarBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.data.RentOrderState
import shy.car.sdk.travel.rent.dialog.RingCarDialogFragment
import shy.car.sdk.travel.rent.presenter.FindAndRentCarPresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
class FindAndRentCarFragment : XTBaseFragment(),
        FindAndRentCarPresenter.CallBack {
    override fun onGetDetailSuccess(t: RentOrderDetail) {
        if (t.status == RentOrderState.Taked) {
            ARouter.getInstance()
                    .build(RouteMap.Driving)
                    .withString(String1, t.orderId)
                    .navigation()
            finish()
        } else {
            binding.detail = t
            //初始化地图
            setupMap(t)
            //获取导航数据
            getRoute()

            calTimeAndDistances()
            startCountDown()
        }
    }

    var dispose: Disposable? = null
    var countdown: CountDownThread? = null
    var countUP: CountUpThread? = null
    val isCountDown = ObservableBoolean(true)
    private fun startCountDown() {
        if (binding.detail?.billingTime!! > 0) {
            countdown = CountDownThread(binding.detail, binding.detail?.billingTime!!)
            countdown?.setFinishListener {
                Observable.just("")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            isCountDown.set(false)
                        }, {})

            }
            countdown?.start(txt_count_down)
        } else {
            isCountDown.set(false)
        }

    }

    private fun calTimeAndDistances() {

//        activity?.let {
//            ProgressDialog.showLoadingView(it)
//        }
        activity?.let {
            MapUtil.getDriveTimeAndDistance(it, NaviLatLng(app.location.lat, app.location.lng), NaviLatLng(presenter.detail?.car?.lat!!, presenter.detail?.car?.lng!!), 2, object : MapUtil.GetDetailListener {
                override fun calculateSuccess(allLength: Int?, allTime: Int?) {
                    activity?.let {
                        ProgressDialog.hideLoadingView(it)
                    }
                    if (allLength != null && allTime != null) {
                        timeAndDistance.set("全程${LNTextUtil.getPriceText(allLength / 1000.0)}公里 步行${allTime / 60}分钟")
                    }
                }

            })
        }
    }

    override fun onUnLockSuccess() {
        ARouter.getInstance()
                .build(RouteMap.Driving)
                .withString(String1, oid)
                .navigation()
        eventBusDefault.post(TakeCarSuccess())
        finish()
    }

    var mDriveRouteResult: DriveRouteResult? = null
    val timeAndDistance = ObservableField<String>("")
    val countDown = ObservableField<String>("")
    private var isFirstInit: Boolean = true
    var oid: String = ""
        set(value) {
            field = value
            presenter.oid = oid
        }

    lateinit var binding: FragmentFindAndRentCarBinding

    private var mStartPoint = LatLonPoint(0.0, 0.0)//起点，108.300745,22.823181
    private var mEndPoint = LatLonPoint(0.0, 0.0)//终点，108.275277,22.873487

    lateinit var presenter: FindAndRentCarPresenter


    override fun onGetDetailError(e: Throwable) {


    }

    override fun onCancelSuccess() {
        activity?.let {
            ToastManager.showShortToast(it, "订单已取消")
        }
        finish()
    }

    override fun onCancelError(e: Throwable) {

    }

    override fun onRingError(e: Throwable) {

    }


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
        binding.mapView.map.uiSettings.isZoomControlsEnabled = false
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.map.animateCamera(CameraUpdateFactory.zoomTo(13f), 1000, null)
        initMap()
    }

    private fun setupMap(order: RentOrderDetail) {

        mStartPoint = LatLonPoint(app.location.lat, app.location.lng)
        mEndPoint = LatLonPoint(order.car?.lat!!, order.car?.lng!!)
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))

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
                                    drivingRouteOverlay.setStartPointResource(-1)
                                    drivingRouteOverlay.setEndPointResource(R.drawable.icon_defaul_locat)
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
        myLocationStyle.interval(30000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.mapView.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style

        binding.mapView.map.isMyLocationEnabled = true
        binding.mapView.map.setOnMyLocationChangeListener {
            if (isFirstInit) {

                Observable.create<String> {
                    while (StringUtils.isEmpty(oid)) {
                        try {
                            Thread.sleep(200)
                        } catch (e: Exception) {

                        }
                    }
                    it.onNext(oid)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            presenter.getOrderDetail()
                        }, {

                        })
            }
        }
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
        dispose?.dispose()
        countdown?.cancel()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    fun ringCar() {
        if (presenter.detail != null) {
            var ring = RingCarDialogFragment()
            ring.carid = presenter.detail?.car?.carID!!
            ring.show(childFragmentManager, "fragment_ring")
        }
    }

    fun unLockCar() {
        if (presenter.detail != null)
            presenter.unLockCar()
    }

    fun cancelOrder() {
        activity?.let {
            HintDialog.with(it, childFragmentManager)
                    .message("当日取消订单${app.setting?.order?.dayMaxCancelNum}次将无法继续租车")
                    .leftButtonText("继续租车")
                    .rightButtonText("取消订单")
                    .listener(object : HintDialog.OnDissmiss {
                        override fun onLeftClick() {

                        }

                        override fun onRightClick() {
                            presenter.cancelOrder()
                        }

                    })
                    .show()
        }
    }

    fun naviTo() {
        val dialog = MapSelectDialogFragment()
        dialog.startLatitude = app.location.lat
        dialog.startLongitude = app.location.lng
        dialog.endLatitude = presenter.detail?.car?.lat!!
        dialog.endLongitude = presenter.detail?.car?.lng!!
        dialog.show(childFragmentManager, "fragment_navi_select")
    }

    private fun moveCameraAndShowLocation(latLng: LatLng) {
        binding.mapView.map.animateCamera(CameraUpdateFactory.changeLatLng(latLng))
    }
}