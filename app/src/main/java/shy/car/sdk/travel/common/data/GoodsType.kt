package shy.car.sdk.travel.common.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import shy.car.sdk.BR

class GoodsType : BaseObservable() {

    @get:Bindable
    var goodsTypeName: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.goodsTypeName)
        }

    var goodsType: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.goodsType)
        }
}