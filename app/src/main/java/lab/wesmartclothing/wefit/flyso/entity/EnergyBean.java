package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName EnergyBean
 * @Date 2018/11/2 18:33
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class EnergyBean {


    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{"athlCalorie":1,"athlCount":1,"athlPlan":0,"basalCalorie":1,"createTime":1511248354000,"createUser":1,"dietPlan":0,"duration":1,"gid":1,"heatCalorie":1,"heatCount":1,"recordDate":"2018-11-02T10:40:58.527Z","status":101,"updateTime":1511248354000,"updateUser":1,"userId":"string","weight":1,"weightCount":1}]
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
         * athlCalorie : 1
         * athlCount : 1
         * athlPlan : 0
         * basalCalorie : 1
         * createTime : 1511248354000
         * createUser : 1
         * dietPlan : 0
         * duration : 1
         * gid : 1
         * heatCalorie : 1
         * heatCount : 1
         * recordDate : 2018-11-02T10:40:58.527Z
         * status : 101
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * weight : 1
         * weightCount : 1
         */

        private int athlCalorie;
        private int heatCalorie;
        private long recordDate;
        private int basalCalorie;


        public int getBasalCalorie() {
            return basalCalorie;
        }

        public void setAthlCalorie(int athlCalorie) {
            this.athlCalorie = athlCalorie;
        }

        public int getAthlCalorie() {
            return athlCalorie;
        }

        public int getHeatCalorie() {
            return heatCalorie;
        }

        public long getRecordDate() {
            return recordDate;
        }
    }
}
