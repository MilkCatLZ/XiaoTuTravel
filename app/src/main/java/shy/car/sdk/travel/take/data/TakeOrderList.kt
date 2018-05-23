package shy.car.sdk.travel.take.data

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.fastjson.annotation.JSONField
import com.android.databinding.library.baseAdapters.BR
import shy.car.sdk.app.route.ObjectSerialisation
import java.io.Serializable
import java.lang.reflect.Type

open class TakeOrderList : BaseObservable() {
    @get:Bindable
    @JSONField(name = "id")
    var id: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
}