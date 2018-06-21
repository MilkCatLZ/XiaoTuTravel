package shy.car.sdk.travel.order.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR

class RentOrderDetail : BaseObservable() {

    /**
     * user_id : 2
     * order_id : 222
     * duration_fee : 256.8
     * mileage_fee : 22
     * money : 247.68
     * realpay_money : 3333
     * coupon_price : 244
     * mileage : 44
     * return_at : 04-18 00:00 ~ 05-03 17:46
     * duration : 1:57
     * status : 5
     * status_name : 已完成
     * car : {"car_model_img":"upload/car/5ae416ed1efe4.png","plate_number":"桂A88888","car_model_name":"A6","car_color":"黑色","cost":{"minute_price":1,"km_price":5,"minimum":10},"discounts":[{"id":24,"txt":"24小时整日租","price":"140"},{"id":"夜","txt":"20:00-次日8:00","price":"88"}]}
     */

    @SerializedName("user_id")
    @get:Bindable
    var userId: String? = null
        set(userId) {
            field = userId
            notifyChange(BR.userId)
        }
    @SerializedName("order_id")
    @get:Bindable
    var orderId: String? = null
        set(orderId) {
            field = orderId
            notifyChange(BR.orderId)
        }
    @SerializedName("duration_fee")
    @get:Bindable
    var durationFee: Double = 0.toDouble()
        set(durationFee) {
            field = durationFee
            notifyChange(BR.durationFee)
        }
    @SerializedName("mileage_fee")
    @get:Bindable
    var mileageFee: Double = 0.toDouble()
        set(mileageFee) {
            field = mileageFee
            notifyChange(BR.mileageFee)
        }
    @SerializedName("money")
    @get:Bindable
    var money: Double = 0.toDouble()
        set(money) {
            field = money
            notifyChange(BR.money)
        }
    @SerializedName("realpay_money")
    @get:Bindable
    var realpayMoney: Double = 0.toDouble()
        set(realpayMoney) {
            field = realpayMoney
            notifyChange(BR.realpayMoney)
        }
    @SerializedName("coupon_price")
    @get:Bindable
    var couponPrice: Double = 0.toDouble()
        set(couponPrice) {
            field = couponPrice
            notifyChange(BR.couponPrice)
        }
    @SerializedName("mileage")
    @get:Bindable
    var mileage: Int = 0
        set(mileage) {
            field = mileage
            notifyChange(BR.mileage)
        }
    @SerializedName("return_at")
    @get:Bindable
    var returnAt: String? = null
        set(returnAt) {
            field = returnAt
            notifyChange(BR.returnAt)
        }
    @SerializedName("duration")
    @get:Bindable
    var duration: String? = null
        set(duration) {
            field = duration
            notifyChange(BR.duration)
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

    open class CarBean : Observable {
        /**
         * car_model_img : upload/car/5ae416ed1efe4.png
         * plate_number : 桂A88888
         * car_model_name : A6
         * car_color : 黑色
         * cost : {"minute_price":1,"km_price":5,"minimum":10}
         * discounts : [{"id":24,"txt":"24小时整日租","price":"140"},{"id":"夜","txt":"20:00-次日8:00","price":"88"}]
         */

        @SerializedName("id")
        @get:Bindable
        var id: String = "1"
            set(id) {
                field = id
                notifyChange(BR.id)
            }
        @SerializedName("car_model_img")
        @get:Bindable
        var carModelImg: String? = null
            set(carModelImg) {
                field = carModelImg
                notifyChange(BR.carModelImg)
            }
        @SerializedName("lat")
        @get:Bindable
        var lat: Double = 0.0
            set(lat) {
                field = lat
                notifyChange(BR.lat)
            }
        @SerializedName("lng")
        @get:Bindable
        var lng: Double = 0.0
            set(lng) {
                field = lng
                notifyChange(BR.lng)
            }
        @SerializedName("plate_number")
        @get:Bindable
        var plateNumber: String? = null
            set(plateNumber) {
                field = plateNumber
                notifyChange(BR.plateNumber)
            }
        @SerializedName("car_model_name")
        @get:Bindable
        var carModelName: String? = null
            set(carModelName) {
                field = carModelName
                notifyChange(BR.carModelName)
            }
        @SerializedName("car_color")
        @get:Bindable
        var carColor: String? = null
            set(carColor) {
                field = carColor
                notifyChange(BR.carColor)
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
        var discounts: List<DiscountsBean>? = null
            set(discounts) {
                field = discounts
                notifyChange(BR.discounts)
            }
        @SerializedName("battery")
        @get:Bindable
        var battery: String = ""
            set(battery) {
                field = battery
                notifyChange(BR.battery)
            }
        @SerializedName("surplus_mileage")
        @get:Bindable
        var surplusMileage: String = ""
            set(surplusMileage) {
                field = surplusMileage
                notifyChange(BR.surplusMileage)
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
             * minute_price : 1
             * km_price : 5
             * minimum : 10
             */

            @SerializedName("minute_price")
            @get:Bindable
            var minutePrice: Double = 0.toDouble()
                set(minutePrice) {
                    field = minutePrice
                    notifyChange(BR.minutePrice)
                }
            @SerializedName("km_price")
            @get:Bindable
            var kmPrice: Double = 0.toDouble()
                set(kmPrice) {
                    field = kmPrice
                    notifyChange(BR.kmPrice)
                }
            @SerializedName("minimum")
            @get:Bindable
            var minimum: Double = 0.toDouble()
                set(minimum) {
                    field = minimum
                    notifyChange(BR.minimum)
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
            /**
             * id : 24
             * txt : 24小时整日租
             * price : 140
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
    }

}