package shy.car.sdk.travel.rent.data

import com.google.gson.annotations.SerializedName
data class Cost(
    @SerializedName("minute_price") var minutePrice: Int,
    @SerializedName("km_price") var kmPrice: Int,
    @SerializedName("minimum") var minimum: Int
)