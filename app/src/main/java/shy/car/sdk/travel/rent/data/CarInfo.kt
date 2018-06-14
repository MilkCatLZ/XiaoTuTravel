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
     * car_model : 奇瑞EQ1
     * car_model_img : http://static.car.com/car/2018/05/10/Aosajfo12dd.jpg
     * plate_number : 桂A88888
     * color : 白色
     * surplus_mileage : 135
     * address : 广西南宁市西乡塘区相思湖西路
     * lng : 108.248593
     * lat : 22.921449
     * cost : {"minute":0.2,"kilometre":0.88,"low_price":"最低消费8元"}
     * discounts : [{"id":24,"txt":"24小时整日租","price":"88"},{"id":"夜","txt":"20:00-次日8:00","price":"88"}]
     */

    @SerializedName("car_id")
    @get:Bindable
    var carId: String? = null
        set(carId) {
            field = carId
            notifyChange(BR.carId)
        }
    @SerializedName("car_model")
    @get:Bindable
    var carModel: String? = null
        set(carModel) {
            field = carModel
            notifyChange(BR.carModel)
        }
    @SerializedName("car_model_img")
    @get:Bindable
    var carModelImg: String? = null
        set(carModelImg) {
            field = carModelImg
            notifyChange(BR.carModelImg)
        }
    @SerializedName("plate_number")
    @get:Bindable
    var plateNumber: String? = null
        set(plateNumber) {
            field = plateNumber
            notifyChange(BR.plateNumber)
        }
    @SerializedName("color")
    @get:Bindable
    var color: String? = null
        set(color) {
            field = color
            notifyChange(BR.color)
        }
    @SerializedName("surplus_mileage")
    @get:Bindable
    var surplusMileage: Double = 0.toDouble()
        set(surplusMileage) {
            field = surplusMileage
            notifyChange(BR.surplusMileage)
        }
    @SerializedName("address")
    @get:Bindable
    var address: String? = null
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
    var cost: Cost? = null
        set(cost) {
            field = cost
            notifyChange(BR.cost)
        }
    @SerializedName("discounts")
    @get:Bindable
    var discounts: List<Discounts>? = null
        set(discounts) {
            field = discounts
            notifyChange(BR.discounts)
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

    class Cost : Observable {
        /**
         * minute : 0.2
         * kilometre : 0.88
         * low_price : 最低消费8元
         */

        @SerializedName("minute")
        @get:Bindable
        var minute: Double = 0.toDouble()
            set(minute) {
                field = minute
                notifyChange(BR.minute)
            }
        @SerializedName("kilometre")
        @get:Bindable
        var kilometre: Double = 0.toDouble()
            set(kilometre) {
                field = kilometre
                notifyChange(BR.kilometre)
            }
        @SerializedName("low_price")
        @get:Bindable
        var lowPrice: String? = null
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


    class Discounts : Observable {
        /**
         * id : 24
         * txt : 24小时整日租
         * price : 88
         */

        @SerializedName("id")
        @get:Bindable
        var id: String? = null
            set(id) {
                field = id
                notifyChange(BR.id)
            }
        @SerializedName("txt")
        @get:Bindable
        var txt: String? = null
            set(txt) {
                field = txt
                notifyChange(BR.txt)
            }
        @SerializedName("price")
        @get:Bindable
        var price: String? = null
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

    /**
     *
     */
    fun costText(): String {
        return "￥${cost?.minute}/分钟+￥${cost?.kilometre}/公里"
    }

}