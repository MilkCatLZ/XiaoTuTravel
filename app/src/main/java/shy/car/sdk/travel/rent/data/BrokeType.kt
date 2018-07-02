package shy.car.sdk.travel.rent.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import shy.car.sdk.BR

class BrokeType : BaseObservable() {
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