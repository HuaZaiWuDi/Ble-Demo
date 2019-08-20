package lab.wesmartclothing.wefit.flyso.ui.ems

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.orhanobut.logger.Logger
import com.vondear.rxtools.model.timer.MyTimer
import com.vondear.rxtools.utils.RxTextUtils
import com.vondear.rxtools.view.RxToast
import com.wesmarclothing.mylibrary.net.RxBus
import kotlinx.android.synthetic.flavors_wesmart.dialog_ems_control.*
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.ble.DeviceInfo
import lab.wesmartclothing.wefit.flyso.ble.EMSApi
import lab.wesmartclothing.wefit.flyso.ble.EMSManager
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils


/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.ems
 * @FileName EmsDialogFragment
 * @Date 2019/8/15 10:50
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
class EmsDialogFragment : DialogFragment() {

    private var index = -1
    private var gear = 1
    private var min = 30
    private var play = false
    private var pause = false
    private val timer: MyTimer by lazy {
        MyTimer(3000, 10_000) {
            EMSApi.getNotifyData()
        }
    }

    companion object {
        fun newInstance(): EmsDialogFragment {
            val args = Bundle()
            val fragment = EmsDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        val params = dialog.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_ems_control, container)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mbtn_ranzhi.setOnClickListener(clickListener)
        mbtn_zengji.setOnClickListener(clickListener)
        mbtn_jingmai.setOnClickListener(clickListener)
        mbtn_huifu.setOnClickListener(clickListener)
        iv_close.setOnClickListener(clickListener)
        iv_mode_add.setOnClickListener(clickListener)
        iv_mode_reduce.setOnClickListener(clickListener)
        iv_min_add.setOnClickListener(clickListener)
        iv_min_reduce.setOnClickListener(clickListener)
        iv_save.setOnClickListener(clickListener)
        iv_pause.setOnClickListener(clickListener)
        iv_end.setOnClickListener(clickListener)

        tv_mode.typeface = MyAPP.typeface
        tv_min.typeface = MyAPP.typeface

        tv_mode.text = RxTextUtils.getBuilder("$gear")
                .append("\t档").setProportion(0.5f)
                .create()
        tv_min.text = RxTextUtils.getBuilder("$min")
                .append("\t分").setProportion(0.5f)
                .create()

        initRxBus()
        EMSApi.getNotifyData()
    }


    override fun onStop() {
        super.onStop()
        Logger.d("stop")
        timer.stopTimer()
    }

    @SuppressLint("CheckResult")
    private fun initRxBus() {
        RxBus.getInstance().register2(DeviceInfo::class.java)
                .compose(RxComposeUtils.bindLife(this))
                .subscribe {
                    initBtnState(it.model)
                    gear = it.strength
                    min = it.min

                    tv_mode.text = RxTextUtils.getBuilder("$gear")
                            .append("\t档").setProportion(0.5f)
                            .create()
                    tv_min.text = RxTextUtils.getBuilder("$min")
                            .append("\t分").setProportion(0.5f)
                            .create()

                    if (!play && gear != 0) {
                        startSport()
                    } else if (gear == 0 && !pause) {
                        pauseSport()
                    }
                }
    }


    private fun stopSport() {
        iv_save.visibility = View.VISIBLE
        iv_end.visibility = View.GONE
        iv_pause.visibility = View.GONE
        play = false
        pause = false

        EMSApi.setupStop()
        EMSApi.setupModel(0)
        EMSApi.getNotifyData()
        timer.stopTimer()
    }

    private fun pauseSport() {
        play = false
        pause = true
        EMSApi.setupPause()
        EMSApi.getNotifyData()
        timer.stopTimer()
    }

    private fun startSport() {
        play = true
        pause = false
        if (gear == 0) {
            EMSApi.setupStrength(true)
        }
        EMSApi.setupStart()
        EMSApi.getNotifyData()
        timer.startTimer()

        iv_end.visibility = View.VISIBLE
        iv_pause.visibility = View.VISIBLE
        iv_save.visibility = View.GONE
    }


    private fun initBtnState(int: Int) {
        index = int
        mbtn_ranzhi.setBackgroundResource(R.color.Gray_ECEBF0)
        mbtn_ranzhi.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.GrayWrite))
        mbtn_zengji.setBackgroundResource(R.color.Gray_ECEBF0)
        mbtn_zengji.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.GrayWrite))
        mbtn_jingmai.setBackgroundResource(R.color.Gray_ECEBF0)
        mbtn_jingmai.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.GrayWrite))
        mbtn_huifu.setBackgroundResource(R.color.Gray_ECEBF0)
        mbtn_huifu.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.GrayWrite))
        when (int) {
            0 -> {
                mbtn_ranzhi.setBackgroundResource(R.color.Gray)
                mbtn_ranzhi.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.white))
            }
            1 -> {
                mbtn_zengji.setBackgroundResource(R.color.Gray)
                mbtn_zengji.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.white))
            }
            2 -> {
                mbtn_jingmai.setBackgroundResource(R.color.Gray)
                mbtn_jingmai.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.white))
            }
            3 -> {
                mbtn_huifu.setBackgroundResource(R.color.Gray)
                mbtn_huifu.setTextColor(ContextCompat.getColor(mbtn_ranzhi.context, R.color.white))
            }
        }
    }


    private val clickListener = View.OnClickListener {
        when (it.id) {
            R.id.mbtn_ranzhi -> {
                state {
                    EMSApi.setupModel(0)
                    EMSApi.getNotifyData()
                }
            }
            R.id.mbtn_zengji -> {
                state {
                    EMSApi.setupModel(1)
                    EMSApi.getNotifyData()
                }
            }
            R.id.mbtn_jingmai -> {
                state {
                    EMSApi.setupModel(2)
                    EMSApi.getNotifyData()
                }
            }
            R.id.mbtn_huifu -> {
                state {
                    EMSApi.setupModel(3)
                    EMSApi.getNotifyData()
                }
            }
            R.id.iv_close -> {
                dismiss()
            }
            R.id.iv_mode_add -> {
                state {
                    if (gear < 15) {
                        EMSApi.setupStrength(true)
                        EMSApi.getNotifyData()
                    }
                }
            }
            R.id.iv_mode_reduce -> {
                if (gear > 0) {
                    EMSApi.setupStrength(false)
                    EMSApi.getNotifyData()
                }
            }
            R.id.iv_min_add -> {
                if (min < 30) {
                    EMSApi.setupDuration(min)
                    EMSApi.getNotifyData()
                }
            }
            R.id.iv_min_reduce -> {
                if (min > 1) {
                    EMSApi.setupDuration(min)
                    EMSApi.getNotifyData()
                }
            }
            R.id.iv_save -> {
                startSport()
            }
            R.id.iv_pause -> {
                pauseSport()
            }
            R.id.iv_end -> {
                stopSport()
            }
        }
    }

    private fun state(block: () -> Unit) {
        if (EMSManager.instance.isConnect()) {
//            if (play) {
            block()
//            } else {
//                RxToast.warning("请先点击开始")
//            }
        } else {
            RxToast.warning("蓝牙未连接")
        }
    }

}