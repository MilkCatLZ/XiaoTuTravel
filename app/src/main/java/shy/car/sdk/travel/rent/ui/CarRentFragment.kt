package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.databinding.DataBindingAdapter
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.overlay.WalkRouteOverlay
import com.base.util.DialogManager
import com.base.util.StringUtils
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_car_rent.*
import kotlinx.android.synthetic.main.layout_car_rent_bottomsheet.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.eventbus.CreateRentCarOrderSuccess
import shy.car.sdk.app.eventbus.RefreshCity
import shy.car.sdk.app.eventbus.RefreshUserInfoSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.MapUtil
import shy.car.sdk.databinding.FragmentCarRentBinding
import shy.car.sdk.databinding.ItemNetworkBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.location.data.LocationChange
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import shy.car.sdk.travel.rent.data.CarCategory
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.presenter.CarRentPresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/11
 * 租车
 * 本应用的经纬度，地址，已这个页面的地图为准
 * 其他地方需要调用，使用app.location
 */
@Route(path = RouteMap.CarRent)
class CarRentFragment : XTBaseFragment() {

    private lateinit var binding: FragmentCarRentBinding
    private lateinit var carRentPresenter: CarRentPresenter

    var nearCarListener: NearCarOpenListener? = null
    var locationRefreshListener: MapLocationRefreshListener? = null
    /**
     * 当前选中的车辆
     */
    var currentSelectedCarInfo = ObservableField<CarInfo>()

    /**
     * 当前选中的车辆
     */
    var currentSelectedNetWork = ObservableField<NearCarPoint>()
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
     * 用来确定车辆列表是否显示
     * true:有可用车辆，显示
     * false:无可用车辆，不现实
     */
    val hasUsableCar = ObservableBoolean(true)

    var isRefershSuccess = false

    lateinit var carListViewPager: ViewPager

    /**
     * 定位默认图标
     */
    lateinit var bitmap: BitmapDescriptor

