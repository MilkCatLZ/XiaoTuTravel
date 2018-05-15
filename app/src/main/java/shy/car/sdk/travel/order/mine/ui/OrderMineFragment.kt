package shy.car.sdk.travel.order.mine.ui

import android.databinding.DataBindingUtil
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
import shy.car.sdk.travel.order.mine.data.OrderMineList
import shy.car.sdk.travel.order.take.presenter.OrderMinePresenter

/**
 * create by lz at 2018/05/15
 * 我的订单
 */
class OrderMineFragment : XTBaseUltimateRecyclerViewFragment() {
    lateinit var binding: FragmentOrderMineBinding
    lateinit var presenter: OrderMinePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = OrderMinePresenter(it, object : OrderMinePresenter.CallBack {
                override fun getListSuccess(list: ArrayList<OrderMineList>) {

                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_mine, null, false)
        return binding.root
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}