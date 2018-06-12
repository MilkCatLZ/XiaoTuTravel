package shy.car.sdk.travel.user.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.util.StringUtils
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import java.io.File


/**
 * create by lz at 2018/06/01
 * 服务认证
 */
class UserVerifyPresenter(context: Context) : BasePresenter(context) {

    interface SubmitListener {
        fun onUploadSuccess()
        fun onUploadError(e: Throwable)
    }

    var listener: SubmitListener? = null

    fun submit() {

        if (checkInput()) {
            ProgressDialog.showLoadingView(context)

            val partText = createParams()
            val partImages = createImageParams()

            ApiManager.getInstance()
                    .toSubscribe(ApiManager.getInstance()
                            .api.uploadUserVerify(partText,partImages.parts(),partImages.boundary()), object : Observer<JsonObject> {
//                            .api.uploadUserVerify(partText, partImages), object : Observer<JsonObject> {
                        //                        .api.uploadUserVerify(name.get()!!, idNumber.get()!!, createImagePart(frontImagePath.get()!!), createImagePart(backImagePath.get()!!), createImagePart(driveImagePath.get()!!)), object : Observer<JsonObject> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(t: JsonObject) {
                            ProgressDialog.hideLoadingView(context)
                            listener?.onUploadSuccess()
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            ProgressDialog.hideLoadingView(context)
                            ErrorManager.managerError(context, e, "提交失败，请重试")
                            listener?.onUploadError(e)
                        }

                    })
        }
    }

    private fun createParams(): Map<String, RequestBody> {
        val params = HashMap<String, RequestBody>()
        params[ParamsConstant.Name] = convertToRequestBody(name.get()!!)
        params[ParamsConstant.IDcard] = convertToRequestBody(idNumber.get()!!)

        return params
    }
//    private fun createParams(): Map<String, String> {
//        val params = HashMap<String, String>()
//        params[ParamsConstant.Name] = name.get()!!
//        params[ParamsConstant.IDcard] = idNumber.get()!!
//
//        return params
//    }


    private fun createImageParams(): MultipartBody {
        val driveFile = File(driveImagePath.get())
        val idCardFile = File(backImagePath.get())
        val holdIDFile = File(frontImagePath.get())

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val drive = RequestBody.create(MediaType.parse("image/*"), driveFile)
        val idCard = RequestBody.create(MediaType.parse("image/*"), idCardFile)
        val holdIDCard = RequestBody.create(MediaType.parse("image/*"), holdIDFile)
//        val namebody = RequestBody.create(MediaType.parse("text/plain"), name.get())
//        val idNum = RequestBody.create(MediaType.parse("text/plain"), idNumber.get())


        builder.addFormDataPart("driving_licence_photo", driveFile.name, drive)
        builder.addFormDataPart("idcard_img_photo", idCardFile.name, idCard)
        builder.addFormDataPart("hold_idcard_photo", holdIDFile.name, holdIDCard)
//        builder.addFormDataPart(ParamsConstant.Name, name.get())
//        builder.addFormDataPart(ParamsConstant.IDcard, idNumber.get())

        return builder.build()

    }

//    private fun createImagePart(path: String): MultipartBody.Part {
//        val driveFile = File(driveImagePath.get())
//
//        return MultipartBody.Part.createFormData("driving_licence_photo", driveFile.name, RequestBody.create(MediaType.parse("image/*"), driveFile))
//
//
//    }

    private fun convertToRequestBody(param: String?): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), param)
    }


    private fun checkInput(): Boolean {


        when {
            StringUtils.isEmpty(name.get()) -> {
                ToastManager.showShortToast(context, "请输入姓名")
                return false
            }
            StringUtils.isEmpty(idNumber.get()) -> {
                ToastManager.showShortToast(context, "请输入身份证号")
                return false
            }
            idNumber.get()?.length!! < 15 -> {
                ToastManager.showShortToast(context, "请输入正确的身份证号")
                return false
            }
            StringUtils.isEmpty(frontImagePath.get()) -> {
                ToastManager.showShortToast(context, "请选择手持身份证照片")
                return false
            }
            StringUtils.isEmpty(backImagePath.get()) -> {
                ToastManager.showShortToast(context, "请选择身份证正面照片")
                return false
            }
            StringUtils.isEmpty(driveImagePath.get()) -> {
                ToastManager.showShortToast(context, "请选择驾驶证照片")
                return false
            }
            else -> return true
        }


    }

    var name = ObservableField<String>("过期猫粮")
    var idNumber = ObservableField<String>("450106198809201012")

    var frontImagePath = ObservableField<String>("/storage/emulated/0/Tencent/QQ_Images/-3e85ffab227c6946.png")
    var backImagePath = ObservableField<String>("/storage/emulated/0/Tencent/QQ_Images/-3e85ffab227c6946.png")
    var driveImagePath = ObservableField<String>("/storage/emulated/0/Tencent/QQ_Images/-3e85ffab227c6946.png")
    
}