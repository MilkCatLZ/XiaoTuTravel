package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.RentOrderDetail

class DrivingPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onGetDetailSuccess(t: RentOrderDetail)
        fun onGetDetailError(e: Throwable)

    }

    var detail = ObservableField<RentOrderDetail>()

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
                detail.set(t)
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

//    fun carRing() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.carAction(detail.get()?.orderId!!, status = 1.toString())
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: JsonObject) {
//                ToastManager.showLongToast(context, "车辆已鸣笛，请尽快找到车辆")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//            override fun onError(e: Throwable) {
//                ErrorManager.managerError(context, e, "操作失败，请重试")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }
//
//    fun openDoor() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.carAction(detail.get()?.car?.carID!!, detail.get()?.orderId!!, status = 2.toString())
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: JsonObject) {
//                ToastManager.showLongToast(context, "车门已打开")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//            override fun onError(e: Throwable) {
//                ErrorManager.managerError(context, e, "操作失败，请重试")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }
//    fun lockDoor() {
//        ProgressDialog.showLoadingView(context)
//        disposable?.dispose()
//        val observable = ApiManager.getInstance()
//                .api.carAction(detail.get()?.car?.carID!!, detail.get()?.orderId!!, status = 3.toString())
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//                disposable = d
//            }
//
//            override fun onNext(t: JsonObject) {
//                ToastManager.showLongToast(context, "车门已关闭")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//            override fun onError(e: Throwable) {
//                ErrorManager.managerError(context, e, "操作失败，请重试")
//                ProgressDialog.hideLoadingView(context)
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }
}