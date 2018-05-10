package shy.car.sdk.travel.login.presenter


import android.content.Context
import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import com.alibaba.fastjson.JSON
import com.base.base.ProgressDialog
import com.base.util.Device
import com.base.util.Phone
import com.base.util.StringUtils
import com.base.util.ToastManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.UserBase


interface VerifyListener {
    fun onGetVerifySuccess()
    fun onGetVerifyError(e: Throwable)
}

/**
 *
 * 登录逻辑处理
 */
class LoginPresenter(val listener: VerifyListener? = null, context: Context) : BasePresenter(context) {

    var phone = ObservableField<String>("")
    var isPhoneCorrect = false

    /**
     * 登录/注册的参数
     *
     * @return
     */
    private val verifyParam: String
        get() {
            val data = JSON.parseObject("{}")
            data[UserBase.PHONE] = phone
            data[ParamsConstant.UUID] = Device.getUUID(context)
            data["umeng_device_token"] = app.device_token

            return data.toString()
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
            isPhoneCorrect = Phone.isMobile(phone.get())
        }
    }
    var d: Disposable? = null
    /**
     * 获取验证码
     */
    fun getVerify() {
        if (isPhoneCorrect) {
            ProgressDialog.showLoadingView(context)
            d?.dispose()
            ApiManager.instance.toSubscribe(ApiManager.instance.api.gerVerify(phone.get()!!), object : Observer<String> {
                override fun onComplete() {
                    ProgressDialog.hideLoadingView(context)
                }

                override fun onSubscribe(d: Disposable) {
                    this@LoginPresenter.d = d
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
        } else {
            ToastManager.showLongToast(context, "请输入正确的手机号")
        }
    }

    override fun destroy() {
        super.destroy()
        d?.dispose()
    }
}

