package shy.car.sdk

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_home_top.*
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.rent.ui.CarRentFragment
import shy.car.sdk.travel.rent.ui.OrderSendFragment
import shy.car.sdk.travel.rent.ui.OrderTakeFragment

@Route(path = "/app/homeActivity")
class MainActivity : XTBaseActivity() {

    lateinit var binding: ActivityMainBinding

    private val carRentFragment = CarRentFragment()
    private val orderTakeFragment = OrderTakeFragment()
    private val orderSendFragment = OrderSendFragment()


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

    private fun changeFragment(fragment: Fragment, tag: String) {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.hide(carRentFragment)
        transaction.hide(orderTakeFragment)
        transaction.hide(orderSendFragment)
        transaction.show( fragment)
        transaction.commit()
    }
}
