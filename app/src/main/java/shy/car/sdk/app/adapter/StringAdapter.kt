package shy.car.sdk.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wx.wheelview.adapter.BaseWheelAdapter
import shy.car.sdk.R
import shy.car.sdk.travel.send.dialog.CommonWheelItem

/**
 * create by LZ at 2018/05/28
 */
class StringAdater(var context: Context) : BaseWheelAdapter<CommonWheelItem>() {

    override fun bindView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_common_wheel_view_item, parent, false)
        }
        val textView = convertView!!.findViewById<TextView>(R.id.txt_title)
        textView.text = mList[position].name
        return convertView
    }

    fun addList(list: List<CommonWheelItem>) {
        mList=list
    }
}