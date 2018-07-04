package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.StringUtils
import com.google.gson.JsonObject
import mall.lianni.alipay.Alipay
import mall.lianni.alipay.PayResult
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentPayBinding
import shy.car.sdk.travel.pay.WXPayUtil
import shy.car.sdk.travel.pay.data.PayAmount
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.pay.presenter.PayPresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by lz at 2018/06/17
 *  充值
 */
class PayFragment : XTBaseFragment(),
        PayPresenter.CallBack {
    override fun onCreatePaySuccess(t: JsonObject) {
            activity?.let {

                if (!WXPayUtil.pay(it as XTBaseActivity, presenter.payMethod.get()!!, t)) {

                }
            }
    }

    override fun onGetListSuccess(t: List<PayAmount>) {

    }

    override fun onGetListError(e: Throwable) {

    }

    lateinit var binding: FragmentPayBinding
    lateinit var presenter: PayPresenter
    val amountText = ObservableField<String>("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = PayPresenter(it, this)
        }
        amountText.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (StringUtils.isNotEmpty(amountText.get()))
                    presenter.amount.set(amountText.get()?.toDouble()!!)
                else {
                    presenter.amount.set(0.0)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, null, false)
        binding.fragment = this
        binding.presenter = presenter
        binding.user = User.instance
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getPayAmountList()
        register(this)
    }

    fun selectPayMethod() {
        val dialog = ARouter.getInstance().build(RouteMap.PaySelect)
                .withObject(ParamsConstant.Object1, presenter.payMethod.get())
                .withInt(ParamsConstant.Int1, 1)//1:充值
                .navigation() as PayMethodSelectDialog
        dialog.listener = object : PayMethodSelectDialog.OnPayClick {
            override fun onPaySelect(payMethod: PayMethod) {
                presenter.payMethod.set(payMethod)
            }
        }
        dialog.show(childFragmentManager, "fragment_pay")
    }

    fun pay() {
        presenter.pay()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(success: PaySuccess) {
        ARouter.getInstance()
                .build(RouteMap.PaySuccess)
                .navigation()
        eventBusDefault.post(RefreshUserInfo())
        finish()
    }
}