package shy.car.sdk.travel.location

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.location.data.CurrentLocation

/**
 * create by LZ at 2018/05/12
 */
class LocationPresenter(context: Context, callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getCitySuccess(list: List<CurrentLocation>)
    }

    val observer = object : Observer<List<CurrentLocation>> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {
            disposable = d
        }

        override fun onNext(t: List<CurrentLocation>) {
            callBack.getCitySuccess(t)
        }

        override fun onError(e: Throwable) {
            ErrorManager.managerError(context, e, "")
        }
    }

    fun getCity() {
        var observerable = ApiManager.getInstance()
                .api.getCityList()
        ApiManager.getInstance()
                .toSubscribe(observerable, observer)


    }
}