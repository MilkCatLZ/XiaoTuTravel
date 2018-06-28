package shy.car.sdk.travel.pay.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.pay.data.PayMethod

class OrderPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetDetailSuccess(t: RentOrderDetail)
        fun onGetDetailError(e: Throwable)
        fun getPayStringSuccess(t: JsonObject)
    }

    val payMethod = ObservableField<PayMethod>()

    private lateinit var detail: RentOrderDetail


    fun getOrderDetail(orderID: String) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getRentOrderDetail(orderID)
        val observer = object : Observer<RentOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                this@OrderPayPresenter.detail = t
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取详情失败")
                callBack.onGetDetailError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun pay() {
        if (payMethod.get() == null) {
            ToastManager.showShortToast(context, "请选择支付方式")
            return
        }

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()

        val observable = ApiManager.getInstance()
                .api.payOrder(detail.orderId!!, payMethod.get()?.id.toString())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.getPayStringSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "支付失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}