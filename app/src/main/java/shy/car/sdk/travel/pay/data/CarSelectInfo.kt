package shy.car.sdk.travel.pay.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import shy.car.sdk.BR

class CarSelectInfo : BaseObservable() {

    @get:Bindable
    var promiseMoneyPrice: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.promiseMoneyPrice)
        }
    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var carName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.carName)
        }
}