package shy.car.sdk.travel.wallet.presenter

import android.content.Context
import com.base.base.ProgressDialog
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.pay.data.PayMethod

/**
 * create by LZ at 2018/06/16
 *
 */
class WalletPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {

    }

    fun changePayMethod(payMethod: PayMethod) {
        ProgressDialog.showLoadingView(context)
        disposable?.dispose()


    }

}