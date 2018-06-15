package shy.car.sdk.travel.rent.data

import com.google.gson.annotations.SerializedName

data class RentOrderDetail(
    @SerializedName("user_id") var userId: Int,
    @SerializedName("order_id") var orderId: Int,
    @SerializedName("duration_fee") var durationFee: Double,
    @SerializedName("mileage_fee") var mileageFee: Double,
    @SerializedName("money") var money: Double,
    @SerializedName("realpay_money") var realpayMoney: Int,
    @SerializedName("coupon_price") var couponPrice: Int,
    @SerializedName("mileage") var mileage: Int,
    @SerializedName("return_at") var returnAt: String,
    @SerializedName("duration") var duration: String,
    @SerializedName("status") var status: Int,
    @SerializedName("status_name") var statusName: String,
    @SerializedName("car") var car: Car
)