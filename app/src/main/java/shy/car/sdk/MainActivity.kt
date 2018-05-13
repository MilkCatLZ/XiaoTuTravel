package shy.car.sdk

import android.Manifest
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_city_select.*
import kotlinx.android.synthetic.main.layout_home_top.*
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

    var isSearchVisible = ObservableBoolean(false)

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
        per.request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
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
                indexable_layout.setLayoutManager(LinearLayoutManager(this@MainActivity))
                val adapter = CityIndexAdapter(this@MainActivity)
                indexable_layout.setAdapter(adapter)
                adapter.setDatas(list)
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
