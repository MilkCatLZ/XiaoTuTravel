package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.databinding.ActivityCarBrokeUploadBinding


/**
 * create by LZ at 2018/07/02
 *
 * 故障上报
 */
class CarBrokeUploadActivity : XTBaseActivity() {
    lateinit var binding: ActivityCarBrokeUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_broke_upload)
    }
}