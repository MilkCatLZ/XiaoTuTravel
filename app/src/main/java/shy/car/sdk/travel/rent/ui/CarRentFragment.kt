package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_car_rent.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.eventbus.RefreshNearCarList
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentCarRentBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
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

    private val callBack = object : CarRentPresenter.CallBack {}
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

    private fun setBinding() {
        binding.p = carRentPresenter
        binding.fragment = this
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        addCarLocation()
        refreshLocation()
        //通知 shy.car.sdk.travel.main.ui.MainNearCarListFragment中 刷新列表
        register(this)
        eventBusDefault.postSticky(RefreshNearCarList())
    }

    lateinit var bitmap: BitmapDescriptor
    /**
     * 初始化地图
     */
    private fun initMap() {

        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)
        activity?.let { binding.map.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
    }

    fun refreshLocation() {
        Observable.create<com.base.location.Location>({
            AmapLocationManager.getInstance()
                    .getLocation(object : AmapOnLocationReceiveListener {
                        override fun onLocationReceive(ampLocation: AMapLocation, location: com.base.location.Location) {
                            app.changeCurrentLocation(location)
                            moveCameraAndShowLocation(location)
                            locationRefreshListener?.onLocationChange()
                            it.onNext(location)
                        }
                    })
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addMarkersToMap(LatLng(it.latitude, it.longitude))
                })

    }

    private fun moveCameraAndShowLocation(location: com.base.location.Location) {
        binding.map.map.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(location.latitude, location.longitude)))
        binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat))
                .anchor(0.5f, 1.0f)
                .snippet(location.address)
                .position(LatLng(location.latitude, location.longitude))
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
        when (user.isLogin) {
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
        if (User.instance.promiseMoneyState) {
            ARouter.getInstance()
                    .build(RouteMap.RentCarDetail)
                    .navigation()
        } else {
            //提示未交保证金
            val dialog = ARouter.getInstance()
                    .build(RouteMap.Dialog_Money_Verify)
                    .navigation() as XTBaseDialogFragment
            dialog.show(fragmentManager, "dialog_money_verify")
        }
    }

    fun changeZoom() {
        binding.edtZoom.text?.let { binding.map.map.animateCamera(CameraUpdateFactory.zoomTo(it.toString().toFloat()), 1000, null) }
    }


    fun addCarLocation() {
        addMarkersToMap(LatLng(22.817746, 108.36637))
        addMarkersToMap(LatLng(22.873487, 108.275277))
        addMarkersToMap(LatLng(22.823181, 108.300745))
    }

    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap(latlng: LatLng) {

        var marker = binding.map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .snippet("小兔")
                .snippet("附近可用车${latlng.latitude}辆")
                .position(latlng)
                .draggable(false))
        marker.showInfoWindow()

    }

    fun onNearCarClick() {
        nearCarListener?.onNearCarClick()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(success: LoginSuccess) {
        if (isRentClick) {
            isRentClick = false
            checkPromiseMoneyPay()
        }
    }
}