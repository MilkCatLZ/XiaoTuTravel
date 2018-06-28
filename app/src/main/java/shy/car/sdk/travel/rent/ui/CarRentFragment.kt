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
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.base.databinding.DataBindingAdapter
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import com.base.overlay.WalkRouteOverlay
import com.base.util.DialogManager
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
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.data.RefreshRentCarState
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.app.util.MapUtil
import shy.car.sdk.databinding.FragmentCarRentBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.location.data.LocationChange
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import shy.car.sdk.travel.rent.data.CarCategory
import shy.car.sdk.travel.rent.data.CarInfo
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.data.RentOrderState
import shy.car.sdk.travel.rent.dialog.RentNoPayDialog
import shy.car.sdk.travel.rent.presenter.CarRentPresenter
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.app.eventbus.RefreshUserInfoSuccess
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
    var currentSelectedCarInfo = ObservableField<CarInfo>()

    var markerList = ArrayList<Marker>()
    private var carPointList = ArrayList<NearCarPoint>()
    val naviInfo = ObservableField<String>("")
    val drivingMode = ObservableBoolean(false)
    val hasUsableCar = ObservableBoolean(false)
    private val callBack = object : CarRentPresenter.CallBack {
        override fun getNetWorkListSuccess(list: ArrayList<NearCarPoint>) {
            if (list.isNotEmpty()) {
                carRentPresenter.getUsableCarList(list[0])
                this@CarRentFragment.carPointList.clear()
                this@CarRentFragment.carPointList.addAll(list)
                addCarPointToMap(list)
                list.map {
                    drawPointAngel(it)
                }
            }
        }

        override fun onGetCarModelSuccess(t: List<CarCategory>) {

        }

        override fun getDetailSuccess(t: RentOrderDetail) {
            binding.detail = t
        }

        override fun onGetUnProgressOrderSuccess(orderMineList: OrderMineList?) {
            if (orderMineList != null)
                when (orderMineList.status) {
                    RentOrderState.Create -> {
                        gotoFindAndRent(orderMineList)
                        drivingMode.set(false)
                    }
                    RentOrderState.Taked -> {
                        drivingMode(orderMineList)
                        drivingMode.set(true)
                    }
                    RentOrderState.Return -> {
                        gotoPayRentOrder(orderMineList)
                        drivingMode.set(false)
                    }
                    else -> {
                        drivingMode.set(false)
                    }

                }
            else {
                drivingMode.set(false)
            }

        }

        override fun onGetCarError(e: Throwable) {
            ToastManager.showShortToast(context, "附近没有可用车辆哦，请选择其他网点")
        }

        override fun onGetCarSuccess(t: List<CarInfo>) {
            if (t.isNotEmpty()) {
                currentSelectedCarInfo.set(t[0])
                calDistanceAndTimeInfo()
                setupCurrentCarInfo(currentSelectedCarInfo.get()!!)
                findRoutToCar(t[0].lat, t[0].lng)
                hasUsableCar.set(true)
                checkUserVerifyState()
            } else {
                hasUsableCar.set(false)
                ToastManager.showShortToast(activity, "该网点没有该车型车辆，请选择其他车型")
                currentSelectedCarInfo.set(null)
                naviInfo.set("")
                findRoutToCar(carRentPresenter.carPoint?.lat!!, carRentPresenter.carPoint?.lng!!)
            }
            circle_indicator.setViewPager(viewPager_car_list)
        }
    }
    var drivingRouteOverlay: WalkRouteOverlay? = null
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
                                    var mDriveRouteResult = result
                                    val drivePath = mDriveRouteResult.paths[0] ?: return
                                    if (drivingRouteOverlay != null) {
                                        drivingRouteOverlay?.removeFromMap()
                                    }
                                    drivingRouteOverlay = WalkRouteOverlay(
                                            it, binding.map.map, drivePath,
                                            mDriveRouteResult.startPos,
                                            mDriveRouteResult.targetPos)
                                    drivingRouteOverlay?.setNodeIconVisibility(false)//设置节点marker是否显示
                                    drivingRouteOverlay?.setStartMarkerEnable(false)
                                    drivingRouteOverlay?.setEndMarkerEnable(false)

                                    drivingRouteOverlay?.addToMap()
                                    drivingRouteOverlay?.zoomToSpan()

                                } else if (result.paths == null) {
                                }

                            }
                        }
                    }
                })


    }

    private fun calDistanceAndTimeInfo() {
        activity?.let {
            MapUtil.getDriveTimeAndDistance(it, NaviLatLng(app.location.lat, app.location.lng), NaviLatLng(currentSelectedCarInfo.get()?.lat!!, currentSelectedCarInfo.get()?.lng!!), 2, object : MapUtil.GetDetailListener {
                override fun calculateSuccess(allLength: Int?, allTime: Int?) {

                    if (allLength != null && allTime != null) {
                        naviInfo.set("全程${LNTextUtil.getPriceText(allLength / 1000.0)}公里 步行${allTime / 60}分钟")
                    }
                }

            })
        }
    }

    private fun gotoPayRentOrder(orderMineList: OrderMineList) {
        val dialog = RentNoPayDialog()
        dialog.orderList = orderMineList
        dialog.show(childFragmentManager, "dialog_order_no_pay")
    }

    /**
     * 已经取车，正在行驶中
     */
    private fun drivingMode(orderMineList: OrderMineList) {
        carRentPresenter.getRentOrderDetail(orderMineList.id)
    }

    private fun gotoFindAndRent(orderMineList: OrderMineList) {
        ARouter.getInstance()
                .build(RouteMap.FindAndRentCar)
                .withString(String1, orderMineList.id)
                .navigation()
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
        initLayoutManager()
        initBottomSheet()
        setBinding()

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        binding.map.onCreate(savedInstanceState)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
//        refreshLocation()
        //通知 shy.car.sdk.travel.main.ui.MainNearCarListFragment中 刷新列表
        register(this)


        initData()

        if (User.instance.login) {
            carRentPresenter.getUnProgressOrder()
        }
        carRentPresenter.getUsableCarModel()
    }

    lateinit var carListViewPager: ViewPager
    private fun initData() {
        carListViewPager = binding.root.findViewById(R.id.viewPager_car_list)
        carListViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                var carInfo = carRentPresenter.carListAdapter.items[position]
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
    }

    private fun setBinding() {
        binding.fragment = this
        binding.p = carRentPresenter
        binding.user = User.instance
    }

    private fun initLayoutManager() {
        var layoutManager = LinearLayoutManager(activity)
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


    lateinit var bitmap: BitmapDescriptor
    /**
     * 初始化地图
     */
    private fun initMap() {

        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(13f), 1000, null)
        activity?.let { binding.map.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
        binding.map.map.setOnMarkerClickListener(
                {
                    var carPoint = findCarPoint(it.position)
                    carRentPresenter.getUsableCarList(carPoint)
                    it.showInfoWindow()
                    true
                }
        )

        binding.map.map.uiSettings.isZoomControlsEnabled = false
    }

    private fun findCarPoint(position: LatLng): NearCarPoint? {
        carPointList.map {
            if (it.lat == position.latitude && it.lng == position.longitude) {
                return it
            }
        }
        return null
    }

    //刷新位置
    fun refreshLocation() {
        Observable.create<com.base.location.Location>({
            AmapLocationManager.getInstance()
                    .getLocation(object : AmapOnLocationReceiveListener {
                        override fun onLocationReceive(ampLocation: AMapLocation, location: com.base.location.Location) {
                            app.changeCurrentLocation(location)
                            getCityCode(location)
                            moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
                            addUserLocationMarker()
                            carRentPresenter.getNetWorkList()
                            locationRefreshListener?.onLocationChange()
                        }
                    })
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

    }

    private fun getCityCode(location: Location) {
        if (app.cityList.isNotEmpty())
            app.cityList.map {
                if (app.location.cityName == it.cityName) {
                    app.location.cityCode = it.cityCode
                }
            }

    }

    private fun moveCameraAndShowLocation(latLng: LatLng) {
        binding.map.map.moveCamera(CameraUpdateFactory.changeLatLng(latLng))

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
        if (User.instance.getIsDeposit()) {
            showConfirmDialog()
        } else {


            if (BuildConfig.DEBUG) {
                if (currentSelectedCarInfo.get() != null)
                    showConfirmDialog()
                else {
                    ToastManager.showShortToast(activity, "当前没有可用车辆")
                }
            }
            //提示未交保证金
            val dialog = ARouter.getInstance()
                    .build(RouteMap.Dialog_Money_Verify)
                    .navigation() as XTBaseDialogFragment
            dialog.show(fragmentManager, "dialog_money_verify")

        }
    }

    private fun showConfirmDialog() {

        if (currentSelectedCarInfo.get() != null) {
            activity?.let {
                DialogManager.with(it, childFragmentManager)
                        .title("提示")
                        .message("确定租用该车辆？\n车型：${currentSelectedCarInfo.get()?.carModel}\n车牌：${currentSelectedCarInfo.get()?.plateNumber}\n颜色：${currentSelectedCarInfo.get()?.color}")
                        .leftButtonText("取消")
                        .rightButtonText("确定")
                        .onRightClick({ dialog, witch ->
                            if (currentSelectedCarInfo.get()?.netWork == null) {
                                currentSelectedCarInfo.get()
                                        ?.netWork = carPointList[0]
                            }
                            carRentPresenter.createRentCarOrder(currentSelectedCarInfo.get()?.carId!!, currentSelectedCarInfo.get()?.netWork?.id!!)
                        })
                        .show()
            }
        } else {
            ToastManager.showShortToast(activity, "当前没有可用车辆")
        }
    }

    fun changeZoom() {
        binding.edtZoom.text?.let { binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(it.toString().toFloat()), 1000, null) }
    }

    /**
     * 添加附近网点
     */
    private fun addCarPointToMap(list: List<NearCarPoint>) {
        binding.map.map.clear()
        markerList.clear()
        list.map {
            addMarkersToMap(it)
        }
        if (markerList.isNotEmpty()) {
            markerList[0].showInfoWindow()
        }
        addUserLocationMarker()
    }


    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap(point: NearCarPoint) {

        var marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .title(point.name)
                .snippet("附近可用车${point.usableCarsNum}辆")
                .position(LatLng(point.lat, point.lng))
                .displayLevel(1)
                .draggable(false))
        marker.isClickable = true
        markerList.add(marker)
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

    fun gotoDriving(oid: String) {
        ARouter.getInstance()
                .build(RouteMap.Driving)
                .withString(String1, oid)
                .navigation()
    }

    private fun checkUserVerifyState() {


        if (User.instance.login)
            if (User.instance.getIsDeposit() && User.instance.getIsIdentityAuth()) {
                carBottomSheet.peekHeight = resources.getDimensionPixelOffset(R.dimen._210dp)
//                    var params = binding.viewBottomSheet.layoutParams as CoordinatorLayout.LayoutParams
//                    params.height = resources.getDimensionPixelOffset(R.dimen._400dp)
//                    binding.viewBottomSheet.layoutParams = params
            } else {
                carBottomSheet.peekHeight = resources.getDimensionPixelOffset(R.dimen._260dp)
//                    var params = binding.viewBottomSheet.layoutParams as CoordinatorLayout.LayoutParams
//                    params.height = resources.getDimensionPixelOffset(R.dimen._450dp)
//                    binding.viewBottomSheet.layoutParams = params
            }
        else {
            carBottomSheet.peekHeight = resources.getDimensionPixelOffset(R.dimen._210dp)
        }
    }

    /**
     * 登录成功事件监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(success: LoginSuccess) {
        if (isRentClick) {
            isRentClick = false
            checkPromiseMoneyPay()
        }
        carRentPresenter.getNetWorkList()
        carRentPresenter.getUnProgressOrder()
        checkUserVerifyState()
    }


    /**
     * 定位成功 监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(change: LocationChange) {
        carRentPresenter.getNetWorkList()
        checkUserVerifyState()
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNearCarPointChange(nearCar: NearCarPoint) {
        markerList.map {
            if (it.position.latitude == nearCar.lat && it.position.longitude == nearCar.lng) {
                it.showInfoWindow()
//                moveCameraAndShowLocation(LatLng(it.position.latitude, it.position.longitude))
                carRentPresenter.getUsableCarList(nearCar)
            }
        }
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReturnCarSuccess(returnCar: RefreshRentCarState) {
        if (User.instance.login) {
            app.goHome()
            ARouter.getInstance()
                    .build(RouteMap.RentCarDetail)
                    .withString(String1, returnCar.oid)
                    .navigation()
            carRentPresenter.getUsableCarList(carRentPresenter.carPoint)
        }
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserInfoRefresh(refresh: RefreshUserInfoSuccess) {
        checkUserVerifyState()
    }
}