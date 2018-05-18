package shy.car.sdk.travel.location.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import me.yokeyword.indexablerv.IndexableEntity
import me.yokeyword.indexablerv.PinyinUtil

class City(cityName: String, cityCode: String) : BaseObservable(), IndexableEntity {

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

}