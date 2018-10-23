package shy.car.sdk.travel.pay.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.Phone
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityReturnPromisemoneySuccessBinding

/**
 * create by 过期猫粮 at 2018/06/19
 * 保证金支付成功
 */
@Route(path = RouteMap.ReturnPromiseMoneySuccess)
class ReturnPromiseMoneySuccessActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityReturnPromisemoneySuccessBinding>(this, R.layout.activity_return_promisemoney_success)
        binding.ac = this

    }

    fun goHome() {
        app.goHome()
    }

    fun call() {
        Phone.call(this, app.servicePhone)
    }
}