package shy.car.sdk.travel.invoice.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentInvoiceListBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceList
import shy.car.sdk.travel.invoice.presenter.InvoiceListPresenter

/**
 * create by Sharon at 2018/07/05
 * 发票列表
 */
class InvoiceListFragment : XTBaseUltimateRecyclerViewFragment(),
        CommonCallBack<List<InvoiceList>> {
    override fun getFragmentName(): String {
        return "开具发票"
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
        return binding.recyclerViewInvoiceList
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    override fun onSuccess(t: List<InvoiceList>) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    override fun onError(e: Throwable) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    lateinit var binding: FragmentInvoiceListBinding
    lateinit var presenter: InvoiceListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = InvoiceListPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoice_list, null, false)
        binding.fragment = this
        binding.presenter = presenter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }

    fun next() {
        ARouter.getInstance()
                .build(RouteMap.InvoicePost)
                .withObject(Object1, presenter.checkList)
                .navigation()
    }
}