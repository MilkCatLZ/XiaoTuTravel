package shy.car.sdk.travel.send.presenter

import android.content.Context
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.order.data.DeliveryOrderDetail

class MoneyChangePresenter(context: Context,var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onChangeSuccess()
    }

    var detail: DeliveryOrderDetail = DeliveryOrderDetail()

    fun submitData(){
        callBack.onChangeSuccess()
    }

}