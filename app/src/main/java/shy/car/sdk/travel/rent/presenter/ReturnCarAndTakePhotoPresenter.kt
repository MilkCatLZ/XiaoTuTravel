package shy.car.sdk.travel.rent.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.StringUtils
import com.base.util.ToastManager
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

    var detail: RentOrderDetail? = null
    var netWorkID: String = ""

    fun getOrderDetail(orderID: String) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()
        val observable = ApiManager.getInstance()
                .api.getRentOrderDetail(orderID)
        val observer = object : Observer<RentOrderDetail> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: RentOrderDetail) {
                ProgressDialog.hideLoadingView(context)
                detail = t
                callBack.onGetDetailSuccess(t)
            }

            override fun onError(e: Throwable) {
                ProgressDialog.hideLoadingView(context)
                callBack.onGetDetailError(e)
            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }

    fun uploadPicAndUnlockCar() {
        if (checkSelect()) {

            ProgressDialog.showLoadingView(context)

            //传手机的经纬度 辅助定位
            val observableUnLock = ApiManager.getInstance()
                    //固定传3
                    .api.returnCar(detail?.orderId!!, netWorkID, app.location.lat.toString(), app.location.lng.toString())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())

            val observableUpload = ApiManager.getInstance()
                    .api.uploadCarPic(detail?.orderId!!, detail?.orderId!!, "2", createImageParams().parts())//type=2 还车拍照
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())

            observableUnLock.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe({
                        disposable = it
                    })
                    .doOnError({
                        ProgressDialog.hideLoadingView(context)
                        disposable?.dispose()
                    })
                    .subscribeOn(Schedulers.io())
                    .flatMap({
                        observableUpload
                    })
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
        return true
    }

    private fun createImageParams(): MultipartBody {
        val leftFile = File(leftImage.get())
        val rightFile = File(rightImage.get())

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val drive = RequestBody.create(MediaType.parse("image/jpeg"), leftFile)
        val idCard = RequestBody.create(MediaType.parse("image/jpeg"), rightFile)

        builder.addFormDataPart("photo1", leftFile.name, drive)
        builder.addFormDataPart("photo2", rightFile.name, idCard)
        return builder.build()

    }

    private fun convertToRequestBody(param: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), param)
    }

}