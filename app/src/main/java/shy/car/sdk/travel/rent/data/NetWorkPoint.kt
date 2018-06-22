package shy.car.sdk.travel.rent.data

import com.google.gson.annotations.SerializedName

data class NetWorkPoint(
    @SerializedName("lng") var lng: Double,
    @SerializedName("lat") var lat: Double
)