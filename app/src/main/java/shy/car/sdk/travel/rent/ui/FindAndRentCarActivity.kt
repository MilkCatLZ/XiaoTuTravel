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
import shy.car.sdk.travel.rent.data.CarInfo

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
@Route(path = RouteMap.FindAndRentCar)
class FindAndRentCarActivity : XTBaseActivity() {


    @Autowired(name = Object1)
    @JvmField
    var carInfo = CarInfo()
    @Autowired(name = String1)
    @JvmField
    var orderID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_and_rent_car)
        ARouter.getInstance()
                .inject(this)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_find_and_rent) as FindAndRentCarFragment
        fragment.carInfo = carInfo
        fragment.orderID = orderID

    }
}