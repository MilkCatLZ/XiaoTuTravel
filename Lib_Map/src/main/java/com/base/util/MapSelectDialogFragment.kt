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
import com.amap.api.maps.CoordinateConverter
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
        txt_baidu.setOnClickListener({
            gotoBaidu()
        })

        txt_gaode.setOnClickListener({
            gotoGaoDe()
        })
    }

    private fun gotoGaoDe() {
        if (isPackageInstalled("com.autonavi.minimap")) {
            val i1 = Intent()
            i1.data = Uri.parse("amapuri://route/plan/?slat=$startLatitude&slon=$startLongitude&dlat=$endLatitude&dlon=$endLongitude&dev=0&t=0")
            startActivity(i1)
        } else {
            activity?.let { ToastManager.showShortToast(it, "请先安装高德地图") }
        }
        dismissAllowingStateLoss()
    }

    private fun gotoBaidu() {
        if (isPackageInstalled("com.baidu.BaiduMap")) {
            activity?.let {
                val converter = CoordinateConverter(it)
// CoordType.GPS 待转换坐标类型
                converter.from(CoordinateConverter.CoordType.GPS)
// sourceLatLng待转换坐标点 LatLng类型
                converter.coord(sourceLatLng)
// 执行转换操作
                val desLatLng = converter.convert()

                val i1 = Intent()
                i1.data = Uri.parse("baidumap://map/direction?&origin=$startLatitude,$startLongitude&destination=latlng:$endLatitude,$endLongitude&mode=driving")
                startActivity(i1)
            }
        } else {
            activity?.let { ToastManager.showShortToast(it, "请先安装百度地图") }
        }

        dismissAllowingStateLoss()
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