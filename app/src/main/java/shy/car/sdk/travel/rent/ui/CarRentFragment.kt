package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
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
import com.base.databinding.DataBindingAdapter
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import com.base.util.DialogManager
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_car_rent.*
import kotlinx.android.synthetic.main.layout_car_rent_bottomsheet.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.eventbus.RefreshCarPointList
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentCarRentBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.location.data.LocationChange
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
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
    var currentSelectedCarInfo = ObservableField<CarInfo>()

    var markerList = ArrayList<Marker>()
    private var carPointList = ArrayList<NearCarPoint>()

    private val callBack = object : CarRentPresenter.CallBack {
        override fun onCreateRentOrderSuccess() {
            ARouter.getInstance()
                    .build(RouteMap.FindAndRentCar)
                    .withObject(Object1, currentSelectedCarInfo.get())
                    .navigation()
        }

        override fun onGetCarError(e: Throwable) {

        }

        override fun onGetCarSuccess(t: List<CarInfo>) {
            if (t.isNotEmpty())
                currentSelectedCarInfo.set(t[0])
        }
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
        refreshLocation()
        //通知 shy.car.sdk.travel.main.ui.MainNearCarListFragment中 刷新列表
        register(this)


        initData()
    }

    lateinit var carListViewPager: ViewPager
    private fun initData() {
        //通知刷新列表
//        var disposable: Disposable? = null
//        Observable.interval(1, 0, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(object : Observer<Long> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        disposable = d
//                    }
//
//                    override fun onNext(t: Long) {
//                        if (app.location.cityCode.isNotEmpty()) {
//                            eventBusDefault.postSticky(RefreshCarPointList())
//                            disposable?.dispose()
//                        }
//                    }
//
//                    override fun onError(e: Throwable) {
//
//                    }
//                })


        carListViewPager = binding.root.findViewById(R.id.viewPager_car_list)
        carListViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                var carInfo = carRentPresenter.carListAdapter.items[position]
                val adapter = DataBindingAdapter<CarInfo.DiscountsBean.DurationBean>(R.layout.item_car_discount, BR.discount, null)
                adapter.setItems(carInfo.discounts?.duration, 1)
                recyclerView_car_discount.adapter = adapter
            }
        })
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

    private fun initBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewBottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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
                            locationRefreshListener?.onLocationChange()
                            it.onNext(location)
                            eventBusDefault.post(RefreshCarPointList())
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
        if (User.instance.isDeposit()) {
            showConfirmDialog()
        } else {
            //提示未交保证金

            if (BuildConfig.DEBUG) {
                if (currentSelectedCarInfo.get() != null)
                    showConfirmDialog()
                else {
                    ToastManager.showShortToast(activity, "当前没有可用车辆")
                }
            } else {
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
    }

    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap(point: NearCarPoint) {

        var marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .snippet(point.name)
                .snippet("附近可用车${point.usableCarsNum}辆")
                .position(LatLng(point.lat, point.lng))
                .draggable(false))
        marker.isClickable = true
        marker.showInfoWindow()
        markerList.add(marker)
    }

    fun onNearCarClick() {
        nearCarListener?.onNearCarClick()
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
    }


    /**
     * 附近网点刷新事件监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(list: List<NearCarPoint>) {
        if (list.isNotEmpty()) {
            if (carPointList.isEmpty()) {
                carRentPresenter.getUsableCarList(list[0])
            }
            this.carPointList.clear()
            this.carPointList.addAll(list)
            addCarPointToMap(list)


        } else if (BuildConfig.DEBUG) {
            var l = ArrayList<NearCarPoint>()
            for (i in 0..5) {

            }
        }
    }

    /**
     * 定位成功 监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(change: LocationChange) {
        moveCameraAndShowLocation(LatLng(app.location.lat, app.location.lng))
        eventBusDefault.post(RefreshCarPointList())
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNearCarPointChange(nearCar: NearCarPoint) {
        markerList.map {
            if (it.position.latitude == nearCar.lat && it.position.longitude == nearCar.lng) {
                it.showInfoWindow()
                moveCameraAndShowLocation(LatLng(it.position.latitude, it.position.longitude))
                carRentPresenter.getUsableCarList(nearCar)
            }
        }
    }
}