package shy.car.sdk.travel.rent.presenter

import android.content.Context
import androidx.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.order.data.RentOrderDetail
import java.io.File

class RentOrderCommentPresenter(context: Context, var callback: CommonCallBack<JsonObject>) : BasePresenter(context) {

    var detail: RentOrderDetail? = null

    val photo = ObservableField<String>("")
    val content = ObservableField<String>("")
    var level = 0

    fun uploadComment() {
        if (!checkInpuut()) {
            return
        }
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.uploadComment(
                ApiManager.toRequestBody(detail?.orderId),
                ApiManager.toRequestBody(level.toString()),
                ApiManager.toRequestBody(content.get()),
                createImageParams().parts())

        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                ProgressDialog.hideLoadingView(context)
                callback.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                ErrorManager.managerError(context, e, "提交失败")
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun checkInpuut():Boolean {
        if(level==0){
            ToastManager.showShortToast(context, "请给本次服务评个分~")
            return false
        }

        if(content.get().isNullOrBlank()){
            ToastManager.showShortToast(context, "请对本次服务做一个评价~")
            return false
        }

        return true

    }

    private fun createImageParams(): MultipartBody {
        val leftFile = File(photo.get())

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val drive = RequestBody.create(MediaType.parse("image/jpeg"), leftFile)

        builder.addFormDataPart("image", leftFile.name, drive)
        return builder.build()

    }
}