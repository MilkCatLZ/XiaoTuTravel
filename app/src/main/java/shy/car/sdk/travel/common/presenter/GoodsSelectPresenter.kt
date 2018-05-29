package shy.car.sdk.travel.common.presenter

import android.content.Context
import android.databinding.ObservableInt
import android.view.View
import com.base.databinding.DataBindingAdapter
import com.base.databinding.DataBindingItemClickAdapter
import com.base.databinding.ItemViewHolder
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.send.adapter.WheelAdapter
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * create by LZ at 2018/05/29
 * 选择货物类型
 */
class GoodsSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var checkID = ObservableInt(0)
    var checkedGoodsType: GoodsType? = null
    val adapter = DataBindingItemClickAdapter<GoodsType>(R.layout.item_goods_type, BR.goodsType, BR.click, View.OnClickListener {
        val goodsType = it.tag as GoodsType
        checkedGoodsType = goodsType
        checkID.set(goodsType.goodsType)
    }, DataBindingAdapter.CallBack { holder, position ->
        holder.binding.setVariable(BR.presenter, this@GoodsSelectPresenter)
    })

    interface CallBack {
        fun onGetListSuccess()
    }

    fun getList() {
        val list = ArrayList<GoodsType>()

        for (i in 0..10) {
            var goodsType = GoodsType()
            goodsType.goodsTypeName = "类型$i"
            goodsType.goodsType = i
            list.add(goodsType)
        }
        adapter.setItems(list, 1)
    }
}