package shy.car.sdk.travel.order.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderSendBinding
import shy.car.sdk.travel.order.send.data.OrderSendList
import shy.car.sdk.travel.order.send.presenter.OrderSendPresenter

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
        override fun getListSuccess(list: ArrayList<OrderSendList>) {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presenter = presenter
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


}