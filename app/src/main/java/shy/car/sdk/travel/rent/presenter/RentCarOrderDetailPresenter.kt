package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.base.ProgressDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.order.net.OrderManager

class RentCarOrderDetailPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getDetailSuccess(t: RentOrderDetail)
        fun onError(e: Throwable)
    }

    fun getRentOrderDetail(orderID:String) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        OrderManager.getOrderDetail(orderID, true, callBack = object : OrderManager.GetDetailCallBack {
            override fun getDetailSuccess(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                callBack.getDetailSuccess(t)
            }

            override fun getDetailError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "获取订单失败")
                callBack.onError(e)
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

        })
    }
}