package shy.car.sdk.travel.invoice.data


import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

import com.google.gson.annotations.SerializedName

import shy.car.sdk.BR


class InvoiceDetail : BaseObservable(), Observable {


    /**
     * id : 1006
     * type : 1
     * type_type : 申请中
     * title : 广西科技有限公司
     * content : 租车费
     * money : 100
     * status : 1
     * status_text : 申请中
     * created_at : 2018.07.06 14:55:41
     * number : 1
     * contact : {"name":"张三","phone":"15296476403","address":"广西科技有限公司","zip_code":"530000"}
     * order : {"number":3,"period_time":"2018-05-11 18:15-2018-06-29 11:00"}
     */

    @SerializedName("id")
    @get:Bindable
    var id: String? = null
        set(id) {
            field = id
            notifyChange(BR.id)
        }
    @SerializedName("type")
    @get:Bindable
    var type: Int = 0
        set(type) {
            field = type
            notifyChange(BR.type)
        }
    @SerializedName("type_type")
    @get:Bindable
    var typeType: String? = null
        set(typeType) {
            field = typeType
            notifyChange(BR.typeType)
        }
    @SerializedName("title")
    @get:Bindable
    var title: String? = null
        set(title) {
            field = title
            notifyChange(BR.title)
        }
    @SerializedName("content")
    @get:Bindable
    var content: String? = null
        set(content) {
            field = content
            notifyChange(BR.content)
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
    @SerializedName("number")
    @get:Bindable
    var number: String? = null
        set(number) {
            field = number
            notifyChange(BR.number)
        }
    @SerializedName("contact")
    @get:Bindable
    var contact: ContactBean? = null
        set(contact) {
            field = contact
            notifyChange(BR.contact)
        }
    @SerializedName("order")
    @get:Bindable
    var order: OrderBean? = null
        set(order) {
            field = order
            notifyChange(BR.order)
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

    class ContactBean : Observable {
        /**
         * name : 张三
         * phone : 15296476403
         * address : 广西科技有限公司
         * zip_code : 530000
         */

        @SerializedName("name")
        @get:Bindable
        var name: String? = null
            set(name) {
                field = name
                notifyChange(BR.name)
            }
        @SerializedName("phone")
        @get:Bindable
        var phone: String? = null
            set(phone) {
                field = phone
                notifyChange(BR.phone)
            }
        @SerializedName("address")
        @get:Bindable
        var address: String? = null
            set(address) {
                field = address
                notifyChange(BR.address)
            }
        @SerializedName("zip_code")
        @get:Bindable
        var zipCode: String? = null
            set(zipCode) {
                field = zipCode
                notifyChange(BR.zipCode)
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


    class OrderBean : Observable {
        /**
         * number : 3
         * period_time : 2018-05-11 18:15-2018-06-29 11:00
         */

        @SerializedName("number")
        @get:Bindable
        var number: String? = null
            set(number) {
                field = number
                notifyChange(BR.number)
            }
        @SerializedName("period_time")
        @get:Bindable
        var periodTime: String? = null
            set(periodTime) {
                field = periodTime
                notifyChange(BR.periodTime)
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
}
