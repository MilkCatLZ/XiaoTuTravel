package shy.car.sdk.travel.send.presenter

import android.content.Context
import shy.car.sdk.app.presenter.BasePresenter

class MoneyChangePresenter(context: Context,var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {
        fun onChangeSuccess()
    }

    var detail: OrderSendDetail = OrderSendDetail()

    fun submitData(){
        callBack.onChangeSuccess()
    }

}