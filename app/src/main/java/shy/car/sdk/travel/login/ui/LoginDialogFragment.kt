package shy.car.sdk.travel.login.ui

import android.app.Dialog
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentLoginBinding
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.login.presenter.LoginPresenter
import shy.car.sdk.travel.login.presenter.VerifyListener

@Route(path = RouteMap.Login)
class LoginDialogFragment : XTBaseDialogFragment() {

    var listener: onLoginDismiss? = null

    lateinit var binding: FragmentLoginBinding
    lateinit var presenter: LoginPresenter
    private var verifyListener = object : VerifyListener {
        override fun verify(interval:Int,verify: String) {
            app.startVerifyDialogVerify(presenter.phone.get()!!, interval,verify = verify)
            dismiss(true)
        }

        override fun onGetVerifyError(e: Throwable) {
            dismiss(BuildConfig.DEBUG)
//            if (BuildConfig.DEBUG) {
//                app.startVerifyDialog(presenter.phone.get()!!)
//            }
        }

        override fun onGetVerifySuccess(interval: Int) {
            dismiss(true)
            if (!BuildConfig.DEBUG)
                app.startVerifyDialog(presenter.phone.get()!!, interval)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = LoginPresenter(verifyListener, it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, null, false)
        binding.fragment = this
        binding.p = presenter
        binding.edtPhone.addTextChangedListener(presenter.phoneTextWatcher)
        return binding.root
    }

    fun onNextClick() {
        presenter.getVerify()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun dismiss(isSuccess: Boolean) {
        if (isSuccess) {
            dismissAllowingStateLoss()
        } else {
            listener?.onCancel()
            dismissAllowingStateLoss()
        }
    }
}