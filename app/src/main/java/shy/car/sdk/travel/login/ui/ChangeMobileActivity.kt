package shy.car.sdk.travel.login.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.eventbus.ChangeMobileStep1Success
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityChangeMobileBinding

/**
 * create by LZ at 2018/07/03
 * 修改手机号
 */
@Route(path = RouteMap.ChangeMobile)
class ChangeMobileActivity : XTBaseActivity() {

    val step1 = ChangeMobileStep1Fragment()
    val step2 = ChangeMobileStep2Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityChangeMobileBinding>(this, R.layout.activity_change_mobile)
        register(this)
        try {
            supportFragmentManager.beginTransaction()
                    .add(R.id.frame_change_mobile, step1, "fragment_step1")
                    .commit()
        } catch (e: Exception) {
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun set1Success(step: ChangeMobileStep1Success) {
        try {
            supportFragmentManager.beginTransaction()
                    .hide(step1)
            supportFragmentManager.beginTransaction()
                    .add(R.id.frame_change_mobile, step2, "fragment_step2")
                    .commit()
        } catch (e: Exception) {
        }

    }
}