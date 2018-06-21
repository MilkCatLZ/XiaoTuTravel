package shy.car.sdk.travel.order.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.take.data.DeliveryOrderList
import shy.car.sdk.travel.order.data.DeliveryOrderDetail

class OrderDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun onGetDetailSuccess(t: DeliveryOrderDetail)
        abstract fun onError(e: Throwable)

    }

    lateinit var orderList: DeliveryOrderList


    fun getOrderDetail() {
        val observable = ApiManager.getInstance().api.getTakeOrderDetail(orderList.id)
        val observer = object : Observer<DeliveryOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: DeliveryOrderDetail) {
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
            }

        }
        ApiManager.getInstance().toSubscribe(observable, observer)
    }

    fun postTakeOrder() {

    }
}