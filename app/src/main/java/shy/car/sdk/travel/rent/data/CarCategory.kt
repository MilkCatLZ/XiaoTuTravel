package shy.car.sdk.travel.rent.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import shy.car.sdk.BR

/**
 * create by LZ at 2018/05/21
 *租车-  车辆选择列表
 */
class CarCategory : BaseObservable() {

    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.carName)
        }
}