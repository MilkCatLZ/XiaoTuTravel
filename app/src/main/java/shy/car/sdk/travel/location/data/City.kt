package shy.car.sdk.travel.location.data

import android.databinding.BaseObservable
import me.yokeyword.indexablerv.IndexableEntity

class City(cityName: String, cityCode: String) : BaseObservable(), IndexableEntity {

    var name = ""
    var cityName = ""
    var cityCode = ""
    var pinyin: String = ""

    init {
        this.cityName = cityName
        this.cityCode = cityCode
    }

    override fun setFieldIndexBy(indexField: String?) {
        this.name = indexField!!
    }

    override fun setFieldPinyinIndexBy(pinyin: String?) {
        this.pinyin = pinyin!!
    }

    override fun getFieldIndexBy(): String {
        return cityName
    }

}