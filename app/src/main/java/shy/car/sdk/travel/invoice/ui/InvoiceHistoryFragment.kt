package shy.car.sdk.travel.invoice.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.FragmentInvoiceHistoryBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceHistory
import shy.car.sdk.travel.invoice.presenter.InvoiceHistoryPresenter


/**
 * create by Sharon at 2018/07/07
 * 开票历史
 */
class InvoiceHistoryFragment : XTBaseUltimateRecyclerViewFragment(), CommonCallBack<List<InvoiceHistory>> {
    override fun onSuccess(t: List<InvoiceHistory>) {
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

    override fun onError(e: Throwable) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    override fun getFragmentName(): String {
        return "开票历史"
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
        return binding.recyclerViewInvoiceHistory
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    lateinit var binding: FragmentInvoiceHistoryBinding
    lateinit var presenter: InvoiceHistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = InvoiceHistoryPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoice_history, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onRealResume() {
        super.onRealResume()
        onRefresh()
    }
}