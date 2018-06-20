package shy.car.sdk.travel.send.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

import com.google.gson.annotations.SerializedName

import shy.car.sdk.BR

class CarUserTime : BaseObservable(),
        Observable {


    /**
     * start : 0
     * end : 0
     */

    @SerializedName("start")
    @get:Bindable
    var start: String? = null
        set(start) {
            field = start
            notifyChange(BR.start)
        }
    @SerializedName("end")
    @get:Bindable
    var end: String? = null
        set(end) {
            field = end
            notifyChange(BR.end)
        }
    @Transient
    private var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

    @Synchronized
    private fun notifyChange(propertyId: Int) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.notifyChange(this, propertyId)
    }

    @Synchronized
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = PropertyChangeRegistry()
        }
        propertyChangeRegistry!!.add(callback)

    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry!!.remove(callback)
        }
    }
}
