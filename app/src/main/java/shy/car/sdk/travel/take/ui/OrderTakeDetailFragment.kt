package shy.car.sdk.travel.take.ui

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
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderTakeDetailBinding
import shy.car.sdk.travel.take.data.TakeOrderDetail
import shy.car.sdk.travel.take.data.TakeOrderList
import shy.car.sdk.travel.take.presenter.OrderTakeDetailPresenter

/**
 * create by LZ at 2018/05/23
 * 接单详情
 */
@Route(path = RouteMap.OrderTakeDetailFragment)
class OrderTakeDetailFragment : XTBaseFragment(), OrderTakeDetailPresenter.CallBack {
    override fun onGetDetailSuccess(t: TakeOrderDetail) {
        activity?.let { ProgressDialog.hideLoadingView(it) }
    }

    override fun onError(e: Throwable) {
        activity?.let {
            ProgressDialog.hideLoadingView(it)
            ErrorManager.managerError(it, e, "获取失败，请重试")
        }

    }

    @Autowired
    @JvmField
    var takeOrderList: TakeOrderList = TakeOrderList()

    lateinit var binding: FragmentOrderTakeDetailBinding

    lateinit var presenter: OrderTakeDetailPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        activity?.let { presenter = OrderTakeDetailPresenter(it, this) }
        presenter.orderList = takeOrderList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_take_detail, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { ProgressDialog.showLoadingView(it) }
        presenter.getOrderDetail()
    }

    fun onConfirmClick() {
        activity?.let {
            DialogManager.with(it, childFragmentManager)
                    .leftButtonText("取消")
                    .rightButtonText("确定")
                    .onRightClick { _, _ -> presenter.postTakeOrder() }
                    .show()
        }
    }
}