package shy.car.sdk.travel.pay.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.constant.ParamsConstant.Int1
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogPayMethodSelectBinding
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.presenter.PayMethodPresenter

/**
 * create by LZ at 2018/05/21
 * 选择支付方式
 */
@Route(path = RouteMap.PaySelect)
class PayMethodSelectDialog : BottomSheetDialogFragment(),
        PayMethodPresenter.GetPayMethodListener {
    override fun onPayMethodClick(tag: PayMethod) {
        listener?.onPaySelect(tag)
        dismissAllowingStateLoss()
    }


    override fun onGetListSuccess(list: Any) {

    }

    interface OnPayClick {
        /**
         * PayMethodSelectDialog.AliPay 支付宝
         * PayMethodSelectDialog.WeChatPay 微信支付
         */
        fun onPaySelect(payMethod: PayMethod)

    }

    var listener: OnPayClick? = null
    lateinit var binding: DialogPayMethodSelectBinding
    lateinit var presenter: PayMethodPresenter

    //1充值2支付
    @Autowired(name = Int1)
    @JvmField
    var type = 1

    @Autowired(name = Object1)
    @JvmField
    var payMethod: PayMethod? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        activity?.let { presenter = PayMethodPresenter(it, this) }
        presenter.type = type
        if (payMethod != null) {
            presenter.checkPayID.set(payMethod!!.id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_pay_method_select, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getPayMethodList()
    }

}