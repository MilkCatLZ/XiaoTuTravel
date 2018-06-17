package shy.car.sdk.travel.pay.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by lz at 2018/06/17
 *  充值
 */
@Route(path = RouteMap.Pay)
class PayActivity :XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
    }
}