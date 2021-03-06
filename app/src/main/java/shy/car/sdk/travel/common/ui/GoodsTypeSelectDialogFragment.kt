package shy.car.sdk.travel.common.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.util.StringUtils
import shy.car.sdk.R
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogGoodsSelectBinding
import shy.car.sdk.travel.common.data.GoodsType
import shy.car.sdk.travel.common.presenter.GoodsSelectPresenter


/**
 * 选择货物类型
 */
@Route(path = RouteMap.GoodsTypeSelect)
class GoodsTypeSelectDialogFragment : BottomSheetDialogFragment(),
        GoodsSelectPresenter.CallBack {

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

        activity?.let {
            presenter = GoodsSelectPresenter(it, this)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_goods_select, null, false)
        binding.fragment = this
        binding.presenter = presenter
        initRecyleView()
        initEdit()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getGoodsType()
    }

    private fun initEdit() {
        binding.edtGoodsType.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && StringUtils.isNotEmpty(p0.toString())) {
                    presenter.checkID.set("0")
                    presenter.checkedGoodsType = null
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun initRecyleView() {
        activity?.let {
            val layoutManager = GridLayoutManager(it, 4)
            binding.recyclerViewGoodsSelect.layoutManager = layoutManager
        }
    }

    fun onConfirm() {
        listener?.onTimeSelect(presenter.getCheckedGoodsType(binding.edtGoodsType.text.toString()))
        dismiss()
    }


}