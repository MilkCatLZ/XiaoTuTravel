package shy.car.sdk.travel.order.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.DialogManager
import com.base.util.Phone
import com.base.util.ToastManager
import com.google.gson.JsonObject
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.eventbus.PaySuccess
import shy.car.sdk.app.eventbus.SendSuccess
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderDetailBinding
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import shy.car.sdk.travel.order.dialog.DriverVerifyHintFragment
import shy.car.sdk.travel.order.presenter.OrderDetailPresenter
import shy.car.sdk.travel.pay.WXPayUtil
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.take.data.OrderState
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/23
 * 接单详情
 */
@Route(path = RouteMap.OrderTakeDetailFragment)
class OrderDetailFragment : XTBaseFragment(),
        OrderDetailPresenter.CallBack {
    override fun paySuccess() {
        activity?.let {
            ToastManager.showShortToast(it, "支付成功")
        }
        presenter.getOrderDetail()
    }

    override fun onCreatePaySuccess(t: JsonObject) {
        activity?.let {
            if (!WXPayUtil.pay(it as XTBaseActivity, presenter.payMethod.get()!!, binding.detail?.freight?.toDouble()!!, t)) {

            }
        }
    }

    override fun cancelOrderSuccess() {
        finish()
    }

    override fun onGetDetailSuccess(t: DeliveryOrderDetail) {
        activity?.let {
            ProgressDialog.hideLoadingView(it)
        }
        binding.detail = t
        setBtnText()

        if (User.instance.phone == t.user?.phone)
            if (t.status != OrderState.StateWaitTake && t.status != OrderState.Close) {
                sendingState.set(true)
            }
    }

    private fun setBtnText() {
        binding.btnText = when (binding.detail?.status) {
            OrderState.StateWaitTake -> {
                if (User.instance.phone == binding.detail?.user?.phone) {
                    isBtnVisible.set(true)
                    canCancel.set(true)
                    "取消订单"
                } else {
                    isBtnVisible.set(true)
                    canCancel.set(false)
                    "确认接单"
                }
            }
            OrderState.StateWaitPay -> {
                isBtnVisible.set(User.instance.phone == binding.detail?.user?.phone)
                canCancel.set(binding.detail?.user?.phone == User.instance.phone || binding.detail?.carrier?.phone == User.instance.phone)
                "确认支付"
            }
            OrderState.StateSending -> {
                if (User.instance.phone == binding.detail?.carrier?.phone) {
                    isBtnVisible.set(true)
                    canCancel.set(false)
                    "确认送达"
                } else {
                    isBtnVisible.set(false)
                    canCancel.set(false)
                    ""
                }
            }
            OrderState.StateSended -> {
                if (User.instance.phone == binding.detail?.user?.phone) {
                    isBtnVisible.set(true)
                    canCancel.set(false)
                    "确认签收"
                } else {
                    isBtnVisible.set(false)
                    canCancel.set(false)
                    ""
                }
            }
            else -> {
                isBtnVisible.set(false)
                canCancel.set(false)
                ""
            }
        }


    }


    override fun onError(e: Throwable) {
        activity?.let {
            ProgressDialog.hideLoadingView(it)
            ErrorManager.managerError(it, e, "获取失败，请重试")
        }

    }

    @Autowired(name = String1)
    @JvmField
    var oid = ""
    lateinit var binding: FragmentOrderDetailBinding
    lateinit var presenter: OrderDetailPresenter

    val isBtnVisible = ObservableBoolean(true)
    val canCancel = ObservableBoolean(false)
    val sendingState = ObservableBoolean(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        activity?.let { presenter = OrderDetailPresenter(it, this) }
        presenter.oid = oid
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { ProgressDialog.showLoadingView(it) }
        presenter.getOrderDetail()
        register(this)
    }

    fun onButtonClick() {

        when (binding.detail?.status) {
            OrderState.StateWaitTake -> {
                if (User.instance.phone == binding.detail?.user?.phone) {
                    cancelOrder()
                } else {
                    if (User.instance.getIsDriverAuth()) {
                        DialogManager.with(activity, childFragmentManager)
                                .leftButtonText("取消")
                                .rightButtonText("确定")
                                .message("确认接单？")
                                .onRightClick { _, _ -> presenter.postTakeOrder() }
                                .show()
                    } else {
                        val hintDialog = DriverVerifyHintFragment()
                        hintDialog.show(childFragmentManager, "dialog-driver-verify-hint")
                    }
                }
            }
            OrderState.StateWaitPay -> {
                selectPayMethod()
            }
            OrderState.StateSending -> {
                if (User.instance.phone == binding.detail?.carrier?.phone) {
                    ARouter.getInstance()
                            .build(RouteMap.OrderSendedPhoto)
                            .withObject(Object1, binding.detail)
                            .navigation()
                }
            }
            OrderState.StateSended -> {
                if (User.instance.phone == binding.detail?.user?.phone) {
                    DialogManager.with(activity, childFragmentManager)
                            .leftButtonText("取消")
                            .rightButtonText("确定")
                            .message("请确认货物已经送达目的地后，确认签收？")
                            .onRightClick { _, _ -> presenter.orderDeliveryFinish() }
                            .show()
                }

            }
            else -> {

            }
        }
    }

    fun callUser() {
        activity?.let {
            if (binding.detail?.user?.phone == User.instance.phone) {
                Phone.call(it, binding.detail?.carrier?.phone)
            } else {
                Phone.call(it, binding.detail?.user?.phone)
            }
        }
    }

    fun cancelOrder() {
        if (binding.detail?.status == OrderState.StateWaitPay) {
            DialogManager.with(activity, childFragmentManager)
                    .leftButtonText("取消")
                    .rightButtonText("确定")
                    .message("该订单已有司机接单，确认取消该订单？")
                    .onRightClick { _, _ -> presenter.cancelOrder() }
                    .show()
        } else {
            DialogManager.with(activity, childFragmentManager)
                    .leftButtonText("取消")
                    .rightButtonText("确定")
                    .message("确认取消该订单？")
                    .onRightClick { _, _ -> presenter.cancelOrder() }
                    .show()
        }
    }

    fun selectPayMethod() {
        val dialog = ARouter.getInstance().build(RouteMap.PaySelect)
                .withObject(ParamsConstant.Object1, presenter.payMethod.get())
                .withInt(ParamsConstant.Int1, 2)//1:充值，2:支付
                .navigation() as PayMethodSelectDialog
        dialog.type = 2
        dialog.listener = object : PayMethodSelectDialog.OnPayClick {
            override fun onPaySelect(payMethod: PayMethod) {
                presenter.payMethod.set(payMethod)
                presenter.createPay()
            }
        }
        dialog.show(childFragmentManager, "fragment_pay")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun paySuccess(success: PaySuccess) {
        activity?.let {
            ToastManager.showShortToast(it, "支付成功")
        }
        presenter.getOrderDetail()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendSuccess(success: SendSuccess) {
        presenter.getOrderDetail()
    }
}

