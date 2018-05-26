package shy.car.sdk.travel.send.adapter

import com.contrarywind.adapter.WheelAdapter
import shy.car.sdk.travel.send.interfaces.WheelItemInerface

class WheelAdapter : WheelAdapter<WheelItemInerface> {

    var list = ArrayList<WheelItemInerface>()

    override fun indexOf(o: WheelItemInerface?): Int {
        return list.indexOf(o)
    }

    override fun getItemsCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): WheelItemInerface {
        return list[index]
    }
}