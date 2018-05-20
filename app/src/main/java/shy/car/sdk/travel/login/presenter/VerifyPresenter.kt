package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase

interface LoginListener {
    fun loginSuccess()
    fun loginFailed(e: Throwable)
    fun loginFailed()
    fun onGetVerifySuccess()
    fun onGetVerifyError(e: Throwable)
}

/**
 *
 * 获取验证码
 */
class VerifyPresenter(val listener: LoginListener? = null, context: Context) : BasePresenter(context) {

    var phone = ObservableField<String>("")
    var verify = ObservableField<String>("")

//    /**
//     * 手机输入是否正确，true:正确，false:不正确
//     */
//    var isPhoneNumCorrect = ObservableBoolean(false)


//    /**
//     * 下一步
//     */
//    var onGetVerifyClickListener: OnClickListener = OnClickListener {
//        if (ClickUtil.canClick() && !User.instance.isLogin && isPhoneNumCorrect.get())
//            login()
//    }
//
//    /**
//     * 电话输入框文字检查
//     */
//    var phoneTextWatcher: TextWatcher = object : TextWatcher {
//        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//        }
//
//        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//        }
//
//        override fun afterTextChanged(editable: Editable) {
//            isPhoneNumCorrect.set(Phone.isMobile(editable.toString()))
//        }
//    }

    /**
     * 登录
     */
    fun login() {
        var observer = ApiManager.instance.api.login(verify.get()!!)
        ApiManager.instance.toSubscribe(observer, object : Observer<String> {
            override fun onComplete() {
                ProgressDialog.hideLoadingView(context)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(result: String) {
                if (isLoginSuccess(result)) {
                    saveLoginState(result)
                    savePhoneNumCache()
                    listener?.loginSuccess()
                    EventBus.getDefault()
                            .post(LoginSuccess())
                } else {
                    listener?.loginFailed()
                }
            }

            override fun onError(e: Throwable) {
                listener?.loginFailed(e)
                ProgressDialog.hideLoadingView(context)
                if (BuildConfig.DEBUG) {
                    EventBus.getDefault()
                            .post(LoginSuccess())
                }
            }
        })
    }

    /**
     * @param ex
     */
    private fun manageVerifyError(ex: Throwable) {
        val error = ErrorManager.managerError(context, ex, R.string.str_verify_failed)
    }

    var d: Disposable? = null
    /**
     * 获取验证码
     */
    fun getVerify() {
        ProgressDialog.showLoadingView(context)
        d?.dispose()
        ApiManager.instance.toSubscribe(ApiManager.instance.api.gerVerify(phone.get()!!), object : Observer<String> {
            override fun onComplete() {
                ProgressDialog.hideLoadingView(context)
            }

            override fun onSubscribe(d: Disposable) {
                this@VerifyPresenter.d = d
            }

            override fun onNext(t: String) {
                if (StringUtils.isNotEmpty(t)) {
                    ToastManager.showLongToast(context, "验证码发送成功")
                    listener?.onGetVerifySuccess()
                }
            }

            override fun onError(e: Throwable) {
                listener?.onGetVerifyError(e)
                ProgressDialog.hideLoadingView(context)

            }
        })
    }

    init {
        val phones = SPCache.getObject(context, LAST_LOGIN_PHONE, String::class.java, "")
        if (StringUtils.isNotEmpty(phones))
            phone.set(SPCache.getObject<Any>(context, LAST_LOGIN_PHONE, String::class.java)!!
                    .toString())

    }

    private fun savePhoneNumCache() {
        SPCache.saveObject(context, LAST_LOGIN_PHONE, phone)
    }

    companion object {
        const val LAST_LOGIN_PHONE = "lastLoginPhone"
    }

    /**
     * 一般登录
     *
     * @param result
     */
    private fun isLoginSuccess(result: String): Boolean {
        val accessToken = JsonManager.getJsonString(result, UserBase.ACCESS_TOKEN)
        return StringUtils.isNotEmpty(accessToken)
    }


    private fun saveLoginState(result: String) {
        User.instance.access_token = JsonManager.getJsonString(result, UserBase.ACCESS_TOKEN)
        User.instance.uid = Integer.parseInt(JsonManager.getJsonString(result, UserBase.UID))
        User.instance.expiresIn = java.lang.Long.parseLong(JsonManager.getJsonString(result, UserBase.EXPIRES_IN))
        Log.d("LoginPresenter", "Long.parseLong(JsonManager.getJsonString(result, User.EXPIRES_IN)" + java.lang.Long.parseLong(JsonManager.getJsonString(result, UserBase.EXPIRES_IN) + ""))
        User.instance.loginTime = System.currentTimeMillis() / 1000L
        User.instance.phone = phone.get()
        User.saveUserState(context)
    }

}

