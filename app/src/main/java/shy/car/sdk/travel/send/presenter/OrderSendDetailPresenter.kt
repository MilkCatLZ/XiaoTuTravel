package shy.car.sdk.travel.send.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.send.data.OrderSendDetail
import shy.car.sdk.travel.send.data.OrderSendList

class OrderSendDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun onGetDetailSuccess(t: OrderSendDetail)
        abstract fun onError(e: Throwable)

    }

    lateinit var orderList: OrderSendList


    fun getOrderDetail() {
        var observable = ApiManager.getInstance().api.getSendOrderDetail(orderList.id)
        val obsever = object : Observer<OrderSendDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: OrderSendDetail) {
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
            }

        }
        ApiManager.getInstance().toSubscribe(observable, obsever)
    }

    fun postTakeOrder() {

    }
}