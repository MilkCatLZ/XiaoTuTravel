package shy.car.sdk.travel.bank.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

import shy.car.sdk.BR

class BankType : BaseObservable(),
        Observable {


    /**
     * freightId : 2
     * name : 中国工商银行
     * logo : 银行logo地址
     */

    @get:Bindable
    var id: String? = null
        set(id) {
            field = id
            notifyChange(BR.id)
        }
    @get:Bindable
    var name: String? = null
        set(name) {
            field = name
            notifyChange(BR.name)
        }
    @get:Bindable
    var logo: String? = null
        set(logo) {
            field = logo
            notifyChange(BR.logo)
        }
    @get:Bindable
    var icon: String? = null
        set(icon) {
            field = icon
            notifyChange(BR.icon)
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
