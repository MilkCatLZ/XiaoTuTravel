package shy.car.sdk.travel.setting.data


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import shy.car.sdk.BR


class FeedBack : BaseObservable(){
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
