package shy.car.sdk.travel.login.presenter


import android.annotation.SuppressLint
import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase
import java.util.concurrent.TimeUnit

interface LoginListener {
    fun loginSuccess()
    fun loginFailed(e: Throwable?)
    fun onGetVerifySuccess()
    fun onGetVerifyError(e: Throwable?)
}

/**
 *
 * 获取验证码
 */
class VerifyPresenter(val listener: LoginListener? = null, context: Context) : BasePresenter(context) {

    var phone = ObservableField<String>("")
    var verify = ObservableField<String>("")

    /**
     * 登录
     */
    fun login() {
        var observer = ApiManager.instance.api.login(verify.get()!!)
        ApiManager.instance.toSubscribe(observer, object : Observer<String> {
            override fun onComplete() {
//                    ProgressDialog.hideLoadingView(context)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(result: String) {
                if (isLoginSuccess(result)) {
                    saveLoginState(result)
                    savePhoneNumCache()
                    listener?.loginSuccess()
                } else {
                    listener?.loginFailed(null)
                }
            }

            override fun onError(e: Throwable) {

                if (shy.car.sdk.BuildConfig.DEBUG) {
                    Observable.timer(3, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                if ("1234".equals(verify.get())) {
                                    User.instance.access_token = "sdklflkjlksjdf"
                                    User.instance.uid = 1985632
                                    User.instance.nickName = "小兔出行"
                                    User.instance.phone = phone.get()
                                    User.instance.status = 1
                                    listener?.loginSuccess()
                                } else {
                                    listener?.loginFailed(e)
                                }
                            })


                } else {
                    listener?.loginFailed(e)
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

