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
import com.google.gson.JsonObject
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderDetailBinding
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import shy.car.sdk.travel.order.presenter.OrderDetailPresenter
import shy.car.sdk.travel.take.data.DeliveryOrderList
import shy.car.sdk.travel.take.data.OrderState
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/23
 * 接单详情
 */
@Route(path = RouteMap.OrderTakeDetailFragment)
class OrderDetailFragment : XTBaseFragment(),
        OrderDetailPresenter.CallBack {
    override fun onTakeOrderSuccess(t: JsonObject) {
        presenter.getOrderDetail()
    }

    override fun onTakeError(e: Throwable) {
        activity?.let {
            ErrorManager.managerError(it, e, "接单失败，请重试")
            presenter.getOrderDetail()
        }
    }

    override fun onGetDetailSuccess(t: DeliveryOrderDetail) {
        activity?.let {
            ProgressDialog.hideLoadingView(it)
        }
        binding.detail = t
        setBtnText()
    }

    private fun setBtnText() {
        binding.btnText = when (binding.detail?.status) {
            OrderState.StateWaitTake -> {
                isBtnVisible.set(User.instance.phone != binding.detail?.user?.phone)
                "确认接单"
            }
            OrderState.StateWaitPay -> {
                isBtnVisible.set(User.instance.phone == binding.detail?.user?.phone)
                "确认支付"
            }
            OrderState.StateSending -> {
                isBtnVisible.set(User.instance.phone == binding.detail?.user?.phone)
                "确认签收"
            }
            else -> {
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

    @Autowired(name = Object1)
    @JvmField
    var takeOrderList: DeliveryOrderList = DeliveryOrderList()
    lateinit var binding: FragmentOrderDetailBinding
    lateinit var presenter: OrderDetailPresenter

    val isBtnVisible = ObservableBoolean(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        activity?.let { presenter = OrderDetailPresenter(it, this) }
        presenter.orderList = takeOrderList
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
    }

    fun onButtonClick() {

        when (binding.detail?.status) {
            OrderState.StateWaitTake -> {
                DialogManager.with(activity, childFragmentManager)
                        .leftButtonText("取消")
                        .rightButtonText("确定")
                        .onRightClick { _, _ -> presenter.postTakeOrder() }
                        .show()

            }
            OrderState.StateWaitPay -> {
                ARouter.getInstance()
                        .build(RouteMap.OrderPay)
                        .withObject(Object1, binding.detail)
                        .navigation()
            }
            else -> {

            }
        }
    }

    fun callUser() {
        activity?.let {
            Phone.call(it, binding.detail?.user?.phone)
        }
    }
}