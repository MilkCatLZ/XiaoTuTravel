package shy.car.sdk.travel.bank.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityBankCardManagerBinding

/**
 * create by 过期猫粮 at 2018/06/18
 * 管理/选择银行
 */
@Route(path = RouteMap.BankCard)
class BankCardManagerActivity : XTBaseActivity() {

    @Autowired(name = ParamsConstant.Boolean1)
    @JvmField
    var selectMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBankCardManagerBinding>(this, R.layout.activity_bank_card_manager)
        var fragment = supportFragmentManager.findFragmentById(R.id.fragment_bank_card_manager) as BankCarManagerFragment

        ARouter.getInstance()
                .inject(this)
        fragment.selectMode = selectMode
    }


}