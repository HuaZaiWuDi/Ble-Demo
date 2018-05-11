package lab.dxythch.com.commonproject.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jk on 2018/5/9.
 */
public class FoodHistoryItem {


    /**
     * depletion : 1000
     * 1 : {"foodList":[{"gid":"03102d9e332c46aeb0bf46bec3d1c4c1","heatDate":1525363200000,"foodId":"AR766369969034952000000000000000","updateUser":"testuser","updateTime":1524810000000,"userId":"testuser","weightType":"100","foodName":"嘉顿 生命面包 450g","foodCount":100,"eatType":1,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/food_small/small_photo_201512618245916435.jpg","calorie":116,"createUser":"testuser","foodUnit":"克"}],"userId":"testuser","intake":116}
     * 2 : {"foodList":[{"gid":"03102d9e332c46aeb0bf46bec3d1c4c3","heatDate":1525363200000,"foodId":"AX851394083442046000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"50","foodName":"包子（三鲜馅）","foodCount":100,"eatType":2,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/0bf279b2b74d4029b5b8d6f26a1dda05.jpg","calorie":112,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c4","heatDate":1525363200000,"foodId":"BE125320884754896000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"60","foodName":"薄荷 超模25代餐粉（香草味）","foodCount":100,"eatType":2,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/upload_food/2015/4/21/1510332_1429575547small.png","calorie":223,"createUser":"testuser","foodUnit":"克"}],"userId":"testuser","intake":335}
     * 3 : {"foodList":[{"gid":"03102d9e332c46aeb0bf46bec3d1c4c5","heatDate":1525363200000,"foodId":"BU493756248793122000000000000000","updateUser":"testuser","updateTime":1525516858594,"userId":"testuser","weightType":"70","foodName":"面包","foodCount":100,"eatType":3,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/3b28d1a3b65d4791b4c4ce25eff85f50.jpg","calorie":375,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c6","heatDate":1525363200000,"foodId":"BX608495576866550000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"120","foodName":"通心面","foodCount":100,"eatType":3,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/food_small/s_1160655599421.jpg","calorie":370,"createUser":"testuser","foodUnit":"克"}],"userId":"testuser","intake":745}
     * 4 : {"foodList":[{"gid":"03102d9e332c46aeb0bf46bec3d1c4c7","heatDate":1525363200000,"foodId":"CC793863792320307000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"50","foodName":"粉丝","foodCount":100,"eatType":4,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/0211b7756f114cd594d0d335a2edaf0a.jpg","calorie":377,"createUser":"testuser","foodUnit":"克"}],"userId":"testuser","intake":377}
     * 5 : {"foodList":[{"gid":"03102d9e332c46aeb0bf46bec3d1c4c8","heatDate":1525363200000,"foodId":"CD526554517436856000000000000000","updateUser":"testuser","updateTime":1524810000000,"userId":"testuser","weightType":"200","foodName":"玉米片(即食粥)","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/d3c2911f07e34c97a75f56960720b32f.jpg","calorie":313,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c9","heatDate":1525363200000,"foodId":"CG505917539370454000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"320","foodName":"稻米","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/3e01a32158e941ccb328d808d6955684.jpg","calorie":102,"createUser":"testuser","foodUnit":"克"}],"userId":"testuser","intake":415}
     * ableIntake : 620
     * warning : true
     * userId : testuser
     * intake : 2380
     */

    private int depletion;
    @SerializedName("1")
    private _$1Bean _$1;
    @SerializedName("2")
    private _$2Bean _$2;
    @SerializedName("3")
    private _$3Bean _$3;
    @SerializedName("4")
    private _$4Bean _$4;
    @SerializedName("5")
    private _$5Bean _$5;
    @SerializedName("6")
    private _$5Bean _$6;
    private int ableIntake;
    private boolean warning;
    private String userId;
    private int intake;

    public int getDepletion() {
        return depletion;
    }

    public void setDepletion(int depletion) {
        this.depletion = depletion;
    }

    public _$1Bean get_$1() {
        return _$1;
    }

    public void set_$1(_$1Bean _$1) {
        this._$1 = _$1;
    }

    public _$2Bean get_$2() {
        return _$2;
    }

    public void set_$2(_$2Bean _$2) {
        this._$2 = _$2;
    }

    public _$3Bean get_$3() {
        return _$3;
    }

