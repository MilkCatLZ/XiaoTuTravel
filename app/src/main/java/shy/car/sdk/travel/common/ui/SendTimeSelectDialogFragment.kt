package shy.car.sdk.travel.common.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.DialogSendTimeSelectBinding
import shy.car.sdk.travel.common.data.CommonWheelItem
import shy.car.sdk.travel.common.presenter.SendTimeSelectPresenter
import shy.car.sdk.travel.send.data.CarUserTime
import kotlin.collections.ArrayList


/**
 * 选择用车时间
 */
@Route(path = RouteMap.SendTimeSelect)
class SendTimeSelectDialogFragment : BottomSheetDialogFragment(),
        SendTimeSelectPresenter.CallBack {

    interface OnItemSelectedListener {
        fun onTimeSelect(date: CommonWheelItem, time: CommonWheelItem)

    }

    var listener: OnItemSelectedListener? = null

    lateinit var presenter: SendTimeSelectPresenter

    lateinit var binding: DialogSendTimeSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = SendTimeSelectPresenter(it, this)
            presenter.setTimeLists(lists)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_send_time_select, null, false)
        binding.fragment = this
        initWheelView()
        presenter.getDateList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wheelSendHoleCarDate.adapter = presenter.adapterDate
        binding.wheelSendHoleCarTime.adapter = presenter.adapterTime
    }

    private fun initWheelView() {
        binding.wheelSendHoleCarDate.setCyclic(false)
        binding.wheelSendHoleCarDate.setTextSize(14f)
        binding.wheelSendHoleCarDate.setLineSpacingMultiplier(2.5f)
        binding.wheelSendHoleCarDate.setTextColorCenter(resources.getColor(R.color.text_primary_333333))

        binding.wheelSendHoleCarTime.setCyclic(false)
        binding.wheelSendHoleCarTime.setTextSize(14f)
        binding.wheelSendHoleCarTime.setLineSpacingMultiplier(2.5f)
        binding.wheelSendHoleCarTime.setTextColorCenter(resources.getColor(R.color.text_primary_333333))
    }


    fun onConfirm() {
        listener?.onTimeSelect(presenter.dateList[binding.wheelSendHoleCarDate.currentItem], presenter.timeList[binding.wheelSendHoleCarTime.currentItem])
        dismiss()
    }

    var lists: List<CarUserTime> = ArrayList<CarUserTime>()
    fun setList(t: List<CarUserTime>) {
        lists = t
    }

}