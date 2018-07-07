package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceHistory

class InvoiceHistoryPresenter(context: Context, var callBack: CommonCallBack<List<InvoiceHistory>>) : BasePresenter(context) {

    var pageSize = 10
    var pageIndex = 1


    val adapter = DataBindingItemClickAdapter<InvoiceHistory>(R.layout.item_invoice_history, BR.history, BR.click, {
        val invoice = it.tag as InvoiceHistory
        ARouter.getInstance()
                .build(RouteMap.InvoiceDetail)
                .withString(String1, invoice.id.toString())
                .navigation()
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