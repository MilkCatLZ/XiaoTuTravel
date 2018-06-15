package shy.car.sdk.travel.rent.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.rent.data.CarInfo

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
@Route(path = RouteMap.FindAndRentCar)
class FindAndRentCarActivity : XTBaseActivity() {


    @Autowired()
    @JvmField
    var carInfo: CarInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_and_rent_car)

        var fragment=supportFragmentManager.findFragmentById(R.id.fragment_find_and_rent)


    }
}