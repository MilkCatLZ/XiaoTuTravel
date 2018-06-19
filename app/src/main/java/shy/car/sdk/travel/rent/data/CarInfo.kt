package shy.car.sdk.travel.rent.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

/**
 * create by LZ at 2018/05/21
 * 首页-bottom-可用车辆
 */
class CarInfo : BaseObservable() {

    /**
     * car_id : 1
     * car_model : EQ1
     * car_img : http://47.106.88.148/upload/car/
     * plate_number : 桂A51457
     * color : 白色
     * surplus_mileage : 0
     * address :
     * lng : 108.27958
     * lat : 22.841126
     * cost : {"minute":0,"kilometre":0,"low_price":"最低消费0元"}
     * discounts : {"duration":[{"id":"id","time":"24小时整日租","price":0},{"id":"夜","time":"20:00-次日8:00","price":88}]}
     */

    @SerializedName("car_id")
    @get:Bindable
    var carId: String = ""
        set(carId) {
            field = carId
            notifyChange(BR.carId)
        }
    @SerializedName("car_model")
    @get:Bindable
    var carModel: String = ""
        set(carModel) {
            field = carModel
            notifyChange(BR.carModel)
        }
    @SerializedName("battery")
    @get:Bindable
    var battery: String = ""
        set(value) {
            field = value
            notifyChange(BR.battery)
        }
    @SerializedName("car_img")
    @get:Bindable
    var carImg: String = ""
        set(carImg) {
            field = carImg
            notifyChange(BR.carImg)
        }
    @SerializedName("plate_number")
    @get:Bindable
    var plateNumber: String = ""
        set(plateNumber) {
            field = plateNumber
            notifyChange(BR.plateNumber)
        }
    @SerializedName("color")
    @get:Bindable
    var color: String = ""
        set(color) {
            field = color
            notifyChange(BR.color)
        }
    @SerializedName("surplus_mileage")
    @get:Bindable
    var surplusMileage: String = ""
        set(surplusMileage) {
            field = surplusMileage
            notifyChange(BR.surplusMileage)
        }
    @SerializedName("address")
    @get:Bindable
    var address: String = ""
        set(address) {
            field = address
            notifyChange(BR.address)
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
    @SerializedName("cost")
    @get:Bindable
    var cost: CostBean? = null
        set(cost) {
            field = cost
            notifyChange(BR.cost)
        }
    @SerializedName("discounts")
    @get:Bindable
    var discounts: DiscountsBean? = null
        set(discounts) {
            field = discounts
            notifyChange(BR.discounts)
        }
    @SerializedName("network")
    @get:Bindable
    var netWork: NearCarPoint? = null
        set(netWork) {
            field = netWork
            notifyChange(BR.netWork)
        }
    @SerializedName("network_id")
    @get:Bindable
    var networkID: String = ""
        set(value) {
            field = value
            notifyChange(BR.networkID)
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

    open class CostBean : Observable {
        /**
         * minute : 0
         * kilometre : 0
         * low_price : 最低消费0元
         */

        @SerializedName("minute")
        @get:Bindable
        var minute: String = ""
            set(minute) {
                field = minute
                notifyChange(BR.minute)
            }
        @SerializedName("kilometre")
        @get:Bindable
        var kilometre: String = ""
            set(kilometre) {
                field = kilometre
                notifyChange(BR.kilometre)
            }
        @SerializedName("low_price")
        @get:Bindable
        var lowPrice: String = ""
            set(lowPrice) {
                field = lowPrice
                notifyChange(BR.lowPrice)
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

    open class DiscountsBean : Observable {
        @SerializedName("duration")
        @get:Bindable
        var duration: List<DurationBean>? = null
            set(duration) {
                field = duration
                notifyChange(BR.duration)
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

        open class DurationBean : Observable {
            /**
             * id : id
             * time : 24小时整日租
             * price : 0
             */

            @SerializedName("id")
            @get:Bindable
            var id: String = ""
                set(id) {
                    field = id
                    notifyChange(BR.id)
                }
            @SerializedName("time")
            @get:Bindable
            var time: String = ""
                set(time) {
                    field = time
                    notifyChange(BR.time)
                }
            @SerializedName("price")
            @get:Bindable
            var price: Double = 0.toDouble()
                set(price) {
                    field = price
                    notifyChange(BR.price)
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

    /**
     *
     */
    fun costText(): String {
        return "￥${cost?.minute}/分钟+￥${cost?.kilometre}/公里"
    }
}