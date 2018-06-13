package shy.car.sdk.travel.user.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.base.base.ProgressDialog
import com.wq.photo.widget.PickConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentUserInfoEditBinding
import shy.car.sdk.travel.user.data.RefreshUserInfo
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
        eventBusDefault.post(RefreshUserInfo())
    }

    override fun onUploadAvatar() {
        activity?.let { ProgressDialog.hideLoadingView(it) }
    }

    lateinit var binding: FragmentUserInfoEditBinding
    lateinit var presenter: UserDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = UserDetailPresenter(it, this)
        }
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
            birthDayView = DatePickerDialog(it, DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                presenter.birthDay.set("$p1${p2 + 1}$p3")
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
    }

    private fun initSpinner() {
        binding.spinnerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                presenter.sex = p2
            }
        }

        binding.spinnerJob.adapter = presenter.jobAdapter
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                activity?.let{ProgressDialog.showLoadingView(it)}
                presenter.uploadAvatar(imgs[0])
            }
        }
    }
}