    /**
     * 记录当前缩放等级,用于判断是显示车辆还是网点
     */
    private var zoomLevel: Float = 14f
    private val callBack = object : CarRentPresenter.CallBack {
        override fun createSuccess(orderMineList: OrderMineList) {
            eventBusDefault.post(CreateRentCarOrderSuccess(orderMineList.id))
            ARouter.getInstance()
                    .build(RouteMap.FindAndRentCar)
                    .withString(String1, orderMineList.id)
                    .navigation()
        }

        override fun getNetWorkListSuccess(t: ArrayList<NearCarPoint>) {
            if (t.isNotEmpty()) {
                carRentPresenter.getUsableCarList(null)
                currentSelectedNetWork.set(t[0])
                this@CarRentFragment.carPointList.clear()
                this@CarRentFragment.carPointList.addAll(t)
//                showMarkers(zoomLevel)
            }

        }

        override fun onGetCarModelSuccess(t: List<CarCategory>) {

        }

        override fun onGetCarError(e: Throwable) {
            ToastManager.showShortToast(context, "附近没有可用车辆哦，请选择其他网点")
        }

        override fun onGetCarSuccess(t: List<CarInfo>) {
            if (t.isNotEmpty()) {
                currentSelectedCarInfo.set(t[0])
                setupCurrentCarInfo(t[0])
                calDistanceAndTimeInfo()
                findRoutToCar(t[0].lat, t[0].lng)
                hasUsableCar.set(true)
            } else {
                hasUsableCar.set(false)
                ToastManager.showShortToast(activity, "该网点没有该车型车辆，请选择其他车型")
                currentSelectedCarInfo.set(null)
                naviInfo.set("")
                walkRouteOverlay?.removeFromMap()
            }
            circle_indicator.setViewPager(viewPager_car_list)
            showMarkers(zoomLevel)
            isRefershSuccess = true
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
//                        binding.map.map.clear()// 清理地图上的所有覆盖物

                        activity?.let {

                            if (result.paths != null) {
                                if (result.paths.size > 0) {
                                    val mDriveRouteResult = result
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

    private fun calDistanceAndTimeInfo() {
        activity?.let {
            getNaviInfo()
            Observable.create<Boolean> {
                while (StringUtils.isEmpty(naviInfo.get())) {
                    try {
                        Thread.sleep(1000)
                        if (StringUtils.isEmpty(naviInfo.get())) {
                            it.onNext(true)
                        }
                    } catch (e: Exception) {
                    }
                }
                it.onComplete()
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        getNaviInfo()
                    }, {})


        }
    }

    private fun getNaviInfo() {
        MapUtil.getDriveTimeAndDistance(app, NaviLatLng(app.location.lat, app.location.lng), NaviLatLng(currentSelectedCarInfo.get()?.lat!!, currentSelectedCarInfo.get()?.lng!!), 2, object : MapUtil.GetDetailListener {
            override fun calculateSuccess(allLength: Int?, allTime: Int?) {

                if (allLength != null && allTime != null) {
                    naviInfo.set("全程${LNTextUtil.getPriceText(allLength / 1000.0)}公里 步行${allTime / 60}分钟")
                }
            }

        })
    }

    override fun getFragmentName(): CharSequence {
        return "租车"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { carRentPresenter = CarRentPresenter(it, callBack) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_rent, null, false)
        binding.map.onCreate(savedInstanceState)
        setBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
        initBottomSheet()
        initMap()
        register(this)
        initData()
        refreshLocation()
        carRentPresenter.getUsableCarModel()
    }


    private fun initData() {
        carListViewPager = binding.root.findViewById(R.id.viewPager_car_list)
        carListViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val carInfo = carRentPresenter.carListAdapter.items[position]
                setupCurrentCarInfo(carInfo)
                calDistanceAndTimeInfo()
                findRoutToCar(carInfo.lat, carInfo.lng)

            }
        })
    }

    private fun setupCurrentCarInfo(carInfo: CarInfo) {
        val adapter = DataBindingAdapter<CarInfo.DiscountsBean.DurationBean>(R.layout.item_car_discount, BR.discount, null)
        adapter.setItems(carInfo.discounts?.duration, 1)
        recyclerView_car_discount.adapter = adapter
        currentSelectedCarInfo.set(carInfo)
        findMarker(LatLng(carInfo.lat, carInfo.lng), carMarkerList)?.showInfoWindow()
    }

    private fun setBinding() {
        binding.fragment = this
        binding.p = carRentPresenter
        binding.user = User.instance
    }

    private fun initLayoutManager() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.recyclerViewCarCategory.layoutManager = layoutManager
    }

