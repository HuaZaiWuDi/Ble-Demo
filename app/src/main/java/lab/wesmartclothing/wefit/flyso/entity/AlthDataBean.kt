package lab.wesmartclothing.wefit.flyso.entity


import com.alibaba.fastjson.annotation.JSONField

data class AlthDataBean(
        @JSONField(name = "age")
        val age: Int,
        @JSONField(name = "athlDate")
        val athlDate: Long,
        @JSONField(name = "athlDesc")
        val athlDesc: String,
        @JSONField(name = "avgHeart")
        val avgHeart: Double,
        @JSONField(name = "avgPace")
        val avgPace: Double,
        @JSONField(name = "birthday")
        val birthday: Long,
        @JSONField(name = "cadence")
        val cadence: Double,
        @JSONField(name = "calorie")
        val calorie: Double,
        @JSONField(name = "complete")
        val complete: Double,
        @JSONField(name = "dataFlag")
        val dataFlag: Int,
        @JSONField(name = "duration")
        val duration: Int,
        @JSONField(name = "endTime")
        val endTime: Long,
        @JSONField(name = "heartCount")
        val heartCount: Int,
        @JSONField(name = "height")
        val height: Int,
        @JSONField(name = "kilometers")
        val kilometers: Double,
        @JSONField(name = "maxHeart")
        val maxHeart: Int,
        @JSONField(name = "maxPace")
        val maxPace: Int,
        @JSONField(name = "minHeart")
        val minHeart: Int,
        @JSONField(name = "minPace")
        val minPace: Int,
        @JSONField(name = "sex")
        val sex: Int,
        @JSONField(name = "startTime")
        val startTime: Long,
        @JSONField(name = "status")
        val status: Int,
        @JSONField(name = "stepNumber")
        val stepNumber: Int,
        @JSONField(name = "userId")
        val userId: String,
        @JSONField(name = "gid")
        val gid: String
)