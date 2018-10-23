package shy.car.sdk.travel.order.presenter

import android.content.Context
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.base.base.ProgressDialog
import com.base.network.retrofit.UploadFileRequestBody
import com.base.util.Log
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.DeliveryOrderDetail
import java.io.File
import kotlin.math.log

class OrderSendedPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun upLoadSuccess(t: JsonObject)
    }

    var detail: DeliveryOrderDetail? = null

    val content = ObservableField<String>()
    val photo1 = ObservableField<String>()
    val photo2 = ObservableField<String>()

    val photo1Progress = ObservableInt(0)
    val photo2Progress = ObservableInt(0)
    /**
     * 上传目的地照片
     */
    fun upload() {
        if (checkSelect()) {
            ProgressDialog.showLoadingView(context)

            Observable.create<Long> {
                while (photo1Progress.get() < 100 || photo2Progress.get() < 100) {
                    Thread.sleep(150)
                }
                it.onNext(0)
            }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        uploadAndSended()
                    }, {})


        }
    }

    private fun uploadAndSended() {
        val observableUpload = ApiManager.getInstance()
                .api.orderSendedPhoto(
                detail?.freightId,
                ApiManager.toRequestBody("4"),
                getPhotoParamsRequestBody())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

        val observableSended = ApiManager.getInstance()
                .api.orderSended(
                detail?.freightId!!,
                lat = app.location.lat,
                lng = app.location.lng)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())

        observableUpload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    ProgressDialog.hideLoadingView(context)
                    ErrorManager.managerError(context, it, "提交失败，请重试")
                }
                .doOnNext {
                    Log.d("observableUpload", it.toString())
                }
                .subscribeOn(Schedulers.io())
                .flatMap {
                    observableSended
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    ProgressDialog.hideLoadingView(context)
                    callBack.upLoadSuccess(it)
                }, {
                    ProgressDialog.hideLoadingView(context)
                    ErrorManager.managerError(context, it, "提交失败，请重试")
                })


//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: JsonObject) {
//                sendedCommit(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context, e, "提交失败")
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
    }


//    fun sendedCommit(t: JsonObject) {
//        val observable = ApiManager.getInstance()
//                .api.orderSended(
//                detail?.freightId!!,
//                lat = app.location.lat,
//                lng = app.location.lng)
//
//        val observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: JsonObject) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.upLoadSuccess(t)
////                sendedCommit(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context, e, "提交失败")
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }

    private fun checkSelect(): Boolean {
        if (StringUtils.isEmpty(photo1.get())) {
            ToastManager.showShortToast(context, "请拍摄目的地照片")
            return false
        }
        if (StringUtils.isEmpty(photo2.get())) {
            ToastManager.showShortToast(context, "请拍摄货物照片")
            return false
        }
        return true
    }


    fun uploadPhoto1() {
        val body = getImageParts(photo1.get()!!, photo1Progress)
        uploadImage(body, "photo1", photo1Progress)
    }

    fun uploadPhoto2() {
        val body = getImageParts(photo2.get()!!, photo2Progress)
        uploadImage(body, "photo2", photo2Progress)
    }

    private fun uploadImage(body: MultipartBody, key: String, progress: ObservableInt) {
        progress.set(0)
        val observable = ApiManager.getInstance()
                //货运送达拍照
                .api.uploadPhoto(convertToRequestBody("4"), convertToRequestBody("1"), body.parts())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: JsonObject) {
                photoList[key] = t.get("id")
                        .asString
                progress.set(100)
            }

            override fun onError(e: Throwable) {
                photoList.remove(key)
                progress.set(-1)
            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun getImageParts(photoPath: String, progressObservable: ObservableInt): MultipartBody {

        val file = File(photoPath)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val left = UploadFileRequestBody(file, object : UploadFileRequestBody.ProgressListener {
            override fun onProgress(hasWrittenLen: Long, totalLen: Long, hasFinish: Boolean) {
                val progress = (hasWrittenLen.toDouble() / totalLen.toDouble() * 99.0).toInt()
                Log.d("onProgress-----------------", "progress============$progress${System.currentTimeMillis()}")
                progressObservable.set(progress)
            }

        })
        builder.addFormDataPart("photo", file.name, left)
        return builder.build()
    }

    private val photoList = HashMap<String, String>()

    private fun getPhotoParamsRequestBody(): RequestBody {
        val list = ArrayList<String>()
        photoList.map {
            list.add(it.value)
        }
        return convertToRequestBody(Gson().toJson(list))
    }

    private fun convertToRequestBody(param: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), param)
    }

}