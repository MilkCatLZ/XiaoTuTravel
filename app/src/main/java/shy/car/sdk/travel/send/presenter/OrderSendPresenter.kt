package shy.car.sdk.travel.send.presenter

import android.content.Context
import android.view.View
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
import shy.car.sdk.travel.send.data.OrderSendList

/**
 * 发货首页
 */
class OrderSendPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getListSuccess(list: List<OrderSendList>)
        fun getListError(e: Throwable)
    }


    var adapter: DataBindingItemClickAdapter<OrderSendList> = DataBindingItemClickAdapter(R.layout.item_order_send, BR.order, BR.click, View.OnClickListener {
        val order = it.tag as OrderSendList
        ARouter.getInstance()
                .build(RouteMap.OrderDetail)
                .withString(String1, order.freightId)
                .navigation()
    })
    var pageSize = 10
    var pageIndex = 1

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
                .api.getOrderSendList("2", (pageIndex - 1) * pageSize, pageSize)
        val observer = object : Observer<List<OrderSendList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: List<OrderSendList>) {
                adapter.setItems(t, pageIndex)
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