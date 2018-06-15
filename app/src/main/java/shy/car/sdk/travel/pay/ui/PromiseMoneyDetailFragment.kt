package shy.car.sdk.travel.pay.ui

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
import shy.car.sdk.databinding.FragmentPromiseMoneyDetailBinding
import shy.car.sdk.travel.pay.data.PromiseMoneyDetail
import shy.car.sdk.travel.pay.presenter.PromiseMoneyDetailPresenter

/**
 * create by LZ at 2018/05/21
 * 保证金明细
 */
class PromiseMoneyDetailFragment : XTBaseUltimateRecyclerViewFragment(),
        PromiseMoneyDetailPresenter.CallBack {


    lateinit var binding: FragmentPromiseMoneyDetailBinding
    lateinit var presenter: PromiseMoneyDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = PromiseMoneyDetailPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_promise_money_detail, null, false)
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }

    override fun getFragmentName(): String {
        return "保证金明细"
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
        return binding.recyclerViewCarSelect
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    override fun onError(e: Throwable) {
        checkHasMore()
//        refreshOrLoadMoreComplete()

    }

    override fun getListSuccess(list: List<PromiseMoneyDetail>) {
        checkHasMore()
//        refreshOrLoadMoreComplete()

    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }
}