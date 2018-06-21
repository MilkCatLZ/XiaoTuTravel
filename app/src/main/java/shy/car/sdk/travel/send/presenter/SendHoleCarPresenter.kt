package shy.car.sdk.travel.send.presenter

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingPagerAdapter
import com.base.util.ToastManager
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
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
class SendHoleCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var startLocation = ObservableField<CurrentLocation>()
    var endLocation = ObservableField<CurrentLocation>()
    var goodsType = ObservableField<GoodsType>()
    var weight = ObservableField<String>()
    var price = ObservableField<String>()
    var remark = ObservableField<String>()
    var volume = ObservableField<String>()
    var agree = ObservableBoolean(false)
    var startTime: String = ""
    var endTime: String = ""
    var carID: String = ""

    interface CallBack {
        fun getCarListSuccess(list: ArrayList<CarInfo>)
        fun onSubmitSuccess()
        fun onSubmitError()
        fun getCarUseTimeSuccess(t2: List<CarUserTime>)
        fun getFreightTypeSuccess(t3: List<GoodsType>)
    }


    var adapter = DataBindingPagerAdapter<CarInfo>(context, R.layout.item_send_hole_car_select, BR.car, null)

    fun getData() {
        ProgressDialog.showLoadingView(context)
        val observable1 = ApiManager.getInstance()
                .api.getCarTypeList()

        val observable2 = ApiManager.getInstance()
                .api.getCarUseTime()
        val observable3 = ApiManager.getInstance()
                .api.getFreightTypeList()

        Observable.zip(observable1, observable2, observable3, Function3<List<CarInfo>, List<CarUserTime>,List<GoodsType>, String> { t1, t2,t3 ->
            Observable.just(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        adapter.setItems(t1, 1)
                        carID = t1[0].id.toString()
                        callBack.getCarUseTimeSuccess(t2)
                        callBack.getFreightTypeSuccess(t3)
                    }, {

                    })
            ""
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    ProgressDialog.hideLoadingView(context)
                }, {
                    ProgressDialog.hideLoadingView(context)
                    it.printStackTrace()
                })
    }

    fun submit() {
        if (checkInput()) {
            ProgressDialog.showLoadingView(context)
            disposable?.dispose()
            val observable = ApiManager.getInstance()
                    .api.postDeliveryOrder(app.location.cityCode,
                    carID,
                    startTime,
                    endTime,
                    startLocation.get()?.address!!,
                    startLocation.get()?.lng.toString(),
                    startLocation.get()?.lat.toString(),
                    endLocation.get()?.address!!,
                    endLocation.get()?.lng.toString(),
                    endLocation.get()?.lat.toString(),
                    "2",
                    goodsType.get()?.goodsType.toString(),
                    goodsType.get()?.goodsTypeName!!,
                    price.get()!!,
                    weight.get()!!,
                    volume.get()!!,
                    remark.get())
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
        if (volume.get() == null) {
            ToastManager.showLongToast(context, "请输入货物体积")
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