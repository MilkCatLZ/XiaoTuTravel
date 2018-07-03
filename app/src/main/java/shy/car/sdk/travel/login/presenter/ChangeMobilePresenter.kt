package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import com.base.base.ProgressDialog
import com.base.util.Phone
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter


/**
 * 修改手机号
 */
class ChangeMobilePresenter(context: Context, var listener: ChangeMobileListener? = null) : BasePresenter(context) {

    interface ChangeMobileListener {
        fun onGetVerifySuccess(interval: Int)
        fun onGetVerifyError(e: Throwable)
        fun verify(interval: Int, verify: String)
    }

    var phone = ObservableField<String>("")
    var verify = ObservableField<String>("")
    var isPhoneCorrect = false
    var isVerifyCorrect = false


    /**
     * 电话输入框文字检查
     */
    var phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            isPhoneCorrect = Phone.isMobile(phone.get())
        }
    }
    /**
     * 电话输入框文字检查
     */
    var verifyTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            isVerifyCorrect = StringUtils.isNotEmpty(verify.get())
        }
    }

    /**
     * 获取验证码
     */
    fun getVerify() {
        if (!isPhoneCorrect) {

            ToastManager.showLongToast(context, "请输入正确的手机号")
            return
        }
        if (!isVerifyCorrect) {
            ToastManager.showLongToast(context, "请输入验证码")
            return
        }

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        ApiManager.getInstance()
                .toSubscribe(ApiManager.getInstance().api.gerVerify(phone.get()!!), object : Observer<JsonObject> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: JsonObject) {
                        ProgressDialog.hideLoadingView(context)
                        if (StringUtils.isNotEmpty(t.toString())) {
                            ToastManager.showLongToast(context, "验证码已发送到您的手机，请注意查看")
                            verify.set(t.get("captcha").toString())
                            listener?.onGetVerifySuccess(t.get("interval").asInt)
                        }
                    }

                    override fun onError(e: Throwable) {
                        ProgressDialog.hideLoadingView(context)
                        ErrorManager.managerError(context, e, "获取验证码失败")
                        listener?.onGetVerifyError(e)

                    }
                })

    }

    /**
     * 获取验证码
     */
    fun changeMobile() {
        if (!isPhoneCorrect) {

            ToastManager.showLongToast(context, "请输入正确的手机号")
            return
        }
        if (!isVerifyCorrect) {
            ToastManager.showLongToast(context, "请输入验证码")
            return
        }

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()

        val observable = ApiManager.getInstance()
                .api.gerVerify(phone.get()!!)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)

            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "修改手机失败")
            }
        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)

    }

}

