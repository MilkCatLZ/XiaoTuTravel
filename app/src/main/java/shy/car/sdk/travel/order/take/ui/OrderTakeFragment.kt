package shy.car.sdk.travel.order.take.ui

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
import shy.car.sdk.databinding.FragmentOrderTakeBinding
import shy.car.sdk.travel.order.take.data.OrderList
import shy.car.sdk.travel.order.take.presenter.OrderTakePresenter

/**
 * create by LZ at 2018/05/11
 * 首页-接单
 */
@Route(path = RouteMap.OrderTake)
class OrderTakeFragment : XTBaseUltimateRecyclerViewFragment() {
    override fun getPrecenter(): BasePresenter? {
        return presenter
    }


    lateinit var binding: FragmentOrderTakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = OrderTakePresenter(it, object : OrderTakePresenter.CallBack {
                override fun getListSuccess(list: ArrayList<OrderList>) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }
            })
        }
    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_take, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presenter = presenter
    }

    lateinit var presenter: OrderTakePresenter


    override fun getFragmentName(): String {
        return "首页-接单"
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
        return binding.recyclerViewOrderTake
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }


}