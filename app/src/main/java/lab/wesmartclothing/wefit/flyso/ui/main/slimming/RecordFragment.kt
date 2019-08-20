package lab.wesmartclothing.wefit.flyso.ui.main.slimming

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.alibaba.fastjson.JSON
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.vondear.rxtools.activity.RxActivityUtils
import com.vondear.rxtools.model.antishake.AntiShake
import com.vondear.rxtools.model.tool.RxQRCode
import com.vondear.rxtools.utils.*
import com.vondear.rxtools.utils.bitmap.RxImageUtils
import com.vondear.rxtools.utils.dateUtils.RxFormat
import com.vondear.rxtools.view.RxToast
import com.vondear.rxtools.view.waveview.RxWaveHelper
import com.wesmarclothing.kotlintools.kotlin.utils.color
import com.wesmarclothing.kotlintools.kotlin.utils.drawable
import com.wesmarclothing.mylibrary.net.RxBus
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.CacheStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_slimming_record.*
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.BaseAcFragment
import lab.wesmartclothing.wefit.flyso.base.BaseShareActivity
import lab.wesmartclothing.wefit.flyso.base.MyAPP
import lab.wesmartclothing.wefit.flyso.entity.DataListBean
import lab.wesmartclothing.wefit.flyso.entity.SlimmingRecordBean
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager
import lab.wesmartclothing.wefit.flyso.netutil.net.RxManager
import lab.wesmartclothing.wefit.flyso.netutil.net.ServiceAPI
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxSubscriber
import lab.wesmartclothing.wefit.flyso.rxbus.RefreshSlimming
import lab.wesmartclothing.wefit.flyso.ui.ems.EmsDialogFragment
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.energy.EnergyActivity
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat.DietRecordActivity
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.plan.PlanWebActivity
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.sports.SmartClothingFragment
import lab.wesmartclothing.wefit.flyso.ui.main.slimming.weight.WeightRecordFragment
import lab.wesmartclothing.wefit.flyso.utils.EnergyUtil
import lab.wesmartclothing.wefit.flyso.utils.GlideImageLoader
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils
import lab.wesmartclothing.wefit.flyso.utils.setMyTypeface
import java.util.*

