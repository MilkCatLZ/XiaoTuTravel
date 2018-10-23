package shy.car.sdk.travel.pay.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class CarSelectInfo : BaseObservable() {

    @SerializedName("deposit")
    @get:Bindable
    var promiseMoneyPrice: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.promiseMoneyPrice)
        }
    @SerializedName("id")
    @get:Bindable
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @SerializedName("name")
    @get:Bindable
    var carName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.carName)
        }
}