package shy.car.sdk.travel.send.presenter

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingPagerAdapter
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.data.CarUserTime

/**
 * 发货-整车发货 填写
 */
class SendCitySmallPackagePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    var startLocation = ObservableField<CurrentLocation>()
    var endLocation = ObservableField<CurrentLocation>()
    var goodsType = ObservableField<GoodsType>()
    var weight = ObservableField<String>()
    var price = ObservableField<String>()
    var remark = ObservableField<String>()
    var agree = ObservableBoolean(true)
    var startTime: String = ""
    var endTime: String = ""

    interface CallBack {
        fun onSubmitSuccess()
        fun onSubmitError()
    }


    var adapter = DataBindingPagerAdapter<CarInfo>(context, R.layout.item_send_hole_car_select, BR.car, null)

    fun getData() {
//        ProgressDialog.showLoadingView(context)
//
//
//        val observable2 = ApiManager.getInstance()
//                .api.getCarUseTime()
//        val observer = object : Observer<List<CarUserTime>> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(t: List<CarUserTime>) {
//                ProgressDialog.hideLoadingView(context)
//                callBack.getCarUseTimeSuccess(t)
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                e.printStackTrace()
//            }
//
//        }
//        ApiManager.getInstance()
//                .toSubscribe(observable2, observer)
    }

    fun submit() {
        if (checkInput()) {
            ProgressDialog.showLoadingView(context)
            disposable?.dispose()
            val observable = ApiManager.getInstance()
                    .api.postDeliveryOrder(app.location.cityCode,
                    null,
                    startTime,
                    endTime,
                    startLocation.get()?.address!!,
                    startLocation.get()?.lng.toString(),
                    startLocation.get()?.lat.toString(),
                    endLocation.get()?.address!!,
                    endLocation.get()?.lng.toString(),
                    endLocation.get()?.lat.toString(),
                    "1",//type=1同城拼车
                    goodsType.get()?.goodsType.toString(),
                    goodsType.get()?.goodsTypeName!!,
                    price.get()!!,
                    weight = weight.get()!!,
                    remark = remark.get()
            )
            val observer = object : Observer<JsonObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: JsonObject) {
                    ProgressDialog.hideLoadingView(context)
                    callBack.onSubmitSuccess()
                }

                override fun onError(e: Throwable) {
                    ProgressDialog.hideLoadingView(context)
                    ErrorManager.managerError(context, e, "提交失败")
                    callBack.onSubmitError()
                }

            }
            ApiManager.getInstance()
                    .toSubscribe(observable, observer)
        }
    }

    private fun checkInput(): Boolean {
        if (startTime.isEmpty()) {
            ToastManager.showLongToast(context, "请选择用车时间")
            return false
        }
        if (startLocation.get() == null) {
            ToastManager.showLongToast(context, "请选择出发地点")
            return false
        }
        if (endLocation.get() == null) {
            ToastManager.showLongToast(context, "请选择到达地点")
            return false
        }
        if (goodsType.get() == null) {
            ToastManager.showLongToast(context, "请选择货物类型")
            return false
        }
        if (weight.get() == null) {
            ToastManager.showLongToast(context, "请输入货物重量")
            return false
        }
        if (price.get() == null) {
            ToastManager.showLongToast(context, "请输入金额")
            return false
        }
        if (!agree.get()) {
            ToastManager.showLongToast(context, "请仔细阅读契约条款")
            return false
        }
        return true
    }


}