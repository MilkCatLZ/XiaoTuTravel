package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentMoneyVerifyPayBinding
import shy.car.sdk.travel.dialog.DialogMoneyVerify

/**
 * create by LZ at 2018/05/21
 * 支付保证金
 */
class MoneyVerifyPayFragment : XTBaseFragment() {

    lateinit var binding: FragmentMoneyVerifyPayBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_money_verify_pay, null, false)
        binding.fragment = this
        return binding.root
    }

    fun onSelectPayClick() {
        var dialog = ARouter.getInstance().build(RouteMap.Dialog_Money_Verify).navigation() as BottomSheetDialogFragment
        dialog.show(childFragmentManager, "dialog_pay_method_select")
    }
}