package shy.car.sdk.travel.rent.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.eventbus.UnLockSuccess
import shy.car.sdk.app.route.RouteMap

/**
 * create by lz at 2018/06/05
 * 找车取车
 */
@Route(path = RouteMap.FindAndRentCar)
class FindAndRentCarActivity : XTBaseActivity() {


    @Autowired(name = String1)
    @JvmField
    var order: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_and_rent_car)
        ARouter.getInstance()
                .inject(this)

        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_find_and_rent) as FindAndRentCarFragment
        fragment.oid = order


        eventBusDefault.register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun unLockSuccess(unLock: UnLockSuccess) {
        finish()
    }
}