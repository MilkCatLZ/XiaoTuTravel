package shy.car.sdk.travel.order.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.base.util.Phone
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.take.data.DeliveryOrderList
import shy.car.sdk.travel.order.data.DeliveryOrderDetail

class OrderDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun onGetDetailSuccess(t: DeliveryOrderDetail)
        fun onError(e: Throwable)
        fun onTakeOrderSuccess(t: JsonObject)
        fun onTakeError(e: Throwable)

    }

    lateinit var orderList: DeliveryOrderList


    fun getOrderDetail() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.getOrderDetail(orderList.id)
        val observer = object : Observer<DeliveryOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: DeliveryOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun postTakeOrder() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.takeDeliveryOrder(orderList.id)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.onTakeOrderSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onTakeError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}