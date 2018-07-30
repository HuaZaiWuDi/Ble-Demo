package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/7/25.
 */
public class AthleticsInfo {


    /**
     * needAthl : 0
     * pageInfo : {"endRow":0,"firstPage":0,"hasNextPage":true,"hasPreviousPage":true,"isFirstPage":true,"isLastPage":true,"lastPage":0,"list":[{"athlDate":"2018-07-26T09:23:30.401Z","athlDesc":"string","athlRecord":"string","avgHeart":0,"calorie":0,"createTime":1511248354000,"createUser":1,"duration":0,"gid":1,"kilometers":0,"maxHeart":0,"minHeart":0,"status":101,"stepNumber":0,"updateTime":1511248354000,"updateUser":1,"userId":"string"}],"navigateFirstPage":0,"navigateLastPage":0,"navigatePages":0,"navigatepageNums":[0],"nextPage":0,"pageNum":0,"pageSize":0,"pages":0,"prePage":0,"size":0,"startRow":0,"total":0}
     * targetSet : true
     */

    private int needAthl;
    private PageInfoBean pageInfo;
    private boolean targetSet;

    public int getNeedAthl() {
        return needAthl;
    }

    public void setNeedAthl(int needAthl) {
        this.needAthl = needAthl;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public boolean isTargetSet() {
        return targetSet;
    }

    public void setTargetSet(boolean targetSet) {
        this.targetSet = targetSet;
    }

    public static class PageInfoBean {
        /**
         * endRow : 0
         * firstPage : 0
         * hasNextPage : true
         * hasPreviousPage : true
         * isFirstPage : true
         * isLastPage : true
         * lastPage : 0
         * list : [{"athlDate":"2018-07-26T09:23:30.401Z","athlDesc":"string","athlRecord":"string","avgHeart":0,"calorie":0,"createTime":1511248354000,"createUser":1,"duration":0,"gid":1,"kilometers":0,"maxHeart":0,"minHeart":0,"status":101,"stepNumber":0,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
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
             * athlDate : 2018-07-26T09:23:30.401Z
             * athlDesc : string
             * athlRecord : string
             * avgHeart : 0
             * calorie : 0
             * createTime : 1511248354000
             * createUser : 1
             * duration : 0
             * gid : 1
             * kilometers : 0
             * maxHeart : 0
             * minHeart : 0
             * status : 101
             * stepNumber : 0
             * updateTime : 1511248354000
             * updateUser : 1
             * userId : string
             */

            private long athlDate;
            private String athlDesc;
            private String athlRecord;
            private int avgHeart;
            private int calorie;
            private long createTime;
            private int createUser;
            private int duration;
            private String gid;
            private int kilometers;
            private int maxHeart;
            private int minHeart;
            private int status;
            private int stepNumber;
            private long updateTime;
            private int updateUser;
            private String userId;

            public long getAthlDate() {
                return athlDate;
            }

            public void setAthlDate(long athlDate) {
                this.athlDate = athlDate;
            }

            public String getAthlDesc() {
                return athlDesc;
            }

            public void setAthlDesc(String athlDesc) {
                this.athlDesc = athlDesc;
            }

            public String getAthlRecord() {
                return athlRecord;
            }

            public void setAthlRecord(String athlRecord) {
                this.athlRecord = athlRecord;
            }

            public int getAvgHeart() {
                return avgHeart;
            }

            public void setAvgHeart(int avgHeart) {
                this.avgHeart = avgHeart;
            }

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getCreateUser() {
                return createUser;
            }

            public void setCreateUser(int createUser) {
                this.createUser = createUser;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getGid() {
                return gid;
            }

            public void setGid(String gid) {
                this.gid = gid;
            }

            public int getKilometers() {
                return kilometers;
            }

            public void setKilometers(int kilometers) {
                this.kilometers = kilometers;
            }

            public int getMaxHeart() {
                return maxHeart;
            }

            public void setMaxHeart(int maxHeart) {
                this.maxHeart = maxHeart;
            }

            public int getMinHeart() {
                return minHeart;
            }

            public void setMinHeart(int minHeart) {
                this.minHeart = minHeart;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getStepNumber() {
                return stepNumber;
            }

            public void setStepNumber(int stepNumber) {
                this.stepNumber = stepNumber;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public int getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(int updateUser) {
                this.updateUser = updateUser;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
