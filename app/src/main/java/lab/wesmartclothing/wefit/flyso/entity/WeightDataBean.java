package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/5/15.
 */
public class WeightDataBean {


    /**
     * weightList : {"pageNum":1,"pageSize":10,"size":10,"startRow":1,"endRow":10,"total":97,"pages":10,"list":[{"gid":"f57bc94702b146549127acdf4dd70067","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1542470400000,"measureTime":1542470400000,"weight":79.8,"bmi":54,"bmr":2270.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70010","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1542643200000,"measureTime":1542643200000,"weight":79.8,"bmi":54,"bmr":1700.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70033","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1543075200000,"measureTime":1543075200000,"weight":79.8,"bmi":54,"bmr":1930.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70056","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1543593600000,"measureTime":1543593600000,"weight":79.8,"bmi":54,"bmr":2160.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70079","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544025600000,"measureTime":1544025600000,"weight":79.8,"bmi":54,"bmr":2390.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70022","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544284800000,"measureTime":1544284800000,"weight":79.8,"bmi":54,"bmr":1820.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70045","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544716800000,"measureTime":1544716800000,"weight":79.8,"bmi":54,"bmr":2050.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70068","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545235200000,"measureTime":1545235200000,"weight":79.8,"bmi":54,"bmr":2280.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70011","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545408000000,"measureTime":1545408000000,"weight":79.8,"bmi":54,"bmr":1710.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70034","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545840000000,"measureTime":1545840000000,"weight":79.8,"bmi":54,"bmr":1940.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0}],"prePage":0,"nextPage":2,"isFirstPage":true,"isLastPage":false,"hasPreviousPage":false,"hasNextPage":true,"navigatePages":8,"navigatepageNums":[1,2,3,4,5,6,7,8],"navigateFirstPage":1,"navigateLastPage":8,"lastPage":8,"firstPage":1}
     * idealWeight : 49.5
     */

    private WeightListBean weightList;
    private double targetWeight;
    private double normWeight;
    private double complete;
    private int hasDays;
    private double initialWeight;
    private boolean targetSet;
    private double stillNeed;
    private double weight;


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getStillNeed() {
        return stillNeed;
    }

    public void setStillNeed(double stillNeed) {
        this.stillNeed = stillNeed;
    }

    public WeightListBean getWeightList() {
        return weightList;
    }

    public void setWeightList(WeightListBean weightList) {
        this.weightList = weightList;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public double getComplete() {
        return complete;
    }

    public void setComplete(double complete) {
        this.complete = complete;
    }

    public int getHasDays() {
        return hasDays;
    }

    public void setHasDays(int hasDays) {
        this.hasDays = hasDays;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public boolean isTargetSet() {
        return targetSet;
    }

    public void setTargetSet(boolean targetSet) {
        this.targetSet = targetSet;
    }

    public double getNormWeight() {
        return normWeight;
    }

    public void setNormWeight(double normWeight) {
        this.normWeight = normWeight;
    }

    public static class WeightListBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * size : 10
         * startRow : 1
         * endRow : 10
         * total : 97
         * pages : 10
         * list : [{"gid":"f57bc94702b146549127acdf4dd70067","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1542470400000,"measureTime":1542470400000,"weight":79.8,"bmi":54,"bmr":2270.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70010","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1542643200000,"measureTime":1542643200000,"weight":79.8,"bmi":54,"bmr":1700.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70033","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1543075200000,"measureTime":1543075200000,"weight":79.8,"bmi":54,"bmr":1930.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70056","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1543593600000,"measureTime":1543593600000,"weight":79.8,"bmi":54,"bmr":2160.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70079","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544025600000,"measureTime":1544025600000,"weight":79.8,"bmi":54,"bmr":2390.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70022","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544284800000,"measureTime":1544284800000,"weight":79.8,"bmi":54,"bmr":1820.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70045","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1544716800000,"measureTime":1544716800000,"weight":79.8,"bmi":54,"bmr":2050.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70068","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545235200000,"measureTime":1545235200000,"weight":79.8,"bmi":54,"bmr":2280.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70011","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545408000000,"measureTime":1545408000000,"weight":79.8,"bmi":54,"bmr":1710.2,"bodyType":"标准型","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0},{"gid":"f57bc94702b146549127acdf4dd70034","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1525941840000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1525941840000,"pageNum":null,"pageSize":null,"userId":"testuser","weightDate":1545840000000,"measureTime":1545840000000,"weight":79.8,"bmi":54,"bmr":1940.2,"bodyType":"偏瘦","bodyAge":54,"bone":10.6,"muscle":24.8,"sinew":25,"protein":31.5,"bodyFat":21.7,"bodyFfm":54,"subfat":31.9,"visfat":21.9,"flesh":24,"water":85.6,"healthScore":0}]
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
         * lastPage : 8
         * firstPage : 1
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
        private int lastPage;
        private int firstPage;
        private List<HealthyInfoBean> list;
        private List<Integer> navigatepageNums;

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

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public int getFirstPage() {
            return firstPage;
        }

        public void setFirstPage(int firstPage) {
            this.firstPage = firstPage;
        }

        public List<HealthyInfoBean> getList() {
            return list;
        }

        public void setList(List<HealthyInfoBean> list) {
            this.list = list;
        }

        public List<Integer> getNavigatepageNums() {
            return navigatepageNums;
        }

        public void setNavigatepageNums(List<Integer> navigatepageNums) {
            this.navigatepageNums = navigatepageNums;
        }

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

    }
}