    public void set_$3(_$3Bean _$3) {
        this._$3 = _$3;
    }

    public _$4Bean get_$4() {
        return _$4;
    }

    public void set_$4(_$4Bean _$4) {
        this._$4 = _$4;
    }

    public _$5Bean get_$5() {
        return _$5;
    }

    public void set_$5(_$5Bean _$5) {
        this._$5 = _$5;
    }

    public _$5Bean get_$6() {
        return _$6;
    }

    public void set_$6(_$5Bean _$6) {
        this._$6 = _$6;
    }

    public int getAbleIntake() {
        return ableIntake;
    }

    public void setAbleIntake(int ableIntake) {
        this.ableIntake = ableIntake;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIntake() {
        return intake;
    }

    public void setIntake(int intake) {
        this.intake = intake;
    }

    public static class _$1Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c1","heatDate":1525363200000,"foodId":"AR766369969034952000000000000000","updateUser":"testuser","updateTime":1524810000000,"userId":"testuser","weightType":"100","foodName":"嘉顿 生命面包 450g","foodCount":100,"eatType":1,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/food_small/small_photo_201512618245916435.jpg","calorie":116,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 116
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }


    public static class _$2Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c3","heatDate":1525363200000,"foodId":"AX851394083442046000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"50","foodName":"包子（三鲜馅）","foodCount":100,"eatType":2,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/0bf279b2b74d4029b5b8d6f26a1dda05.jpg","calorie":112,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c4","heatDate":1525363200000,"foodId":"BE125320884754896000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"60","foodName":"薄荷 超模25代餐粉（香草味）","foodCount":100,"eatType":2,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/upload_food/2015/4/21/1510332_1429575547small.png","calorie":223,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 335
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

    public static class _$3Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c5","heatDate":1525363200000,"foodId":"BU493756248793122000000000000000","updateUser":"testuser","updateTime":1525516858594,"userId":"testuser","weightType":"70","foodName":"面包","foodCount":100,"eatType":3,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/3b28d1a3b65d4791b4c4ce25eff85f50.jpg","calorie":375,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c6","heatDate":1525363200000,"foodId":"BX608495576866550000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"120","foodName":"通心面","foodCount":100,"eatType":3,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/food_small/s_1160655599421.jpg","calorie":370,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 745
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

    public static class _$4Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c7","heatDate":1525363200000,"foodId":"CC793863792320307000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"50","foodName":"粉丝","foodCount":100,"eatType":4,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/0211b7756f114cd594d0d335a2edaf0a.jpg","calorie":377,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 377
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

    public static class _$5Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c8","heatDate":1525363200000,"foodId":"CD526554517436856000000000000000","updateUser":"testuser","updateTime":1524810000000,"userId":"testuser","weightType":"200","foodName":"玉米片(即食粥)","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/d3c2911f07e34c97a75f56960720b32f.jpg","calorie":313,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c9","heatDate":1525363200000,"foodId":"CG505917539370454000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"320","foodName":"稻米","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/3e01a32158e941ccb328d808d6955684.jpg","calorie":102,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 415
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

    public static class _$6Bean {
        /**
         * foodList : [{"gid":"03102d9e332c46aeb0bf46bec3d1c4c8","heatDate":1525363200000,"foodId":"CD526554517436856000000000000000","updateUser":"testuser","updateTime":1524810000000,"userId":"testuser","weightType":"200","foodName":"玉米片(即食粥)","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/d3c2911f07e34c97a75f56960720b32f.jpg","calorie":313,"createUser":"testuser","foodUnit":"克"},{"gid":"03102d9e332c46aeb0bf46bec3d1c4c9","heatDate":1525363200000,"foodId":"CG505917539370454000000000000000","updateUser":"testuser","updateTime":1524800000000,"userId":"testuser","weightType":"320","foodName":"稻米","foodCount":100,"eatType":5,"createTime":1524810000000,"foodImg":"http://s2.boohee.cn/house/new_food/small/3e01a32158e941ccb328d808d6955684.jpg","calorie":102,"createUser":"testuser","foodUnit":"克"}]
         * userId : testuser
         * intake : 415
         */

        private String userId;
        private int intake;
        private List<FoodListBean> foodList;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIntake() {
            return intake;
        }

        public void setIntake(int intake) {
            this.intake = intake;
        }

        public List<FoodListBean> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListBean> foodList) {
            this.foodList = foodList;
        }

    }

}
