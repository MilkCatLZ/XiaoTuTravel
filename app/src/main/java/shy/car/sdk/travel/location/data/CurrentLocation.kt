package shy.car.sdk.travel.location.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.amap.api.services.core.PoiItem
import com.android.databinding.library.baseAdapters.BR
import com.base.location.Location
import com.google.gson.annotations.SerializedName
import me.yokeyword.indexablerv.IndexableEntity
import me.yokeyword.indexablerv.PinyinUtil

class CurrentLocation : BaseObservable(),
        IndexableEntity {

    @SerializedName("name")
    @get:Bindable
    var cityName = ""
        set(value) {
            field = value
            pinyin = PinyinUtil.getPingYin(field)
            notifyPropertyChanged(BR.cityName)
        }
    @SerializedName("id")
    @get:Bindable
    var cityCode = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.cityCode)
        }
    @get:Bindable
    var address = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.address)
        }
    @get:Bindable
    var pinyin: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pinyin)
        }
    @get:Bindable
    var lat: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.lat)
        }
    //        get():Double {
//            return if (BuildConfig.DEBUG) {
//                22.841251
//            } else {
//                field
//            }
//        }
    @get:Bindable
    var lng: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.lng)
        }
//        get():Double {
//            return if (BuildConfig.DEBUG) {
//                108.283127
//            } else {
//                field
//            }
//        }

    @get:Bindable
    var district: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.district)
        }

    init {
        this.cityName = cityName
        this.cityCode = cityCode
        pinyin = PinyinUtil.getPingYin(cityName)
    }

    override fun setFieldIndexBy(indexField: String?) {
        this.cityName = indexField!!
    }

    override fun setFieldPinyinIndexBy(pinyin: String?) {
        this.pinyin = pinyin!!
    }

    override fun getFieldIndexBy(): String {
        return cityName
    }

    fun copy(location: Location) {
        cityName = location.city
        lat = location.latitude
        lng = location.longitude
        address = location.address
        district = location.district
//        cityCode = location.cityCode
    }

    fun copy(location: PoiItem) {
        cityName = location.cityName
        lat = location.latLonPoint.latitude
        lng = location.latLonPoint.longitude
        address = location.cityName + location.businessArea + location.snippet + location.toString()
        district = location.adName
        cityCode = location.cityCode
    }

    fun copy(): CurrentLocation {
        var newLocation = CurrentLocation()
        newLocation.cityName = this.cityName
        newLocation.lat = this.lat
        newLocation.lng = this.lng
        newLocation.address = this.address
        newLocation.district = this.district
        newLocation.cityCode = this.cityCode
        return newLocation
    }

}