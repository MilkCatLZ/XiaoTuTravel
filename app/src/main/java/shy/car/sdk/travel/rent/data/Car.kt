package shy.car.sdk.travel.rent.data

import com.google.gson.annotations.SerializedName
data class Car(
    @SerializedName("car_model_img") var carModelImg: String,
    @SerializedName("plate_number") var plateNumber: String,
    @SerializedName("car_model_name") var carModelName: String,
    @SerializedName("car_color") var carColor: String,
    @SerializedName("cost") var cost: Cost,
    @SerializedName("discounts") var discounts: List<Discount>
)