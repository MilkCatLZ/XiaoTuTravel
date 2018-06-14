package shy.car.sdk.travel.pay.ui

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.R
import shy.car.sdk.app.LNTextUtil
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentMoneyVerifyPayBinding
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
    override fun getPromiseMoneyError(e: Throwable) {

    }

    override fun onGetPromiseMoneySuccess(t: Double) {

        if (t == 0.0) {
            btnText.set("支付保证金${LNTextUtil.getPriceText(t)}元")
        } else if (presenter.carSelect.get()?.promiseMoneyPrice!! > t) {
            btnText.set("还需支付保证金${LNTextUtil.getPriceText(presenter.carSelect.get()?.promiseMoneyPrice!! - t)}元")
        }
        promiseMoney.set(LNTextUtil.getPriceText(t))
    }

    lateinit var presenter: PromiseMoneyPayPresenter
    lateinit var binding: FragmentMoneyVerifyPayBinding
    var payName = ObservableField<String>("请选择支付方式")

    val promiseMoney = ObservableField<String>("0.00")
    val btnText = ObservableField<String>("支付保证金0.00元")
    override fun onPaySelect(paytMethod: PayMethod) {
        payName.set(paytMethod.name)
        payName.set("请选择支付方式")
        presenter.payMethod = paytMethod
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = PromiseMoneyPayPresenter(it, this) }
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
    }

    var dialog: PayMethodSelectDialog? = null
    fun onSelectPayClick() {
        if (dialog == null) {
            //2:充值的可用支付方式
            dialog = ARouter.getInstance().build(RouteMap.PaySelect).withInt("type", 2).navigation() as PayMethodSelectDialog
            dialog?.listener = this
        }
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
    }

    fun gotoPromiseMoneyDetail() {
        ARouter.getInstance()
                .build(RouteMap.PromiseMoneyDetail)
                .navigation()
    }

    fun pay() {
        presenter.createPay()
    }
}