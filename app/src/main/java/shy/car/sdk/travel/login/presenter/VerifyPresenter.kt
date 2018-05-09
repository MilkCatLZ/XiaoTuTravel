package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnClickListener
import com.base.util.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User


interface VerifyListener {
    fun onGetVerifySuccess()
    fun onGetVerifyError(e: Throwable)
}

/**
 *
 * 获取验证码
 */
class VerifyPresenter(val listener: VerifyListener? = null, context: Context) : BasePresenter(context) {

    var phone = ObservableField<String>("")

    /**
     * 手机输入是否正确，true:正确，false:不正确
     */
    var isPhoneNumCorrect = ObservableBoolean(false)


    /**
     * 下一步
     */
    var onGetVerifyClickListener: OnClickListener = OnClickListener {
        if (ClickUtil.canClick() && !User.instance.isLogin && isPhoneNumCorrect.get())
            getVerify()
    }

    /**
     * 电话输入框文字检查
     */
    var phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

        }

        override fun afterTextChanged(editable: Editable) {
            isPhoneNumCorrect.set(Phone.isMobile(editable.toString()))
        }
    }

    /**
     * 获取验证码
     */
    fun getVerify() {
        ApiManager.instance.toSubscribe(ApiManager.instance.api.gerVerify(phone.get()!!), object : Observer<String> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: String) {
                if (StringUtils.isNotEmpty(t)) {
                    ToastManager.showLongToast(context, "验证码发送成功")
                    listener?.onGetVerifySuccess()
                }
            }

            override fun onError(e: Throwable) {
                listener?.onGetVerifyError(e)
            }
        })
    }

    /**
     * @param ex
     */
    private fun manageVerifyError(ex: Throwable) {
        val error = ErrorManager.managerError(context, ex, R.string.str_verify_failed)
    }


    init {
        val phones = SPCache.getObject(context, LAST_LOGIN_PHONE, String::class.java, "")
        if (StringUtils.isNotEmpty(phones))
            phone.set(SPCache.getObject<Any>(context, LAST_LOGIN_PHONE, String::class.java)!!
                    .toString())

    }

    companion object {
        const val LAST_LOGIN_PHONE = "lastLoginPhone"
    }
}

