package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.widget.CheckBox
import com.base.databinding.DataBindingItemClickAdapter
import com.base.widget.FullLinearLayoutManager
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceList

class InvoiceListPresenter(context: Context, var callBack: CommonCallBack<List<InvoiceList>>) : BasePresenter(context) {

    val checkList = ObservableArrayList<InvoiceList.Orders>()
    val invoiceList = ArrayList<InvoiceList>()
    val adapter = DataBindingItemClickAdapter<InvoiceList>(R.layout.item_invoice_list, BR.invoice, BR.click, {

    }, { holder, position ->

        val orderAdapter = DataBindingItemClickAdapter<InvoiceList.Orders>(R.layout.item_invoice_list_detail, BR.orders, BR.click, {}, { holder, position ->
            val checkBox = holder.binding.root.findViewById<CheckBox>(R.id.check_order)
            checkBox.setOnCheckedChangeListener { p0, p1 ->
                if (p1) {
                    checkList.add(checkBox.tag as InvoiceList.Orders)
                } else {
                    checkList.remove(checkBox.tag)
                }
            }
            holder.binding.setVariable(BR.presenter,this@InvoiceListPresenter)
        })
        orderAdapter.setItems(invoiceList[position].orders,1)
        val recycler = holder.binding.root.findViewById<RecyclerView>(R.id.recyclerView_invoice_order)
        recycler.layoutManager = FullLinearLayoutManager(context)
        recycler.adapter = orderAdapter

    })

    init {

    }

    fun getInvoiceList() {
        val observable = ApiManager.getInstance()
                .api.getInvoiceList(offset = (pageIndex - 1) * pageSize, limit = pageSize)
        val observer = object : Observer<List<InvoiceList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<InvoiceList>) {
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

    var pageSize = 10
    var pageIndex = 1

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

    fun selectNone() {


    }

    fun selectAll() {


    }

}