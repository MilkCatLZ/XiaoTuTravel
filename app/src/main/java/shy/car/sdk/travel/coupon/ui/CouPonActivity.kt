package shy.car.sdk.travel.coupon.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by 过期猫粮 at 2018/06/18
 * 管理/选择银行
 * 选择结果通过EventBus抛出
 */
@Route(path = RouteMap.BankCard)
class CouPonActivity : XTBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     setContentView( R.layout.activity_coupon)

    }


}