package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderPayBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
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
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    fun setOid(oid: String) {
        presenter.getOrderDetail(oid)
    }

    fun selectPayMethod() {
        val dialog = ARouter.getInstance().build(RouteMap.PaySelect).withObject(ParamsConstant.Object1, presenter.payMethod.get()).withInt(ParamsConstant.Int1, 1).navigation() as PayMethodSelectDialog
        dialog.listener = object : PayMethodSelectDialog.OnPayClick {
            override fun onPaySelect(payMethod: PayMethod) {
                presenter.payMethod.set(payMethod)
            }
        }
        dialog.show(childFragmentManager, "fragment_pay")
    }

    fun selectCoupon() {
        ARouter.getInstance()
                .build(RouteMap.Coupon)
                .withBoolean(ParamsConstant.Boolean1, true)
                .navigation()
    }

    fun pay(){

    }

}