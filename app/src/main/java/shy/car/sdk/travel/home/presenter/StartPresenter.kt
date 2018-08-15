package shy.car.sdk.travel.home.presenter

import android.content.Context
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.home.data.StartInfo

class StartPresenter(context: Context,var adListener:AdListener?=null) : BasePresenter(context) {

    interface AdListener{
        fun getAdSuccess(startInfo: StartInfo)
        fun getAdError()
    }

    fun getStartInfo() {
        val observable = ApiManager.getInstance()
                .api.getStartInfo()
        val observer = object : Observer<StartInfo> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: StartInfo) {
                adListener?.getAdSuccess(t)
            }

            override fun onError(e: Throwable) {
                if(BuildConfig.DEBUG){
                    adListener?.getAdSuccess(StartInfo())
                }else {
                    adListener?.getAdError()
                }
            }
        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}