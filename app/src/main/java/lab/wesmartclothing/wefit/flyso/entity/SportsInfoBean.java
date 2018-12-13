package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/5/18.
 */
public class SportsInfoBean {


    /**
     * duration : 497
     * athleticsList : {"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":200,"pages":20,"list":[{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526918400000,"athlDesc":null,"calorie":4895,"duration":497,"avgHeart":107,"minHeart":58,"maxHeart":184,"stepNumber":11649,"kilometers":5,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526832000000,"athlDesc":null,"calorie":1384,"duration":1304,"avgHeart":154,"minHeart":54,"maxHeart":260,"stepNumber":19285,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526745600000,"athlDesc":null,"calorie":2462,"duration":1123,"avgHeart":105,"minHeart":94,"maxHeart":182,"stepNumber":6069,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526659200000,"athlDesc":null,"calorie":4638,"duration":2002,"avgHeart":111,"minHeart":82,"maxHeart":177,"stepNumber":18559,"kilometers":1,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526572800000,"athlDesc":null,"calorie":4961,"duration":875,"avgHeart":116,"minHeart":55,"maxHeart":272,"stepNumber":8892,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526486400000,"athlDesc":null,"calorie":4015,"duration":2949,"avgHeart":121,"minHeart":54,"maxHeart":231,"stepNumber":7447,"kilometers":5,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526400000000,"athlDesc":null,"calorie":4801,"duration":3662,"avgHeart":119,"minHeart":90,"maxHeart":181,"stepNumber":6228,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526313600000,"athlDesc":null,"calorie":3143,"duration":2157,"avgHeart":109,"minHeart":100,"maxHeart":210,"stepNumber":5842,"kilometers":2,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526227200000,"athlDesc":null,"calorie":3193,"duration":3761,"avgHeart":110,"minHeart":78,"maxHeart":276,"stepNumber":9582,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526140800000,"athlDesc":null,"calorie":241,"duration":4152,"avgHeart":116,"minHeart":66,"maxHeart":284,"stepNumber":17475,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null}],"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"navigateFirstPage":1,"navigateLastPage":8,"firstPage":1,"lastPage":8}
     * kilometers : 5
     * calorie : 4895
     * athlDate : 1526918400000
     * maxHeart : 184
     * stepNumber : 11649
     * minHeart : 58
     * avgHeart : 107
     */

    private int duration;
    private AthleticsListBean athleticsList;
    private int kilometers;
    private int calorie;
    private long athlDate;
    private int maxHeart;
    private int stepNumber;
    private int minHeart;
    private int avgHeart;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public AthleticsListBean getAthleticsList() {
        return athleticsList;
    }

