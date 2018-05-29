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
import java.util.*


/**
 * 选择用车时间
 */
@Route(path = RouteMap.SendTimeSelect)
class SendTimeSelectDialogFragment : BottomSheetDialogFragment(), SendTimeSelectPresenter.CallBack {

    interface OnItemSelectedListener {
        fun onTimeSelect(date: CommonWheelItem, time: CommonWheelItem)

    }

    var listener: OnItemSelectedListener? = null
    override fun onGetListSuccess(list: ArrayList<CommonWheelItem>) {
        binding.wheelSendHoleCarDate.adapter = presenter.adapterDate
        binding.wheelSendHoleCarTime.adapter = presenter.adapterTime

    }


    lateinit var presenter: SendTimeSelectPresenter

    lateinit var binding: DialogSendTimeSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = SendTimeSelectPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_send_time_select, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWheelView()



        presenter.getList()
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

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
//
//        try {
//            val mBehaviorField = bottomSheetDialog.javaClass.getDeclaredField("mBehavior")
//            mBehaviorField.isAccessible = true
//            val behavior = mBehaviorField.get(bottomSheetDialog) as? BottomSheetBehavior<*>
//            behavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
//                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                    }
//                }
//
//                override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
//            })
//        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        }
//
//        return bottomSheetDialog
//    }
}