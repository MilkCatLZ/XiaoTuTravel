package shy.car.sdk.travel.rent.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.route.RouteMap

/**
 * create by lz at 2018/06/05
 * 租车详情
 */
@Route(path = RouteMap.RentCarDetail)
class RentCarOrderDetailActivity : XTBaseActivity() {


    @Autowired(name = ParamsConstant.String1)
    @JvmField
    var orderID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent_car_order_detail)
        ARouter.getInstance()
                .inject(this)
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_find_and_rent) as RentCarOrderDetailFragment

        if (fragment != null)
            fragment.orderID = orderID
    }
}