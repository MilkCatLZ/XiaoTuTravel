package shy.car.sdk.travel.location.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.amap.api.services.core.PoiItem
import com.android.databinding.library.baseAdapters.BR
import com.base.location.Location
import me.yokeyword.indexablerv.IndexableEntity
import me.yokeyword.indexablerv.PinyinUtil

class CurrentLocation : BaseObservable, IndexableEntity {

    constructor()
    constructor(cityName: String, cityCode: String) {
        this.cityName = cityName
        this.cityCode = cityCode
    }

    @get:Bindable
    var cityName = ""
        set(value) {
            field = value
            pinyin = PinyinUtil.getPingYin(field)
            notifyPropertyChanged(BR.cityName)
        }
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
    @get:Bindable
    var lng: Double = 0.0
        set(value) {
            field = value
            notifyPropertyChanged(BR.lng)
        }

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
        cityCode = location.cityCode
    }

    fun copy(location: PoiItem) {
        cityName = location.cityName
        lat = location.latLonPoint.latitude
        lng = location.latLonPoint.longitude
        address = location.snippet
        district = location.adName
        cityCode = location.cityCode
    }

}