    public void setAthleticsList(AthleticsListBean athleticsList) {
        this.athleticsList = athleticsList;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public long getAthlDate() {
        return athlDate;
    }

    public void setAthlDate(long athlDate) {
        this.athlDate = athlDate;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public void setMaxHeart(int maxHeart) {
        this.maxHeart = maxHeart;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public int getMinHeart() {
        return minHeart;
    }

    public void setMinHeart(int minHeart) {
        this.minHeart = minHeart;
    }

    public int getAvgHeart() {
        return avgHeart;
    }

    public void setAvgHeart(int avgHeart) {
        this.avgHeart = avgHeart;
    }

    public static class AthleticsListBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * total : 200
         * pages : 20
         * list : [{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526918400000,"athlDesc":null,"calorie":4895,"duration":497,"avgHeart":107,"minHeart":58,"maxHeart":184,"stepNumber":11649,"kilometers":5,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526832000000,"athlDesc":null,"calorie":1384,"duration":1304,"avgHeart":154,"minHeart":54,"maxHeart":260,"stepNumber":19285,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526745600000,"athlDesc":null,"calorie":2462,"duration":1123,"avgHeart":105,"minHeart":94,"maxHeart":182,"stepNumber":6069,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526659200000,"athlDesc":null,"calorie":4638,"duration":2002,"avgHeart":111,"minHeart":82,"maxHeart":177,"stepNumber":18559,"kilometers":1,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526572800000,"athlDesc":null,"calorie":4961,"duration":875,"avgHeart":116,"minHeart":55,"maxHeart":272,"stepNumber":8892,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526486400000,"athlDesc":null,"calorie":4015,"duration":2949,"avgHeart":121,"minHeart":54,"maxHeart":231,"stepNumber":7447,"kilometers":5,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526400000000,"athlDesc":null,"calorie":4801,"duration":3662,"avgHeart":119,"minHeart":90,"maxHeart":181,"stepNumber":6228,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526313600000,"athlDesc":null,"calorie":3143,"duration":2157,"avgHeart":109,"minHeart":100,"maxHeart":210,"stepNumber":5842,"kilometers":2,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526227200000,"athlDesc":null,"calorie":3193,"duration":3761,"avgHeart":110,"minHeart":78,"maxHeart":276,"stepNumber":9582,"kilometers":6,"fileRoute":null,"useStartTime":null,"useEndTime":null},{"gid":null,"status":null,"createUser":null,"createTime":null,"updateUser":null,"updateTime":null,"pageNum":null,"pageSize":null,"userId":null,"athlDate":1526140800000,"athlDesc":null,"calorie":241,"duration":4152,"avgHeart":116,"minHeart":66,"maxHeart":284,"stepNumber":17475,"kilometers":3,"fileRoute":null,"useStartTime":null,"useEndTime":null}]
         * prePage : 0
         * nextPage : 2
         * isFirstPage : true
         * isLastPage : false
         * hasPreviousPage : false
         * hasNextPage : true
         * navigatePages : 8
         * navigatepageNums : [1,2,3,4,5,6,7,8]
         * navigateFirstPage : 1
         * navigateLastPage : 8
         * firstPage : 1
         * lastPage : 8
         */

        private int pageNum;
        private int pageSize;
        private int size;
        private int startRow;
        private int endRow;
        private int total;
        private int pages;
        private int prePage;
        private int nextPage;
        private boolean isFirstPage;
        private boolean isLastPage;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
        private int navigatePages;
        private int navigateFirstPage;
        private int navigateLastPage;
        private int firstPage;
        private int lastPage;
        private List<ListBean> list;
        private List<Integer> navigatepageNums;

        public boolean isFirstPage() {
            return isFirstPage;
        }

        public void setFirstPage(boolean firstPage) {
            isFirstPage = firstPage;
        }

        public boolean isLastPage() {
            return isLastPage;
        }

        public void setLastPage(boolean lastPage) {
            isLastPage = lastPage;
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

        public int getEndRow() {
            return endRow;
        }

        public void setEndRow(int endRow) {
            this.endRow = endRow;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
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

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
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

        public boolean isHasPreviousPage() {
            return hasPreviousPage;
        }

        public void setHasPreviousPage(boolean hasPreviousPage) {
            this.hasPreviousPage = hasPreviousPage;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public int getNavigatePages() {
            return navigatePages;
        }

        public void setNavigatePages(int navigatePages) {
            this.navigatePages = navigatePages;
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

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
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
             * gid : null
             * status : null
             * createUser : null
             * createTime : null
             * updateUser : null
             * updateTime : null
             * pageNum : null
             * pageSize : null
             * userId : null
             * athlDate : 1526918400000
             * athlDesc : null
             * calorie : 4895
             * duration : 497
             * avgHeart : 107
             * minHeart : 58
             * maxHeart : 184
             * stepNumber : 11649
             * kilometers : 5
             * fileRoute : null
             * useStartTime : null
             * useEndTime : null
             */

            private Object gid;
            private Object status;
            private Object createUser;
            private Object createTime;
            private Object updateUser;
            private Object updateTime;
            private Object pageNum;
            private Object pageSize;
            private Object userId;
            private long athlDate;
            private Object athlDesc;
            private int calorie;
            private int duration;
            private int avgHeart;
            private int minHeart;
            private int maxHeart;
            private int stepNumber;
            private int kilometers;
            private Object fileRoute;
            private Object useStartTime;
            private Object useEndTime;

            public Object getGid() {
                return gid;
            }

            public void setGid(Object gid) {
                this.gid = gid;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getCreateUser() {
                return createUser;
            }

            public void setCreateUser(Object createUser) {
                this.createUser = createUser;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
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

            public Object getUserId() {
                return userId;
            }

            public void setUserId(Object userId) {
                this.userId = userId;
            }

            public long getAthlDate() {
                return athlDate;
            }

            public void setAthlDate(long athlDate) {
                this.athlDate = athlDate;
            }

            public Object getAthlDesc() {
                return athlDesc;
            }

            public void setAthlDesc(Object athlDesc) {
                this.athlDesc = athlDesc;
            }

            public int getCalorie() {
                return calorie;
            }

            public void setCalorie(int calorie) {
                this.calorie = calorie;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getAvgHeart() {
                return avgHeart;
            }

            public void setAvgHeart(int avgHeart) {
                this.avgHeart = avgHeart;
            }

            public int getMinHeart() {
                return minHeart;
            }

            public void setMinHeart(int minHeart) {
                this.minHeart = minHeart;
            }

            public int getMaxHeart() {
                return maxHeart;
            }

            public void setMaxHeart(int maxHeart) {
                this.maxHeart = maxHeart;
            }

            public int getStepNumber() {
                return stepNumber;
            }

            public void setStepNumber(int stepNumber) {
                this.stepNumber = stepNumber;
            }

            public int getKilometers() {
                return kilometers;
            }

            public void setKilometers(int kilometers) {
                this.kilometers = kilometers;
            }

            public Object getFileRoute() {
                return fileRoute;
            }

            public void setFileRoute(Object fileRoute) {
                this.fileRoute = fileRoute;
            }

            public Object getUseStartTime() {
                return useStartTime;
            }

            public void setUseStartTime(Object useStartTime) {
                this.useStartTime = useStartTime;
            }

            public Object getUseEndTime() {
                return useEndTime;
            }

            public void setUseEndTime(Object useEndTime) {


                this.useEndTime = useEndTime;
            }


        }
    }
}
