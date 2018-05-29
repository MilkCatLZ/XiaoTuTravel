package shy.car.sdk.travel.common.data

import com.contrarywind.interfaces.IPickerViewData
import shy.car.sdk.travel.send.interfaces.WheelItemInerface

class CommonWheelItem : WheelItemInerface, IPickerViewData {
    override fun getPickerViewText(): String {
        return getTitle()
    }

    override fun getTitle(): String {
        return name
    }

    var name: String = ""
    var id: String = ""
}