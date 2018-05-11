package shy.car.sdk.travel.rent.presenter

import android.content.Context
import com.base.databinding.DataBindingPagerAdapter
import shy.car.sdk.R
import shy.car.sdk.BR
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.rent.data.CarInfo

class CarRentPresenter(context: Context, callBack: CallBack) : BasePresenter(context) {
    lateinit var carListAdapter: DataBindingPagerAdapter<CarInfo>

    init {
        carListAdapter = DataBindingPagerAdapter(context, R.layout.item_home_car, BR.car, null)
    }

    interface CallBack {

    }


}