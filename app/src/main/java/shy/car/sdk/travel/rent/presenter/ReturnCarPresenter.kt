package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.location.Location
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.NearCarPoint

class ReturnCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun onError(e: Throwable)
        fun getListSuccess(t: ArrayList<NearCarPoint>)

    }

    val location = ObservableField<Location>()
    val agree = ObservableBoolean(false)
    val locationCheckText = ObservableField<String>("")
    var isInNetWork: Boolean = false

    init {

    }

    fun getNetWork() {

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
                ProgressDialog.hideLoadingView(context)
                callBack.getListSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onError(e)
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

}