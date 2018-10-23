package shy.car.sdk.travel.bank.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.ToastManager
import com.google.gson.JsonObject
import okhttp3.Response
import okhttp3.ResponseBody
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityAddBackCarBinding
import shy.car.sdk.travel.bank.data.BankType
import shy.car.sdk.travel.bank.dialog.BankTypeSelectDialogFragment
import shy.car.sdk.travel.bank.presenter.AddBankCarPresenter

/**
 * create by 过期猫粮 at 2018/06/18
 *
 */
@Route(path = RouteMap.AddBankCard)
class AddBankCardActivity : XTBaseActivity(),
        AddBankCarPresenter.CallBack {
    override fun onError(e: Throwable) {

    }

    override fun onSubmitSuccess(t: JsonObject) {
        ToastManager.showShortToast(this, "添加成功")
        finish()
    }

    lateinit var binding: ActivityAddBackCarBinding
    lateinit var presenter: AddBankCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_back_car)
        presenter = AddBankCarPresenter(this, this)

        binding.ac = this
        binding.presenter = presenter
    }


    fun selectBankCardType() {
        var dialog = BankTypeSelectDialogFragment()
        dialog.listener = object : BankTypeSelectDialogFragment.OnBankTypeSelectedListener {
            override fun onBankTypeSelected(bankType: BankType) {
                presenter.bankType.set(bankType)
            }

        }
        dialog.show(supportFragmentManager, "dialog_select_bank_type")
    }

    fun submit() {
        presenter.submit()
    }
}