package shy.car.sdk.travel.rent.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import shy.car.sdk.BR

class CarSelectInfo : BaseObservable() {

    @get:Bindable
    var moneyVerify: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.moneyVerify)
        }
    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
}