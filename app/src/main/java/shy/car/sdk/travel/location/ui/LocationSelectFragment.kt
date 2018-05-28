package shy.car.sdk.travel.location.ui

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentLocationSelectBinding
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter

/**
 * create by LZ at 2018/05/28
 * 选择地址
 */
class LocationSelectFragment : XTBaseFragment() {

    lateinit var binding: FragmentLocationSelectBinding
    lateinit var bitmap: Bitmap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_select, null, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        refreshLocation()
    }

    private fun initMap() {
        var bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat)
        var myLocationStyle = MyLocationStyle().myLocationIcon(bitmap)
                .anchor(0.5f, 0.5f)
        binding.mapLocationSelect.map.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)

        binding.mapLocationSelect.map.myLocationStyle = myLocationStyle
        activity?.let { binding.mapLocationSelect.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }


    }

    var location = CurrentLocation()
    /**
     * 刷新定位
     */
    private fun refreshLocation() {
        binding.mapLocationSelect.map.clear()
        Observable.create<CurrentLocation>({
            AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
                override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                    this@LocationSelectFragment.location.copy(location)
                }
            })
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap {
//                    AmapLocationManager.instance.getAddress(it.lat, it.lng)
//                }
//                .doOnNext({
//                                        address = it?.regeocodeAddress?.toString()!!
//                })
                .subscribe({
                    addMarkersToMap()
                })


    }

    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap() {
        binding.mapLocationSelect.map.clear()
        var marker = binding.mapLocationSelect.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .snippet("当前位置")
                .snippet(location.address)
                .position(LatLng(location.lat, location.lng))
                .draggable(false))
        marker.showInfoWindow()

    }

    fun onConfirmClick() {
        eventBusDefault.post(location)
    }
}