package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import android.databinding.*
import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.ItemViewHolder
import com.base.widget.FullLinearLayoutManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceHistory
import shy.car.sdk.travel.invoice.data.InvoiceList
import kotlin.reflect.jvm.internal.impl.util.Check

class InvoiceHistoryPresenter(context: Context, var callBack: CommonCallBack<List<InvoiceHistory>>) : BasePresenter(context) {

    var pageSize = 10
    var pageIndex = 1


    val adapter = DataBindingItemClickAdapter<InvoiceHistory>(R.layout.item_invoice_history, BR.history, BR.click, {
    }, { holder, position ->

    })


    init {
        initData()
    }


    private fun initData() {

    }

    private fun getInvoiceList() {
        val observable = ApiManager.getInstance()
                .api.getInvoiceHistoryList(offset = (pageIndex - 1) * pageSize, limit = pageSize)
        val observer = object : Observer<List<InvoiceHistory>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<InvoiceHistory>) {
                adapter.setItems(t, pageIndex)
                callBack.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }


    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getInvoiceList()
    }

    fun nextPage() {
        pageIndex++
        getInvoiceList()
    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }

}