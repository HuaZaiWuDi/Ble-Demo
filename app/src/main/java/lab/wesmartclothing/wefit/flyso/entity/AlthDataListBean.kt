package lab.wesmartclothing.wefit.flyso.entity

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName AlthDataListBean
 * @Date 2019/7/2 16:12
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
data class AlthDataListBean(
        val list: MutableList<AlthDataBean>
) : GroupRecordBean()