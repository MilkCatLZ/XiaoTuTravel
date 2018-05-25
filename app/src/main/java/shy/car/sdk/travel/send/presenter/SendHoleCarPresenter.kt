package shy.car.sdk.travel.send.presenter

import android.content.Context
import android.view.View
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.DataBindingPagerAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.data.OrderSendList

/**
 * 发货-整车发货 填写
 */
class SendHoleCarPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    interface CallBack {
        fun getCarListSuccess(list: ArrayList<CarInfo>)
    }


    var adapter = DataBindingPagerAdapter<CarInfo>(context, R.layout.item_send_hole_car_select, BR.car, null)

    init {
        var list = ArrayList<CarInfo>()
        for (i in 1..8) {
            list.add(CarInfo())
        }

        adapter.setItems(list, 1)
    }

}