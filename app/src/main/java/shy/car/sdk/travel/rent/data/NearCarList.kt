package shy.car.sdk.travel.rent.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import me.yokeyword.indexablerv.IndexableEntity
import me.yokeyword.indexablerv.PinyinUtil

class NearCarList : BaseObservable(), IndexableEntity {
    override fun setFieldIndexBy(indexField: String?) {
        this.address = indexField!!
    }

    override fun setFieldPinyinIndexBy(pinyin: String?) {
        this.pinyin = pinyin!!
    }

    override fun getFieldIndexBy(): String {
        return address
    }

    @get:Bindable
    @SerializedName("address")
    var address = ""
        set(value) {
            field = value
            pinyin = PinyinUtil.getPingYin(field)
            notifyPropertyChanged(BR.address)
        }

    @get:Bindable
    var pinyin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pinyin)
        }

    /**
     * id : 10025
     * name : 委屈委屈委屈
     * address : 2
     * lng : 108.320004
     * lat : 22.82402
     * usable_cars_num : 0
     * usable_parking_place : 1
     * distance : 560.53
     */

    @SerializedName("id")
    var id: String? = null
    @SerializedName("name")
    @get:Bindable
    var name: String? = null
        set(name) {
            field = name
            notifyPropertyChanged(shy.car.sdk.BR.name)
        }

    @SerializedName("lng")
    @get:Bindable
    var lng: Double = 0.toDouble()
        set(lng) {
            field = lng
            notifyPropertyChanged(shy.car.sdk.BR.lng)
        }
    @SerializedName("lat")
    @get:Bindable
    var lat: Double = 0.toDouble()
        set(lat) {
            field = lat
            notifyPropertyChanged(shy.car.sdk.BR.lat)
        }
    @SerializedName("usable_cars_num")
    @get:Bindable
    var usableCarsNum: Int = 0
        set(usableCarsNum) {
            field = usableCarsNum
            notifyPropertyChanged(shy.car.sdk.BR.usableCarsNum)
        }
    @SerializedName("usable_parking_place")
    @get:Bindable
    var usableParkingPlace: Int = 0
        set(usableParkingPlace) {
            field = usableParkingPlace
            notifyPropertyChanged(shy.car.sdk.BR.usableParkingPlace)
        }
    @SerializedName("distance")
    @get:Bindable
    var distance: Double = 0.toDouble()
        set(distance) {
            field = distance
            notifyPropertyChanged(shy.car.sdk.BR.distance)
        }
}
