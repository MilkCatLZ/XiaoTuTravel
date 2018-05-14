package shy.car.sdk

import android.Manifest
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
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
import kotlinx.android.synthetic.main.layout_city_select.*
import kotlinx.android.synthetic.main.layout_home_top.*
import me.yokeyword.indexablerv.SimpleHeaderAdapter
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.location.LocationPresenter
import shy.car.sdk.travel.location.LocationPresenter.CallBack
import shy.car.sdk.travel.location.adapter.CityIndexAdapter
import shy.car.sdk.travel.location.data.City
import shy.car.sdk.travel.rent.ui.CarRentFragment
import shy.car.sdk.travel.rent.ui.OrderSendFragment
import shy.car.sdk.travel.rent.ui.OrderTakeFragment

@Route(path = "/app/homeActivity")
class MainActivity : XTBaseActivity() {

    private val LOCATING = "正在定位..."

    var isSearchVisible = ObservableBoolean(false)
    val cityName = ObservableField<String>(LOCATING)
    lateinit var binding: ActivityMainBinding

    private val carRentFragment = CarRentFragment()
    private val orderTakeFragment = OrderTakeFragment()
    private val orderSendFragment = OrderSendFragment()

    lateinit var locationPresenter: LocationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject
        ARouter.getInstance()
                .inject(this)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.ac = this
        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_fragment_content, carRentFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderTakeFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderSendFragment, tag)
        transaction.commit()

        radio_car_rent.performClick()

        checkPermissi()
    }

    private fun checkPermissi() {
        val per = RxPermissions(this)
        per.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        initLocation()
                    } else {

                    }
                }

    }

    /**
     * 初始化城市列表
     */
    private fun initLocation() {

        locationPresenter = LocationPresenter(this, object : CallBack {
            override fun getCitySuccess(list: java.util.ArrayList<City>) {
                //添加数据
                indexable_layout.setLayoutManager(LinearLayoutManager(this@MainActivity))
                val adapter = CityIndexAdapter(this@MainActivity)
                indexable_layout.setAdapter(adapter)
                adapter.setDatas(list)
                adapter.setOnItemContentClickListener { v, originalPosition, currentPosition, entity ->

                }


                val hotCity = ArrayList<City>()
                for (i in 0..5) {
                    hotCity.add(City("南宁$i", i.toString()))
                }
                //添加头部
                val headerAdapter = SimpleHeaderAdapter<City>(adapter, "热","热门城市", hotCity)
                indexable_layout.addHeaderAdapter(headerAdapter)



            }
        })
        locationPresenter.getCity()
    }


    fun onClick(v: View) {
        app.startLoginDialog()
    }

    fun changeToOrderTakeFragment() {
        changeFragment(orderTakeFragment, "fragment_order_take")
    }

    fun changeToCarRentFragment() {
        changeFragment(carRentFragment, "fragment_car_rent")
    }

    fun changeToOrderSendFragment() {
        changeFragment(orderSendFragment, "fragment_order_send")
    }

    fun onAvatarClick() {
        drawer.openDrawer(Gravity.LEFT)
    }


    fun onCityClick() {
        isSearchVisible.set(!isSearchVisible.get())
        if (isSearchVisible.get()) {
            cityName.set(LOCATING)
            AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
                override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                    cityName.set(location.city)
                }
            })
        }
    }

    private fun changeFragment(fragment: Fragment, tag: String) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.hide(carRentFragment)
        transaction.hide(orderTakeFragment)
        transaction.hide(orderSendFragment)
        transaction.show(fragment)
        transaction.commit()
    }
}
