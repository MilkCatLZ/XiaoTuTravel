package shy.car.sdk.travel.take.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName

open class TakeOrderList : BaseObservable() {
    @get:Bindable
    @SerializedName("id")
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
    @get:Bindable
    @SerializedName("status")
    var status: Int = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.status)
        }
}