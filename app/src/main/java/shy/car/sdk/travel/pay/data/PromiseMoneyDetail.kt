package shy.car.sdk.travel.pay.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class PromiseMoneyDetail : BaseObservable() {
    /**
     * txt : 缴纳保证金
     * amount : 999
     * date : 2018-05-01 15:00
     */

    @SerializedName("txt")
    @get:Bindable
    var txt: String? = null
        set(txt) {
            field = txt
            notifyChange(BR.txt)
        }
    @SerializedName("amount")
    @get:Bindable
    var amount: String = ""
        set(amount) {
            field = amount
            notifyChange(BR.amount)
        }
    @SerializedName("date")
    @get:Bindable
    var date: String? = null
        set(date) {
            field = date
            notifyChange(BR.date)
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