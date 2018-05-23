package shy.car.sdk.travel.take.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.ObjectSerialisation
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.take.data.TakeOrderList

/**
 * create by 过期猫粮 at 2018/05/21
 * 选择车辆
 */
@Route(path = RouteMap.OrderTakeDetail)
class OrderTakeDetailActivity : XTBaseActivity() {
//    @Autowired(name = ObjectSerialisation.object1)
//    var takeOrderList: TakeOrderList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_take_detail)
        ARouter.getInstance()
                .inject(this)
//        var fragment = ARouter.getInstance()
//                .build(RouteMap.OrderTakeDetailFragment)
//                .withObject(ObjectSerialisation.object1, takeOrderList)
//                .navigation() as Fragment
//        supportFragmentManager.beginTransaction()
//                .add(R.id.frame_order_take_detail, fragment, "fragment_order_take_detail")
//                .commitAllowingStateLoss()
    }
}