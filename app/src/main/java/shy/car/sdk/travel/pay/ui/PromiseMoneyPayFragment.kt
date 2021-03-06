package shy.car.sdk.travel.pay.ui

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonObject
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.Int1
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentMoneyVerifyPayBinding
import shy.car.sdk.travel.pay.WXPayUtil
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.pay.presenter.PromiseMoneyPayPresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/21
 * 支付保证金
 */
class PromiseMoneyPayFragment : XTBaseFragment(),
        PayMethodSelectDialog.OnPayClick,
        PromiseMoneyPayPresenter.CallBack {
    override fun onCreatePaySuccess(t: JsonObject) {
        activity?.let {
            WXPayUtil.pay(it as XTBaseActivity, presenter.payMethod!!, presenter.needPayAmount.get(), t)
        }
    }

//    override fun getPromiseMoneyError(e: Throwable) {
//
//    }
//
//    override fun onGetPromiseMoneySuccess(t: Double) {
//
//
//    }

    lateinit var presenter: PromiseMoneyPayPresenter
    lateinit var binding: FragmentMoneyVerifyPayBinding
    var payName = ObservableField<String>("请选择支付方式")

    val promiseMoney = ObservableField<String>("0.00")
    val btnText = ObservableField<String>("支付保证金0.00元")
    override fun onPaySelect(payMethod: PayMethod) {
        payName.set(payMethod.name)
        presenter.payMethod = payMethod
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = PromiseMoneyPayPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_verify_pay, null, false)
        binding.fragment = this
        binding.user = User.instance
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register(this)
        if (User.instance.deposit > 0.0) {
            btnText.set("已支付保证金${LNTextUtil.getPriceText(User.instance.deposit)}元")
        } else {
            btnText.set("支付保证金0.0元")
        }
        promiseMoney.set(LNTextUtil.getPriceText(User.instance.deposit))
    }

    var dialog: PayMethodSelectDialog? = null
    fun onSelectPayClick() {
//        if (dialog == null) {
        //2:充值的可用支付方式
        dialog = ARouter.getInstance().build(RouteMap.PaySelect)
                .withInt(Int1, 1)//1：充值
                .withObject(Object1, presenter.payMethod)
                .navigation() as PayMethodSelectDialog

        dialog?.listener = this
        dialog!!.type = 1
        dialog?.show(childFragmentManager, "dialog_pay_method_select")
    }

    fun onSelectCarTypeClick() {
        ARouter.getInstance()
                .build(RouteMap.CarTypeSelect)
                .navigation()
    }

    @Subscribe
    fun onCarReceive(carSelectInfo: CarSelectInfo) {
        presenter.carSelect.set(carSelectInfo)
        checkNeedPayAmount()
    }

    private fun checkNeedPayAmount() {
        if (User.instance.deposit == 0.0) {
            if (presenter.carSelect.get() != null) {
                presenter.needPayAmount.set(presenter.carSelect.get()?.promiseMoneyPrice!!)
                btnText.set("支付保证金${LNTextUtil.getPriceText(presenter.needPayAmount.get())}元")
            } else {
                presenter.needPayAmount.set(0.0)
                btnText.set("支付保证金0.0元")
            }

        } else if (presenter.carSelect.get() != null && presenter.carSelect.get()?.promiseMoneyPrice!! > User.instance.deposit) {
            presenter.needPayAmount.set(presenter.carSelect.get()?.promiseMoneyPrice!! - User.instance.deposit)
            btnText.set("还需支付保证金${LNTextUtil.getPriceText(presenter.needPayAmount.get())}元")
        } else {
            presenter.needPayAmount.set(0.0)
            btnText.set("还需支付保证金0.0元")
        }
    }

    fun gotoPromiseMoneyDetail() {
        ARouter.getInstance()
                .build(RouteMap.PromiseMoneyDetail)
                .navigation()
    }

    fun pay() {
        presenter.createPay()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(success: PaySuccess) {
        ARouter.getInstance()
                .build(RouteMap.PaySuccess)
                .withObject(Object1, success)
                .navigation()
        eventBusDefault.post(RefreshUserInfo())
        finish()
    }
}