/**
 * @Package lab.wesmartclothing.wefit.flyso.ui.main.slimming
 * @FileName RecordFragment
 * @Date 2019/7/30 15:59
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
class RecordFragment : BaseAcFragment() {

    companion object {
        fun newInstance(): RecordFragment {
            val args = Bundle()
            val fragment = RecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mWaveHelper: RxWaveHelper? = null
    private val dates = ArrayList<String>(7)
    private val recommendList = ArrayList<Int>(7)
    private val item = ArrayList<Int>(7)

    override fun layoutId(): Int {
        return R.layout.fragment_slimming_record
    }

    override fun initViews() {
        super.initViews()
        initTextTypeface()
        initWareView()
        initChart(mLineChart)
        initShare()
        initClick()



    }

    private fun initWareView() {
        pro_complete.apply {
            setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite))
            setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"))
            isShowWave = true
        }

        pro_complete2.apply {
            setBorder(1, ContextCompat.getColor(mContext, R.color.GrayWrite))
            setWaveColor(Color.parseColor("#7DB8FFDC"), Color.parseColor("#FFB8FFDC"))
            setWaterLevelRatio(0f)
        }
    }

    private fun initTextTypeface() {
        tv_continuousRecord.setMyTypeface()
        tv_continuousRecord2.setMyTypeface()
        tv_progress.setMyTypeface()
        tv_progress2.setMyTypeface()
        tv_weightInfo.setMyTypeface()
        tv_healthScore.setMyTypeface()
        tv_currentWeight.setMyTypeface()
        tv_currentDiet.setMyTypeface()
        tv_currentKcal.setMyTypeface()
        tv_currentEnergy.setMyTypeface()
    }


    override fun initRxBus() {
        super.initRxBus()

        RxBus.getInstance().register2(RefreshSlimming::class.java)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(object : RxSubscriber<RefreshSlimming>() {
                    override fun _onNext(refreshSlimming: RefreshSlimming) {
                        initNetData()
                    }
                })
    }

    override fun initNetData() {
        super.initNetData()
        initUserInfo()
        getData()
    }

    private fun initUserInfo() {
        GlideImageLoader.displayImage(mActivity, MyAPP.gUserInfo?.imgUrl
                ?: "", R.mipmap.userimg, img_userImg)
    }

    private fun getData() {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().indexInfo(1, 7))
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().transformObservable("indexInfo", String::class.java,
                        CacheStrategy.firstRemote()))
                .map(CacheResult.MapFunc())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : RxNetSubscriber<String>() {
                    override fun _onNext(s: String) {
                        val bean = JSON.parseObject(s, SlimmingRecordBean::class.java)
                        updateUI(bean)
                    }

                    override fun _onError(error: String, code: Int) {
                        super._onError(error, code)
                        RxToast.normal(error)
                    }
                })
    }

    private fun updateUI(bean: SlimmingRecordBean) {
        slimmingTarget(bean)
        healthScore(bean)
        weightRecord(bean)
        dietRecord(bean.dataList)
        sportingRecord(bean.dataList)
        energyRecord(bean.dataList)
    }

    /**
     * 食材记录
     *
     * @param list
     */
    private fun dietRecord(list: List<DataListBean>) {
        val calendar = Calendar.getInstance()
        var max = 1f
        recommendList.clear()
        dates.clear()
        item.clear()

        if (!RxDataUtils.isEmpty(list)) {
            calendar.timeInMillis = list[list.size - 1].recordDate
            val size = list.size
            for (i in 0..6) {
                if (i < size) {
                    val bean = list[i]
                    dates.add(RxFormat.setFormatDate(bean.recordDate, "MM/dd"))
                    item.add(bean.heatCalorie.toInt())
                    max = Math.max(max.toDouble(), bean.heatCalorie).toFloat()
                    recommendList.add(bean.dietPlan.toInt())
                    max = Math.max(max.toDouble(), bean.dietPlan).toFloat()
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                    item.add(0)
                    recommendList.add(0)
                }
            }
        } else {
            for (i in 0..6) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                item.add(i, 0)
                recommendList.add(0)
            }
        }

        RxTextUtils.getBuilder("${item[0]}")
                .append("\tkcal").setProportion(0.5f)
                .into(tv_currentDiet)

        recommendList.reverse()
        item.reverse()
        dates.reverse()

        line_normalDiet.setBezierLine(true)
        line_normalDiet.setDashPath(DashPathEffect(floatArrayOf(10f, 5f), 5f))
        line_normalDiet.setColor(ContextCompat.getColor(mContext, R.color.Gray))
        line_normalDiet.setTexts(arrayOf(getString(R.string.proposal), getString(R.string.intake)))
        line_normalDiet.setData(recommendList)
        line_normalDiet.setMaxMinValue(max.toInt(), 0)

        tv_diet_chart_7.text = dates[6]
        tv_diet_chart_6.setText(dates[5])
        tv_diet_chart_5.setText(dates[4])
        tv_diet_chart_4.setText(dates[3])
        tv_diet_chart_3.setText(dates[2])
        tv_diet_chart_2.setText(dates[1])
        tv_diet_chart_1.setText(dates[0])

        dietProgress_6.setColor(if (item[5] > recommendList[5])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        dietProgress_5.setColor(if (item[4] > recommendList[4])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        dietProgress_4.setColor(if (item[3] > recommendList[3])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        dietProgress_3.setColor(if (item[2] > recommendList[2])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))

        dietProgress_2.setColor(if (item[1] > recommendList[1])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        dietProgress_1.setColor(if (item[0] > recommendList[0])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))

        dietProgress_7.setProgress((item[6] * 100 / max).toInt(), false)
        dietProgress_6.setProgress((item[5] * 100 / max).toInt(), false)
        dietProgress_5.setProgress((item[4] * 100 / max).toInt(), false)
        dietProgress_4.setProgress((item[3] * 100 / max).toInt(), false)
        dietProgress_3.setProgress((item[2] * 100 / max).toInt(), false)
        dietProgress_2.setProgress((item[1] * 100 / max).toInt(), false)
        dietProgress_1.setProgress((item[0] * 100 / max).toInt(), false)
    }

    /**
     * 能量记录
     *
     * @param list
     */
    private fun energyRecord(list: List<DataListBean>?) {
        val calendar = Calendar.getInstance()
        var max = 1f
        dates.clear()
        item.clear()
        recommendList.clear()
        if (list != null && !list.isEmpty()) {
            calendar.timeInMillis = list[list.size - 1].recordDate
            val size = list.size
            for (i in 0..6) {
                if (i < size) {
                    val bean = list[i]
                    dates.add(RxFormat.setFormatDate(bean.recordDate, "MM/dd"))
                    val value = EnergyUtil.energy(bean.athlCalorie, bean.heatCalorie, bean.basalCalorie)
                    max = Math.max(max.toDouble(), Math.abs(value)).toFloat()
                    item.add(Math.abs(value).toInt())
                    var normalEnergy = EnergyUtil.energy(bean.athlPlan, bean.dietPlan, bean.basalCalorie)

                    normalEnergy = Math.max(normalEnergy, 0.0)
                    recommendList.add(normalEnergy.toInt())
                    max = Math.max(max.toDouble(), Math.abs(normalEnergy)).toFloat()
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                    item.add(0)
                    recommendList.add(0)
                }
            }
        } else {
            for (i in 0..6) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                item.add(i, 0)
                recommendList.add(0)
            }
        }

        val value = item[0]

        energyProgress_7.setColor(color(if (value < 0) R.color.red else R.color.orange_FF7200))
        tv_currentEnergy.setTextColor(color(if (value < 0) R.color.red else R.color.orange_FF7200))
        tv_energy_chart_7.getHelper().backgroundColorNormal = color(if (value < 0) R.color.red else R.color.orange_FF7200)
        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(Math.abs(value).toDouble(), 0))
                .append("\tkcal").setProportion(0.5f)
                .into(tv_currentEnergy)

        Collections.reverse(recommendList)
        Collections.reverse(item)
        Collections.reverse(dates)

        line_normalEnergy.setBezierLine(true)
        line_normalEnergy.setDashPath(DashPathEffect(floatArrayOf(10f, 5f), 5f))
        line_normalEnergy.setColor(ContextCompat.getColor(mContext, R.color.Gray))
        line_normalEnergy.setTexts(arrayOf(getString(R.string.energy), getString(R.string.proposal)))
        line_normalEnergy.setData(recommendList)
        line_normalEnergy.setMaxMinValue(max.toInt(), 0)


        energyProgress_7.setProgress((item[6] * 100 / max).toInt(), false)
        energyProgress_6.setProgress((item[5] * 100 / max).toInt(), false)
        energyProgress_5.setProgress((item[4] * 100 / max).toInt(), false)
        energyProgress_4.setProgress((item[3] * 100 / max).toInt(), false)
        energyProgress_3.setProgress((item[2] * 100 / max).toInt(), false)
        energyProgress_2.setProgress((item[1] * 100 / max).toInt(), false)
        energyProgress_1.setProgress((item[0] * 100 / max).toInt(), false)

        energyProgress_6.setColor(if (item[5] > recommendList[5])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        energyProgress_5.setColor(if (item[4] > recommendList[4])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        energyProgress_4.setColor(if (item[3] > recommendList[3])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        energyProgress_3.setColor(if (item[2] > recommendList[2])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        energyProgress_2.setColor(if (item[1] > recommendList[1])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        energyProgress_1.setColor(if (item[0] > recommendList[0])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))

        tv_energy_chart_7.setText(dates[6])
        tv_energy_chart_6.setText(dates[5])
        tv_energy_chart_5.setText(dates[4])
        tv_energy_chart_4.setText(dates[3])
        tv_energy_chart_3.setText(dates[2])
        tv_energy_chart_2.setText(dates[1])
        tv_energy_chart_1.setText(dates[0])

    }

    /**
     * 运动记录
     *
     * @param list
     */
    private fun sportingRecord(list: List<DataListBean>?) {
        val calendar = Calendar.getInstance()
        var max = 1f
        dates.clear()
        item.clear()
        recommendList.clear()
        if (list != null && !list.isEmpty()) {
            calendar.timeInMillis = list[list.size - 1].recordDate
            val size = list.size
            for (i in 0..6) {
                if (i < size) {
                    val bean = list[i]
                    dates.add(RxFormat.setFormatDate(bean.recordDate, "MM/dd"))
                    max = Math.max(max.toDouble(), bean.athlCalorie).toFloat()
                    item.add(bean.athlCalorie.toInt())
                    recommendList.add(bean.athlPlan.toInt())
                    max = Math.max(max.toDouble(), bean.athlPlan).toFloat()
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                    item.add(0)
                    recommendList.add(0)
                }
            }
        } else {
            for (i in 0..6) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                item.add(i, 0)
                recommendList.add(0)
            }
        }

        RxTextUtils.getBuilder("${item[0]}")
                .append("\tkcal").setProportion(0.5f)
                .into(tv_currentKcal)

        Collections.reverse(recommendList)
        Collections.reverse(item)
        Collections.reverse(dates)

        line_normalSport.setBezierLine(true)
        line_normalSport.setDashPath(DashPathEffect(floatArrayOf(20f, 5f), 0f))
        line_normalSport.setColor(ContextCompat.getColor(mContext, R.color.Gray))
        line_normalSport.setTexts(arrayOf(getString(R.string.proposal), getString(R.string.consume)))
        line_normalSport.setData(recommendList)
        line_normalSport.setMaxMinValue(max.toInt(), 0)


        sportingProgress_6.setColor(if (item[5] > recommendList[5])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        sportingProgress_5.setColor(if (item[4] > recommendList[4])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        sportingProgress_4.setColor(if (item[3] > recommendList[3])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        sportingProgress_3.setColor(if (item[2] > recommendList[2])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        sportingProgress_2.setColor(if (item[1] > recommendList[1])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))
        sportingProgress_1.setColor(if (item[0] > recommendList[0])
            color(R.color.Gray)
        else
            color(R.color.BrightGray))

        sportingProgress_7.setProgress((item[6] * 100 / max).toInt(), false)
        sportingProgress_6.setProgress((item[5] * 100 / max).toInt(), false)
        sportingProgress_5.setProgress((item[4] * 100 / max).toInt(), false)
        sportingProgress_4.setProgress((item[3] * 100 / max).toInt(), false)
        sportingProgress_3.setProgress((item[2] * 100 / max).toInt(), false)
        sportingProgress_2.setProgress((item[1] * 100 / max).toInt(), false)
        sportingProgress_1.setProgress((item[0] * 100 / max).toInt(), false)

        tv_sporting_chart_7.setText(dates[6])
        tv_sporting_chart_6.setText(dates[5])
        tv_sporting_chart_5.setText(dates[4])
        tv_sporting_chart_4.setText(dates[3])
        tv_sporting_chart_3.setText(dates[2])
        tv_sporting_chart_2.setText(dates[1])
        tv_sporting_chart_1.setText(dates[0])
    }

    /**
     * 体重记录
     *
     * @param bean
     */
    private fun weightRecord(bean: SlimmingRecordBean) {
        img_weightSwitch.setOnClickListener {
            if (img_weightSwitch.tag as Boolean) {
                img_weightSwitch.tag = false
                setLineChartData(bean, false)
                img_weightSwitch.setImageResource(R.mipmap.ic_weight_close)
            } else {
                img_weightSwitch.setImageResource(R.mipmap.ic_weight_open)
                img_weightSwitch.tag = true
                setLineChartData(bean, true)
            }
        }

        img_weightSwitch.tag = true
        setLineChartData(bean, true)
    }

    private fun initChart(lineChartBase: BarLineChartBase<*>) {
        lineChartBase.isEnabled = false
        lineChartBase.setTouchEnabled(false)//可以点击
        lineChartBase.legend.isEnabled = false
        lineChartBase.description.isEnabled = false
        lineChartBase.setDrawGridBackground(false)
        val xAxis = lineChartBase.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawLabels(false)

        val leftAxis = lineChartBase.axisLeft
        val rightAxis = lineChartBase.axisRight
        rightAxis.isEnabled = false
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawLabels(false)
        mLineChart.setData(LineData())

    }

    private fun setLineChartData(bean: SlimmingRecordBean, isWeight: Boolean) {
        dates.clear()
        val list = bean.weightInfoList
        val calendar = Calendar.getInstance()
        val lineEntry = ArrayList<Entry>(7)

        if (list != null && !list.isEmpty()) {
            calendar.timeInMillis = list[list.size - 1].weightDate
            val size = list.size
            for (i in 0..6) {
                if (i < size) {
                    var value = 0.0
                    if (isWeight) {
                        value = list[i].weight
                    } else
                        value = list[i].bodyFat
                    lineEntry.add(Entry((6 - i).toFloat(), value.toFloat()))
                    dates.add(RxFormat.setFormatDate(list[i].weightDate, "MM/dd"))
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, -1)
                    dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                    lineEntry.add(Entry((6 - i).toFloat(), 0f))
                }
            }
        } else {
            RxTextUtils.getBuilder("0")
                    .append("\t" + if (isWeight) "kg" else "%").setProportion(0.5f)
                    .into(tv_currentWeight)

            for (i in 0..6) {
                dates.add(RxFormat.setFormatDate(calendar, "MM/dd"))
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                lineEntry.add(Entry((6 - i).toFloat(), 0f))
            }
        }

        RxTextUtils.getBuilder(String.format("%.1f", lineEntry[0].y))
                .append("\t" + if (isWeight) "kg" else "%").setProportion(0.5f)
                .into(tv_currentWeight)

        val max = Collections.max(lineEntry) { o1, o2 -> (o1.y - o2.y).toInt() }.y.toInt()
        val yAxis = mLineChart.getAxisLeft()
        yAxis.axisMaximum = Math.max(10, max).toFloat() * 1.1f

        var set = mLineChart.data.getDataSetByLabel("weight", false) as? LineDataSet
        if (set == null) {
            set = createSet()
            mLineChart.getLineData().addDataSet(set)
        }
        lineEntry.reverse()

        set.values = lineEntry

        mLineChart.getData().notifyDataChanged()
        mLineChart.notifyDataSetChanged()
        mLineChart.invalidate()
        mLineChart.animateX(500)

        tv_weight_chart_1.setText(dates[6])
        tv_weight_chart_2.setText(dates[5])
        tv_weight_chart_3.setText(dates[4])
        tv_weight_chart_4.setText(dates[3])
        tv_weight_chart_5.setText(dates[2])
        tv_weight_chart_6.setText(dates[1])
        tv_weight_chart_7.setText(dates[0])
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "weight")
        set.setDrawIcons(false)
        set.setDrawValues(true)
        val colors = intArrayOf(R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.BrightGray, R.color.green_61D97F)

        set.valueTextColor = ContextCompat.getColor(mContext, R.color.Gray)
        set.valueTextSize = 8f
        set.color = color(R.color.green_62D981)
        //渐变
        set.setDrawFilled(true)
        val drawable = drawable(R.drawable.fade_green)
        set.fillDrawable = drawable

        set.setDrawCircleHole(false)
        set.setDrawCircles(true)
        set.circleRadius = 3f
        set.lineWidth = 1f
        set.setCircleColors(colors, mActivity)
        return set
    }

    /**
     * 健康分数
     *
     * @param bean
     */
    private fun healthScore(bean: SlimmingRecordBean) {
        val weightInfo = bean.weightInfo
        if (weightInfo == null) {
            RxTextUtils.getBuilder("--")
                    .append("\t" + getString(R.string.weightAndUnit) + "\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\tBMI\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t" + getString(R.string.bodyFatAndUnit) + "\n").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append("--")
                    .append("\t" + getString(R.string.bmrAndUnit)).setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .into(tv_weightInfo)

            RxTextUtils.getBuilder("0.0")
                    .append("\t分").setProportion(0.5f)
                    .into(tv_healthScore)


            RxTextUtils.getBuilder(getString(R.string.body) + "：\t").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append(getString(R.string.unknown))
                    .append("\t\t\t" + getString(R.string.riskOfLllness) + "：").setProportion(0.8f)
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                    .append(getString(R.string.unknown))
                    .into(tv_healthInfo)

            tv_healthDate.text = RxFormat.setFormatDate(System.currentTimeMillis(), "yyyy/MM/dd")
            return
        }

        RxTextUtils.getBuilder(String.format("%.1f", weightInfo.weight))
                .append("\t" + getString(R.string.weightAndUnit) + "\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.bmi, 1))
                .append("\tBMI\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(RxFormatValue.fromat4S5R(weightInfo.bodyFat, 1))
                .append("\t" + getString(R.string.bodyFatAndUnit) + "\n").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.bmr.toString() + "")
                .append("\t" + getString(R.string.bmrAndUnit)).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(tv_weightInfo)

        RxTextUtils.getBuilder(RxFormatValue.fromat4S5R(weightInfo.healthScore, 1))
                .append("\t分").setProportion(0.5f)
                .into(tv_healthScore)

        RxTextUtils.getBuilder(getString(R.string.body) + "：\t").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(weightInfo.bodyType)
                .append("\t\t\t" + getString(R.string.riskOfLllness) + "：").setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .append(bean.levelDesc)
                .into(tv_healthInfo)

        Healthy_level.switchLevel(bean.sickLevel)
        tv_healthDate.text = RxFormat.setFormatDate(weightInfo.weightDate, "yyyy/MM/dd")
    }

    /**
     * 今日完成目标
     *
     * @param bean
     */
    private fun slimmingTarget(bean: SlimmingRecordBean) {
        img_breakfast.setImageResource(if (bean.isBreakfastDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_lunch.setImageResource(if (bean.isLunchDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_dinner.setImageResource(if (bean.isDinnerDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_sporting.setImageResource(if (bean.isAthlDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_weigh.setImageResource(if (bean.isWeightDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)

        img_breakfast2.setImageResource(if (bean.isBreakfastDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_lunch2.setImageResource(if (bean.isLunchDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_dinner2.setImageResource(if (bean.isDinnerDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_sporting2.setImageResource(if (bean.isAthlDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)
        img_weigh2.setImageResource(if (bean.isWeightDone) R.mipmap.icon_food_complete else R.drawable.shape_gray_17dp)


        var complete = 0
        if (bean.isBreakfastDone) complete += 20
        if (bean.isLunchDone) complete += 20
        if (bean.isDinnerDone) complete += 20
        if (bean.isAthlDone) complete += 20
        if (bean.isWeightDone) complete += 20

        pro_complete.setWaterLevelRatio(complete / 100f)
        pro_complete2.setWaterLevelRatio(complete / 100f)
        //        pro_complete.setWaterLevelRatio(0.8f);
        if (complete / 100f > 0) {
            mWaveHelper = RxWaveHelper(pro_complete)
            mWaveHelper?.start()
        }
        RxTextUtils.getBuilder(complete.toString() + "")
                .append("\t%").setProportion(0.5f)
                .into(tv_progress)
        RxTextUtils.getBuilder(complete.toString() + "")
                .append("\t%").setProportion(0.5f)
                .into(tv_progress2)


        RxTextUtils.getBuilder(getString(R.string.totalRecord) + "\t")
                .append(bean.totalCompleteDays.toString() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(tv_continuousRecord)
        RxTextUtils.getBuilder(getString(R.string.totalRecord) + "\t")
                .append(bean.totalCompleteDays.toString() + "").setProportion(1.5f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.Gray))
                .append("\t天").into(tv_continuousRecord2)

    }


    override fun onInvisible() {
        super.onInvisible()
        mWaveHelper?.cancel()
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.Gray))
                .setLightStatusBar(false)
                .process()
    }


    override fun onVisible() {
        super.onVisible()
        mWaveHelper?.start()
        //屏幕沉浸
        StatusBarUtils.from(this)
                .setStatusBarColor(Color.parseColor("#ABCBE0"))
                .setLightStatusBar(false)
                .process()
    }

    /**
     * 分享
     */
    private fun initShare() {
        RxQRCode.builder(ServiceAPI.APP_DOWN_LOAD_URL)
                .codeSide(800)
                .logoBitmap(R.mipmap.icon_app, resources)
                .into(img_QRcode)

        val info = MyAPP.getgUserInfo()
        GlideImageLoader.displayImage(mActivity, info!!.imgUrl, R.mipmap.userimg, img_userImg2)
        GlideImageLoader.displayImage(mActivity, info.imgUrl, R.mipmap.userimg, img_userImg3)


        RxTextUtils.getBuilder(info.userName + "\n")
                .append(getString(R.string.appDays, getString(R.string.appName), info.getRegisterTime())).setProportion(0.8f)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.GrayWrite))
                .into(tv_userName)
        tv_date.text = RxFormat.setFormatDate(System.currentTimeMillis(), "MM/dd")

        RxTextUtils.getBuilder(getString(R.string.HealthSlim) + "\n")
                .append(getString(R.string.appName) + "v" + RxDeviceUtils.getAppVersionName())
                .setProportion(0.5f)
                .into(tv_appVersion)
    }


    private fun initClick() {
        img_email.setOnClickListener(onClickListener)
        layout_weight_title.setOnClickListener(onClickListener)
        layout_diet_title.setOnClickListener(onClickListener)
        layout_sports_title.setOnClickListener(onClickListener)
        layout_energy_title.setOnClickListener(onClickListener)
        layout_HealthReport.setOnClickListener(onClickListener)
    }


    private val onClickListener = View.OnClickListener {
        if (AntiShake.getInstance().check(it.id)) return@OnClickListener
        when (it.id) {
            R.id.img_email ->
                //分享
                share(true)
            R.id.layout_weight_title -> RxActivityUtils.skipActivity(mContext, WeightRecordFragment::class.java)
            R.id.layout_diet_title -> RxActivityUtils.skipActivity(mContext, DietRecordActivity::class.java)
            R.id.layout_sports_title -> RxActivityUtils.skipActivity(mContext, SmartClothingFragment::class.java)
            R.id.layout_energy_title -> RxActivityUtils.skipActivity(mContext, EnergyActivity::class.java)
            R.id.layout_HealthReport -> {
                //判断是否审核通过
                val userInfo = MyAPP.getgUserInfo()
                if (userInfo.planState == 3) {
                    val url = ServiceAPI.SHARE_INFORM_URL + userInfo.informId + "&sign=true"
                    PlanWebActivity.startActivity(mContext, url)
                }
            }
        }
    }

    private fun share(isStart: Boolean) {
        layout_shareHead.visibility = if (isStart) View.VISIBLE else View.INVISIBLE
        layout_shareTitle.visibility = if (isStart) View.VISIBLE else View.GONE

        if (isStart) {
            pro_complete2.isShowWave = true
            //延迟500毫秒，需要等到控件显示
            pro_complete2.postDelayed({
                //控件转图片
                val bitmap = RxImageUtils.view2Bitmap(prant, ContextCompat.getColor(mContext, R.color.white))

                if (activity is BaseShareActivity) {
                    val activity = activity as BaseShareActivity?
                    activity?.shareBitmap(bitmap)
                }
                share(false)
            }, 100)
        }
    }

}