package shy.car.sdk.travel.common.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogGoodsSelectBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.presenter.GoodsSelectPresenter


/**
 * 选择货物类型
 */
@Route(path = RouteMap.GoodsTypeSelect)
class GoodsTypeSelectDialogFragment : BottomSheetDialogFragment(), GoodsSelectPresenter.CallBack {

    override fun onGetListSuccess() {

    }

    interface OnItemSelectedListener {
        fun onTimeSelect(goodsType: GoodsType)
    }

    var listener: OnItemSelectedListener? = null


    lateinit var presenter: GoodsSelectPresenter

    lateinit var binding: DialogGoodsSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = GoodsSelectPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_goods_select, null, false)
        binding.fragment = this
        binding.presenter = presenter
        activity?.let {
            val layoutManager = GridLayoutManager(it, 4)
            binding.recyclerViewGoodsSelect.layoutManager = layoutManager
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getList()
    }


    fun onConfirm() {
        listener?.onTimeSelect(presenter.checkedGoodsType!!)
        dismiss()
    }

}