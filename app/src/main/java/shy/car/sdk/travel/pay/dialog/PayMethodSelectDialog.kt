package shy.car.sdk.travel.pay.dialog

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogPayMethodSelectBinding

/**
 * create by LZ at 2018/05/21
 * 选择支付方式
 */
@Route(path = RouteMap.PaySelect)
class PayMethodSelectDialog : BottomSheetDialogFragment() {
    companion object {
        val AliPay = 1
        val WeChatPay = 2
    }

    interface onPayClick {
        /**
         * PayMethodSelectDialog.AliPay 支付宝
         * PayMethodSelectDialog.WeChatPay 微信支付
         */
        fun onPaySelect(paytMethod: Int)

    }

    var listener: onPayClick? = null
    lateinit var binding: DialogPayMethodSelectBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_pay_method_select, null, false)
        binding.fragment = this
        return binding.root
    }


    var payMethod = ObservableInt(WeChatPay)

    fun onAliPayClick() {
        payMethod.set(AliPay)
        listener?.onPaySelect(AliPay)
        dismiss()

    }

    fun onWeChatPayClick() {
        payMethod.set(WeChatPay)
        listener?.onPaySelect(WeChatPay)
        dismiss()
    }
}