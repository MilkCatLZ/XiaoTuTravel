package shy.car.sdk.travel.common.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class GoodsType : BaseObservable() {

    @SerializedName("name")
    @get:Bindable
    var goodsTypeName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.goodsTypeName)
        }

    @SerializedName("id")
    var goodsType: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.goodsType)
        }
}