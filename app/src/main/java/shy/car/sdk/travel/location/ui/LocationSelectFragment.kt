package shy.car.sdk.travel.location.ui

import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.PoiItem
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentLocationSelectBinding
import shy.car.sdk.travel.location.data.CurrentLocation
import shy.car.sdk.travel.location.presenter.LocationSelectPresenter
import shy.car.sdk.travel.rent.adapter.NearInfoWindowAdapter

/**
 * create by LZ at 2018/05/28
 * 选择地址
 */
class LocationSelectFragment : XTBaseFragment(),
        LocationSelectPresenter.CallBack {

    lateinit var binding: FragmentLocationSelectBinding

    lateinit var presenter: LocationSelectPresenter
    val isResultVisible = ObservableBoolean(false)
    val address = ObservableField<String>("")
    var location = CurrentLocation()

    override fun getPoiListSuccess() {
        isResultVisible.set(true)
    }

    override fun onAddressClick(poiItem: PoiItem) {
        moveCameraAndShowLocation(poiItem)
        isResultVisible.set(false)
    }

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
        binding.fragment = this
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

    var disposable: Disposable? = null
    private fun initMap() {
        binding.mapLocationSelect.map.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null)
        activity?.let { binding.mapLocationSelect.map.setInfoWindowAdapter(NearInfoWindowAdapter(it)) }
        binding.mapLocationSelect.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(p0: CameraPosition?) {
                getAddresss(p0?.target?.latitude, p0?.target?.longitude)
            }

            override fun onCameraChange(p0: CameraPosition?) {

            }
        })
    }

    private fun getAddresss(latitude: Double?, longitude: Double?) {
        AmapLocationManager.getInstance()
                .getAddress(latitude!!, longitude!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    disposable?.dispose()
                    disposable = it
                }
                .subscribe({
                    val location = Location()
                    location.latitude = it.regeocodeQuery.point.latitude
                    location.longitude = it.regeocodeQuery.point.longitude
                    location.address = it.regeocodeAddress.formatAddress
                    location.city = it.regeocodeAddress.city
                    location.district = it.regeocodeAddress.district
                    this@LocationSelectFragment.location.copy(location)
                    this@LocationSelectFragment.address.set(this@LocationSelectFragment.location.address)
                }, {})
    }

    /**
     * 刷新定位
     */
    private fun refreshLocation() {
        Observable.create<CurrentLocation> {
            while (this.location.lat == 0.0) {
                AmapLocationManager.getInstance()
                        .getLocation(object : AmapOnLocationReceiveListener {
                            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                                this@LocationSelectFragment.location.copy(location)
                                it.onNext(this@LocationSelectFragment.location)
                            }
                        })
                Thread.sleep(3000)
            }

        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    moveCameraAndShowLocation(location)
                    getAddresss(location.lat, location.lng)
                }, {

                })


    }

    private fun moveCameraAndShowLocation(it: Any) {
        when (it) {
            is PoiItem -> this.location.copy(it)
            is Location -> this.location.copy(it)
        }
        binding.mapLocationSelect.map.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(location.lat, location.lng)))
        this.address.set(this.location.address)
    }

//    /**
//     * 在地图上添加marker
//     */
//    private fun addMarkersToMap() {
//        binding.mapLocationSelect.map.mapScreenMarkers.map {
//            it.remove()
//        }
//        binding.mapLocationSelect.map.mapScreenMarkers.clear()
//        val marker = binding.mapLocationSelect.map.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_defaul_label))
//                .anchor(0.5f, 1.0f)
//                .title("当前位置")
//                .snippet(location.address)
//                .position(LatLng(location.lat, location.lng))
//                .draggable(false))
//        marker.showInfoWindow()
//
//    }

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