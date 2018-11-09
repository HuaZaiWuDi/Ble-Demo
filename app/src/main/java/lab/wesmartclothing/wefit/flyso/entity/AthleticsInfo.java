package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/7/25.
 */
public class AthleticsInfo {


    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{"athlList":[{"age":18,"athlDate":"2018-11-05T03:36:11.641Z","athlDesc":"string","athlRecord":"string","athlScore":0,"avgHeart":1,"birthday":"2018-11-05T03:36:11.641Z","calorie":1,"createTime":1511248354000,"createUser":1,"duration":1,"endTime":"2018-11-05T03:36:11.641Z","gid":1,"heartCount":1,"height":170,"kilometers":1,"maxHeart":1,"minHeart":1,"planFlag":0,"sex":1,"startTime":"2018-11-05T03:36:11.641Z","status":101,"stepNumber":1,"updateTime":1511248354000,"updateUser":1,"userId":"string"}],"dayAthl":{"age":18,"athlDate":"2018-11-05T03:36:11.641Z","athlDesc":"string","athlRecord":"string","avgHeart":1,"birthday":"2018-11-05T03:36:11.641Z","calorie":1,"createTime":1511248354000,"createUser":1,"duration":1,"gid":1,"heartCount":1,"height":170,"kilometers":1,"maxHeart":1,"minHeart":1,"planflag":0,"sex":1,"status":101,"stepNumber":1,"updateTime":1511248354000,"updateUser":1,"userId":"string"}}]
     * navigateFirstPage : 0
     * navigateLastPage : 0
     * navigatePages : 0
     * navigatepageNums : [0]
     * nextPage : 0
     * pageNum : 0
     * pageSize : 0
     * pages : 0
     * prePage : 0
     * size : 0
     * startRow : 0
     * total : 0
     */

    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int navigatePages;
    private int nextPage;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;
    private int startRow;
    private int total;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * athlList : [{"age":18,"athlDate":"2018-11-05T03:36:11.641Z","athlDesc":"string","athlRecord":"string","athlScore":0,"avgHeart":1,"birthday":"2018-11-05T03:36:11.641Z","calorie":1,"createTime":1511248354000,"createUser":1,"duration":1,"endTime":"2018-11-05T03:36:11.641Z","gid":1,"heartCount":1,"height":170,"kilometers":1,"maxHeart":1,"minHeart":1,"planFlag":0,"sex":1,"startTime":"2018-11-05T03:36:11.641Z","status":101,"stepNumber":1,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
         * dayAthl : {"age":18,"athlDate":"2018-11-05T03:36:11.641Z","athlDesc":"string","athlRecord":"string","avgHeart":1,"birthday":"2018-11-05T03:36:11.641Z","calorie":1,"createTime":1511248354000,"createUser":1,"duration":1,"gid":1,"heartCount":1,"height":170,"kilometers":1,"maxHeart":1,"minHeart":1,"planflag":0,"sex":1,"status":101,"stepNumber":1,"updateTime":1511248354000,"updateUser":1,"userId":"string"}
         */

        private DayAthlBean dayAthl;
        private List<AthlListBean> athlList;

        public DayAthlBean getDayAthl() {
            return dayAthl;
        }

        public void setDayAthl(DayAthlBean dayAthl) {
            this.dayAthl = dayAthl;
        }

        public List<AthlListBean> getAthlList() {
            return athlList;
        }

        public void setAthlList(List<AthlListBean> athlList) {
            this.athlList = athlList;
        }

        public static class DayAthlBean {
            /**
             * age : 18
             * athlDate : 2018-11-05T03:36:11.641Z
             * athlDesc : string
             * athlRecord : string
             * avgHeart : 1
             * birthday : 2018-11-05T03:36:11.641Z
             * calorie : 1
             * createTime : 1511248354000
             * createUser : 1
             * duration : 1
             * gid : 1
             * heartCount : 1
             * height : 170
             * kilometers : 1
             * maxHeart : 1
             * minHeart : 1
             * planflag : 0
             * sex : 1
             * status : 101
             * stepNumber : 1
             * updateTime : 1511248354000
             * updateUser : 1
             * userId : string
             */

            private long athlDate;
            private String athlDesc;
            private String athlRecord;
            private int avgHeart;
            private int calorie;
            private int duration;
            private int heartCount;
            private int height;
            private int kilometers;
            private int maxHeart;
            private int minHeart;
            private int planflag;
            private int stepNumber;

            public long getAthlDate() {
                return athlDate;
            }

            public String getAthlDesc() {
                return athlDesc;
            }

            public String getAthlRecord() {
                return athlRecord;
            }

            public int getAvgHeart() {
                return avgHeart;
            }

            public int getCalorie() {
                return calorie;
            }

            public int getDuration() {
                return duration;
            }

            public int getHeartCount() {
                return heartCount;
            }

            public int getHeight() {
                return height;
            }

            public int getKilometers() {
                return kilometers;
            }

            public int getMaxHeart() {
                return maxHeart;
            }

            public int getMinHeart() {
                return minHeart;
            }

            public int getPlanflag() {
                return planflag;
            }

            public int getStepNumber() {
                return stepNumber;
            }
        }

        public static class AthlListBean {
            /**
             * age : 18
             * athlDate : 2018-11-05T03:36:11.641Z
             * athlDesc : string
             * athlRecord : string
             * athlScore : 0
             * avgHeart : 1
             * birthday : 2018-11-05T03:36:11.641Z
             * calorie : 1
             * createTime : 1511248354000
             * createUser : 1
             * duration : 1
             * endTime : 2018-11-05T03:36:11.641Z
             * gid : 1
             * heartCount : 1
             * height : 170
             * kilometers : 1
             * maxHeart : 1
             * minHeart : 1
             * planFlag : 0//0是自由运动，1是课程运动
             * sex : 1
             * startTime : 2018-11-05T03:36:11.641Z
             * status : 101
             * stepNumber : 1
             * updateTime : 1511248354000
             * updateUser : 1
             * userId : string
             */

            private String gid;
            private long athlDate;
            private String athlDesc;
            private String athlRecord;
            private int athlScore;
            private int avgHeart;
            private int calorie;
            private int duration;
            private long endTime;
            private int heartCount;
            private int height;
            private int kilometers;
            private int maxHeart;
            private int minHeart;
            private int planFlag;
            private long startTime;
            private int stepNumber;

            public String getGid() {
                return gid;
            }

            public long getAthlDate() {
                return athlDate;
            }

            public String getAthlDesc() {
                return athlDesc;
            }

            public String getAthlRecord() {
                return athlRecord;
            }

            public int getAthlScore() {
                return athlScore;
            }

            public int getAvgHeart() {
                return avgHeart;
            }

            public int getCalorie() {
                return calorie;
            }

            public int getDuration() {
                return duration;
            }

            public long getEndTime() {
                return endTime;
            }

            public int getHeartCount() {
                return heartCount;
            }

            public int getHeight() {
                return height;
            }

            public int getKilometers() {
                return kilometers;
            }

            public int getMaxHeart() {
                return maxHeart;
            }

            public int getMinHeart() {
                return minHeart;
            }

            public int getPlanFlag() {
                return planFlag;
            }

            public long getStartTime() {
                return startTime;
            }

            public int getStepNumber() {
                return stepNumber;
            }
        }
    }
}
