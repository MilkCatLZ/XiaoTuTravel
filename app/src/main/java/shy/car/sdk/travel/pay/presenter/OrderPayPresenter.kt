package shy.car.sdk.travel.pay.presenter

import android.content.Context
import android.databinding.ObservableField
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.order.net.OrderManager
import shy.car.sdk.travel.pay.data.PayMethod

class OrderPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetDetailSuccess(t: RentOrderDetail)
        fun onGetDetailError(e: Throwable)
        fun getPayStringSuccess(t: JsonObject)
        fun paySuccess()
    }

    val payMethod = ObservableField<PayMethod>()

    private lateinit var detail: RentOrderDetail


    fun getOrderDetail(orderID: String) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        OrderManager.getOrderDetail(orderID, true, callBack = object : OrderManager.GetDetailCallBack {
            override fun getDetailSuccess(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                this@OrderPayPresenter.detail = t
                callBack.onGetDetailSuccess(t)
            }

            override fun getDetailError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取详情失败")
                callBack.onGetDetailError(e)
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

        })

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
                try {
                    if (t.get("payment").asInt == 3) {
                        callBack.paySuccess()
                    } else {
                        callBack.getPayStringSuccess(t)
                    }
                } catch (_: Exception) {
                    throw NullPointerException()
                }

            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                if(e is NullPointerException){
                    ToastManager.showShortToast(context, "支付失败，如有疑问请联系客服")
                }else {
                    val err = ErrorManager.managerError(context, e, "支付失败")
                    if (err?.error_code == 400118) {
                        ARouter.getInstance()
                                .build(RouteMap.Pay)
                                .navigation()
                    }
                }
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}