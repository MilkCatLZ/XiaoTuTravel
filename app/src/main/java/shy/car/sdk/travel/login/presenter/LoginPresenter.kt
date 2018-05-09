package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnClickListener
import com.alibaba.fastjson.JSON
import com.base.base.ProgressDialog
import com.base.util.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase


interface LoginListener {
    fun loginSuccess()
    fun loginFailed(e: Throwable)
    fun loginFailed()
}

/**
 *
 * 登录逻辑处理
 */
class LoginPresenter(val listener: LoginListener? = null, context: Context) : BasePresenter(context) {

    var phone = ""
    var verify = ObservableField<String>("")

    /**
     * 手机输入是否正确，true:正确，false:不正确
     */
    var isVerifyCorrect = ObservableBoolean(false)

    /**
     * 登录/注册的参数
     *
     * @return
     */
    private val loginParam: String
        get() {
            val data = JSON.parseObject("{}")

            data[UserBase.PHONE] = phone
            data[ParamsConstant.VERIFY] = verify.get()
            data[ParamsConstant.UUID] = Device.getUUID(context)
            data["umeng_device_token"] = app.device_token

            return data.toString()
        }


    /**
     * 登录/注册按钮点击
     */
    var onLoginClickListener: OnClickListener = OnClickListener {
        if (ClickUtil.canClick() && !User.instance.isLogin && isVerifyCorrect.get())
            login()
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
            isVerifyCorrect.set(StringUtils.isNotEmpty(verify.get()) && verify.get()?.length == 6)
        }
    }

    /**
     * 登录
     */
    private fun login() {
        var observer = ApiManager.instance.api.login(loginParam)
        ApiManager.instance.toSubscribe(observer, object : Observer<String> {
            override fun onComplete() {
                ProgressDialog.hideLoadingView(context)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(result: String) {
                processResult(result)
            }

            override fun onError(e: Throwable) {
                listener?.loginFailed(e)
            }
        })
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
        User.instance.type = Integer.parseInt(JsonManager.getJsonString(result, UserBase.BUYER_TYPE))
        Log.d("LoginPresenter", "Long.parseLong(JsonManager.getJsonString(result, User.EXPIRES_IN)" + java.lang.Long.parseLong(JsonManager.getJsonString(result, UserBase.EXPIRES_IN) + ""))
        User.instance.loginTime = System.currentTimeMillis() / 1000L
        User.instance.phone = phone
        User.saveUserState(context)
    }


    private fun processResult(result: String) {
        if (isLoginSuccess(result)) {
            saveLoginState(result)
            savePhoneNumCache()
            listener?.loginSuccess()
        } else {
            listener?.loginFailed()
        }
    }

    private fun savePhoneNumCache() {
        SPCache.saveObject(context, LAST_LOGIN_PHONE, phone)
    }

    companion object {
        val LAST_LOGIN_PHONE = "lastLoginPhone"
    }
}

