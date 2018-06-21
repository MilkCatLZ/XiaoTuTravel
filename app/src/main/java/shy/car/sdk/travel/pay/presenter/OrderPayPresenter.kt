package shy.car.sdk.travel.pay.presenter

import android.content.Context
import com.base.base.ProgressDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.RentOrderDetail

class OrderPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onGetDetailSuccess(t: RentOrderDetail)
        fun onGetDetailError(e: Throwable)
    }

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
}