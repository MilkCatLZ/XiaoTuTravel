package shy.car.sdk.travel.bank.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityAddBackCarBinding
import shy.car.sdk.travel.bank.presenter.AddBankCarPresenter

/**
 * create by 过期猫粮 at 2018/06/18
 *
 */
@Route(path = RouteMap.AddBankCard)
class AddBankCardActivity : XTBaseActivity() {

    lateinit var binding: ActivityAddBackCarBinding
    lateinit var presenter: AddBankCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_back_car)
        binding.ac = this
    }

}