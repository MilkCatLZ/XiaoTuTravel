package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Phone
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityPaySuccessBinding

/**
 * create by 过期猫粮 at 2018/06/19
 * 保证金支付成功
 */
@Route(path = RouteMap.PaySuccess)
class PromiseMoneyPaySuccessActivity : XTBaseActivity() {


    @Autowired(name = Object1)
    @JvmField
    var paySuccess: PaySuccess? = null

    val payMethodText = ObservableField<String>("")
    val price = ObservableField<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        payMethodText.set(paySuccess?.payMethod?.name)
        price.set(paySuccess?.price)
        var binding = DataBindingUtil.setContentView<ActivityPaySuccessBinding>(this, R.layout.activity_pay_success)
        binding.ac = this
    }

    fun goHome() {
        app.goHome()
    }

    fun goPromiseMoneyDetail() {
        ARouter.getInstance()
                .build(RouteMap.PromiseMoneyDetail)
                .navigation()
        finish()
    }

    fun call() {
        Phone.call(this, app.servicePhone)
    }
}