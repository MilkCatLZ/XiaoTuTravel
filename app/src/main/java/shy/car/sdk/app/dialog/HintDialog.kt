package shy.car.sdk.app.dialog

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.FragmentManager
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

    fun show(): HintDialog {
        try {
            show(manager, LNCUSTOM_DIALOG_DIALOG)
        } catch (e: Exception) {

        }
        return this
    }


    interface OnDissmiss {
        fun onConfirmClick()
    }

    fun closeDialog() {
        listener?.onConfirmClick()

        dismissAllowingStateLoss()
    }


}