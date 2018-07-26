package shy.car.sdk.travel.home.ui

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import shy.car.sdk.MainActivity
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import java.util.concurrent.TimeUnit

class StartActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        Observable.timer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startActivity(MainActivity::class.java)
                    finish()
                }, {})
    }
}