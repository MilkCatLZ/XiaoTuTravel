package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import kotlinx.android.synthetic.main.fragment_car_rent.*
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.eventbus.RefreshNearCarList
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentCarRentBinding
import shy.car.sdk.travel.interfaces.BottomSheetDragListener
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import shy.car.sdk.travel.rent.presenter.CarRentPresenter
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase


/**
 * create by LZ at 2018/05/11
 * 租车
 */
@Route(path = RouteMap.CarRent)
class CarRentFragment : XTBaseFragment() {

    private lateinit var binding: FragmentCarRentBinding
    private lateinit var carRentPresenter: CarRentPresenter

    var nearCarListener: NearCarOpenListener? = null
    var bottomSheetListener: BottomSheetDragListener? = null
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
        binding.p = carRentPresenter
        binding.fragment = this

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        binding.map.onCreate(savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewBottomSheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheetListener?.onBottomSheetStateChange(bottomSheet, slideOffset)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        addCarLocation()
        refreshLocation()
        eventBusDefault.postSticky(RefreshNearCarList())
    }

    lateinit var bitmap: BitmapDescriptor
    /**
     * 初始化地图
     */
    private fun initMap() {
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        map.map.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)
        map.map.setOnMyLocationChangeListener {
            var geocoderSearch = GeocodeSearch(context)
            val query = RegeocodeQuery(LatLonPoint(it.latitude, it.longitude), 200f, GeocodeSearch.AMAP)
            geocoderSearch.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(p0: RegeocodeResult?, p1: Int) {
                    locationRefreshListener?.onLocationChange(City(p0?.regeocodeAddress?.city!!, ""))
                }

                override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {

                }
            })
            geocoderSearch.getFromLocationAsyn(query)
        }
        activity?.let { map.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
    }

    /**
     * 刷新定位
     */
    fun refreshLocation() {
        var myLocationStyle = MyLocationStyle().myLocationIcon(bitmap)
                .anchor(0.5f, 0.5f)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        myLocationStyle.interval(10000)
        map.map.setMyLocationStyle(myLocationStyle)
        map.map.isMyLocationEnabled = true// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false

    }


    override fun onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.map.onDestroy()
        bottomSheetListener = null
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


    /**
     * 租车点击事件
     */
    fun onRentClick() {
        val user = User.instance
        when (user.isLogin && BuildConfig.DEBUG) {
            true -> checkPromiseMoneyPay()
            else -> app.startLoginDialog(null, null)
        }

    }

    /**
     * 租车点击-检查是否认证
     */
    private fun checkPromiseMoneyPay() {
        if (User.instance.promiseMoney == UserBase.PromissState.MONEY_PAYED) {
            ARouter.getInstance()
                    .build(RouteMap.MessageCenter)
                    .navigation()
        } else {
            val dialog = ARouter.getInstance()
                    .build(RouteMap.Dialog_Money_Verify)
                    .navigation() as XTBaseDialogFragment
            dialog.show(fragmentManager, "dialog_money_verify")
            if (BuildConfig.DEBUG)
                ARouter.getInstance()
                        .build(RouteMap.MessageCenter)
                        .navigation()
        }
    }

    fun changeZoom() {
        edt_zoom.text?.let { map.map.animateCamera(CameraUpdateFactory.zoomTo(it.toString().toFloat()), 1000, null) }
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

        var marker = map.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
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
}