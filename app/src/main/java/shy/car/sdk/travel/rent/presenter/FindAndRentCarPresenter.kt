package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.CarInfo

/**
 * 找车取车
 */
class FindAndRentCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    var carInfo = CarInfo()


    interface CallBack {
        fun onRingError(e: Throwable)

    }

    fun carRing() {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.ringCar(carInfo.carId, status = 1.toString())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: JsonObject) {
                ToastManager.showLongToast(context, "车辆已鸣笛，请尽快找到车辆")
                ProgressDialog.hideLoadingView(context)
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, "操作失败，请重试")
                ProgressDialog.hideLoadingView(context)
                callBack.onRingError(e)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}