package shy.car.sdk.travel.remain.presenter

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.base.base.ProgressDialog
import com.base.databinding.DataBindingItemClickAdapter
import com.google.gson.JsonObject
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import shy.car.sdk.BR
import shy.car.sdk.BuildConfig
import shy.car.sdk.R
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.data.ErrorManager
import shy.car.sdk.app.net.ApiManager
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.remain.data.QAList

/**
 * create by LZ at 2018/06/13
 * 我的余额
 */
class QAPresenter(context: Context) : BasePresenter(context) {


    var adapter = DataBindingItemClickAdapter<QAList>(R.layout.item_qa, BR.list, BR.click) {
        val list = it.tag as QAList
        ARouter.getInstance()
                .build(RouteMap.QADetail)
                .withString(String1, BuildConfig.Host+"questions/${list.id}")
                .navigation()
//        getQADetail(list.id)
    }

    fun getQAList() {
        var observable = ApiManager.getInstance()
                .api.getQAList()
        var observer = object : Observer<List<QAList>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(list: List<QAList>) {
                adapter.setItems(list, 1)

            }

            override fun onError(e: Throwable) {
                ErrorManager.managerError(context, e, null)

            }

        }

        ApiManager.getInstance()
                .toSubscribe(observable, observer)
    }
//
//    fun getQADetail(id: String) {
//        ProgressDialog.showLoadingView(context)
//        var observable = ApiManager.getInstance()
//                .api.getQADetail(id)
//        var observer = object : Observer<JsonObject> {
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//
//            }
//
//            override fun onNext(o: JsonObject) {
//                ProgressDialog.hideLoadingView(context)
//                val url = o.get("url")
//                        .asString
//                ARouter.getInstance()
//                        .build(RouteMap.QADetail)
//                        .withString(String1, url)
//                        .navigation()
//            }
//
//            override fun onError(e: Throwable) {
//                ProgressDialog.hideLoadingView(context)
//                ErrorManager.managerError(context, e, null)
//
//            }
//
//        }
//
//        ApiManager.getInstance()
//                .toSubscribe(observable, observer)
//    }


}