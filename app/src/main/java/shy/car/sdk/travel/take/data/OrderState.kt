package shy.car.sdk.travel.take.data

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName

open class OrderState {
    companion object {
        const val StateWaitTake = 1
        const val StateWaitPay = 2
        const val StateSending = 3
        const val StateFinish = 4
    }
}