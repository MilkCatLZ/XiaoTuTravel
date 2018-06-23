package shy.car.sdk.travel.bank.data


import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

import com.google.gson.annotations.SerializedName

import shy.car.sdk.BR


class BankCard : BaseObservable(), Observable {


    /**
     * id : 1
     * name : 中国银行
     * logo : 银行logo地址
     * description : 尾号8888储蓄卡
     * default : 1
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
    @SerializedName("logo")
    @get:Bindable
    var logo: String? = null
        set(logo) {
            field = logo
            notifyChange(BR.logo)
        }
    @SerializedName("desc")
    @get:Bindable
    var description: String? = null
        set(description) {
            field = description
            notifyChange(BR.description)
        }
    @SerializedName("default")
    @get:Bindable
    var defaultX: Int = 0
        set(defaultX) {
            field = defaultX
            notifyChange(BR.defaultX)
        }
    @SerializedName("type")
    @get:Bindable
    var type: String=""
        set(value) {
            field = value
            notifyChange(BR.type)
        }
    @SerializedName("card")
    @get:Bindable
    var card: String=""
        set(value) {
            field = value
            notifyChange(BR.card)
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
