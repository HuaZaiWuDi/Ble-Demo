package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created icon_hide_password jk on 2018/5/10.
 */
public class AddFoodItem {

    /**
     * {
     * "userId":"df99dss00asl"，
     * "addDate":"2018-05-05"，
     * "eatType":1，
     * "intakeList"：[
     * {
     * "foodId":"df99dss00asl"，
     * "foodName":"米饭"，
     * "foodCount":100，
     * "foodUnit":"克"，
     * "weight":"1碗"，
     * "weightType":""
     * }
     * ]
     * }
     */
    private String userId;
    private long addDate;
    private int eatType;
    private List<intakeList> intakeList;


    public static class intakeList implements Serializable {
        private String foodId;
        private String foodName;
        private int foodCount;
        private String unit;
        private String weight;
        private String weightType;
        private String gid;
        private int unitCount;
        private String remark;
        private int calorie;
        private String foodImg;
        private long heatDate;
        private int unitCalorie;


        public void setUnitCalorie(int unitCalorie) {
            this.unitCalorie = unitCalorie;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public void setFoodImg(String foodImg) {
            this.foodImg = foodImg;
        }

        public void setHeatDate(long heatDate) {
            this.heatDate = heatDate;
        }

        public void setUnitCount(int unitCount) {
            this.unitCount = unitCount;
        }

        public void setFoodId(String foodId) {
            this.foodId = foodId;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public void setFoodCount(int foodCount) {
            this.foodCount = foodCount;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public void setWeightType(String weightType) {
            this.weightType = weightType;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        @Override
        public String toString() {
            return "intakeList{" +
                    "foodId='" + foodId + '\'' +
                    ", foodName='" + foodName + '\'' +
                    ", foodCount=" + foodCount +
                    ", foodUnit='" + unit + '\'' +
                    ", weight='" + weight + '\'' +
                    ", weightType='" + weightType + '\'' +
                    ", gid='" + gid + '\'' +
                    '}';
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public void setEatType(int eatType) {
        this.eatType = eatType;
    }

    public void setIntakeLists(List<intakeList> intakeLists) {
        intakeList = intakeLists;
    }
}
