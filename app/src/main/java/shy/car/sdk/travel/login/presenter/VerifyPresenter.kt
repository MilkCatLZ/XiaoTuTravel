package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.SPCache
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase
import shy.car.sdk.travel.user.data.UserDetailCache


/**
 *
 * 获取验证码
 */
class VerifyPresenter(val listener: LoginListener? = null, context: Context) : BasePresenter(context) {
    interface LoginListener {
        fun loginSuccess()
        fun loginFailed(e: Throwable?)
        fun onGetVerifySuccess()
        fun onGetVerifyError(e: Throwable?)
    }

    var phone = ObservableField<String>("")
    var verify = ObservableField<String>("")

    /**
     * 登录
     */
    fun login() {

        ApiManager.getInstance()
                .api.login(phone.get()!!, verify.get()!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JsonObject> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(result: JsonObject) {
                        if (isLoginSuccess(result)) {
                            copyLoginInfo(result)
                            savePhoneNumCache()
                            listener?.loginSuccess()
                        } else {
                            listener?.loginFailed(null)
                        }
                    }

                    override fun onError(e: Throwable) {
                        listener?.loginFailed(e)
                        manageVerifyError(e)
                    }
                })
    }

    private fun copyLoginInfo(result: JsonObject) {
        User.instance.tokenType = result.get(UserBase.TokenType)
                .asString
        User.instance.expiresIn = result.get(UserBase.EXPIRES_IN)
                .asLong
        User.instance.accessToken = result.get(UserBase.ACCESS_TOKEN)
                .asString
        User.instance.scope = result.get(UserBase.Scope)
                .asString
        User.instance.refreshToken = result.get(UserBase.REFRESH_TOKEN)
                .asString
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
        ApiManager.getInstance()
                .toSubscribe(ApiManager.getInstance().api.gerVerify(phone.get()!!), object : Observer<JsonObject> {
                    override fun onComplete() {
                        ProgressDialog.hideLoadingView(context)
                    }

                    override fun onSubscribe(d: Disposable) {
                        this@VerifyPresenter.d = d
                    }

                    override fun onNext(t: JsonObject) {
                        if (StringUtils.isNotEmpty(t.toString())) {
                            ToastManager.showLongToast(context, "验证码发送成功${if (BuildConfig.DEBUG) t.toString() else ""}")
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
        SPCache.saveObject(context, LAST_LOGIN_PHONE, phone.get())
    }

    companion object {
        const val LAST_LOGIN_PHONE = "lastLoginPhone"
    }

    /**
     * 一般登录
     *
     * @param result
     */
    private fun isLoginSuccess(result: JsonObject): Boolean {
        val accessToken = result.get(UserBase.ACCESS_TOKEN)
                .asString
        return StringUtils.isNotEmpty(accessToken)
    }


    private fun saveLoginState() {
        User.saveUserState(context)
    }

}

