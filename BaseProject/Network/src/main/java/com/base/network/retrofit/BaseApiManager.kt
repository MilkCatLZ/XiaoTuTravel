package com.base.net

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 管理类
 */
open class BaseApiManager<T>(baseUrl: String, interceptor: BaseInterceptor? = null, var listener: BaseRetrofitInterface? = null, c: Class<T>) {
    var api: T
    var builder = OkHttpClient.Builder()
    var client: OkHttpClient
    var retrofit: Retrofit
    var cookieJar: CookieJar? = null

    init {
        //初始化拦截器
        if (interceptor != null) {
            builder.addInterceptor(interceptor)
        }
        builder.addInterceptor(HttpLoggingInterceptor())
        if (cookieJar != null) {
            builder.cookieJar(cookieJar)
        }
        client = builder.build()


        //初始化retrofit
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //生成api
        api = retrofit.create(c)
    }

    protected fun getApiService(): T {
        return api
    }

    //正式订阅
    open fun <T> toSubscribe(o: Observable<T>, s: Observer<T>?) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<T> {
                    override fun onComplete() {
                        s?.onComplete()
                    }

                    override fun onSubscribe(d: Disposable) {
                        s?.onSubscribe(d)
                    }

                    override fun onNext(t: T) {
                        s?.onNext(t)
                    }

                    override fun onError(e: Throwable) {
                        try {
                            if (listener != null) {
                                listener!!.onError(e)
                            }
                        } catch (e: Exception) {
                        }
                        s?.onError(e)
                    }
                })
    }

}

