package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.constant.ParamsConstant.String2
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityTixianSuccessBinding

/**
 * create by Syokora at 2018/06/23
 * 提现成功
 */
@Route(path = RouteMap.TiXianSuccess)
class TiXianSuccessActivity : XTBaseActivity() {

    lateinit var binding: ActivityTixianSuccessBinding

    @Autowired(name = String1)
    @JvmField
    var method: String = ""

    @Autowired(name = String2)
    @JvmField
    var amount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tixian_success)
        ARouter.getInstance().inject(this)

        amount = "￥${amount}元"

        binding.ac = this
    }

    fun gotoRemain() {
        ARouter.getInstance().build(RouteMap.RemainDetail).navigation()
    }
}