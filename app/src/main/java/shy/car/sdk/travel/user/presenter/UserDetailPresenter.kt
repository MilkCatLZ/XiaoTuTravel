package shy.car.sdk.travel.user.presenter

import android.content.Context
import com.base.base.ProgressDialog
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import java.io.File

/**
 * create by LZ at 2018/06/11
 *
 */
class UserDetailPresenter(context: Context) : BasePresenter(context) {

    interface UploadListener {
        fun onUploadSuccess()
    }

    var listener: UploadListener? = null
    fun uploadAvatar(url: String) {
        ProgressDialog.showLoadingView(context)
        ApiManager.getInstance()
                .api.uploadAvatar(File(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JsonObject> {
                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: JsonObject) {
                        User.instance.avatar = BuildConfig.Host + t.get("avatar")
                        ProgressDialog.hideLoadingView(context)
                        listener?.onUploadSuccess()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
}