package shy.car.sdk.app.dialog

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogCancelHintBinding

class HintDialog : XTBaseDialogFragment() {

    companion object {
        fun with(context: Context, manager: FragmentManager): HintDialog {
            val dialog = HintDialog()
            dialog.contexts = context
            dialog.manager = manager
            return dialog
        }
    }


    private val LNCUSTOM_DIALOG_DIALOG = "DialogManager:dialog"

    lateinit var contexts: Context
    lateinit var manager: FragmentManager
    var listener: OnDissmiss? = null


    lateinit var binding: DialogCancelHintBinding
    var message: String = ""
    var leftButtonText: String = "取消"
    var rightButtonText: String = "确定"
    var leftBottomVisible = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_cancel_hint, null, false)
        binding.dialog = this
        return binding.root

    }


    fun listener(l: OnDissmiss): HintDialog {
        listener = l
        return this
    }

    fun message(m: String): HintDialog {
        this.message = m
        return this
    }

    fun leftButtonText(m: String): HintDialog {
        this.leftButtonText = m
        return this
    }

    fun rightButtonText(m: String): HintDialog {
        this.rightButtonText = m
        return this
    }
    fun leftBottomVisible(m: Boolean): HintDialog {
        this.leftBottomVisible = m
        return this
    }

    fun show(): HintDialog {
        try {
            show(manager, LNCUSTOM_DIALOG_DIALOG)
        } catch (e: Exception) {

        }
        return this
    }


    interface OnDissmiss {
        fun onLeftClick()
        fun onRightClick()
    }

    fun leftButtonClick() {
        listener?.onLeftClick()

        dismissAllowingStateLoss()
    }

    fun rightButtonClick() {
        listener?.onRightClick()

        dismissAllowingStateLoss()
    }


}