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
import com.base.util.ToastManager
import com.lianni.mall.location.AmapLocationManager
import com.lianni.mall.location.AmapOnLocationReceiveListener
import com.lianni.mall.location.Location
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_home_top.*
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.main.ui.MainCitySelectFragment
import shy.car.sdk.travel.main.ui.MainNearCarListFragment
import shy.car.sdk.travel.rent.ui.CarRentFragment
import shy.car.sdk.travel.user.data.User
import java.util.concurrent.TimeUnit

/**
 * create by LZ at 2018/05/22
 * 首页
 */
@Route(path = "/app/homeActivity")
class MainActivity : NearCarOpenListener, MapLocationRefreshListener, MainCitySelectFragment.CitySelectListener, XTBaseActivity(), MainNearCarListFragment.CancelListener {
    override fun onCancelClick() {
        isNearVisible.set(false)
    }

    override fun onCitySelected(city: City) {
        isCitySelectVisible.set(false)
        currentCity.set(city)
    }

    override fun onSearchClosed() {
        isCitySelectVisible.set(false)
    }

    override fun onLocationChange(city: City) {
        currentCity.set(city)
    }

    override fun onNearCarClick() {
        isNearVisible.set(!isNearVisible.get())
    }


    private val LOCATING = "正在定位..."

    var isNearVisible = ObservableBoolean(false)
    var isCitySelectVisible = ObservableBoolean(false)

    lateinit var binding: ActivityMainBinding

    private val carRentFragment = ARouter.getInstance().build(RouteMap.CarRent).navigation() as CarRentFragment
    private val orderTakeFragment = ARouter.getInstance().build(RouteMap.OrderTake).navigation() as Fragment
    private val orderSendFragment = ARouter.getInstance().build(RouteMap.OrderSend).navigation() as Fragment
    var citySelectFragment = MainCitySelectFragment()
    var nearCarListFragment = MainNearCarListFragment()


    var currentCity = ObservableField<City>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initPageFragment()
        checkPermissi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ac = this
        binding.user = User.instance

    }

    private fun initPageFragment() {

        carRentFragment.nearCarListener = this
        citySelectFragment.listener = this
        nearCarListFragment.listener = this
        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_fragment_content, carRentFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderTakeFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderSendFragment, tag)
        transaction.add(R.id.frame_city_select, citySelectFragment, "citySelectFragment")
        transaction.add(R.id.frame_near_car_list, nearCarListFragment, "nearCarListFragment")
        transaction.hide(orderSendFragment)
        transaction.hide(orderTakeFragment)
        transaction.commit()

        radio_car_rent.performClick()
    }

    private fun checkPermissi() {
        val per = RxPermissions(this)
        per.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(granted: Boolean) {
                        if (granted) {
                            refreshLocation()
                        } else {

                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        per.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()

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
                var city = City(location.city, getCode(location))
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




    fun onMessageClick() {
        ARouter.getInstance()
                .build(RouteMap.MessageCenter)
                .navigation()
    }

    fun onCityClick() {
        isCitySelectVisible.set(true)
        citySelectFragment.getLocation()
    }

    var isBackPress = false

    override fun onBackPressed() {
        when {
            isNearVisible.get() -> {
                isNearVisible.set(false)
            }
            isCitySelectVisible.get() -> {
                isCitySelectVisible.set(false)
            }
            else -> {

                if (isBackPress) {
                    super.onBackPressed()
                } else {
                    isBackPress = true;
                    ToastManager.showShortToast(this@MainActivity, "再次点击返回退出");
                    Observable.timer(2, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(Consumer<Long> {
                                isBackPress = false
                            })
                }
            }
        }

    }
}
