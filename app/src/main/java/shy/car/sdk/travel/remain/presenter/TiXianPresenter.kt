package shy.car.sdk.travel.remain.presenter

import android.content.Context
import android.databinding.ObservableField
import android.text.TextUtils
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.user.data.User

class TiXianPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    var remainText = ObservableField<String>("")
    var amount = ObservableField<String>("")
    var selectedBankCard = ObservableField<BankCard>()

    interface CallBack {
        fun onTiXianSuccess(t: JsonObject)

    }

    init {
        remainText.set("可提现金额 ￥${User.instance.balance}")
    }

    fun tixian() {
        if (checkInput()) return
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance().api.tixian(selectedBankCard.get()?.id!!, amount.get()!!)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onTiXianSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "提现失败")
            }

        }
        ApiManager.getInstance().toSubscribe(observable, observer)
    }

    private fun checkInput(): Boolean {
        if (selectedBankCard.get() == null) {
            ToastManager.showShortToast(context, "请选择银行卡")
            return false
        }
        if (TextUtils.isEmpty(amount.get())) {
            ToastManager.showShortToast(context, "请输入提现金额")
            return false
        }

        return true
    }

}