package shy.car.sdk.travel.bank.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.Response
import okhttp3.ResponseBody
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.bank.data.BankType
import shy.car.sdk.travel.user.data.User

class AddBankCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    val bankType = ObservableField<BankType>()
    val name = ObservableField<String>()
    val cardNum = ObservableField<String>()
    val bankName = ObservableField<String>()
    val phone = ObservableField<String>()

    interface CallBack {
        fun onError(e: Throwable)
        fun onSubmitSuccess(t: retrofit2.Response<Void>)

    }


    fun submit() {
        if (!checkInput()) {
            return
        }

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()

        val observable = ApiManager.getInstance()
                .api.addBankCard(User.instance.phone, bankType.get()?.id!!, bankName.get()!!, name.get()!!, cardNum.get()!!, phone.get()!!)
        val observer = object : Observer<retrofit2.Response<Void>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: retrofit2.Response<Void>) {
                ProgressDialog.hideLoadingView(context)
                callBack.onSubmitSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "添加失败")
                callBack.onError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun checkInput(): Boolean {
        when {
            bankType.get() == null -> {
                ToastManager.showShortToast(context, "请选择银行卡类型")
                return false
            }
            StringUtils.isEmpty(name.get()) -> {
                ToastManager.showShortToast(context, "请填写持卡人姓名")
                return false
            }
            StringUtils.isEmpty(cardNum.get()) -> {
                ToastManager.showShortToast(context, "请填写银行卡号码")
                return false
            }
            StringUtils.isEmpty(bankName.get()) -> {
                ToastManager.showShortToast(context, "请填写开户行名称")
                return false
            }
            StringUtils.isEmpty(phone.get()) -> {
                ToastManager.showShortToast(context, "请填写银行预留手机号")
                return false
            }
        }
        return true
    }
}