package shy.car.sdk.travel.invoice.data


import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

import com.google.gson.annotations.SerializedName

import shy.car.sdk.BR


class InvoiceHistory : BaseObservable(), Observable {

    /**
     * id : 1003
     * title : 广西科技有限公司
     * money : 1
     * status : 1
     * status_text : 申请中
     * created_at : 2018.07.04 11:29:56
     */

    @SerializedName("id")
    @get:Bindable
    var id: Int = 0
        set(id) {
            field = id
            notifyChange(BR.id)
        }
    @SerializedName("title")
    @get:Bindable
    var title: String? = null
        set(title) {
            field = title
            notifyChange(BR.title)
        }
    @SerializedName("money")
    @get:Bindable
    var money: Double? = null
        set(money) {
            field = money
            notifyChange(BR.money)
        }
    @SerializedName("status")
    @get:Bindable
    var status: Int = 0
        set(status) {
            field = status
            notifyChange(BR.status)
        }
    @SerializedName("status_text")
    @get:Bindable
    var statusText: String? = null
        set(statusText) {
            field = statusText
            notifyChange(BR.statusText)
        }
    @SerializedName("created_at")
    @get:Bindable
    var createdAt: String? = null
        set(createdAt) {
            field = createdAt
            notifyChange(BR.createdAt)
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
