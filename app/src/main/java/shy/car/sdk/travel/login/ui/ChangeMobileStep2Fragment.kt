package shy.car.sdk.travel.login.ui

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_change_mobile_step2.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentChangeMobileStep2Binding
import shy.car.sdk.travel.login.presenter.ChangeMobilePresenter
import java.util.concurrent.TimeUnit

/**
 * create by LZ at 2018/07/03
 * 修改手机号
 */
@Route(path = RouteMap.ChangeMobile)
class ChangeMobileStep2Fragment : XTBaseFragment(),
        ChangeMobilePresenter.ChangeMobileListener {
    override fun verifyMobileSuccess(t: JsonObject) {
        app.logout()
        app.goHome()
        activity?.let { ToastManager.showShortToast(it, "手机修改成功"); }
    }

    override fun onGetVerifySuccess(interval: Int) {
        startCountDown(interval)
    }

    override fun onGetVerifyError(e: Throwable) {

    }


    lateinit var presenter: ChangeMobilePresenter

    lateinit var binding: FragmentChangeMobileStep2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_mobile_step2, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edt_phone.addTextChangedListener(presenter.phoneTextWatcher)
        edt_verify.addTextChangedListener(presenter.verifyTextWatcher)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = ChangeMobilePresenter(it, this)
        }
    }

    var disposable: Disposable? = null

    /**
     * 倒计时
     */
    private fun startCountDown(interval: Int) {

        txt_get_verify.isEnabled = false
        Observable.intervalRange(1, interval.toLong(), 0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { interval - it }
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                        txt_get_verify.text = "点击重发"
                        txt_get_verify.isEnabled = true
                    }

                    override fun onSubscribe(d: Disposable) {
                        this@ChangeMobileStep2Fragment.disposable = d
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onNext(t: Long) {
                        txt_get_verify.text = "${t}秒后重发"
                    }

                    override fun onError(e: Throwable) {
                    }
                })

    }

    fun getVerify() {
        presenter.getVerify()
    }

    fun changeMobile() {
        presenter.changeMobile()
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}