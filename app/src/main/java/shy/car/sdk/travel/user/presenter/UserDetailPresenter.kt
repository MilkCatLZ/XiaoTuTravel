package shy.car.sdk.travel.user.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingBaseAdapter
import com.base.util.key.KeyValue
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
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

    var nickName = ObservableField<String>("")


    var listener: UploadListener? = null
    var sex: Int = 0
    var birthDay = ObservableField<String>("")
    var jobAdapter = DataBindingBaseAdapter<com.base.util.key.KeyValue>(R.layout.item_spinner, BR.item, context, null)

    init {
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
        jobAdapter.items.add(KeyValue("IT工程师", "IT工程师"))
    }

    fun uploadAvatar(url: String) {
        ProgressDialog.showLoadingView(context)
        ApiManager.getInstance()
                .api.uploadAvatar(File(url))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<JsonObject> {
                    override fun onComplete() {

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