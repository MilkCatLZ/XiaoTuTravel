package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
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
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.rent.data.CarSelectInfo
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/21
 * 支付保证金
 */
class MoneyVerifyPayFragment : XTBaseFragment(), PayMethodSelectDialog.onPayClick {
    override fun onPaySelect(paytMethod: Int) {
        
    }

    lateinit var binding: FragmentMoneyVerifyPayBinding

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

    fun onSelectPayClick() {
        var dialog = ARouter.getInstance().build(RouteMap.PaySelect).navigation() as PayMethodSelectDialog
        dialog.listener=this
        dialog.show(childFragmentManager, "dialog_pay_method_select")
    }

    fun onSelectCarTypeClick() {
        ARouter.getInstance()
                .build(RouteMap.CarTypeSelect)
                .navigation()
    }

    @Subscribe
    fun onCarReceive(carSelectInfo: CarSelectInfo) {
        binding.car = carSelectInfo
    }
}