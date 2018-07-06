package shy.car.sdk.travel.invoice.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.invoice.data.InvoiceList

/**
 * create by LZ at 2018/07/06
 * 提交开发票申请
 */
@Route(path = RouteMap.InvoicePost)
class InvoicePostActivity : XTBaseActivity() {

    @Autowired(name = Object1)
    @JvmField
    var resource = ArrayList<InvoiceList.Orders>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_post)

        ARouter.getInstance()
                .inject(this)


        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_invoice_post) as InvoicePostFragment
//        fragment.list = Gson().fromJson(resource, TypeToken<ArrayList<InvoiceList.Orders>>())
        fragment.list = resource
//        fragment.list = Gson().fromJson(resource, object : TypeToken<ArrayList<InvoiceList.Orders>>() {}.type)
        Log.d("sdkljflksjdlkfjsldkfj", fragment.list.size.toString())
    }
}