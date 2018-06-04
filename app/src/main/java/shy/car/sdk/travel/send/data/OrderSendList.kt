package shy.car.sdk.travel.send.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import shy.car.sdk.BR

open class OrderSendList : BaseObservable() {

    @get:Bindable
    var id = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    var status = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }
}