package shy.car.sdk.travel.rent.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.data.CarInfo

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
@Route(path = RouteMap.ReturnCar)
class ReturnCarActivity : XTBaseActivity() {


    @Autowired(name = String1)
    @JvmField
    var order: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_car)
        ARouter.getInstance()
                .inject(this)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_return_car) as ReturnCarFragment
        fragment.setOid(order)

    }
}