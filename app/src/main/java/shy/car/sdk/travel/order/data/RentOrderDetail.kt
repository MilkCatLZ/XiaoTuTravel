package shy.car.sdk.travel.order.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.google.gson.annotations.SerializedName
import shy.car.sdk.BR
import shy.car.sdk.app.LNTextUtil

class RentOrderDetail : BaseObservable() {


    /**
     * user_id : 8
     * order_id : 1527039003
     * duration_fee : 0
     * mileage_fee : 0
     * money : 0
     * realpay_money : 0
     * coupon_price : 0
     * mileage : 0
     * use_car_time : 05-23 09:33 ~ 01-01 08:00
     * duration : 0:0
     * status : 3
     * status_name : 已取车
     * from_network : 广西壮族自治区南宁市江南区金凯街道迎凯路
     * to_network : 广西壮族自治区南宁市江南区金凯街道迎凯路
     * pay_method : 微信支付
     * created_at : 05-23 09:30
     * obtain_at : 05-23 09:33
     * return_at : 07-01 08:00
     * finish_at : 06-01 08:00
     * comment_id : 1
     * car : {"model_img":"http://www.api.com/upload/car/upload/car/5ae416fcdc2da.png","plate_number":"桂E88888","model_name":"A4","color":"白色","cost":{"minute_price":1.5,"km_price":8,"minimum":8},"discounts":[{"id":24,"txt":"24小时整日租","price":500}]}
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
    var mileage: Double = 0.toDouble()
        set(mileage) {
            field = mileage
            notifyChange(BR.mileage)
        }
    @SerializedName("use_car_time")
    @get:Bindable
    var useCarTime: String? = null
        set(useCarTime) {
            field = useCarTime
            notifyChange(BR.useCarTime)
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
    @SerializedName("from_network")
    @get:Bindable
    var fromNetwork: String? = null
        set(fromNetwork) {
            field = fromNetwork
            notifyChange(BR.fromNetwork)
        }
    @SerializedName("to_network")
    @get:Bindable
    var toNetwork: String? = null
        set(toNetwork) {
            field = toNetwork
            notifyChange(BR.toNetwork)
        }
    @SerializedName("pay_method")
    @get:Bindable
    var payMethod: String? = null
        set(payMethod) {
            field = payMethod
            notifyChange(BR.payMethod)
        }
    @SerializedName("created_at")
    @get:Bindable
    var createdAt: String? = null
        set(createdAt) {
            field = createdAt
            notifyChange(BR.createdAt)
        }
    @SerializedName("obtain_at")
    @get:Bindable
    var obtainAt: String? = null
        set(obtainAt) {
            field = obtainAt
            notifyChange(BR.obtainAt)
        }
    @SerializedName("return_at")
    @get:Bindable
    var returnAt: String? = null
        set(returnAt) {
            field = returnAt
            notifyChange(BR.returnAt)
        }
    @SerializedName("finish_at")
    @get:Bindable
    var finishAt: String? = null
        set(finishAt) {
            field = finishAt
            notifyChange(BR.finishAt)
        }
    @SerializedName("comment_id")
    @get:Bindable
    var commentId: String? = null
        set(commentId) {
            field = commentId
            notifyChange(BR.commentId)
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

    fun carInfo(): String {
        return "${car?.plateNumber} | ${car?.modelName} | ${car?.color}"
    }

    fun costInfo(): String {
        return "计费标准： ￥${car?.cost?.minutePrice}/分钟+￥${car?.cost?.kmPrice}/公里；最低消费：${LNTextUtil.getPriceText(car?.cost?.minimum!!)}元"
    }

    open class CarBean : Observable {
        /**
         * model_img : http://www.api.com/upload/car/upload/car/5ae416fcdc2da.png
         * plate_number : 桂E88888
         * model_name : A4
         * color : 白色
         * cost : {"minute_price":1.5,"km_price":8,"minimum":8}
         * discounts : [{"id":24,"txt":"24小时整日租","price":500}]
         */

        @SerializedName("car_id")
        @get:Bindable
        var carID: String? = null
            set(carID) {
                field = carID
                notifyChange(BR.carID)
            }
        @SerializedName("model_img")
        @get:Bindable
        var modelImg: String? = null
            set(modelImg) {
                field = modelImg
                notifyChange(BR.modelImg)
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
        @SerializedName("color")
        @get:Bindable
        var color: String? = null
            set(color) {
                field = color
                notifyChange(BR.color)
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
             * minute_price : 1.5
             * km_price : 8
             * minimum : 8
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
             * price : 500
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