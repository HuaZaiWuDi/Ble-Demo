package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName HealthReportBean
 * @Date 2019/3/26 18:05
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class HealthReportBean {


    /**
     * targetInfo : {"completeStatus":0,"count":28,"createTime":1543286092000,"createUser":"9801eee7ffce4364bed693e9cca8b681","cycleFlag":1,"finalWeight":64.1,"gid":"79821992ba2f413692fd3f1289091716","initialWeight":64,"status":101,"targetDate":1560182400000,"targetWeight":49.6,"updateTime":1544422715000,"updateUser":"9801eee7ffce4364bed693e9cca8b681","userId":"9801eee7ffce4364bed693e9cca8b681"}
     * targetWeight : {"cycleFlag":1,"gid":"79821992ba2f413692fd3f1289091716","hasDays":75,"initialWeight":64,"latestWeight":66.15,"stillNeed":16.550000000000004,"targetDaysDiff":75,"targetWeight":49.6,"weightDaysDiff":0}
     * userInform : {"additionHeat":null,"athlComment":"1","athlHeat":674,"basalHeat":1607,"belongUser":"c82e9e7612a447358c2a82ef437f3fh3","bmi":21.9,"bmiLevel":"","bodyAge":22,"bodyFat":14.1,"bodyFfm":54.99,"bodyLevel":5,"bodyType":"标准","createTime":1540971135000,"createUser":"9801eee7ffce4364bed693e9cca8b681","dietComment":"1","fillDate":1540915200000,"gid":"a9ed75956bb5485685bf27df3bb3a0fa","healthScore":null,"informNo":"M28W64BS_BBC","initialWeight":64,"maxHeart":160,"maxTime":60,"minHeart":120,"minTime":30,"planCycle":84,"planState":3,"sinew":52.23,"sportState":null,"status":101,"targetId":"79821992ba2f413692fd3f1289091716","targetWeight":null,"updateTime":1553654301000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","useStatus":1,"userId":"9801eee7ffce4364bed693e9cca8b681"}
     */

    private TargetInfoBean targetInfo;
    private TargetWeightBean targetWeight;
    private UserInformBean userInform;

    public TargetInfoBean getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(TargetInfoBean targetInfo) {
        this.targetInfo = targetInfo;
    }

    public TargetWeightBean getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(TargetWeightBean targetWeight) {
        this.targetWeight = targetWeight;
    }

    public UserInformBean getUserInform() {
        return userInform;
    }

    public void setUserInform(UserInformBean userInform) {
        this.userInform = userInform;
    }

    public static class TargetInfoBean {
        /**
         * completeStatus : 0
         * count : 28
         * createTime : 1543286092000
         * createUser : 9801eee7ffce4364bed693e9cca8b681
         * cycleFlag : 1
         * finalWeight : 64.1
         * gid : 79821992ba2f413692fd3f1289091716
         * initialWeight : 64
         * status : 101
         * targetDate : 1560182400000
         * targetWeight : 49.6
         * updateTime : 1544422715000
         * updateUser : 9801eee7ffce4364bed693e9cca8b681
         * userId : 9801eee7ffce4364bed693e9cca8b681
         */


        private int completeStatus;
        private double finalWeight;
        private double initialWeight;
        private long targetDate;
        private double targetWeight;
        private long createTime;


        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }


        /**
         * -1:失败，0：进行，1：完成，9：中断
         *
         * @return
         */
        public int getCompleteStatus() {
            return completeStatus;
        }

        public void setCompleteStatus(int completeStatus) {
            this.completeStatus = completeStatus;
        }

        public double getFinalWeight() {
            return finalWeight;
        }

        public void setFinalWeight(double finalWeight) {
            this.finalWeight = finalWeight;
        }

        public double getInitialWeight() {
            return initialWeight;
        }

        public void setInitialWeight(double initialWeight) {
            this.initialWeight = initialWeight;
        }

        public long getTargetDate() {
            return targetDate;
        }

        public void setTargetDate(long targetDate) {
            this.targetDate = targetDate;
        }

        public double getTargetWeight() {
            return targetWeight;
        }

        public void setTargetWeight(double targetWeight) {
            this.targetWeight = targetWeight;
        }
    }

    public static class TargetWeightBean {
        /**
         * cycleFlag : 1
         * gid : 79821992ba2f413692fd3f1289091716
         * hasDays : 75
         * initialWeight : 64
         * latestWeight : 66.15
         * stillNeed : 16.550000000000004
         * targetDaysDiff : 75
         * targetWeight : 49.6
         * weightDaysDiff : 0
         */

        private double stillNeed;
        private double complete;


        public double getComplete() {
            return complete;
        }

        public void setComplete(double complete) {
            this.complete = complete;
        }

        public double getStillNeed() {
            return stillNeed;
        }

        public void setStillNeed(double stillNeed) {
            this.stillNeed = stillNeed;
        }
    }

    public static class UserInformBean {
        /**
         * additionHeat : null
         * athlComment : 1
         * athlHeat : 674
         * basalHeat : 1607
         * belongUser : c82e9e7612a447358c2a82ef437f3fh3
         * bmi : 21.9
         * bmiLevel :
         * bodyAge : 22
         * bodyFat : 14.1
         * bodyFfm : 54.99
         * bodyLevel : 5
         * bodyType : 标准
         * createTime : 1540971135000
         * createUser : 9801eee7ffce4364bed693e9cca8b681
         * dietComment : 1
         * fillDate : 1540915200000
         * gid : a9ed75956bb5485685bf27df3bb3a0fa
         * healthScore : null
         * informNo : M28W64BS_BBC
         * initialWeight : 64
         * maxHeart : 160
         * maxTime : 60
         * minHeart : 120
         * minTime : 30
         * planCycle : 84
         * planState : 3
         * sinew : 52.23
         * sportState : null
         * status : 101
         * targetId : 79821992ba2f413692fd3f1289091716
         * targetWeight : null
         * updateTime : 1553654301000
         * updateUser : c82e9e7612a447358c2a82ef437f3d11
         * useStatus : 1
         * userId : 9801eee7ffce4364bed693e9cca8b681
         */

        private String gid;
        private String informNo;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getInformNo() {
            return informNo;
        }

        public void setInformNo(String informNo) {
            this.informNo = informNo;
        }
    }
}
