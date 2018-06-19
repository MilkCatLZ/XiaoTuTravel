package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityUnlockCarBinding
import shy.car.sdk.travel.rent.presenter.UnLockCarPresenter

/**
 * create by 过期猫粮 at 2018/06/19
 *
 */
@Route(path = RouteMap.UnLockCar)
class UnLockCarActivity : XTBaseActivity() {

    lateinit var presenter: UnLockCarPresenter
    var isLeft = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityUnlockCarBinding>(this, R.layout.activity_unlock_car)
        binding.ac = this

        presenter = UnLockCarPresenter(this)
    }

    fun submitAndUnLock() {

    }

    fun leftPhotoClick() {
        isLeft = true
    }

    fun rightPhotoClick() {
        isLeft = false
    }
}