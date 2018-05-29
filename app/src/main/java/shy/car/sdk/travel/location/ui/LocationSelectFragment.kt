package shy.car.sdk.travel.location.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.PoiItem
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import com.base.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentLocationSelectBinding
import shy.car.sdk.travel.location.LocationPresenter
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.location.presenter.LocationSelectPresenter
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter
import java.util.concurrent.TimeUnit

/**
 * create by LZ at 2018/05/28
 * 选择地址
 */
class LocationSelectFragment : XTBaseFragment(), LocationSelectPresenter.CallBack {
    override fun onAddressClick(poiItem: PoiItem) {
        moveCameraAndShowLocation(poiItem)
    }

    lateinit var binding: FragmentLocationSelectBinding
    lateinit var bitmap: BitmapDescriptor

    lateinit var presenter: LocationSelectPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = LocationSelectPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_select, null, false)
        binding.mapLocationSelect.onCreate(savedInstanceState)
        binding.presenter = presenter
        binding.recyclerViewLocationSelect.enableDefaultSwipeRefresh(false)
        binding.recyclerViewLocationSelect.isEnabled = false
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        initEdit()
        refreshLocation()
    }

    private fun initEdit() {
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null)
                    presenter.getAddressList(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun initMap() {
        binding.mapLocationSelect.map.animateCamera(CameraUpdateFactory.zoomTo(10f), 1000, null)
        activity?.let { binding.mapLocationSelect.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
    }

    var location = CurrentLocation()
    /**
     * 刷新定位
     */
    private fun refreshLocation() {
        binding.mapLocationSelect.map.clear()
        Observable.create<CurrentLocation>({
            AmapLocationManager.getInstance()
                    .getLocation(object : AmapOnLocationReceiveListener {
                        override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                            moveCameraAndShowLocation(location)
                        }
                    })
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    addMarkersToMap()
                })

    }

    private fun moveCameraAndShowLocation(it: Any) {
        when (it) {
            is PoiItem -> this.location.copy(it)
            is Location -> this.location.copy(it)
        }

        binding.mapLocationSelect.map.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(location.lat, location.lng)))
        binding.mapLocationSelect.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_locat))
                .anchor(0.5f, 1.0f)
                .snippet(location.address)
                .position(LatLng(location.lat, location.lng))
                .draggable(false))
    }

    /**
     * 在地图上添加marker
     */
    private fun addMarkersToMap() {
        binding.mapLocationSelect.map.clear()
        val marker = binding.mapLocationSelect.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
                .anchor(0.5f, 1.0f)
                .snippet("当前位置")
                .snippet(location.address)
                .position(LatLng(location.lat, location.lng))
                .draggable(false))
        marker.showInfoWindow()

    }

    fun onConfirmClick() {
        eventBusDefault.post(location)
        finish()
    }


    override fun onDestroy() {
        binding.mapLocationSelect.onDestroy()
        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
        binding.mapLocationSelect.onResume()
    }


    override fun onPause() {
        super.onPause()
        binding.mapLocationSelect.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapLocationSelect.onSaveInstanceState(outState)
    }
}