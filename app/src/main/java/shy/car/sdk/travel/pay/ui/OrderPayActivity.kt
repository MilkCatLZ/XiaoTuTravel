package shy.car.sdk.travel.pay.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap

/**
 * create by lz at 2018/06/17
 * 租车订单支付
 */
@Route(path = RouteMap.OrderPay)
class OrderPayActivity : XTBaseActivity() {

    @JvmField
    @Autowired(name = String1)
    var oid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_pay)
        ARouter.getInstance()
                .inject(this)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_order_pay) as OrderPayFragment
        fragment.setOid(oid)
    }
}