package lab.wesmartclothing.wefit.flyso.ui.main.slimming.heat

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.vondear.rxtools.utils.RxDataUtils
import com.vondear.rxtools.utils.RxTextUtils
import com.vondear.rxtools.utils.RxUtils
import com.vondear.rxtools.utils.StatusBarUtils
import com.vondear.rxtools.utils.dateUtils.RxFormat
import com.vondear.rxtools.view.RxToast
import com.zchu.rxcache.RxCache
import com.zchu.rxcache.data.CacheResult
import com.zchu.rxcache.stategy.CacheStrategy
import kotlinx.android.synthetic.main.activity_diet_record.*
import lab.wesmartclothing.wefit.flyso.R
import lab.wesmartclothing.wefit.flyso.base.BaseActivity
import lab.wesmartclothing.wefit.flyso.entity.DataListBean
import lab.wesmartclothing.wefit.flyso.entity.GroupDataListBean
import lab.wesmartclothing.wefit.flyso.netutil.net.NetManager
import lab.wesmartclothing.wefit.flyso.netutil.utils.RxNetSubscriber
import lab.wesmartclothing.wefit.flyso.tools.GroupType
import lab.wesmartclothing.wefit.flyso.utils.RxComposeUtils
import lab.wesmartclothing.wefit.flyso.view.line.LineBean
import lab.wesmartclothing.wefit.flyso.view.line.SuitLines
import lab.wesmartclothing.wefit.flyso.view.line.Unit
import java.util.*

class DietRecordActivity : BaseActivity() {
    private var date: Long = System.currentTimeMillis()
    @GroupType
    private var groupType = GroupType.TYPE_DAYS
    private var pageNum = 1
    private var list: MutableList<DataListBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun layoutId() = R.layout.activity_diet_record


    override fun initViews() {
        super.initViews()
        //屏幕沉浸
        StatusBarUtils.from(mActivity)
                .setStatusBarColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                .setLightStatusBar(false)
                .process()

        rxtitle.apply {
            setLeftFinish(this@DietRecordActivity)
        }
        tv_dietDetail.setOnClickListener {
            FoodRecommend.start(this, date)
        }
        img_switchDate.setOnClickListener {
            if (RxUtils.isFastClick(800)) return@setOnClickListener
            if (groupType == GroupType.TYPE_DAYS) {
                groupType = GroupType.TYPE_MONTHS
                img_switchDate.setImageResource(R.mipmap.ic_select_month)
            } else {
                groupType = GroupType.TYPE_DAYS
                img_switchDate.setImageResource(R.mipmap.ic_select_day)
            }
            pageNum = 1
            initData()
        }
    }

    override fun initNetData() {
        super.initNetData()
        initData()
    }

    private fun initData() {
        NetManager.getApiService().heatFetchGroupTypeRecordList(groupType, pageNum, 10)
                .compose(RxComposeUtils.handleResult())
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().transformObservable("heatFetchGroupTypeRecordList$pageNum$groupType",
                        String::class.java, CacheStrategy.firstRemote()))
                .map(CacheResult.MapFunc())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(object : RxNetSubscriber<String>() {
                    override fun _onNext(s: String) {
                        val bean = JSON.parseObject(s, GroupDataListBean::class.java)
                        if (!RxDataUtils.isEmpty(bean.list))
                            updateUI(bean.list)
                    }

                    override fun _onError(error: String, code: Int) {
                        super._onError(error, code)
                        RxToast.normal(error)
                    }
                })
    }

    fun updateUI(dataList: MutableList<DataListBean>) {
        if (pageNum == 1) {
            this.list = dataList
            initLineChart()
            pageNum++
        } else {
            dataList.reverse()
            this.list?.addAll(0, dataList)

            val lines_Heat = ArrayList<Unit>()
            val lines_Base = ArrayList<Unit>()
            list?.forEach {
                val date = RxFormat.setFormatDate(it.recordDate, if (GroupType.TYPE_DAYS == groupType) "MM/dd" else "yyyy/MM")
                val color = if (it.heatCalorie < it.dietPlan) -0x78000001 else -0x1
                lines_Heat.add(Unit(it.heatCalorie.toFloat(), date, color))
                lines_Base.add(Unit(it.dietPlan.toFloat(), ""))
            }
            suitChart.addDataChart(listOf(lines_Heat, lines_Base))
            pageNum++
        }
    }

    private fun initLineChart() {
        val lines_Heat = ArrayList<Unit>()
        val lines_Base = ArrayList<Unit>()
        if (RxDataUtils.isEmpty(this.list)) return
        this.list?.reverse()

        list?.forEach {
            val date = RxFormat.setFormatDate(it.recordDate, if (GroupType.TYPE_DAYS == groupType) "MM/dd" else "yyyy/MM")
            val color = if (it.heatCalorie < it.dietPlan) -0x78000001 else -0x1
            lines_Heat.add(Unit(it.heatCalorie.toFloat(), date, color))
            lines_Base.add(Unit(it.dietPlan.toFloat(), ""))
        }

        val heatLine = LineBean()
        heatLine.units = lines_Heat
        heatLine.barWidth = RxUtils.dp2px(10f)
        heatLine.chartType = SuitLines.ChartType.TYPE_BAR

        val timeLine = LineBean()
        timeLine.units = lines_Base
        timeLine.isShowUpText = false
        timeLine.lineType = SuitLines.LineType.CURVE
        timeLine.lineWidth = RxUtils.dp2px(1f)
        timeLine.setDashed(true)
        suitChart.setYSpace(1f, 0f)
        SuitLines.LineBuilder()
                .add(heatLine)
                .add(timeLine)
                .build(suitChart)

        suitChart.setLineChartSelectItemListener { valueX ->
            if (this.list!!.isEmpty()) return@setLineChartSelectItemListener
            val bean = this.list!![valueX]
            date = bean.recordDate
            tv_dietDetail.text = RxFormat.setFormatDate(bean.recordDate, RxFormat.Date_CH)

            RxTextUtils.getBuilder("基础代谢\n")
                    .append(bean.basalCalorie.toString())
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .setProportion(1.8f)
                    .append("kcal")
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .into(tv_baseCalorie)

            RxTextUtils.getBuilder("摄入热量\n")
                    .append(bean.heatCalorie.toString())
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .setProportion(1.8f)
                    .append("kcal")
                    .setForegroundColor(ContextCompat.getColor(mContext, R.color.orange_FF7200))
                    .into(tv_intakeCalorie)

        }

        suitChart.setLineChartScrollEdgeListener(object : SuitLines.LineChartScrollEdgeListener {
            override fun leftEdge() {
                initData()
            }

            override fun rightEdge() {

            }
        })

    }


}
