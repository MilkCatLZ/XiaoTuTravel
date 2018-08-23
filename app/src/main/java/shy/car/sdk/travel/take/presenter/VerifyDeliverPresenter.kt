package shy.car.sdk.travel.take.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import java.io.File

class VerifyDeliverPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun upLoadSuccess(t: JsonObject)
    }
    val content = ObservableField<String>()
    val photo = ObservableField<String>()

    /**
     * 上传目的地照片
     */
    fun upload() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.uploadVerifyDiliver(
                createImageParams().parts())

        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callBack.upLoadSuccess(t)

            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "提交失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun createImageParams(): MultipartBody {

        val leftFile = File(photo.get())

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val drive = RequestBody.create(MediaType.parse("image/jpeg"), leftFile)

        builder.addFormDataPart("freight_img", leftFile.name, drive)
        return builder.build()

    }
}