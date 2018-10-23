package shy.car.sdk.travel.pay.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityRentPaySuccessBinding
import shy.car.sdk.travel.order.data.RentOrderDetail

/**
 * create by 过期猫粮 at 2018/06/19
 * 保证金支付成功
 */
@Route(path = RouteMap.RentCarPaySuccess)
class RentPaySuccessActivity : XTBaseActivity() {

    @Autowired(name = Object1)
    @JvmField
    var detail: RentOrderDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance()
                .inject(this)
        var binding = DataBindingUtil.setContentView<ActivityRentPaySuccessBinding>(this, R.layout.activity_rent_pay_success)
        binding.ac = this
        binding.detail = detail
    }

    fun goHome() {
        app.goHome()
    }

    fun gotoDetail() {
        ARouter.getInstance()
                .build(RouteMap.RentCarDetail)
                .withString(String1, detail?.orderId)
                .navigation()
        finish()
    }
}