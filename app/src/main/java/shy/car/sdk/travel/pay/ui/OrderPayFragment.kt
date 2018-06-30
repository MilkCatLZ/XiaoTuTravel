package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonObject
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.eventbus.RentCarPaySuccess
import shy.car.sdk.app.route.RouteMap

import shy.car.sdk.databinding.FragmentOrderPayBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.pay.WXPayUtil
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.pay.presenter.OrderPayPresenter

/**
 * create by LZ at 2018/06/21
 * 支付订单
 */
class OrderPayFragment : XTBaseFragment(),
        OrderPayPresenter.CallBack {
    override fun paySuccess() {
        ARouter.getInstance()
                .build(RouteMap.RentCarPaySuccess)
                .withObject(Object1, binding.detail)
                .navigation()
        eventBusDefault.post(RentCarPaySuccess())
        finish()
    }

    override fun getPayStringSuccess(json: JsonObject) {
        activity?.let {
            if (!WXPayUtil.pay(it as XTBaseActivity, presenter.payMethod.get()!!, json)) {

            }
        }
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register(this)
    }

    fun setOid(oid: String) {
        presenter.getOrderDetail(oid)
    }

    fun selectPayMethod() {
        val dialog = ARouter.getInstance().build(RouteMap.PaySelect)
                .withObject(ParamsConstant.Object1, presenter.payMethod.get())
                .withInt(ParamsConstant.Int1, 2)//2:支付
                .navigation() as PayMethodSelectDialog
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

    fun pay() {
        presenter.pay()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(success: PaySuccess) {
        ARouter.getInstance()
                .build(RouteMap.RentCarPaySuccess)
                .withObject(Object1, binding.detail)
                .navigation()
        finish()
    }
}