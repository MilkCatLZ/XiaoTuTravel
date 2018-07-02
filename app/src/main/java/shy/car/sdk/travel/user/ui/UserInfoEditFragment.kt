package shy.car.sdk.travel.user.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.base.base.ProgressDialog
import com.base.util.ImageUtil
import com.base.util.ToastManager
import com.wq.photo.widget.PickConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_user_info_edit.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.eventbus.RefreshUserInfo
import shy.car.sdk.databinding.FragmentUserInfoEditBinding
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.presenter.UserDetailPresenter
import java.util.*

/**
 * create by lz at 2018/06/11
 *
 */
class UserInfoEditFragment : XTBaseFragment(),
        UserDetailPresenter.UserEditListener {
    override fun onUploadAvatarSuccess() {
        activity?.let { ProgressDialog.hideLoadingView(it) }
        activity?.let {
            ToastManager.showShortToast(it, "修改成功")
        }
        finish()
    }

    override fun onUploadAvatarError() {
        activity?.let { ProgressDialog.hideLoadingView(it) }
    }

    lateinit var binding: FragmentUserInfoEditBinding
    lateinit var presenter: UserDetailPresenter
    lateinit var cities: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = UserDetailPresenter(it, this)
        }
        cities = resources.getStringArray(R.array.city_array)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_edit, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
        initCalendarView()
    }

    private fun initCalendarView() {
        val calendar = Calendar.getInstance()

        activity?.let {
            birthDayView = DatePickerDialog(it, DatePickerDialog.OnDateSetListener { p0, year, month, monOfDay ->
                var s = "$year-${month + 1}-$monOfDay"
                presenter.birthDay.set(s)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
    }

    private fun initSpinner() {
        binding.spinnerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter.sex = p2
                presenter.sexText.set(presenter.sexAdapter.items[p2].key)
            }
        }
        binding.spinnerSex.adapter = presenter.sexAdapter
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.city = cities[position]
            }
        }

        cities.forEachIndexed { index, value ->
            if (User.instance.city == value) {
                binding.spinnerCity.setSelection(index)
                presenter.city = value
            }
        }
    }

    lateinit var birthDayView: DatePickerDialog

    fun onBirthDayClick() {
        birthDayView.show()
    }

    fun onAvatarClick() {
        activity?.let {
            PickConfig.with(it)
                    .pickMode(PickConfig.MODE_SINGLE_PICK)
                    .isneedcamera(true)
                    .isneedactionbar(true)
                    .isneedcrop(true)
                    .start()
        }
    }

    fun upload() {
        presenter.upload()
    }

    fun sexClick() {
        binding.spinnerSex.performClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                Observable.create<String> {
                    val path = ImageUtil.saveBitmapToSD(ImageUtil.compressImage(BitmapFactory.decodeFile(imgs[0]), 350), Environment.getExternalStorageDirectory().absolutePath + "/cache")
                    it.onNext(path)
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            activity?.let {
                                ProgressDialog.hideLoadingView(it)
                            }
                            presenter.avatar.set(it)

                        }, {
                            activity?.let {
                                ProgressDialog.hideLoadingView(it)
                            }
                            it.printStackTrace()
                        })

            }
        }
    }
}