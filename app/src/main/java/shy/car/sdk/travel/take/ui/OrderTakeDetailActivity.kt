package shy.car.sdk.travel.take.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.take.data.TakeOrderList

/**
 * create by 过期猫粮 at 2018/05/21
 */
@Route(path = RouteMap.OrderTakeDetail)
open class OrderTakeDetailActivity : XTBaseActivity() {


    @Autowired(name = Object1)
    @JvmField
    var takeOrderList: TakeOrderList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_take_detail)
        ARouter.getInstance()
                .inject(this)
        var fragment = ARouter.getInstance()
                .build(RouteMap.OrderTakeDetailFragment)
                .withObject(Object1, takeOrderList)
                .navigation() as OrderTakeDetailFragment
        supportFragmentManager.beginTransaction()
                .add(R.id.frame_order_take_detail, fragment, "fragment_order_take_detail")
                .commitAllowingStateLoss()
    }
}