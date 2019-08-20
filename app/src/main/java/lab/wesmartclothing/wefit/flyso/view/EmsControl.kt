package lab.wesmartclothing.wefit.flyso.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kongzue.dialog.v2.CustomDialog
import com.vondear.rxtools.view.RxToast
import com.wesmarclothing.mylibrary.net.RxBus
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.ble.DeviceInfo
import lab.wesmartclothing.wefit.flyso.ble.EMSApi
import lab.wesmartclothing.wefit.flyso.ble.EMSManager

/**
 * @Package lab.wesmartclothing.wefit.flyso.view
 * @FileName EmsControl
 * @Date 2019/8/15 10:12
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
class EmsControl {

    private lateinit var btn_ranzhi: TextView
    private lateinit var mbtn_zengji: TextView
    private lateinit var mbtn_jingmai: TextView
    private lateinit var mbtn_huifu: TextView
    private lateinit var iv_save: ImageView
    private lateinit var iv_pause: ImageView
    private lateinit var iv_end: ImageView
    private lateinit var dialog: CustomDialog
    private lateinit var tv_mode: TextView
    private lateinit var tv_min: TextView
    private var index = -1
    private var gear = 1
    private var min = 30
    private var play = false
    private var pause = false

    fun showDialog(context: Context) {



        val layout = context.resources.getIdentifier("dialog_ems_control", "layout", context.packageName)
        dialog = CustomDialog.show(context, R.layout.dialog_ems_control) { dialog, rootView ->
            btn_ranzhi = rootView.findViewById<TextView>(R.id.mbtn_ranzhi)
                    .apply {
                        setOnClickListener(clickListener)
                    }
            mbtn_zengji = rootView.findViewById<TextView>(R.id.mbtn_zengji)
                    .apply {
                        setOnClickListener(clickListener)
                    }
            mbtn_jingmai = rootView.findViewById<TextView>(R.id.mbtn_jingmai)
                    .apply {
                        setOnClickListener(clickListener)
                    }
            mbtn_huifu = rootView.findViewById<TextView>(R.id.mbtn_huifu)
                    .apply {
                        setOnClickListener(clickListener)
                    }

            rootView.findViewById<ImageView>(R.id.iv_close)
                    .setOnClickListener(clickListener)
            rootView.findViewById<ImageView>(R.id.iv_mode_add)
                    .setOnClickListener(clickListener)
            rootView.findViewById<ImageView>(R.id.iv_mode_reduce)
                    .setOnClickListener(clickListener)
            rootView.findViewById<ImageView>(R.id.iv_min_add)
                    .setOnClickListener(clickListener)
            rootView.findViewById<ImageView>(R.id.iv_min_reduce)
                    .setOnClickListener(clickListener)
            iv_save = rootView.findViewById<ImageView>(R.id.iv_save)
                    .apply {
                        setOnClickListener(clickListener)
                    }
            iv_pause = rootView.findViewById<ImageView>(R.id.iv_pause)
                    .apply {
                        setOnClickListener(clickListener)
                    }
            iv_end = rootView.findViewById<ImageView>(R.id.iv_end)
                    .apply {
                        setOnClickListener(clickListener)
                    }

            tv_mode = rootView.findViewById(R.id.tv_mode)
            tv_min = rootView.findViewById(R.id.tv_min)
        }




        RxBus.getInstance().register2(DeviceInfo::class.java)
                .filter {
                    dialog.isDialogShown
                }
                .subscribe {
                    initBtnState(it.model)
                    gear = it.strength
                    tv_mode.text = "${gear} 档"
                    min = it.min
                    tv_min.text = "$min min"

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
    }

    private fun pauseSport() {
        play = false
        pause = true
        EMSApi.setupPause()
    }

    private fun startSport() {
        play = true
        pause = false
        if (gear == 0) {
            EMSApi.setupStrength(true)
        }
        EMSApi.setupStart()
        EMSApi.getNotifyData()
        iv_end.visibility = View.VISIBLE
        iv_pause.visibility = View.VISIBLE
    }


    private fun initBtnState(int: Int) {
        index = int
        btn_ranzhi.setBackgroundResource(R.color.GrayWrite)
        btn_ranzhi.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.GrayWrite))
        mbtn_zengji.setBackgroundResource(R.color.GrayWrite)
        mbtn_zengji.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.GrayWrite))
        mbtn_jingmai.setBackgroundResource(R.color.GrayWrite)
        mbtn_jingmai.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.GrayWrite))
        mbtn_huifu.setBackgroundResource(R.color.GrayWrite)
        mbtn_huifu.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.GrayWrite))
        when (int) {
            0 -> {
                mbtn_zengji.setBackgroundResource(R.color.Gray)
                mbtn_zengji.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.white))
            }
            1 -> {
                btn_ranzhi.setBackgroundResource(R.color.Gray)
                btn_ranzhi.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.white))
            }
            2 -> {
                mbtn_jingmai.setBackgroundResource(R.color.Gray)
                mbtn_jingmai.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.white))
            }
            3 -> {
                mbtn_huifu.setBackgroundResource(R.color.Gray)
                mbtn_huifu.setTextColor(ContextCompat.getColor(btn_ranzhi.context, R.color.white))
            }
        }
    }


    private val clickListener = View.OnClickListener {
        when (it.id) {
            R.id.mbtn_ranzhi -> {
                state {
                    EMSApi.setupModel(0)
                }
            }
            R.id.mbtn_zengji -> {
                state {
                    EMSApi.setupModel(1)
                }
            }
            R.id.mbtn_jingmai -> {
                state {
                    EMSApi.setupModel(2)
                }
            }
            R.id.mbtn_huifu -> {
                state {
                    EMSApi.setupModel(3)
                }
            }
            R.id.iv_close -> {
                dialog.doDismiss()
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
            if (play) {
                block()
            } else {
                RxToast.warning("请先点击开始")
            }
        } else {
            RxToast.warning("蓝牙未连接")
        }
    }
}