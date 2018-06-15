package shy.car.sdk.travel.rent.data

import com.google.gson.annotations.SerializedName
data class Discount(
    @SerializedName("id") var id: String,
    @SerializedName("txt") var txt: String,
    @SerializedName("price") var price: String
)