package shy.car.sdk.travel.pay.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by 过期猫粮 at 2018/05/21
 * 选择车辆
 */
@Route(path = RouteMap.CarTypeSelect)
class CarTypeSelectActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_type_select)
    }
}