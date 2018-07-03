package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingItemClickAdapter
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
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.data.BrokeType
import java.io.File

class CarBrokeUploadPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun getTypeSuccess(t: List<BrokeType>)
        fun upLoadSuccess(t: JsonObject)

    }

    val adapter = DataBindingItemClickAdapter<BrokeType>(R.layout.item_broke_type, BR.type, BR.click, {
        val type = it.tag as BrokeType
        checkID.set(type.id)
    }, { holder, position ->
        holder.binding.setVariable(BR.presenter, this@CarBrokeUploadPresenter)
    })

    var detail: RentOrderDetail? = null
    val checkID = ObservableField<String>()
    val content = ObservableField<String>()
    val photo = ObservableField<String>()

    fun getBrokeType() {
        val observable = ApiManager.getInstance()
                .api.getBrokeType()
        val observer = object : Observer<List<BrokeType>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<BrokeType>) {
                ProgressDialog.hideLoadingView(context)
                adapter.setItems(t, 1)
                callBack.getTypeSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)

            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun uploadCarBroke() {
        ProgressDialog.showLoadingView(context)
        val observable = ApiManager.getInstance()
                .api.uploadCarBroke(ApiManager.toRequestBody(detail?.car?.carID),
                ApiManager.toRequestBody(checkID.get()),
                ApiManager.toRequestBody(content.get()),
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

        builder.addFormDataPart("image", leftFile.name, drive)
        return builder.build()

    }
}