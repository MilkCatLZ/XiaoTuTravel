package shy.car.sdk.travel.order.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.FragmentOrderMineBinding
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.order.presenter.OrderMinePresenter

/**
 * create by lz at 2018/05/15
 * 我的订单
 */
class OrderMineFragment : XTBaseUltimateRecyclerViewFragment() {
    lateinit var binding: FragmentOrderMineBinding
    lateinit var presenter: OrderMinePresenter

    val checkedTab = ObservableInt(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = OrderMinePresenter(it, object : OrderMinePresenter.CallBack {
                override fun getListError(e: Throwable) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }

                override fun getListSuccess(list: List<OrderMineList>) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_mine, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }


    override fun getFragmentName(): String {
        return "我的订单"
    }

    override fun getPrecenter(): BasePresenter? {
        return presenter
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
        return binding.recyclerViewOrderMine
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    fun checkChange(i: Int) {
        checkedTab.set(i)
        presenter.type = i
        onRefresh()
    }

}