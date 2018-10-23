package shy.car.sdk.travel.send.ui

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.base.ProgressDialog
import com.base.util.StringUtils
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogOrderSendDetailChangeDeliveryMoneyBinding
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import shy.car.sdk.travel.send.presenter.MoneyChangePresenter

/**
 * create by lz at 2018/06/04
 * 发货-更改运费
 */

class OrderSendDetailChangeDeliveryMoneyFragmentDialog : XTBaseDialogFragment() {

    interface MoneyChangeListener {
        fun onChangeSuccess()
    }

    var listener: MoneyChangeListener? = null


    lateinit var binding: DialogOrderSendDetailChangeDeliveryMoneyBinding
    var money = ObservableField<String>()
    lateinit var presenter: MoneyChangePresenter

    var detail: DeliveryOrderDetail = DeliveryOrderDetail()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = MoneyChangePresenter(it, object : shy.car.sdk.travel.send.presenter.MoneyChangePresenter.CallBack {
                override fun onChangeSuccess() {
                    listener?.onChangeSuccess()
                    dismissAllowingStateLoss()
                }
            })

            presenter.detail = this@OrderSendDetailChangeDeliveryMoneyFragmentDialog.detail
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_order_send_detail_change_delivery_money, null, false)
        return binding.root
    }

    fun onConfirmClick() {
        if (StringUtils.isNotEmpty(money.get())) {
            presenter.submitData()
        }
        activity?.let {
            ProgressDialog.showLoadingView(it)
        }
    }
}