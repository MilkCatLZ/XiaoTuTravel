package shy.car.sdk.travel.pay.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by LZ at 2018/06/28
 * 保证金退还
 */
@Route(path = RouteMap.ReturnPromiseMoney)
class PromiseMoneyReturnActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_promise_money)
    }
}