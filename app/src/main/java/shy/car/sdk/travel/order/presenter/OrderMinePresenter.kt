package shy.car.sdk.travel.order.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.OrderMineList

class OrderMinePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: ArrayList<OrderMineList>)
        fun getListError(e: Throwable)
    }

    var adapter: DataBindingItemClickAdapter<OrderMineList> = DataBindingItemClickAdapter(R.layout.item_order_mine, BR.order, BR.click, {
        var order= it.tag as OrderMineList
        ARouter.getInstance()
                .build(RouteMap.RentCarDetail)
                .withString("orderID",order.id)
                .navigation()
    })
    var pageSize = 10
    var pageIndex = 1

    init {
        val list = ArrayList<OrderMineList>()
        for (i in 1..9) {
            list.add(OrderMineList())
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
        ApiManager.getInstance()
                .api.getOrdreMineList(pageIndex, pageSize)
                .subscribe(object : Observer<ArrayList<OrderMineList>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: ArrayList<OrderMineList>) {
                        callBack.getListSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        callBack.getListError(e)
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