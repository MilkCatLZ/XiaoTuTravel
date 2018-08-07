package com.base.util

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.location.R
import kotlinx.android.synthetic.main.dialog_map_select.*


class MapSelectDialogFragment : DialogFragment() {

    var startLongitude = 0.0
    var startLatitude = 0.0
    var endLongitude = 0.0
    var endLatitude = 0.0
    var address = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_map_select, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_baidu.setOnClickListener {
            gotoBaidu()
        }

        txt_gaode.setOnClickListener {
            gotoGaoDe()
        }
    }

    private fun gotoGaoDe() {
        if (isPackageInstalled("com.autonavi.minimap")) {
            val i1 = Intent()
            i1.data = Uri.parse("amapuri://route/plan/?sourceApplication=lnapp&slat=$startLatitude&slon=$startLongitude&dlat=$endLatitude&dlon=$endLongitude&dname=$address&&dev=0&t=0")
            startActivity(i1)
        } else {
            activity?.let { ToastManager.showShortToast(it, "请先安装高德地图") }
        }
        dismissAllowingStateLoss()
    }

    private fun gotoBaidu() {
        if (isPackageInstalled("com.baidu.BaiduMap")) {
            activity?.let {
                val startLatLong = gaoDeToBaidu(startLatitude, startLongitude)
                val endLatLong = gaoDeToBaidu(endLatitude, endLongitude)

                val i1 = Intent()
                i1.data = Uri.parse("baidumap://map/direction?&origin=${startLatLong[0]},${startLatLong[1]}&destination=${endLatLong[0]},${endLatLong[1]}&mode=driving")
                startActivity(i1)
            }
        } else {
            activity?.let { ToastManager.showShortToast(it, "请先安装百度地图") }
        }

        dismissAllowingStateLoss()
    }


    private fun gaoDeToBaidu(gd_lon: Double, gd_lat: Double): DoubleArray {
        val bd_lat_lon = DoubleArray(2)
        val PI = 3.14159265358979324 * 3000.0 / 180.0
        val z = Math.sqrt(gd_lon * gd_lon + gd_lat * gd_lat) + 0.00002 * Math.sin(gd_lat * PI)
        val theta = Math.atan2(gd_lat, gd_lon) + 0.000003 * Math.cos(gd_lon * PI)
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006
        return bd_lat_lon

    }

    fun isPackageInstalled(packagename: String): Boolean {

        activity?.let {
            var packageInfo: PackageInfo? = null
            try {
                packageInfo = it.getPackageManager()
                        .getPackageInfo(packagename, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                packageInfo = null
                e.printStackTrace()
            } finally {
                return packageInfo != null
            }
        }
        return false
    }


}