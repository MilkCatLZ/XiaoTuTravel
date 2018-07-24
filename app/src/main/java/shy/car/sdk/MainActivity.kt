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
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_home_top.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.data.ReturnCarSuccess
import shy.car.sdk.app.eventbus.*
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.home.ui.DeliveryFragment
import shy.car.sdk.travel.interfaces.MapLocationRefreshListener
import shy.car.sdk.travel.interfaces.NearCarOpenListener
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.location.data.LocationChange
import shy.car.sdk.travel.main.ui.MainCitySelectFragment
import shy.car.sdk.travel.main.ui.MainNearNetWorkFragment
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.data.NearCarPoint
import shy.car.sdk.travel.rent.data.RentOrderState
import shy.car.sdk.travel.rent.ui.CarRentFragment
import shy.car.sdk.travel.rent.ui.CarRentOrderingFragment
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.ui.UserCenterFragment
import java.util.concurrent.TimeUnit

/**
 * create by LZ at 2018/05/22
 * 首页
 */
@Route(path = "/app/homeActivity")
class MainActivity : NearCarOpenListener,
        MapLocationRefreshListener,
        MainCitySelectFragment.CitySelectListener,
        XTBaseActivity(),
        MainNearNetWorkFragment.CancelListener {

    private val LOCATING = "正在定位..."

    var isNearVisible = ObservableBoolean(false)
    var isCitySelectVisible = ObservableBoolean(false)

    private var RentOrdering: String = "fragment_ordering"
    private var Delivery: String = "fragment_delivery"
    private var dilivery = false

    lateinit var binding: ActivityMainBinding

    private val carRentFragment = CarRentFragment()
    private val carRentOrderingFragment = CarRentOrderingFragment()
    private val deliveryFragment = DeliveryFragment()
    var citySelectFragment = MainCitySelectFragment()
    var nearCarListFragment = MainNearNetWorkFragment()
    var userCenterFragment = UserCenterFragment()

    var city = ObservableField<CurrentLocation>()

    override fun onCitySelected(location: CurrentLocation) {
        isCitySelectVisible.set(false)
        app.location = location.copy()
        city.set(app.location)
        eventBusDefault.post(LocationChange())
    }

    override fun onNearPointClick(nearPoint: NearCarPoint) {
        isNearVisible.set(false)
    }

    override fun onCancelClick() {
        isNearVisible.set(false)
    }

    override fun onSearchClosed() {
        isCitySelectVisible.set(false)
    }

    override fun onLocationChange() {
        city.set(app.location)
    }

    override fun onNearCarClick() {
        isNearVisible.set(!isNearVisible.get())
        //刷新附近网点列表
        eventBusDefault.post(RefreshCarPointList())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initPageFragment()
        checkPermissi()
        checkOrderState()
        register(this)
    }

    private fun checkOrderState() {

    }


    /**
     * 获取已预约订单
     */
    fun getUnProgressOrder() {
        ProgressDialog.showLoadingView(this@MainActivity)

        val observable = ApiManager.getInstance()
                .api.getRentOrderList("1", "1", 0, 1)
        val observer = object : Observer<List<OrderMineList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<OrderMineList>) {
                ProgressDialog.hideLoadingView(this@MainActivity)
                if (t.isNotEmpty()) {
                    changeFragment(carRentOrderingFragment, RentOrdering)
                    carRentOrderingFragment.oid = t[0].id
                    carRentOrderingFragment.getOrderDetail()
                    isCreate = false
                    rentMode = false
                } else {
                    changeFragment(carRentFragment, RouteMap.CarRent)
                    rentMode = true
                }
                isCreate = false
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(this@MainActivity)
                changeFragment(carRentFragment, RouteMap.CarRent)
                isCreate = false
                rentMode = true
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ac = this
        binding.user = User.instance

    }

    private fun initPageFragment() {

        carRentFragment.nearCarListener = this
        carRentFragment.locationRefreshListener = this

        carRentOrderingFragment.nearCarListener = this
        carRentOrderingFragment.locationRefreshListener = this

        citySelectFragment.listener = this
        nearCarListFragment.listener = this
        var transaction = supportFragmentManager.beginTransaction()


//        transaction.add(R.id.frame_fragment_content, carRentFragment, "car_rent")
//        transaction.add(R.id.frame_fragment_content, carRentFragment, "car_rent")

        transaction.add(R.id.frame_city_select, citySelectFragment, "citySelectFragment")
        transaction.add(R.id.frame_near_car_list, nearCarListFragment, "nearCarListFragment")
        transaction.add(R.id.frame_user_center, userCenterFragment, "user_center")

//        transaction.hide(deliveryFragment)
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

    fun changeToCarRentFragment() {
        dilivery = false
        onResume()
    }


    fun changeToDelivery() {
        dilivery = true
        changeFragment(deliveryFragment, Delivery)
    }

    fun onAvatarClick() {
        binding.drawer.openDrawer(Gravity.START)
        eventBusDefault.post(RefreshUserInfo())
    }

    /**
     * 刷新定位
     */
    private fun refreshLocation() {
        carRentFragment.refreshLocation()
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        if (supportFragmentManager.fragments.isEmpty() || !supportFragmentManager.fragments.contains(fragment))
            transaction.add(R.id.frame_fragment_content, fragment, tag)

        transaction.hide(carRentFragment)
        transaction.hide(deliveryFragment)
        transaction.hide(carRentOrderingFragment)
        transaction.show(fragment)
        try {
            transaction.commit()
        } catch (_: Exception) {

        }
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
            binding.drawer.isDrawerOpen(Gravity.START) -> {
                binding.drawer.closeDrawer(Gravity.START)
            }
            else -> {

                if (isBackPress) {
                    super.onBackPressed()
                } else {
                    isBackPress = true;
                    ToastManager.showShortToast(this@MainActivity, "再次点击返回退出")
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun createSuccess(success: CreateRentCarOrderSuccess) {
        rentMode = false
        changeFragment(carRentOrderingFragment, RentOrdering)
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun takeCarSuccess(take: TakeCarSuccess) {
        rentMode = false
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun returnSuccess(returnCar: ReturnCarSuccess) {
        rentMode = false
        app.goHome()
        ARouter.getInstance()
                .build(RouteMap.OrderPay)
                .withString(String1, returnCar.oid)
                .navigation()
    }

    /**
     * 近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun cancelOrder(cancel: RentOrderCanceled) {
        getUnProgressOrder()
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginSuccess(success: LoginSuccess) {
        getUnProgressOrder()
    }

    private var rentMode: Boolean = true
    private var isCreate: Boolean = true

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(success: PaySuccess) {
        if (!rentMode) {
            if (carRentOrderingFragment.detail != null && carRentOrderingFragment.detail?.status == RentOrderState.Return) {
                rentMode = true
            }
        }
    }

    /**
     * 附近网点列表 点击监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(logout: UserLogout) {
        rentMode = true
        switchMode()
    }

    override fun onResume() {
        super.onResume()
        switchMode()
    }

    fun switchMode() {
        if (!dilivery)
            if (!isCreate) {
                if (rentMode) {
                    changeFragment(carRentFragment, RouteMap.CarRent)
                } else {
                    changeFragment(carRentOrderingFragment, RentOrdering)
                }
            } else {
                if (User.instance.login) {
                    getUnProgressOrder()
                } else {
                    changeFragment(carRentFragment, RouteMap.CarRent)
                }
            }
    }
}
