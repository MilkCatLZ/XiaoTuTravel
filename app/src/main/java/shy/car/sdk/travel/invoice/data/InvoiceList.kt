package shy.car.sdk.travel.invoice.data


import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR
import java.util.*


class InvoiceList : BaseObservable(), Observable {


    /**
     * month : 5
     * list : [{"car_plate_number":"桂E88888","car_model_name":"A4","money":0,"created_at":"2018.05.11 18:15:41"},{"car_plate_number":"桂E88888","car_model_name":"A4","money":10,"created_at":"2018.05.11 18:16:54"}]
     */

    @SerializedName("month")
    @get:Bindable
    var month: Int = 0
        set(month) {
            field = month
            notifyChange(BR.month)
        }

    @SerializedName("orders")
    @get:Bindable
    var orders: ArrayList<Orders>? = null
//        get() {
//            if ((orders == null || orders!!.isEmpty()) && BuildConfig.DEBUG) {
//                orders = ArrayList()
//                for (i in 0..2) {
//                    val order = Orders()
//                    order.id = i.toString()
//                    order.carModelName = "EQ1"
//                    order.carPlateNumber = "桂A77995"
//                    order.money = 554.23
//                    order.createdAt = "2018.07.07 10:38"
//                    orders!!.add(order)
//                }
//                return orders
//            }
//            return field
//        }

    @Transient
    private var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

    val monthText: String
        get() = this.month.toString() + "月"

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

    class Orders : Observable {
        /**
         * car{car_plate_number : 桂E88888,car_model_name : A4}
         * money : 0
         * created_at : 2018.05.11 18:15:41
         * id : 1
         */
        val carInfo: String
            @Bindable
            get() = this.car?.carModelName + " | " + this.car?.carPlateNumber


        @SerializedName("id")
        var id: String? = null

        @SerializedName("car")
        var car: Car? = null
            set(car) {
                field = car
                notifyChange(BR.car)
            }


        @SerializedName("money")
        @get:Bindable
        var money: Double? = null
            set(money) {
                field = money
                notifyChange(BR.money)
            }
        @SerializedName("created_at")
        @get:Bindable
        var createdAt: String? = null
            set(createdAt) {
                field = createdAt
                notifyChange(BR.createdAt)
            }
        @Transient
        var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

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

    class Car :Observable{
        @Transient
        var propertyChangeRegistry: PropertyChangeRegistry? = PropertyChangeRegistry()

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

        @SerializedName("plate_number")
        @get:Bindable
        var carPlateNumber: String? = null
            set(carPlateNumber) {
                field = carPlateNumber
                notifyChange(BR.carPlateNumber)
            }
        @SerializedName("model_name")
        @get:Bindable
        var carModelName: String? = null
            set(carModelName) {
                field = carModelName
                notifyChange(BR.carModelName)
            }
    }
}
