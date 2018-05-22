package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentMoneyVerifyPayBinding
import shy.car.sdk.travel.pay.data.PromiseMoneyPayPresenter
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.pay.data.CarSelectInfo
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/21
 * 支付保证金
 */
class PromiseMoneyPayFragment : XTBaseFragment(), PayMethodSelectDialog.onPayClick {

    lateinit var moneyVerifyPayPresenter: PromiseMoneyPayPresenter
    lateinit var binding: FragmentMoneyVerifyPayBinding
    var payName = ObservableField<String>("请选择支付方式")
    var carSelect = ObservableField<CarSelectInfo>()


    override fun onPaySelect(paytMethod: Int) {
        when (paytMethod) {
            PayMethodSelectDialog.WeChatPay -> payName.set("微信支付")
            PayMethodSelectDialog.AliPay -> payName.set("支付宝")
            else -> payName.set("请选择支付方式")
        }
        moneyVerifyPayPresenter?.payMethod.set(paytMethod)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { moneyVerifyPayPresenter = PromiseMoneyPayPresenter(it, object : PromiseMoneyPayPresenter.CallBack {}) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_verify_pay, null, false)
        binding.fragment = this
        binding.user = User.instance
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register(this)
    }

    var dialog: PayMethodSelectDialog? = null
    fun onSelectPayClick() {
        if (dialog == null) {
            dialog = ARouter.getInstance().build(RouteMap.PaySelect).navigation() as PayMethodSelectDialog
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
        carSelect.set(carSelectInfo)
    }

    fun gotoPromiseMoneyDetail() {
        ARouter.getInstance()
                .build(RouteMap.PromiseMoneyDetail)
                .navigation()
    }
}