package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityPaySuccessBinding

/**
 * create by 过期猫粮 at 2018/06/19
 * 保证金支付成功
 */
@Route(path = RouteMap.PaySuccess)
class PromiseMoneyPaySuccessActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityPaySuccessBinding>(this, R.layout.activity_pay_success)
    }
}