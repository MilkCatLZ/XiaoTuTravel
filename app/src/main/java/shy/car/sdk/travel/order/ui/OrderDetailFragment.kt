package shy.car.sdk.travel.order.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.DialogManager
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

/**
 * create by LZ at 2018/05/23
 * 接单详情
 */
@Route(path = RouteMap.OrderTakeDetailFragment)
class OrderDetailFragment : XTBaseFragment(), OrderDetailPresenter.CallBack {
    override fun onGetDetailSuccess(t: DeliveryOrderDetail) {
        activity?.let {
            ProgressDialog.hideLoadingView(it)
            detail = t
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
    var detail: DeliveryOrderDetail? = null

    lateinit var binding: FragmentOrderDetailBinding

    lateinit var presenter: OrderDetailPresenter
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

        detail?.let {
            when (it.status) {
                OrderState.StateWaitTake -> {
                    DialogManager.with(activity, childFragmentManager)
                            .leftButtonText("取消")
                            .rightButtonText("确定")
                            .onRightClick { _, _ -> presenter.postTakeOrder() }
                            .show()

                }
                OrderState.StateWaitPay -> {

                }
                OrderState.StateSending -> {

                }
                OrderState.StateFinish -> {

                }
                else -> {

                }
            }
        }

    }
}