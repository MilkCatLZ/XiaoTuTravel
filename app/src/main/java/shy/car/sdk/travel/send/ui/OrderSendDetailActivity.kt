package shy.car.sdk.travel.send.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.take.data.TakeOrderList
import shy.car.sdk.travel.take.ui.OrderTakeDetailFragment
import shy.car.sdk.R
import shy.car.sdk.travel.send.data.OrderSendList

/**
 * create by 过期猫粮 at 2018/05/21
 */
@Route(path = RouteMap.OrderSendDetail)
open class OrderSendDetailActivity : XTBaseActivity() {


    @Autowired(name = "takeOrderList")
    @JvmField
    var sendOrderList: OrderSendList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_send_detail)
        ARouter.getInstance()
                .inject(this)
        var fragment = ARouter.getInstance()
                .build(RouteMap.OrderSendDetailFragment)
                .withObject("sendOrderList", sendOrderList)
                .navigation()
        supportFragmentManager.beginTransaction()
                .add(R.id.frame_order_take_detail, fragment, "fragment_order_send_detail")
                .commitAllowingStateLoss()
    }
}