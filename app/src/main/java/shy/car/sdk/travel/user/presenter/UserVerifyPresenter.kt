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
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.user.data.RefreshUserInfo
import java.io.File


/**
 * create by lz at 2018/06/01
 * 服务认证
 */
class UserVerifyPresenter(context: Context) : BasePresenter(context) {

    interface SubmitListener {
        fun onUploadSuccess()
        fun onUploadError(e: Throwable)
        fun alreadyUpLoad(e: Throwable)
    }

    var listener: SubmitListener? = null

    fun submit() {

        if (checkInput()) {
            ProgressDialog.showLoadingView(context)




            val partImages = createImageParams()
            ApiManager.getInstance()
                    .toSubscribe(ApiManager.getInstance()
                            .api.uploadUserVerify(convertToRequestBody(name.get()), convertToRequestBody(idNumber.get()), partImages.parts()),
                            object : Observer<JsonObject> {
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
                                    var err=ErrorManager.managerError(context, e, null)
                                    if(err?.error_code==400302){
                                        listener?.alreadyUpLoad(e)
                                    }else {
                                        err?.showError(context,"提交失败，请重试")
                                        listener?.onUploadError(e)
                                    }
                                }

                            })
        }
    }

    private fun createImageParams(): MultipartBody {
        val driveFile = File(driveImagePath.get())
        val idCardFile = File(backImagePath.get())
        val holdIDFile = File(frontImagePath.get())

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        val drive = RequestBody.create(MediaType.parse("image/*"), driveFile)
        val idCard = RequestBody.create(MediaType.parse("image/*"), idCardFile)
        val holdIDCard = RequestBody.create(MediaType.parse("image/*"), holdIDFile)

        builder.addFormDataPart("driving_licence_photo", driveFile.name, drive)
        builder.addFormDataPart("idcard_img_photo", idCardFile.name, idCard)
        builder.addFormDataPart("hold_idcard_photo", holdIDFile.name, holdIDCard)
        return builder.build()

    }

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

    var name = ObservableField<String>("")
    var idNumber = ObservableField<String>("")

    var frontImagePath = ObservableField<String>("")
    var backImagePath = ObservableField<String>("")
    var driveImagePath = ObservableField<String>("")

}