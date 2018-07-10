package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.base.base.ProgressDialog
import com.base.network.retrofit.UploadFileRequestBody
import com.base.util.Log
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.Gson
import com.google.gson.JsonObject
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
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.order.net.OrderManager
import java.io.File

class ReturnCarAndTakePhotoPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onGetDetailSuccess(t: RentOrderDetail)
        fun onGetDetailError(e: Throwable)
        fun onReturnSuccess()
        fun onReturnError()

    }

    val leftImage = ObservableField<String>()
    val rightImage = ObservableField<String>()
    val driveRoom = ObservableField<String>()
    val backRoom = ObservableField<String>()

    val leftProgress = ObservableInt(0)
    val rightProgress = ObservableInt(0)
    val driveProgress = ObservableInt(0)
    val backProgress = ObservableInt(0)

    var detail: RentOrderDetail? = null
    var netWorkID: String = ""

    fun getOrderDetail(orderID: String) {

        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        OrderManager.getOrderDetail(orderID, true, callBack = object : OrderManager.GetDetailCallBack {
            override fun getDetailSuccess(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                detail = t
                callBack.onGetDetailSuccess(t)
            }

            override fun getDetailError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailError(e)
            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

        })
    }

    fun uploadPicAndUnlockCar() {
        if (checkSelect()) {

            ProgressDialog.showLoadingView(context)

            //传手机的经纬度 辅助定位
            val observableUnLock = ApiManager.getInstance()
                    //固定传3
                    .api.returnCar(detail?.orderId!!,
                    netWorkID,
                    app.location.lat.toString(),
                    app.location.lng.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())

            val observableUpload = ApiManager.getInstance()
                    .api.uploadCarPic(detail?.orderId!!, detail?.orderId!!,
                    ApiManager.toRequestBody("2")!!,
                    getPhotoParamsString())//type=2 还车拍照
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())

            observableUnLock.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        disposable = it
                    }
                    .doOnError {
                        ProgressDialog.hideLoadingView(context)
                        disposable?.dispose()
                    }
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        observableUpload
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<JsonObject> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: JsonObject) {
                            ProgressDialog.hideLoadingView(context)
                            callBack.onReturnSuccess()
                        }

                        override fun onError(e: Throwable) {
                            ProgressDialog.hideLoadingView(context)
                            ErrorManager.managerError(context, e, "还车失败")
                            callBack.onReturnError()
                        }

                    })


        }

    }

    private fun checkSelect(): Boolean {
        if (StringUtils.isEmpty(leftImage.get())) {
            ToastManager.showShortToast(context, "请拍摄车辆左侧照片")
            return false
        }
        if (StringUtils.isEmpty(rightImage.get())) {
            ToastManager.showShortToast(context, "请拍摄车辆右侧照片")
            return false
        }
        if (StringUtils.isEmpty(driveRoom.get())) {
            ToastManager.showShortToast(context, "请拍摄车辆驾驶室找")
            return false
        }
        if (StringUtils.isEmpty(backRoom.get())) {
            ToastManager.showShortToast(context, "请拍摄车辆后尾箱照片")
            return false
        }
        return true
    }

    private fun getPhotoParamsString(): RequestBody {
        val list = ArrayList<String>()
        photoList.map {
            list.add(it.value)
        }
        return convertToRequestBody(Gson().toJson(list))
    }
//    private fun createImageParams(): MultipartBody {
//        val leftFile = File(leftImage.get())
//        val rightFile = File(rightImage.get())
//
//        val builder = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//
//        val drive = RequestBody.create(MediaType.parse("image/jpeg"), leftFile)
//        val idCard = RequestBody.create(MediaType.parse("image/jpeg"), rightFile)
//
//        builder.addFormDataPart("photo1", leftFile.name, drive)
//        builder.addFormDataPart("photo2", rightFile.name, idCard)
//        return builder.build()
//
//    }

    val disposableList = ArrayList<Disposable>()
    val photoList = HashMap<String, String>()

    fun uploadLeftCarImage() {
        val body = getImageParts(leftImage.get()!!, leftProgress)
        uploadImage(body, "left")
    }

    fun uploadRightCarImage() {
        val body = getImageParts(rightImage.get()!!, rightProgress)
        uploadImage(body, "right")
    }

    fun uploadDriveRoomImage() {
        val body = getImageParts(driveRoom.get()!!, driveProgress)
        uploadImage(body, "left")
    }

    fun uploadBackRoomImage() {
        val body = getImageParts(backRoom.get()!!, backProgress)
        uploadImage(body, "right")
    }

    fun uploadImage(body: MultipartBody, key: String) {
        val observable = ApiManager.getInstance()
                .api.uploadPhoto(convertToRequestBody("1"), convertToRequestBody("1"), body.parts())
        val observer = object : Observer<JsonObject> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposableList.add(d)
            }

            override fun onNext(t: JsonObject) {
                photoList[key] = t.get("id")
                        .asString
            }

            override fun onError(e: Throwable) {

            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    private fun getImageParts(photoPath: String, progressObservable: ObservableInt): MultipartBody {

        val file = File(photoPath)

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val cache = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val left = UploadFileRequestBody(cache, object : UploadFileRequestBody.ProgressListener {
            override fun onProgress(hasWrittenLen: Long, totalLen: Long, hasFinish: Boolean) {
                val progress = (hasWrittenLen.toDouble() / totalLen.toDouble() * 100.0).toInt()
                Log.d("onProgress-----------------", "progress============$progress")
                progressObservable.set(progress)
            }

        })

        builder.addFormDataPart("photo", file.name, left)
        return builder.build()
    }


    private fun convertToRequestBody(param: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), param)
    }

    override fun destroy() {
        disposableList.map {
            try {
                it.dispose()
            } catch (e: Exception) {
            }
        }
        super.destroy()
    }

}