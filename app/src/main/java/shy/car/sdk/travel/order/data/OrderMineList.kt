package shy.car.sdk.travel.order.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class OrderMineList : BaseObservable() {
    companion object {
        /**
         * 全部
         */
        @JvmStatic
        open val ALL = 0
        /**
         * 租车
         */
        @JvmStatic
        var RENT = 1
        /**
         * 接单
         */
        @JvmStatic
        var TAKE = 2
        /**
         * 发货
         */
        @JvmStatic
        var SEND = 3
    }

    @SerializedName("order_id")
    @get:Bindable
    var id: String = "0"
        set(value) {
            field = value
            notifyChange(BR.id)
        }

    /**
     * type : 2
     * type_name : 小兔接单
     * plate_number : -
     * model_name : 辉腾
     * from_address : 广西南宁青秀区
     * to_address : 广西南宁江南区
     * status : 1
     * status_name : 待接单
     * created_at : 06月19日 15:09
     */

    @SerializedName("type")
    @get:Bindable
    var type: Int = 0
        set(type) {
            field = type
            notifyChange(BR.type)
        }
    @SerializedName("type_name")
    @get:Bindable
    var typeName: String? = null
        set(typeName) {
            field = typeName
            notifyChange(BR.typeName)
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
    @SerializedName("created_at")
    @get:Bindable
    var createdAt: String? = null
        set(createdAt) {
            field = createdAt
            notifyChange(BR.createdAt)
        }
    @SerializedName("car")
    @get:Bindable
    var car: Car? = null
        set(car) {
            field = car
            notifyChange(BR.car)
        }

    fun carInfo(): String {
        return "${car?.plateNumber} | ${car?.modelName}"
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


    open class Car : BaseObservable() {
        /**
         * freightId : 2
         * plate_number : 桂E88888
         * model_name : A4
         * lng : 118.320004
         * lat : 22.82402
         * electricity : 22%
         * surplus_mileage : 55.0km
         */

        @SerializedName("freight_id")
        @get:Bindable
        var freightId: String? = null
            set(freightId) {
                field = freightId
                notifyChange(BR.freightId)
            }
        @SerializedName("plate_number")
        @get:Bindable
        var plateNumber: String? = null
            set(plateNumber) {
                field = plateNumber
                notifyChange(BR.plateNumber)
            }
        @SerializedName("model_name")
        @get:Bindable
        var modelName: String? = null
            set(modelName) {
                field = modelName
                notifyChange(BR.modelName)
            }
        @SerializedName("lng")
        @get:Bindable
        var lng: Double = 0.toDouble()
            set(lng) {
                field = lng
                notifyChange(BR.lng)
            }
        @SerializedName("lat")
        @get:Bindable
        var lat: Double = 0.toDouble()
            set(lat) {
                field = lat
                notifyChange(BR.lat)
            }
        @SerializedName("electricity")
        @get:Bindable
        var electricity: String? = null
            set(electricity) {
                field = electricity
                notifyChange(BR.electricity)
            }
        @SerializedName("surplus_mileage")
        @get:Bindable
        var surplusMileage: String? = null
            set(surplusMileage) {
                field = surplusMileage
                notifyChange(BR.surplusMileage)
            }
        @SerializedName("color")
        @get:Bindable
        var color: String? = null
            set(color) {
                field = color
                notifyChange(BR.color)
            }
        @SerializedName("img")
        @get:Bindable
        var img: String? = null
            set(color) {
                field = img
                notifyChange(BR.img)
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