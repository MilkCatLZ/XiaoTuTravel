package shy.car.sdk.travel.common.presenter

import android.content.Context
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.base.databinding.DataBindingAdapter
import com.base.databinding.DataBindingItemClickAdapter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.common.data.GoodsType


/**
 * create by LZ at 2018/05/29
 * 选择货物类型
 */
class GoodsSelectPresenter(context: Context, var callBack: CallBack) : BasePresenter(context) {

    var checkID = ObservableField<String>("")
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

    fun getCheckedGoodsType(typeString: String): GoodsType {

        return if (checkedGoodsType != null) {
            checkedGoodsType!!
        } else {
            var type = GoodsType()
            type.goodsTypeName = typeString
            type

        }
    }


    fun getGoodsType() {
        val observable = ApiManager.getInstance()
                .api.getFreightTypeList()

        val observer = object : Observer<List<GoodsType>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: List<GoodsType>) {
                adapter.setItems(t, 1)
                checkedGoodsType = t[0]
                checkID.set(checkedGoodsType?.goodsType)
            }

            override fun onError(e: Throwable) {

            }

        }
        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
}