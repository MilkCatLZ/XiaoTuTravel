package shy.car.sdk.travel.send.presenter

import android.content.Context
import android.databinding.ObservableField
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.location.data.CurrentLocation

/**
 * 发货-整车发货 填写
 */
class SendCitySmallPackagePresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {
    var startLocation = ObservableField<CurrentLocation>()
    var endLocation = ObservableField<CurrentLocation>()
    var goodsType = ObservableField<GoodsType>()

    interface CallBack {

    }

    init {

    }

}