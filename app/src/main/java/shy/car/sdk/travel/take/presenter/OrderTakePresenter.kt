package shy.car.sdk.travel.take.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.take.data.TakeOrderList

class OrderTakePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: List<TakeOrderList>)
        fun onItemClick(takeOrderList: TakeOrderList)
        fun getListError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<TakeOrderList> = DataBindingItemClickAdapter(R.layout.item_order_take, BR.order, BR.click, {
        callBack.onItemClick(it.tag as TakeOrderList)
    })
    var pageSize = 10
    var pageIndex = 1

    init {
//        val list = ArrayList<TakeOrderList>()
//        for (i in 1..9) {
//            var orderTakeList = TakeOrderList()
//            orderTakeList.id = i.toString()
//            orderTakeList.status = i % 4
//            list.add(orderTakeList)
//        }
//
//        adapter.setItems(list, 1)
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
        val observable = ApiManager.getInstance()
                .api.getOrderList(offset = (pageIndex - 1) * pageSize, limit = pageSize)
        val observer = object : Observer<List<TakeOrderList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: List<TakeOrderList>) {
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.getListError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)

    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }

}