    lateinit var carBottomSheet: BottomSheetBehavior<View>
    private fun initBottomSheet() {
        carBottomSheet = BottomSheetBehavior.from(binding.viewBottomSheet)
        carBottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                view_back.alpha = slideOffset
            }
        })

    }

    /**
     * 初始化地图
     */
    private fun initMap() {


        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 1000, null)
        activity?.let { binding.map.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
        binding.map.map.setOnMarkerClickListener {
            if (zoomLevel >= MaxZoom) {
                val car = findCar(it.position)
                currentSelectedCarInfo.set(car)
                carListViewPager.currentItem = carRentPresenter.carListAdapter.items.indexOf(car)
                it.showInfoWindow()
            } else {
                val netWork = findCarPoint(it.position)
                currentSelectedNetWork.set(netWork)
                findRoutToCar(it.position.latitude, it.position.longitude)
//                carRentPresenter.getUsableCarList(null)
//                it.hideInfoWindow()
            }

            true
        }

        binding.map.map.uiSettings.isZoomControlsEnabled = false

        binding.map.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(cameraPosition: CameraPosition?) {

                if (zoomLevel != cameraPosition?.zoom) {
                    showMarkers(cameraPosition?.zoom!!)
                    findRoutToMarker(cameraPosition?.zoom!!)
                }
                zoomLevel = cameraPosition.zoom
            }

            override fun onCameraChange(p0: CameraPosition?) {

            }

        })

        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        binding.map.map.myLocationStyle = myLocationStyle//设置定位蓝点的Style

        binding.map.map.isMyLocationEnabled = true
        binding.map.map.setOnMyLocationChangeListener {

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
                        carRentPresenter.getNetWorkList()
                    }, {

                    })

            binding.map.map.setOnMyLocationChangeListener(null)

        }
    }

    val MaxZoom = 14f
    private fun showMarkers(zoomLevel: Float) {

        if (zoomLevel >= MaxZoom) {
            showCarMarker()
            if (currentSelectedCarInfo.get() != null) {
                val marker = findMarker(LatLng(currentSelectedCarInfo.get()?.lat!!, currentSelectedCarInfo.get()?.lng!!), carMarkerList)
                marker?.showInfoWindow()
            }
        } else {
            showNetWork()
        }

        carPointList.map {
            drawPointAngel(it)
        }
    }

    private fun findRoutToMarker(zoomLevel: Float) {

        if (zoomLevel >= MaxZoom) {
            if (currentSelectedCarInfo.get() != null)
                findRoutToCar(currentSelectedCarInfo.get()?.lat!!, currentSelectedCarInfo.get()?.lng!!)
        } else {
            if (currentSelectedNetWork.get() != null)
                findRoutToCar(currentSelectedNetWork.get()?.lat!!, currentSelectedNetWork.get()?.lng!!)
        }

    }

    private fun showNetWork() {
        binding.map.map.clear()
        netWorkMarkerList.clear()

        carPointList.map {
            addNetWorkMarkersToMap(it)
        }
//        if (carMarkerList.isNotEmpty())
//            carMarkerList[0].showInfoWindow()
        if (binding.detail == null)
            walkRouteOverlay?.addToMap()
        addUserLocationMarker()
    }

    private fun showCarMarker() {
        binding.map.map.clear()
        carMarkerList.clear()
        carRentPresenter.carListAdapter.items.map {
            addCarMarkersToMap(it.carModel, it.plateNumber, it.lat, it.lng)
        }
        if (binding.detail == null)
            walkRouteOverlay?.addToMap()
        addUserLocationMarker()
    }

    private fun findCarPoint(position: LatLng): NearCarPoint? {
        carPointList.map {
            if (it.lat == position.latitude && it.lng == position.longitude) {
                return it
            }
        }
        return null
    }

    private fun findCar(position: LatLng): CarInfo? {
        carRentPresenter.carListAdapter.items.map {
            if (it.lat == position.latitude && it.lng == position.longitude) {
                return it
            }
        }
        return null
    }

    private fun findMarker(position: LatLng, list: List<Marker>): Marker? {
        list.map {
            if (it.position.latitude == position.latitude && it.position.longitude == position.longitude) {
                return it
            }
        }
        return null
    }

    //刷新位置
    fun refreshLocation() {
        Observable.create<com.base.location.Location> {
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
        }
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

    var userMaker: Marker? = null
    fun addUserLocationMarker() {
        userMaker?.remove()
        userMaker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat))
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


    private var isRentClick: Boolean = false

    /**
     * 租车点击事件
     */
    fun onRentClick() {
        val user = User.instance
        when (user.login) {
            true -> checkPromiseMoneyPay()
            else -> {
                app.startLoginDialog(null, null, object : onLoginDismiss {
                    override fun onCancel() {
                        isRentClick = false
                    }
                })
                isRentClick = true
            }
        }

    }

    /**
     * 租车点击-检查是否认证
     */
    private fun checkPromiseMoneyPay() {
        //已交保证金
        if (User.instance.getIsDeposit() && User.instance.getIsIdentityAuth()) {
            if (currentSelectedCarInfo.get()?.netWork == null) {
                currentSelectedCarInfo.get()
                        ?.netWork = carPointList[0]
            }
            carRentPresenter.createRentCarOrder(currentSelectedCarInfo.get()?.carId!!, currentSelectedCarInfo.get()?.netWork?.id!!)
        } else {
            //提示未交保证金
            if (userVisibleHint) {
                val dialog = ARouter.getInstance()
                        .build(RouteMap.Dialog_Money_Verify)
                        .navigation() as XTBaseDialogFragment
                dialog.show(fragmentManager, "dialog_money_verify")
            }

        }
    }

    private fun showConfirmDialog() {

        if (currentSelectedCarInfo.get() != null) {
            activity?.let {
                if (userVisibleHint) {
                    DialogManager.with(it, childFragmentManager)
                            .title("提示")
                            .message("确定租用该车辆？\n车型：${currentSelectedCarInfo.get()?.carModel}\n车牌：${currentSelectedCarInfo.get()?.plateNumber}\n颜色：${currentSelectedCarInfo.get()?.color}")
                            .leftButtonText("取消")
                            .rightButtonText("确定")
                            .onRightClick { dialog, witch ->
                                if (currentSelectedCarInfo.get()?.netWork == null) {
                                    currentSelectedCarInfo.get()
                                            ?.netWork = carPointList[0]
                                }
                                carRentPresenter.createRentCarOrder(currentSelectedCarInfo.get()?.carId!!, currentSelectedCarInfo.get()?.netWork?.id!!)
                            }
                            .show()
                }
            }
        } else {
            ToastManager.showShortToast(activity, "当前没有可用车辆")
        }
    }

    fun changeZoom() {
        binding.edtZoom.text?.let { binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(it.toString().toFloat()), 1000, null) }
    }

    /**
     * 在地图上添加marker
     */
    private fun addNetWorkMarkersToMap(point: NearCarPoint) {
        activity?.let {
            val markerBinding = DataBindingUtil.inflate<ItemNetworkBinding>(LayoutInflater.from(it), R.layout.item_network, null, false)
            val txt = markerBinding.root.findViewById(R.id.txt_car_num) as TextView
            txt.text = "${point.usableCarsNum}辆"
            val marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromView(markerBinding.root))
                    .anchor(0.5f, 1.0f)
                    .title(point.name)
                    .snippet("可用车${point.usableCarsNum}辆")
                    .position(LatLng(point.lat, point.lng))
                    .displayLevel(1)
                    .draggable(false))
            marker.isClickable = true
            netWorkMarkerList.add(marker)
        }
    }

    /**
     * 在地图上添加marker
     */
    private fun addCarMarkersToMap(carModel: String, plateNumber: String, lat: Double, lng: Double) {

        val marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_samll_car))
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
        val polygonOptions = PolygonOptions()
        network.range?.map {
            val latLng = LatLng(it.lat, it.lng)
            polygonOptions.add(latLng)
        }
        // 添加 多边形的每个顶点（顺序添加）
        polygonOptions.strokeWidth(1f) // 多边形的边框
                .strokeColor(Color.argb(0, 0, 0, 0)) // 边框颜色
                .fillColor(Color.argb(120, 0, 179, 138))   // 多边形的填充色
        binding.map.map.addPolygon(polygonOptions)
    }

    fun onNearCarClick() {
        nearCarListener?.onNearCarClick()
    }

    fun gotoVerify() {
        if (User.instance.login) {
            if (!User.instance.getIsIdentityAuth()) {
                ARouter.getInstance()
                        .build(RouteMap.UserVerify)
                        .navigation()
            } else if (!User.instance.getIsDeposit()) {
                ARouter.getInstance()
                        .build(RouteMap.MoneyVerify)
                        .navigation()
            }
        }
    }


    fun refreshAll() {
        isRefershSuccess = false
        activity?.let {
            val operatingAnim: Animation = AnimationUtils.loadAnimation(it, R.anim.rotate)
            val lin = LinearInterpolator()
            operatingAnim.interpolator = lin

            operatingAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationEnd(p0: Animation?) {
                    if (!isRefershSuccess) {
                        binding.refresh.startAnimation(operatingAnim)
                    }
                }

                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationRepeat(p0: Animation?) {

                }

            })
            binding.refresh.startAnimation(operatingAnim)
        }
        eventBusDefault.post(RefreshCity())
        carRentPresenter.getNetWorkList()
    }


    /**
     * 定位成功 监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(change: LocationChange) {
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
    }

    /**
     * 定位成功 监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(refresh: RefreshUserInfoSuccess) {
        binding.user = User.instance
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNearCarPointChange(nearCar: NearCarPoint) {
        netWorkMarkerList.map {
            if (it.position.latitude == nearCar.lat && it.position.longitude == nearCar.lng) {
                it.showInfoWindow()
//                carRentPresenter.getUsableCarModel()
                carRentPresenter.getUsableCarList(nearCar)
            }
        }
    }

}