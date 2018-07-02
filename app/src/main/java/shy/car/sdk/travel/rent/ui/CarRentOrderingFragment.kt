package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.overlay.WalkRouteOverlay
import com.base.util.StringUtils
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.MapUtil
import shy.car.sdk.databinding.FragmentCarRentOrderingBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.location.data.LocationChange
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.data.RentOrderState
import shy.car.sdk.travel.rent.dialog.RentNoPayDialog
import shy.car.sdk.travel.rent.presenter.CarRentOrderingPresenter
import shy.car.sdk.travel.user.data.User


/**
 * create by LZ at 2018/05/11
 * 租车
 * 本应用的经纬度，地址，已这个页面的地图为准
 * 其他地方需要调用，使用app.location
 */
@Route(path = RouteMap.CarRent)
class CarRentOrderingFragment : XTBaseFragment() {

    private lateinit var binding: FragmentCarRentOrderingBinding
    var carRentPresenter: CarRentOrderingPresenter? = null

    var nearCarListener: NearCarOpenListener? = null
    var locationRefreshListener: MapLocationRefreshListener? = null
    /**
     * 在地图上展出的网点的marker列表
     */
    var netWorkMarkerList = ArrayList<Marker>()

    /**
     * 在地图上展出的网点的marker列表
     */
    var carMarkerList = ArrayList<Marker>()
    /**
     * 当前可用的网点列表
     */
    private var carPointList = ArrayList<NearCarPoint>()
    /**
     * 当前选中的车辆的定位信息，距离，时间
     */
    val naviInfo = ObservableField<String>("")
    /**
     * 是否已经取车，如果有取车订单，则隐藏租车
     * 显示行程中
     */
    val drivingMode = ObservableBoolean(false)
    /**
     * 有未取车的订单
     */
    val takeMode = ObservableBoolean(false)
    /**
     * 有未支付的订单
     */
    val payMode = ObservableBoolean(false)

    /**
     * 定位默认图标
     */
    lateinit var bitmap: BitmapDescriptor


