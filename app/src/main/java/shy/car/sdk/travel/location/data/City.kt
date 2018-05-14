package shy.car.sdk.travel.location.data

import android.databinding.BaseObservable
import me.yokeyword.indexablerv.IndexableEntity
import me.yokeyword.indexablerv.PinyinUtil

class City(cityName: String, cityCode: String) : BaseObservable(), IndexableEntity {

    var cityName = ""
        set(value) {
            field = value
            pinyin = PinyinUtil.getPingYin(field)
        }
    var cityCode = ""
    var pinyin: String = ""

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