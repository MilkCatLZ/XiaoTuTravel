package shy.car.sdk.travel.order.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

open class DeliveryOrderDetail : BaseObservable() {

    /**
     * freight_id : 1527039003
     * status : 0
     * status_name : 已关闭
     * use_car_at : 05月28日 今天 10:08 - 18:03
     * from_address : 广西南宁青秀区
     * to_address : 广西南宁江南区
     * freight_type : 普货:
     * weight : 5
     * volume : 8
     * freight : 0
     * remark :
     * created_at : 05-29 11:17
     * receipt_at : 05-29 14:57
     * pay_at : 06-06 10:53
     * finish_at : 05-29 17:55
     * user : {"name":"张三","phone":"171159758414","rank_text":"白银"}
     * carrier : {"name":"张伟","phone":"15296476403","rank_text":"白银"}
     * car : {"weight":5,"volume":5,"long":5,"wide":5,"height":5}
     */

    @SerializedName("freight_id")
    @get:Bindable
    var freightId: String? = null
        set(freightId) {
            field = freightId
            notifyChange(BR.freightId)
        }
    @SerializedName("status")
    @get:Bindable
    var status: Int = 0
        set(status) {
            field = status
            notifyChange(BR.status)
        }
    @SerializedName("status_name")
    @get:Bindable
    var statusName: String? = null
        set(statusName) {
            field = statusName
            notifyChange(BR.statusName)
        }
    @SerializedName("use_car_at")
    @get:Bindable
    var useCarAt: String? = null
        set(useCarAt) {
            field = useCarAt
            notifyChange(BR.useCarAt)
        }
    @SerializedName("from_address")
    @get:Bindable
    var fromAddress: String? = null
        set(fromAddress) {
            field = fromAddress
            notifyChange(BR.fromAddress)
        }
    @SerializedName("to_address")
    @get:Bindable
    var toAddress: String? = null
        set(toAddress) {
            field = toAddress
            notifyChange(BR.toAddress)
        }
    @SerializedName("freight_type")
    @get:Bindable
    var freightType: String? = null
        set(freightType) {
            field = freightType
            notifyChange(BR.freightType)
        }
    @SerializedName("weight")
    @get:Bindable
    var weight: String? = null
        set(weight) {
            field = weight
            notifyChange(BR.weight)
        }
    @SerializedName("volume")
    @get:Bindable
    var volume: String? = null
        set(volume) {
            field = volume
            notifyChange(BR.volume)
        }
    @SerializedName("freight")
    @get:Bindable
    var freight: String = "0.0"
        set(freight) {
            field = freight
            notifyChange(BR.freight)
        }
    @SerializedName("remark")
    @get:Bindable
    var remark: String? = null
        set(remark) {
            field = remark
            notifyChange(BR.remark)
        }
    @SerializedName("created_at")
    @get:Bindable
    var createdAt: String? = null
        set(createdAt) {
            field = createdAt
            notifyChange(BR.createdAt)
        }
    @SerializedName("receipt_at")
    @get:Bindable
    var receiptAt: String? = null
        set(receiptAt) {
            field = receiptAt
            notifyChange(BR.receiptAt)
        }
    @SerializedName("pay_at")
    @get:Bindable
    var payAt: String? = null
        set(payAt) {
            field = payAt
            notifyChange(BR.payAt)
        }
    @SerializedName("finish_at")
    @get:Bindable
    var finishAt: String? = null
        set(finishAt) {
            field = finishAt
            notifyChange(BR.finishAt)
        }
    @SerializedName("type")
    @get:Bindable
    var type: Int = 0
        set(type) {
            field = type
            notifyChange(BR.type)
        }
    @SerializedName("user")
    @get:Bindable
    var user: UserBean? = null
        set(user) {
            field = user
            notifyChange(BR.user)
        }
    @SerializedName("carrier")
    @get:Bindable
    var carrier: CarrierBean? = null
        set(carrier) {
            field = carrier
            notifyChange(BR.carrier)
        }
    @SerializedName("car")
    @get:Bindable
    var car: CarBean? = null
        set(car) {
            field = car
            notifyChange(BR.car)
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

    open class UserBean : Observable {
        /**
         * name : 张三
         * phone : 171159758414
         * rank_text : 白银
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
        @SerializedName("rank_text")
        @get:Bindable
        var rankText: String? = null
            set(rankText) {
                field = rankText
                notifyChange(BR.rankText)
            }
        @SerializedName("logo")
        @get:Bindable
        var logo: String? = null
            set(logo) {
                field = logo
                notifyChange(BR.logo)
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


    class CarrierBean : Observable {
        /**
         * name : 张伟
         * phone : 15296476403
         * rank_text : 白银
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
        @SerializedName("rank_text")
        @get:Bindable
        var rankText: String? = null
            set(rankText) {
                field = rankText
                notifyChange(BR.rankText)
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


    open class CarBean : Observable {
        /**
         * weight : 5
         * volume : 5
         * long : 5
         * wide : 5
         * height : 5
         */

        @SerializedName("weight")
        @get:Bindable
        var weight: String? = null
            set(weight) {
                field = weight
                notifyChange(BR.weight)
            }
        @SerializedName("volume")
        @get:Bindable
        var volume: String? = null
            set(volume) {
                field = volume
                notifyChange(BR.volume)
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

        fun sizeInfo(): String {
            return "$longX * $wide * $height"
        }
    }
}