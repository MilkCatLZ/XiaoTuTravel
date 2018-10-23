package shy.car.sdk.travel.rent.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import shy.car.sdk.R
import shy.car.sdk.databinding.ItemNearInfoWindowBinding

class NearInfoWindowAdapter(var context: Context) : AMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    override fun getInfoWindow(p0: Marker?): View {
        var binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_near_info_window, null, false) as ItemNearInfoWindowBinding
        binding.name = p0?.snippet
        return binding.root
    }
}