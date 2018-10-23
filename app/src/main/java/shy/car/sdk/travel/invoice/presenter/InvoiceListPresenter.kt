package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import androidx.databinding.*
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.ItemViewHolder
import com.base.util.DoubleUtil
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
    val selectAllChecked = ObservableBoolean(false)
    private val invoiceList = ArrayList<InvoiceList>()


    var totalInvoicePrice = ObservableDouble(0.0)
    var pageSize = 10
    var pageIndex = 1
    val invoiceCount = ObservableField<String>("0")
    /**
     * 保存列表中所有发票的总数，用于判断是否全选
     */
    private var totalInvoiceCount: Int = 0

    val allChecked = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            val checked = sender as ObservableBoolean
            if (checked.get()) {
                selectAll()
            } else {
                selectNone()
            }
        }

    }

    val adapter = DataBindingItemClickAdapter<InvoiceList>(R.layout.item_invoice_list, BR.invoice, BR.click, {
    }, { holder, position ->
        setupInvoice(holder, position)
    })

    private fun setupInvoice(holder: ItemViewHolder<*>, position: Int) {

        val orderAdapter = DataBindingItemClickAdapter<InvoiceList.Orders>(R.layout.item_invoice_list_detail, BR.orders, BR.click, {
        }, { holder, position ->
            setupOrders(holder, position)
        })
        orderAdapter.setItems(invoiceList[position].orders, 1)
        val recycler = holder.binding.root.findViewById<RecyclerView>(R.id.recyclerView_invoice_order)
        recycler.layoutManager = FullLinearLayoutManager(context)
        recycler.adapter = orderAdapter

    }

    private fun setupOrders(holder: ItemViewHolder<*>, position: Int) {
//        val checkBox = holder.binding.root.findViewById<CheckBox>(R.id.check_order)
//        checkBox.setOnClickListener {
//            val check = it as CheckBox
//            if (check.isChecked) {
//                if (!checkList.contains(checkBox.tag))
//                    checkList.add(checkBox.tag as InvoiceList.Orders)
//                if (checkList.size == invoiceCount.get()?.toInt()) {
//                    selectAllChecked.set(true)
//                }
//            } else {
//                checkList.remove(checkBox.tag)
//                if (selectAllChecked.get()) {
//                    selectAllChecked.removeOnPropertyChangedCallback(allChecked)
//                    selectAllChecked.set(false)
//                    selectAllChecked.addOnPropertyChangedCallback(allChecked)
//                }
//
//            }
//            invoiceCount.set(checkList.size.toString())
//        }

        holder.binding.setVariable(BR.presenter, this@InvoiceListPresenter)
        holder.binding.setVariable(BR.click, View.OnClickListener {
            val orders = it.tag as InvoiceList.Orders
            if (checkList.contains(orders)) {
                checkList.remove(orders)

            } else {
                checkList.add(orders)
            }
        })

    }

    init {
        initData()
        addListListener()
        addCheckedListener()
    }

    private fun addCheckedListener() {

        selectAllChecked.addOnPropertyChangedCallback(allChecked)
    }

    private fun addListListener() {

        checkList.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<InvoiceList.Orders>>() {
            override fun onChanged(sender: ObservableArrayList<InvoiceList.Orders>?) {
                invoiceCount.set(sender?.size.toString())
                countTotal()
            }

            override fun onItemRangeRemoved(sender: ObservableArrayList<InvoiceList.Orders>?, positionStart: Int, itemCount: Int) {
                invoiceCount.set(sender?.size.toString())
                countTotal()
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<InvoiceList.Orders>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                invoiceCount.set(sender?.size.toString())
                countTotal()
            }

            override fun onItemRangeInserted(sender: ObservableArrayList<InvoiceList.Orders>?, positionStart: Int, itemCount: Int) {
                invoiceCount.set(sender?.size.toString())
                countTotal()
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<InvoiceList.Orders>?, positionStart: Int, itemCount: Int) {
                invoiceCount.set(sender?.size.toString())
                countTotal()
            }

        })
    }

    private fun countTotal() {
        var price = 0.0
        checkList.map {
            price = DoubleUtil.add(price, it.money)
        }
        totalInvoicePrice.set(price)

        if (selectAllChecked.get()) {
            if (checkList.size < totalInvoiceCount) {
                selectAllChecked.removeOnPropertyChangedCallback(allChecked)
                selectAllChecked.set(false)
                selectAllChecked.addOnPropertyChangedCallback(allChecked)
            }
        } else if (checkList.size == totalInvoiceCount) {
            selectAllChecked.set(true)
        }
    }

    private fun initData() {
//        if (BuildConfig.DEBUG) {
//            val list = ArrayList<InvoiceList>()
//            for (i in 1..3) {
//                val invoice = InvoiceList()
//                invoice.month = i
//                list.add(invoice)
//            }
//            invoiceList.addAll(list)
//            invoiceList.map {
//                totalInvoiceCount += it.orders!!.size
//            }
//            adapter.setItems(invoiceList, 1)
//        }
    }


    private fun getInvoiceList() {
        val observable = ApiManager.getInstance()
                .api.getInvoiceList(offset = (pageIndex - 1) * pageSize, limit = pageSize)
        val observer = object : Observer<List<InvoiceList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<InvoiceList>) {
                invoiceList.clear()
                invoiceList.addAll(t)

                adapter.setItems(invoiceList, pageIndex)

                totalInvoiceCount = 0
                invoiceList.map {
                    totalInvoiceCount += it.orders!!.size
                }
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

    fun selectNone() {
        checkList.clear()
//        adapter.notifyDataSetChanged()
    }

    fun selectAll() {
        invoiceList.map {
            it.orders?.map {
                if (!checkList.contains(it))
                    checkList.add(it)
            }
        }
//        adapter.notifyDataSetChanged()
    }

}