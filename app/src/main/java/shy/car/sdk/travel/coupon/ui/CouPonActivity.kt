package shy.car.sdk.travel.coupon.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Boolean1
import shy.car.sdk.app.route.RouteMap

/**
 * create by 过期猫粮 at 2018/06/18
 * 优惠券
 *
 */
@Route(path = RouteMap.Coupon)
class CouPonActivity : XTBaseActivity() {

    @Autowired(name = Boolean1)
    @JvmField
    var selectMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)

        ARouter.getInstance().inject(this)

        var fragment=supportFragmentManager.findFragmentById(R.id.fragment_coupon) as CouPonFragment
        fragment.setSelectedMode(selectMode)
    }


}