    var detail: RentOrderDetail? = null
    private val callBack = object : CarRentOrderingPresenter.CallBack {
        override fun getNetWorkListSuccess(list: ArrayList<NearCarPoint>) {
            if (list.isNotEmpty()) {
                this@CarRentOrderingFragment.carPointList.clear()
                this@CarRentOrderingFragment.carPointList.addAll(list)
                showMarkers()
            }

        }


        override fun getDetailSuccess(t: RentOrderDetail) {
            detail = t
            drivingMode.set(false)
            takeMode.set(false)
            payMode.set(false)
            binding.detail = t
            when (t.status) {
                RentOrderState.Create -> {
                    gotoFindAndRent(t)
                    takeMode(t)
                }
                RentOrderState.Taked -> {
                    drivingMode(t)
                }
                RentOrderState.Return -> {
                    gotoPayRentOrder(t)

                }
            }
        }

        override fun onGetUnProgressOrderSuccess(orderMineList: OrderMineList?) {

        }

        override fun onGetCarError(e: Throwable) {
            ToastManager.showShortToast(context, "附近没有可用车辆哦，请选择其他网点")
        }
    }
    var walkRouteOverlay: WalkRouteOverlay? = null
    private fun findRoutToCar(lat: Double, lng: Double) {

        val routeSearch = RouteSearch(activity)

        val query = RouteSearch.WalkRouteQuery(RouteSearch.FromAndTo(LatLonPoint(app.location.lat, app.location.lng), LatLonPoint(lat, lng)))



        Observable.create<WalkRouteResult> {
            routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
                override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {

                }

                override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {

                }

                override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {

                }

                override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
                    if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result != null) {
                            it.onNext(result)
                        }
                    }
                }

            })
            routeSearch.calculateWalkRouteAsyn(query)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<WalkRouteResult> {
                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(result: WalkRouteResult) {
                        activity?.let {

                            if (result.paths != null) {
                                if (result.paths.size > 0) {
                                    var mDriveRouteResult = result
                                    val drivePath = mDriveRouteResult.paths[0] ?: return
                                    if (walkRouteOverlay != null) {
                                        walkRouteOverlay?.removeFromMap()
                                    }
                                    walkRouteOverlay = WalkRouteOverlay(
                                            it, binding.map.map, drivePath,
                                            mDriveRouteResult.startPos,
                                            mDriveRouteResult.targetPos)
                                    walkRouteOverlay?.setNodeIconVisibility(false)//设置节点marker是否显示
                                    walkRouteOverlay?.setStartMarkerEnable(false)
                                    walkRouteOverlay?.setEndMarkerEnable(false)

                                    walkRouteOverlay?.addToMap()
                                    walkRouteOverlay?.zoomToSpan()

                                } else if (result.paths == null) {
                                }

                            }
                        }
                    }
                })


    }

    private fun calDistanceAndTimeInfo(lat: Double, lng: Double) {
        activity?.let {
            MapUtil.getDriveTimeAndDistance(it, NaviLatLng(app.location.lat, app.location.lng), NaviLatLng(lat, lng), 2, object : MapUtil.GetDetailListener {
                override fun calculateSuccess(allLength: Int?, allTime: Int?) {

                    if (allLength != null && allTime != null) {
                        naviInfo.set("全程${LNTextUtil.getPriceText(allLength / 1000.0)}公里 步行${allTime / 60}分钟")
                    }
                }

            })
        }
    }

    private fun gotoPayRentOrder(detail: RentOrderDetail) {
        if (userVisibleHint) {
            try {
                val dialog = RentNoPayDialog()
                dialog.oid = detail.orderId!!
                dialog.show(childFragmentManager, "dialog_order_no_pay")
            } catch (e: Exception) {

            }
        }
        payMode.set(true)
    }

    private fun gotoFindAndRent(detail: RentOrderDetail) {
        Observable.create<String> {
            var i = 0
            while (StringUtils.isEmpty(naviInfo.get())) {
                try {
                    i++
                    Thread.sleep(100)
                    if (i == 40) {
                        i = 1
                        calDistanceAndTimeInfo(detail.car?.lat!!, detail.car?.lng!!)
                    }
                } catch (e: Exception) {
                }
            }
            it.onNext("")
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    ARouter.getInstance()
                            .build(RouteMap.FindAndRentCar)
                            .withString(String1, detail.orderId)
                            .navigation()
                }, {})

    }

    override fun getFragmentName(): CharSequence {
        return "租车-订单模式"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { carRentPresenter = CarRentOrderingPresenter(it, callBack) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_rent_ordering, null, false)
        binding.map.onCreate(savedInstanceState)
        setBinding()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        refreshLocation()
        register(this)
    }

    private fun setBinding() {
        binding.fragment = this
        binding.p = carRentPresenter
        binding.user = User.instance
    }

    private var isFirstInit: Boolean = true

    var oid: String = ""

    /**
     * 初始化地图
     */
    private fun initMap() {

        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(15f), 1000, null)
        activity?.let { binding.map.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
        binding.map.map.setOnMarkerClickListener(
                {
                    it.showInfoWindow()
                    true
                }
        )

        binding.map.map.uiSettings.isZoomControlsEnabled = false

        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.map.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style

        binding.map.map.isMyLocationEnabled = true
        binding.map.map.setOnMyLocationChangeListener {
            if (isFirstInit) {
                isFirstInit = false
                Observable.create<String> {
                    while (StringUtils.isEmpty(app.location.cityCode)) {
                        try {
                            Thread.sleep(200)
                        } catch (e: Exception) {

                        }
                    }
                    it.onNext(app.location.cityCode)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            carRentPresenter?.getNetWorkList()
                        }, {

                        })

                binding.map.map.setOnMyLocationChangeListener(null)
            }
        }
    }


    private fun showMarkers() {
        showNetWork()
        carPointList.map {
            drawPointAngel(it)
        }

    }

    /**
     * 已经取车，正在行驶中
     */
    private fun drivingMode(detail: RentOrderDetail) {
        walkRouteOverlay?.removeFromMap()
        binding.map.map.clear()
        drivingMode.set(true)
        addUserLocationMarker()
        addCarMarkersToMap(detail.car?.modelName!!, detail.car?.plateNumber!!, detail.car?.lat!!, detail.car?.lng!!)
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
    }

    /**
     * 已经取车，正在行驶中
     */
    private fun takeMode(detail: RentOrderDetail) {
        walkRouteOverlay?.removeFromMap()
        binding.map.map.clear()
        takeMode.set(true)
        calDistanceAndTimeInfo(detail.car?.lat!!, detail.car?.lng!!)
        findRoutToCar(detail.car?.lat!!, detail.car?.lng!!)
        addCarMarkersToMap(detail.car?.modelName!!, detail.car?.plateNumber!!, detail.car?.lat!!, detail.car?.lng!!)
        addUserLocationMarker()
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
    }

    private fun showNetWork() {
        binding.map.map.clear()
        netWorkMarkerList.clear()

        carPointList.map {
            addNetWorkMarkersToMap(it)
        }
        if (binding.detail == null)
            walkRouteOverlay?.addToMap()
        addUserLocationMarker()
    }

    //刷新位置
    fun refreshLocation() {
        Observable.create<com.base.location.Location>({
            AmapLocationManager.getInstance()
                    .getLocation(object : AmapOnLocationReceiveListener {
                        override fun onLocationReceive(ampLocation: AMapLocation, location: com.base.location.Location) {
                            app.changeCurrentLocation(location)
                            getCityCode()
                            moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
                            addUserLocationMarker()
                            locationRefreshListener?.onLocationChange()
                        }
                    })
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

    }

    private fun getCityCode() {
        if (app.cityList.isNotEmpty())
            app.cityList.map {
                if (app.location.cityName == it.cityName) {
                    app.location.cityCode = it.cityCode
                }
            }

    }

    private fun moveCameraAndShowLocation(latLng: LatLng) {
        binding.map.map.animateCamera(CameraUpdateFactory.changeLatLng(latLng))
    }


    fun addUserLocationMarker() {
        binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat))
                .anchor(0.5f, 1.0f)
                .snippet(app.location.address)
                .position(LatLng(app.location.lat, app.location.lng))
                .draggable(false))
    }

    override fun onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.map.onDestroy()
        nearCarListener = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.map.onResume()
        naviInfo.notifyChange()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.map.onSaveInstanceState(outState)
    }

    /**
     * 在地图上添加marker
     */
    private fun addNetWorkMarkersToMap(point: NearCarPoint) {

        var marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .title(point.name)
                .snippet("可用车${point.usableCarsNum}辆")
                .position(LatLng(point.lat, point.lng))
                .displayLevel(1)
                .draggable(false))
        marker.isClickable = true
        netWorkMarkerList.add(marker)
    }

    /**
     * 在地图上添加marker
     */
    private fun addCarMarkersToMap(carModel: String, plateNumber: String, lat: Double, lng: Double) {

        var marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .title(carModel)
                .snippet("$carModel | $plateNumber")
                .position(LatLng(lat, lng))
                .displayLevel(1)
                .draggable(false))
        marker.isClickable = true
        carMarkerList.add(marker)
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
        binding.map.map.addPolygon(polygonOptions)
    }

    fun onNearCarClick() {
        nearCarListener?.onNearCarClick()
    }

    /**
     * 查看行程中 按钮点击
     */
    fun gotoDriving(oid: String) {
        ARouter.getInstance()
                .build(RouteMap.Driving)
                .withString(String1, oid)
                .navigation()
    }

    /**
     * 去取车 按钮点击
     */
    fun gotoFindAndTake(oid: String) {
        ARouter.getInstance()
                .build(RouteMap.FindAndRentCar)
                .withString(String1, oid)
                .navigation()
    }

    fun goPay(oid: String) {
        ARouter.getInstance()
                .build(RouteMap.OrderPay)
                .withString(String1, oid)
                .navigation()
    }

    /**
     * 定位成功 监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(change: LocationChange) {
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNearCarPointChange(nearCar: NearCarPoint) {
        netWorkMarkerList.map {
            if (it.position.latitude == nearCar.lat && it.position.longitude == nearCar.lng) {
                it.showInfoWindow()
            }
        }
    }

    /**
     * 详情刷新时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGetDetailSuccess(detail: RentOrderDetail) {
       checkMode(detail)
    }

    private fun checkMode(detail: RentOrderDetail) {
        this.detail = detail
        drivingMode.set(false)
        takeMode.set(false)
        payMode.set(false)
        binding.detail = detail
        when (detail.status) {
            RentOrderState.Create -> {
                takeMode(detail)
            }
            RentOrderState.Taked -> {
                drivingMode(detail)
            }
            RentOrderState.Return -> {
                payMode.set(true)
            }
        }
    }

    fun getOrderDetail() {
        Observable.create<Boolean> {
            while (carRentPresenter == null) {
                try {
                    Thread.sleep(100)
                } catch (e: Exception) {
                }

            }
            it.onNext(true)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    carRentPresenter?.getRentOrderDetail(oid)
                }, {})

    }
}