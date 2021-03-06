package shy.car.sdk.travel.login.presenter


import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnClickListener
import com.base.base.ProgressDialog
import com.base.util.ClickUtil
import com.base.util.StringUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.util.PassWordEncode
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.data.UserBase


interface SetupPassWordListener {
    fun setPassSuccess()
    fun setPassError(e: Throwable?)
}

/**
 *
 * 设置登录密码
 */
class SetupPassWordPresenter(val listener: SetupPassWordListener? = null, context: Context) : BasePresenter(context) {

    var phone = ""
    var password = ObservableField<String>("")

    /**
     * 手机输入是否正确，true:正确，false:不正确
     */
    var isPasswordCorrect = ObservableBoolean(false)

    /**
     * 登录/注册按钮点击
     */
    var onSetPassClickListener: OnClickListener = OnClickListener {
        if (ClickUtil.canClick() && isPasswordCorrect.get())
            setupPassWord()
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
            isPasswordCorrect.set(StringUtils.isNotEmpty(password.get()) && password.get()!!.length >= 6)
        }
    }

    /**
     * 修改密码
     */
    private fun setupPassWord() {
        var observer = ApiManager.getInstance().api.setupPassWord(User.instance.phone, password.get()!!)
        ApiManager.getInstance().toSubscribe(observer, object : Observer<String> {
            override fun onComplete() {
                ProgressDialog.hideLoadingView(context)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(result: String) {
                processResult(result)
            }

            override fun onError(e: Throwable) {
                listener?.setPassError(e)
            }
        })
    }


    private fun processResult(result: String) {
        if (isSetupSuccess(result)) {
            listener?.setPassSuccess()
        } else {
            listener?.setPassError(null)
        }
    }

    private fun isSetupSuccess(result: String): Boolean {
        return true
    }

}

