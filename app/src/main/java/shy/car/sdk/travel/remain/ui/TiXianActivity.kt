package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonObject
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.*
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityTixianBinding
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.remain.presenter.TiXianPresenter
import shy.car.sdk.travel.user.data.User


@Route(path = RouteMap.TiXian)
class TiXianActivity : XTBaseActivity(), TiXianPresenter.CallBack {
    override fun onTiXianSuccess(t: JsonObject) {
        ARouter.getInstance().build(RouteMap.TiXianSuccess).withString(String1, presenter.selectedBankCard.get()?.type).withString(String2, presenter.amount.get()).navigation()

        finish()
    }

    lateinit var binding: ActivityTixianBinding
    lateinit var presenter: TiXianPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tixian)
        presenter = TiXianPresenter(this, this)

        binding.ac = this
        binding.presenter = presenter

        register(this)

    }

    fun tixianAll() {
        presenter.amount.set(User.instance.balance.toString())
    }

    fun selectBankCard() {
        ARouter.getInstance().build(RouteMap.BankCard).withBoolean(Boolean1, true).navigation()
    }

    fun tixianClick() {
        presenter.tixian()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBankCardSelected(card: BankCard) {
        presenter.selectedBankCard.set(card)
    }
}