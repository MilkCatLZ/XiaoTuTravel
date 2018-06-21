package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentOrderPayBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.pay.presenter.OrderPayPresenter

/**
 * create by LZ at 2018/06/21
 * 支付订单
 */
class OrderPayFragment : XTBaseFragment(),
        OrderPayPresenter.CallBack {
    override fun onGetDetailSuccess(t: RentOrderDetail) {
        binding.detail = t
    }

    override fun onGetDetailError(e: Throwable) {
        finish()
    }

    lateinit var binding: FragmentOrderPayBinding
    lateinit var presenter: OrderPayPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = OrderPayPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_pay, null, false)
        return binding.root
    }

    fun setOid(oid: String) {
        presenter.getOrderDetail(oid)
    }
}