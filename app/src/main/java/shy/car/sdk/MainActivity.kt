package shy.car.sdk

import android.Manifest
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.lianni.mall.location.AmapLocationManager
import com.lianni.mall.location.AmapOnLocationReceiveListener
import com.lianni.mall.location.Location
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_home_top.*
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.interfaces.BottomSheetDragListener
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.main.ui.MainCitySelectFragment
import shy.car.sdk.travel.rent.data.NearCarList
import shy.car.sdk.travel.rent.presenter.NearCarPresenter
import shy.car.sdk.travel.rent.ui.CarRentFragment
import shy.car.sdk.travel.user.data.User


@Route(path = "/app/homeActivity")
class MainActivity : BottomSheetDragListener, NearCarOpenListener, MapLocationRefreshListener, MainCitySelectFragment.CitySelectListener, XTBaseActivity() {
    override fun onCitySelected(get: City) {
        isCitySelectVisible.set(false)

    }

    override fun onSearchClosed() {
        isCitySelectVisible.set(false)
    }

    override fun onLocationChange(city: City) {
        currentCity.set(city)
    }

    override fun onBottomSheetStateChange(bottomSheet: View, slideOffset: Float) {
        binding.viewBack.alpha = slideOffset
    }

    override fun onNearCarClick() {
        isNearVisible.set(!isNearVisible.get())
        refreshNearCarList()
    }


    private val LOCATING = "正在定位..."

    var isNearVisible = ObservableBoolean(false)
    var isCitySelectVisible = ObservableBoolean(false)

    lateinit var binding: ActivityMainBinding

    private val carRentFragment = ARouter.getInstance().build(RouteMap.CarRent).navigation() as CarRentFragment
    private val orderTakeFragment = ARouter.getInstance().build(RouteMap.OrderTake).navigation() as Fragment
    private val orderSendFragment = ARouter.getInstance().build(RouteMap.OrderSend).navigation() as Fragment
    var citySelectFragment = MainCitySelectFragment()

    var nearCarListPresenter: NearCarPresenter? = null
    var currentCity = ObservableField<City>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject
        ARouter.getInstance()
                .inject(this)
        initBinding()
        initPageFragment()

        checkPermissi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ac = this
        binding.user = User.instance
        citySelectFragment.listener = this
    }

    private fun initPageFragment() {

        carRentFragment.nearCarListener = this
        carRentFragment.bottomSheetListener = this

        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_fragment_content, carRentFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderTakeFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderSendFragment, tag)
        transaction.add(R.id.frame_city_select, citySelectFragment, "citySelectFragment")
        transaction.hide(orderSendFragment)
        transaction.hide(orderTakeFragment)
        transaction.commit()

        radio_car_rent.performClick()
    }

    private fun checkPermissi() {
        val per = RxPermissions(this)
        per.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        refreshLocation()
                    } else {

                    }
                }

    }


    fun onClick(v: View) {
        app.startLoginDialog(null, null)
    }

    fun changeToOrderTakeFragment() {
        changeFragment(orderTakeFragment)
    }

    fun changeToCarRentFragment() {
        changeFragment(carRentFragment)
    }

    fun changeToOrderSendFragment() {
        changeFragment(orderSendFragment)
    }

    fun onAvatarClick() {
        drawer.openDrawer(Gravity.START)
    }

    /**
     * 刷新定位
     */
    private fun refreshLocation() {
        AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                var city = City(location.name, getCode(location))
                city.lat = location.latitude
                city.lng = location.longitude
                currentCity.set(city)
            }
        })
    }

    private fun getCode(location: Location): String {
        return ""
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(carRentFragment)
        transaction.hide(orderTakeFragment)
        transaction.hide(orderSendFragment)
        transaction.show(fragment)
        transaction.commit()
    }


    fun onUserPicClick() {
        ARouter.getInstance()
                .build(RouteMap.UserDetail)
                .navigation()
        app.startLoginDialog(null, null)
    }

    fun onWalletClick() {
        ARouter.getInstance()
                .build(RouteMap.Wallet)
                .navigation()
    }

    fun onSettingClick() {
        ARouter.getInstance()
                .build(RouteMap.Setting)
                .navigation()
    }

    fun onKeFuClick() {
        ARouter.getInstance()
                .build(RouteMap.KeFu)
                .navigation()
    }

    fun onOrderClick() {
        ARouter.getInstance()
                .build(RouteMap.OrderMine)
                .navigation()
    }

    fun onMessageClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageCenter)
                .navigation()
    }

    fun onNearCityClick() {

    }

    fun onCityClick() {
        isCitySelectVisible.set(true)
    }


    private fun refreshNearCarList() {
        if (nearCarListPresenter == null) {
            nearCarListPresenter = NearCarPresenter(this, object : NearCarPresenter.CallBack {
                override fun getListSuccess(list: ArrayList<NearCarList>) {
                    carRentFragment.getNearCarListSuccess(list)
                }
            })
        }
        nearCarListPresenter?.getNearList(app.location?.latitude.toString(), app.location?.longitude.toString())
    }
}
