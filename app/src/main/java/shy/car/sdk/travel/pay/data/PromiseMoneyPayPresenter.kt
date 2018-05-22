package shy.car.sdk.travel.pay.data

import android.content.Context
import android.databinding.ObservableInt
import shy.car.sdk.app.presenter.BasePresenter

/**
 * create by LZ at 2018/05/22
 * 支付保证金
 */
class PromiseMoneyPayPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    interface CallBack {

    }

    /**
     *
     */
    var payMethod = ObservableInt(0)
}