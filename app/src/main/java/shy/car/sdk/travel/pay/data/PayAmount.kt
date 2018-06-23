package shy.car.sdk.travel.pay.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

/**
 * 充值
 */
class PayAmount : BaseObservable() {

    @SerializedName("amount")
    @get:Bindable
    var price: Double =0.0
    set(value) {
        field=value
        notifyPropertyChanged(BR.price)
    }
    @SerializedName("price")
    @get:Bindable
    var realPrice: Double = 0.0
        set(value) {
            field=value
            notifyPropertyChanged(BR.realPrice)
        }
}