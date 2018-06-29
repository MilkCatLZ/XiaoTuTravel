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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.User
import java.io.File

/**
 * create by LZ at 2018/06/11
 *
 */
class UserDetailPresenter(context: Context,var listener: UserEditListener? = null) : BasePresenter(context) {
    interface UserEditListener {
        fun onUploadAvatarSuccess()
        fun onUploadAvatar()

    }

    var nickName = ObservableField<String>("")
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



    fun uploadAvatar(path: String) {


        var observable = ApiManager.getInstance()
                .api.uploadAvatar(getAvatarPat(path).parts())
        var observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                listener?.onUploadAvatarSuccess()
            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, "上传失败，请重试")
                listener?.onUploadAvatar()
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun getAvatarPat(path: String): MultipartBody {
        val image = File(path)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val imageBody = RequestBody.create(MediaType.parse("image/jpeg"), image)
        builder.addFormDataPart("avatar", image.name, imageBody)
        return builder.build()
    }
}