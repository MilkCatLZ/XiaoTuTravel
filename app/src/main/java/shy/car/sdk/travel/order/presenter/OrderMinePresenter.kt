package shy.car.sdk.travel.order.presenter

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
import shy.car.sdk.travel.order.data.OrderMineList
import shy.car.sdk.travel.rent.data.RentOrderState

class OrderMinePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun getListSuccess(list: List<OrderMineList>)
        fun getListError(e: Throwable)
    }


    var adapter: DataBindingItemClickAdapter<OrderMineList> = DataBindingItemClickAdapter(R.layout.item_order_mine, BR.order, BR.click) {
        val order = it.tag as OrderMineList
        if (type == 1) {
            when (order.status) {
            //1:预约单
                RentOrderState.Create -> {
                    ARouter.getInstance()
                            .build(RouteMap.FindAndRentCar)
                            .withString(String1, order.id)
                            .navigation()
                }
            //待支付
                RentOrderState.Return -> {
                    ARouter.getInstance()
                            .build(RouteMap.OrderPay)
                            .withString(String1, order.id)
                            .navigation()
                }
            //已取车
                RentOrderState.Taked -> {
                    ARouter.getInstance()
                            .build(RouteMap.Driving)
                            .withString(String1, order.id)
                            .navigation()
                }
                else -> {
                    ARouter.getInstance()
                            .build(RouteMap.RentCarDetail)
                            .withString(String1, order.id)
                            .navigation()
                }
            }
        } else {
            //货运订单
            ARouter.getInstance()
                    .build(RouteMap.OrderDetail)
                    .withString(String1, order.id)
                    .navigation()
        }
    }
    var pageSize = 10
    var pageIndex = 1
    var type = OrderMineList.RENT

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
                .api.getOrderMineList(type = type.toString(), offset = (pageIndex - 1) * pageSize, limit = pageSize)
        val observer = object : Observer<List<OrderMineList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: List<OrderMineList>) {
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

//    private fun getAllOrderList() {
//        type = OrderMineList.ALL
//        getOrderList()
//    }
//
//    private fun getRentOrderList() {
//        type = OrderMineList.RENT
//        getOrderList()
//    }
//
//    private fun getTakeOrderList() {
//        type = OrderMineList.TAKE
//        getOrderList()
//    }
//
//    private fun getSendOrderList() {
//        type = OrderMineList.SEND
//        getOrderList()
//    }

    fun getTotal(): Int {
        return if (hasMore())
            adapter.adapterItemCount + 5
        else
            adapter.adapterItemCount
    }
}