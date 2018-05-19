package shy.car.sdk.travel.rent.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
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
}
