package shy.car.sdk.travel.login.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableInt
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Log
import com.base.util.StringUtils
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_verify.*
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentVerifyBinding
import shy.car.sdk.travel.login.presenter.LoginListener
import shy.car.sdk.travel.login.presenter.VerifyPresenter
import java.util.concurrent.TimeUnit


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
    /**
     * 记录返回键按下的次数
     */
    private var repeatCount: Int = 0
    private var d: Disposable? = null


    lateinit var presenter: VerifyPresenter

    @Autowired(name = "phone")
    @JvmField
    var phone: String = ""

    lateinit var binding: FragmentVerifyBinding
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
        dialog.setOnKeyListener { _: DialogInterface, _: Int, keyEvent: KeyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_UP) {
                if (keyEvent.keyCode == KeyEvent.KEYCODE_DEL && repeatCount >= 1) {
                    backEvent()
                } else {
                    repeatCount++
                }
            }

            false
        }
        return dialog
    }

    /**
     * 网络回调监听
     */
    private var listener = object : LoginListener {
        override fun loginSuccess() {
            isLoginSuccess.set(true)

            Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                    .map { i -> 3 - i }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {
                            dismiss()
                            EventBus.getDefault()
                                    .post(LoginSuccess())
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Long) {
                            txt_login_success.text = "登录成功...$t"
                        }

                        override fun onError(e: Throwable) {

                        }
                    })

        }

        override fun loginFailed(e: Throwable) {
            isLoginFailed.set(true)
            //倒计时
            Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                    .map { i -> 3 - i }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {
                            dismiss()
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Long) {
                            txt_login_failed.text = "登录失败，请重试...$t"
                        }

                        override fun onError(e: Throwable) {

                        }
                    })

        }

        override fun loginFailed() {
            isLoginFailed.set(true)
            //倒计时
            Observable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                    .map { i -> 3 - i }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Long> {
                        override fun onComplete() {
                            dismiss()
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Long) {
                            txt_login_failed.text = "登录失败，请重试$t"
                        }

                        override fun onError(e: Throwable) {

                        }
                    })
        }

        override fun onGetVerifySuccess() {
            startCountDown()
        }

        override fun onGetVerifyError(e: Throwable) {
            activity?.let { ToastManager.showLongToast(it, "获取验证码失败，请重试") }
        }
    }
    var disposable: Disposable? = null
    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    /**
     * 输入监听
     */
    var textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            val t = s.toString()
            if (StringUtils.isNotEmpty(t)) {
                nextEditText()
            }
            if (currentVerifyNum.get() == 3 && StringUtils.isNotEmpty(edt_verify_3.text)) {
                var verify = edt_verify_0.text.toString() + edt_verify_1.text.toString() + edt_verify_2.text.toString() + edt_verify_3.text.toString()
                presenter.verify.set(verify)
                presenter.phone.set(phone)
                presenter.login()
                isLogining.set(true)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d("beforeTextChanged", s?.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("onTextChanged", "onTextChanged")
        }
    }

    /**
     * 退回前一个EditText
     */
    private fun forwardEditTextRequestFocus(): EditText {
        repeatCount = 1
        return when (currentVerifyNum.get()) {
            1 -> {
                repeatCount = 0
                currentVerifyNum.set(0)
                binding.edtVerify0.requestFocus()
                binding.edtVerify0
            }
            2 -> {
                currentVerifyNum.set(1)
                binding.edtVerify1.requestFocus()
                binding.edtVerify1
            }
            3 -> {
                currentVerifyNum.set(2)
                binding.edtVerify2.requestFocus()
                binding.edtVerify2
            }
            else -> binding.edtVerify0
        }
    }

    /**
     * 下一个editText获取焦点
     */
    private fun nextEditText(): EditText {
        repeatCount = 1
        return when (currentVerifyNum.get()) {
            0 -> {
                binding.edtVerify1.requestFocus()
                currentVerifyNum.set(1)
                binding.edtVerify1
            }

            1 -> {
                binding.edtVerify2.requestFocus()
                currentVerifyNum.set(2)
                binding.edtVerify2
            }
            2 -> {
                binding.edtVerify3.requestFocus()
                currentVerifyNum.set(3)
                binding.edtVerify3
            }
            3 -> {
                lockAndSubmit()
                repeatCount = 0
                binding.edtVerify3
            }
            else -> binding.edtVerify0
        }
    }

    private fun currentEditText(): EditText {
        return when (currentVerifyNum.get()) {
            0 -> binding.edtVerify0
            1 -> binding.edtVerify1
            2 -> binding.edtVerify2
            3 -> binding.edtVerify3
            else -> binding.edtVerify0
        }
    }

    /**
     * 当
     */
    private fun backEvent() {
        //当输入框已经为空，再次按下退格键，删除前一个editText的内容
        forwardEditTextRequestFocus().setText("")
    }

    fun touchEvent() {
        when (currentVerifyNum.get()) {
            0 -> binding.edtVerify0.requestFocus()
            1 -> binding.edtVerify1.requestFocus()
            2 -> binding.edtVerify2.requestFocus()
            3 -> binding.edtVerify3.requestFocus()
        }
    }

    /**
     * 提交验证码
     */
    private fun lockAndSubmit() {

    }

    /**
     * 倒计时
     */
    private fun startCountDown() {
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
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
        binding.edtVerify0.addTextChangedListener(textWatcher)
        binding.edtVerify1.addTextChangedListener(textWatcher)
        binding.edtVerify2.addTextChangedListener(textWatcher)
        binding.edtVerify3.addTextChangedListener(textWatcher)

    }

    override fun dismiss() {
        d?.dispose()
        super.dismiss()
    }

    fun back() {
        dismiss()
        app.startLoginDialog(null, null)
    }

    fun getVerify() {
        presenter.getVerify()
    }

}