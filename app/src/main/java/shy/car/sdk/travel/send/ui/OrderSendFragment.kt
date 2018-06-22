package shy.car.sdk.travel.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderSendBinding
import shy.car.sdk.travel.send.data.OrderSendList
import shy.car.sdk.travel.send.presenter.OrderSendPresenter

/**
 * create by LZ at 2018/05/11
 */
@Route(path = RouteMap.OrderSend)
class OrderSendFragment : XTBaseUltimateRecyclerViewFragment() {
    override fun getFragmentName(): String {
        return "首页-发货"
    }

    lateinit var presenter: OrderSendPresenter
    override fun getPrecenter(): BasePresenter? {
        return presenter
    }

    private val callBack = object : OrderSendPresenter.CallBack {
        override fun getListError(e: Throwable) {
            refreshOrLoadMoreComplete()
            checkHasMore()
        }

        override fun getListSuccess(list: List<OrderSendList>) {
            refreshOrLoadMoreComplete()
            checkHasMore()
        }
    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = OrderSendPresenter(it, callBack) }
    }


    lateinit var binding: FragmentOrderSendBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_send, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presenter = presenter
        onRefresh()
    }

    override fun refresh() {
        presenter.refresh()
    }

    override fun getTotal(): Int {
        return presenter.getTotal()
    }

    override fun nextPage() {
        presenter.nextPage()
    }

    override fun getUltimateRecyclerView(): UltimateRecyclerView {
        return binding.recyclerViewOrderSend
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    fun onSendHoleCar() {
        ARouter.getInstance()
                .build(RouteMap.SendHoleCarEdit)
                .navigation()
    }
    fun onSendCitySmallPackageCar() {
        ARouter.getInstance()
                .build(RouteMap.SendCitySmallPackageSelect)
                .navigation()
    }
}