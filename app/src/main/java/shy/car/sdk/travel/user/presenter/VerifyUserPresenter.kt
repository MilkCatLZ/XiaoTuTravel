package shy.car.sdk.travel.user.presenter

import android.content.Context
import android.databinding.ObservableField
import com.base.util.StringUtils
import com.base.util.ToastManager
import shy.car.sdk.app.presenter.BasePresenter


/**
 * create by lz at 2018/06/01
 * 服务认证
 */
class VerifyUserPresenter(context: Context) : BasePresenter(context) {
    fun submit() {

        if (checkInput()) {

        }
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
                ToastManager.showShortToast(context, "请选择身份证正面照片")
                return false
            }
            StringUtils.isEmpty(backImagePath.get()) -> {
                ToastManager.showShortToast(context, "请选择身份证反面照片")
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