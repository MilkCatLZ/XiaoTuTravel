package shy.car.sdk.travel.take.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.take.data.TakeOrderDetail
import shy.car.sdk.travel.take.data.DeliveryOrderList

class OrderTakeDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun onGetDetailSuccess(t: TakeOrderDetail)
        abstract fun onError(e: Throwable)

    }

    lateinit var orderList: DeliveryOrderList


    fun getOrderDetail() {
        var observable = ApiManager.getInstance().api.getTakeOrderDetail(orderList.id)
        val obsever = object : Observer<TakeOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: TakeOrderDetail) {
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