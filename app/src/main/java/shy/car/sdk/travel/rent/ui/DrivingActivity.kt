package shy.car.sdk.travel.rent.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap

/**
 * create by 过期猫粮 at 2018/06/24
 *
 */
@Route(path = RouteMap.Driving)
class DrivingActivity : XTBaseActivity() {

    @Autowired(name = String1)
    @JvmField
    var oid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driving)

        ARouter.getInstance().inject(this)

        var fragment=supportFragmentManager.findFragmentById(R.id.fragment_driving) as DrivingFragment
        fragment.setOid(oid)
    }
}