package shy.car.sdk.travel.rent.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.NearCarPoint

/**
 * 还车区域
 */
class ReturnAreaPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getListSuccess(t: ArrayList<NearCarPoint>)
        fun onError(e: Throwable)

    }

    fun getNetWorkList() {

        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getNearList(app.location.cityCode, app.location.lat.toString(), app.location.lng.toString(), 0, 10)
        val observer = object : Observer<ArrayList<NearCarPoint>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: ArrayList<NearCarPoint>) {
                EventBus.getDefault()
                        .post(t)
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}