package shy.car.sdk.travel.login.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentVerifyBinding
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.login.presenter.VerifyPresenter
import java.util.concurrent.TimeUnit


/**
 * create by LZ at 2018/05/29
 * 验证码-登录
 */
@Route(path = RouteMap.Verify)
class VerifyDialogFragment : XTBaseDialogFragment() {


    /**
     * 当前所处的EditText位置（0~3）
     */
    var currentVerifyNum = ObservableInt(0)
    /**
     * 当前所处的EditText位置（0~3）
     */
    var isLogining = ObservableBoolean(false)
    /**
     * 登录成功
     */
    var isLoginSuccess = ObservableBoolean(false)
    /**
     * 登录失败
     */
    var isLoginFailed = ObservableBoolean(false)

    private var d: Disposable? = null


    lateinit var presenter: VerifyPresenter
    var dismissListener: onLoginDismiss? = null
    @Autowired(name = "phone")
    @JvmField
    var phone: String = ""

    @Autowired(name = "interval")
    @JvmField
    var interval: Int = 60

    lateinit var binding: FragmentVerifyBinding

    /**
     * 输入监听
     */
    var textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            progressInput(s.toString())
            lockAndSubmit()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        activity?.let { presenter = VerifyPresenter(listener, it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify, null, false)
        binding.fragment = this
        setData()
        startCountDown()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    /**
     * 网络回调监听
     */
    private var listener = object : VerifyPresenter.LoginListener {
        override fun loginSuccess() {
            isLoginSuccess.set(true)

            Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                    .map { i -> 3 - i }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {
                            dismiss(true)
                            EventBus.getDefault()
                                    .post(LoginSuccess())
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Long) {
                            binding.txtLoginSuccess.text = "登录成功...$t"
                        }

                        override fun onError(e: Throwable) {

                        }
                    })

        }

        override fun loginFailed(e: Throwable?) {
            isLoginFailed.set(true)
            //倒计时
            Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                    .map { i -> 3 - i }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {
                            dismiss(false)
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Long) {
                            binding.txtLoginFailed.text = "登录失败，请重试...$t"
                        }

                        override fun onError(e: Throwable) {

                        }
                    })

        }

        override fun onGetVerifySuccess() {
            startCountDown()
        }

        override fun onGetVerifyError(e: Throwable?) {
            activity?.let { ToastManager.showLongToast(it, "获取验证码失败，请重试") }
        }
    }

    fun dismiss(isSuccess: Boolean) {
        if (isSuccess) {
            dismissListener?.onCancel()
            dismiss()
        } else {
            dismiss()
        }
    }

    var disposable: Disposable? = null
    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }


    private fun progressInput(result: String) {
        currentVerifyNum.set(result.length)
        val array = result.toCharArray()
                .copyOf(VerifyLenght)
//        binding.edtVerify5.text = array[5].toString()
//        binding.edtVerify4.text = array[4].toString()
        binding.edtVerify3.text = array[3].toString()
        binding.edtVerify2.text = array[2].toString()
        binding.edtVerify1.text = array[1].toString()
        binding.edtVerify0.text = array[0].toString()

    }

    var VerifyLenght = 4
    /**
     * 提交验证码
     */
    private fun lockAndSubmit() {
        if (binding.edtInput.text.length == VerifyLenght) {
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        var verify = binding.edtInput.text.toString()
                        presenter.verify.set(verify)
                        presenter.phone.set(phone)
                        presenter.login()
                        isLogining.set(true)
                    })
        }
    }

    /**
     * 倒计时
     */
    private fun startCountDown() {
        d?.dispose()
        binding.txtCountDown.isEnabled = false
        Observable.intervalRange(1, interval.toLong(), 0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { 60 - it }
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        binding.txtCountDown.text = "点击重发"
                        binding.txtCountDown.isEnabled = true
                    }

                    override fun onSubscribe(d: Disposable) {
                        this@VerifyDialogFragment.d = d
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNext(t: Long) {
                        binding.txtCountDown.text = "${t}秒后重发"
                    }

                    override fun onError(e: Throwable) {
                    }
                })

    }

    private fun setData() {
        binding.phone = phone
        binding.edtInput.addTextChangedListener(textWatcher)
    }

    override fun dismiss() {
        d?.dispose()
        super.dismiss()
    }

    fun back() {
        dismiss()
        app.startLoginDialog(null, null, dismissListener)
        dismissListener = null
    }

    fun getVerify() {
        presenter.getVerify()
    }

}