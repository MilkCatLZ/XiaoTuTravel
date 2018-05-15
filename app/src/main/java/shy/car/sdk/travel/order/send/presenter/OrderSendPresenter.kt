package shy.car.sdk.travel.order.send.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.send.data.OrderSendList

/**
 * 发货首页
 */
class OrderSendPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: ArrayList<OrderSendList>)
    }

    var adapter: DataBindingItemClickAdapter<OrderSendList> = DataBindingItemClickAdapter(R.layout.item_order_send, BR.order, BR.click, {})
    var pageSize = 10
    var pageIndex = 1

    init {
        val list = ArrayList<OrderSendList>()
        for (i in 1..9) {
            list.add(OrderSendList())
        }

        adapter.setItems(list, 1)
    }

    fun hasMore(): Boolean {
        return adapter.adapterItemCount >= pageIndex * pageSize
    }

    fun refresh() {
        pageIndex = 1
        getOrderList()
    }

    fun nextPage() {
        pageIndex++
        getOrderList()
    }

    private fun getOrderList() {
        ApiManager.instance.api.getOrderSendList(pageIndex,pageSize).subscribe(object: Observer<ArrayList<OrderSendList>>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: ArrayList<OrderSendList>) {
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {

            }
        })
    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }
}