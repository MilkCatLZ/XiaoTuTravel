package shy.car.sdk.travel.order.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.DeliveryOrderDetail

class OrderDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {


    interface CallBack {
        fun onGetDetailSuccess(t: DeliveryOrderDetail)
        fun onError(e: Throwable)
    }

    var oid: String = ""


    fun getOrderDetail() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.getOrderDetail(oid)
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

    private fun convertToRequestBody(param: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), param)
    }

    /**
     * 接单
     */
    fun postTakeOrder() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.takeDeliveryOrder(oid)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                ToastManager.showShortToast(context, "接单成功，请尽快发货")
                getOrderDetail()
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "接单失败")
                getOrderDetail()
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    /**
     * 确认收货
     */
    fun orderDeliveryFinish() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.deliveryFinish(oid)
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                ToastManager.showShortToast(context, "订单已签收")
                getOrderDetail()
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "签收失败")
                getOrderDetail()
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    /**
     * 取消订单
     */
    fun cancelOrder() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.cancelOrder(oid)
        val observer = object : Observer<Response<Void>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

                disposable = d
            }

            override fun onNext(t: Response<Void>) {
                ProgressDialog.hideLoadingView(context)
                ToastManager.showShortToast(context, "订单已取消")
                ApiManager.getInstance().clearCache()
                getOrderDetail()
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "取消失败")
                ApiManager.getInstance().clearCache()
                getOrderDetail()

            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}