package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.databinding.DataBindingPagerAdapter
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.CarInfo
/**
 * create by LZ at 2018/05/11
 */
class CarRentPresenter(context: Context, callBack: CallBack) : BasePresenter(context) {
    var carListAdapter: DataBindingPagerAdapter<CarInfo> = DataBindingPagerAdapter(context, R.layout.item_home_car, BR.car, null)

    init {
        for(i in 1..4){
            carListAdapter.addItem(CarInfo())
        }
    }

    interface CallBack {

    }



}