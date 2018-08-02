package lab.wesmartclothing.wefit.flyso.entity;

/**
 * Created by jk on 2018/7/30.
 */
public class WeightDetailsBean {


    /**
     * bodyType : string
     * levelDesc : string
     * sickLevel : string
     * weightInfo : {"bmi":0,"bmr":0,"bodyAge":0,"bodyFat":0,"bodyFfm":0,"bodyType":"string","bone":0,"createTime":1511248354000,"createUser":1,"flesh":0,"gid":1,"healthScore":0,"measureTime":"2018-07-30T09:35:52.213Z","muscle":0,"protein":0,"sinew":0,"status":101,"subfat":0,"updateTime":1511248354000,"updateUser":1,"userId":"string","visfat":0,"water":0,"weight":0,"weightDate":"2018-07-30T09:35:52.213Z"}
     */
    private int bodyLevel;//1-9表示体型标准等级，0表示未获取
    private String bodyType;
    private String levelDesc;
    private String sickLevel;
    private WeightInfoBean weightInfo;


    public int getBodyLevel() {
        return bodyLevel;
    }

    public void setBodyLevel(int bodyLevel) {
        this.bodyLevel = bodyLevel;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public String getSickLevel() {
        return sickLevel;
    }

    public void setSickLevel(String sickLevel) {
        this.sickLevel = sickLevel;
    }

    public WeightInfoBean getWeightInfo() {
        return weightInfo;
    }

    public void setWeightInfo(WeightInfoBean weightInfo) {
        this.weightInfo = weightInfo;
    }

    public static class WeightInfoBean {
        /**
         * bmi : 0
         * bmr : 0
         * bodyAge : 0
         * bodyFat : 0
         * bodyFfm : 0
         * bodyType : string
         * bone : 0
         * createTime : 1511248354000
         * createUser : 1
         * flesh : 0
         * gid : 1
         * healthScore : 0
         * measureTime : 2018-07-30T09:35:52.213Z
         * muscle : 0
         * protein : 0
         * sinew : 0
         * status : 101
         * subfat : 0
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * visfat : 0
         * water : 0
         * weight : 0
         * weightDate : 2018-07-30T09:35:52.213Z
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private Object pageNum;
        private Object pageSize;
        private String userId;
        private long weightDate;
        private long measureTime;
        private double weight;
        private double bmi;
        private double bmr;
        private String bodyType;
        private int bodyAge;
        private double bone;
        private double muscle;
        private double sinew;
        private double protein;
        private double bodyFat;
        private double bodyFfm;
        private double subfat;
        private double visfat;
        private double flesh;
        private double water;
        private double healthScore;


        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getPageNum() {
            return pageNum;
        }

        public void setPageNum(Object pageNum) {
            this.pageNum = pageNum;
        }

        public Object getPageSize() {
            return pageSize;
        }

        public void setPageSize(Object pageSize) {
            this.pageSize = pageSize;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public long getWeightDate() {
            return weightDate;
        }

        public void setWeightDate(long weightDate) {
            this.weightDate = weightDate;
        }

        public long getMeasureTime() {
            return measureTime;
        }

        public void setMeasureTime(long measureTime) {
            this.measureTime = measureTime;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
            this.bmi = bmi;
        }

        public double getBmr() {
            return bmr;
        }

        public void setBmr(double bmr) {
            this.bmr = bmr;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public int getBodyAge() {
            return bodyAge;
        }

        public void setBodyAge(int bodyAge) {
            this.bodyAge = bodyAge;
        }

        public double getBone() {
            return bone;
        }

        public void setBone(double bone) {
            this.bone = bone;
        }

        public double getMuscle() {
            return muscle;
        }

        public void setMuscle(double muscle) {
            this.muscle = muscle;
        }

        public double getSinew() {
            return sinew;
        }

        public void setSinew(double sinew) {
            this.sinew = sinew;
        }

        public double getProtein() {
            return protein;
        }

        public void setProtein(double protein) {
            this.protein = protein;
        }

        public double getBodyFat() {
            return bodyFat;
        }

        public void setBodyFat(double bodyFat) {
            this.bodyFat = bodyFat;
        }

        public double getBodyFfm() {
            return bodyFfm;
        }

        public void setBodyFfm(double bodyFfm) {
            this.bodyFfm = bodyFfm;
        }

        public double getSubfat() {
            return subfat;
        }

        public void setSubfat(double subfat) {
            this.subfat = subfat;
        }

        public double getVisfat() {
            return visfat;
        }

        public void setVisfat(double visfat) {
            this.visfat = visfat;
        }

        public double getFlesh() {
            return flesh;
        }

        public void setFlesh(double flesh) {
            this.flesh = flesh;
        }

        public double getWater() {
            return water;
        }

        public void setWater(double water) {
            this.water = water;
        }

        public double getHealthScore() {
            return healthScore;
        }

        public void setHealthScore(double healthScore) {
            this.healthScore = healthScore;
        }
    }
}
