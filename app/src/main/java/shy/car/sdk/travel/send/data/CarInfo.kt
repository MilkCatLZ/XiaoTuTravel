package shy.car.sdk.travel.send.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class CarInfo : BaseObservable() {

    /**
     * freightId : 16
     * name : QQ
     * img : http://www.api.com/upload/car/upload/car/5b231e8ab54e8.png
     * weight : 10
     * long : 2
     * wide : 2
     * height : 2
     * volume : 10
     */

    @SerializedName("id")
    @get:Bindable
    var id: String? = null
        set(id) {
            field = id
            notifyChange(BR.id)
        }
    @SerializedName("name")
    @get:Bindable
    var name: String? = null
        set(name) {
            field = name
            notifyChange(BR.name)
        }
    @SerializedName("img")
    @get:Bindable
    var img: String? = null
        set(img) {
            field = img
            notifyChange(BR.img)
        }
    @SerializedName("weight")
    @get:Bindable
    var weight: String? = null
        set(weight) {
            field = weight
            notifyChange(BR.weight)
        }
    @SerializedName("long")
    @get:Bindable
    var longX: String? = null
        set(longX) {
            field = longX
            notifyChange(BR.longX)
        }
    @SerializedName("wide")
    @get:Bindable
    var wide: String? = null
        set(wide) {
            field = wide
            notifyChange(BR.wide)
        }
    @SerializedName("height")
    @get:Bindable
    var height: String? = null
        set(height) {
            field = height
            notifyChange(BR.height)
        }
    @SerializedName("volume")
    @get:Bindable
    var volume: Int = 0
        set(volume) {
            field = volume
            notifyChange(BR.volume)
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

    fun info(): String {
        return "载重${weight}kg / 长宽高:$longX*$wide*${height}m³ / 载货体积${volume}m³"
    }
}