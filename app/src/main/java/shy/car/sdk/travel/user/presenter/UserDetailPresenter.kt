package shy.car.sdk.travel.user.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingBaseAdapter
import com.base.util.key.KeyValue
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.BR
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
class UserDetailPresenter(context: Context, var listener: UserEditListener? = null) : BasePresenter(context) {
    interface UserEditListener {
        fun onUploadAvatarSuccess()
        fun onUploadAvatarError()

    }

    var nickName = ObservableField<String>()
    var sex: Int = 0
    var sexText = ObservableField<String>()
    var city: String? = null
    var avatar = ObservableField<String>(User.instance.avatar)
    var birthDay = ObservableField<String>()
    var job = ObservableField<String>()
    //    var jobAdapter = DataBindingBaseAdapter<com.base.util.key.KeyValue>(R.layout.item_spinner, BR.item, context, null)
    var sexAdapter = DataBindingBaseAdapter<com.base.util.key.KeyValue>(R.layout.item_spinner, BR.item, context, null)

    init {
        nickName.set(User.instance.nickName)
        sexAdapter.items.add(KeyValue("保密", "0"))
        sexAdapter.items.add(KeyValue("男", "1"))
        sexAdapter.items.add(KeyValue("女", "2"))

        sex = User.instance.sex
        sexText.set(sexAdapter.items[sex].key)
        city = User.instance.city
        job.set(User.instance.profession)
    }


    fun upload() {

        ProgressDialog.showLoadingView(context)
        var observable = ApiManager.getInstance()
                .api.uploadUserDetail(ApiManager.toRequestBody(nickName.get()),
                ApiManager.toRequestBody(sex.toString()),
                ApiManager.toRequestBody(birthDay.get()),
                ApiManager.toRequestBody(city),
                ApiManager.toRequestBody(job.get()),
                getAvatarPat(avatar.get()))
        var observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                listener?.onUploadAvatarSuccess()
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "上传失败，请重试")
                listener?.onUploadAvatarError()
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun getAvatarPat(path: String? = null): List<MultipartBody.Part>? {
        if (path == null || avatar.get() == User.instance.avatar) {
            return null
        }
        val image = File(path)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val imageBody = RequestBody.create(MediaType.parse("image/jpeg"), image)
        builder.addFormDataPart("avatar", image.name, imageBody)
        return builder.build()
                .parts()
    }
}