package shy.car.sdk.travel.pay.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by 过期猫粮 at 2018/05/21
 * 保证金明细
 */
@Route(path = RouteMap.PromiseMoneyDetail)
class PromiseMoneyDetailActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promise_money_detail)
    }
}