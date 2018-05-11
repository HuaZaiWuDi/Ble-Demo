package lab.dxythch.com.commonproject.entity;

import java.util.List;

/**
 * Created by jk on 2018/5/9.
 */
public class HotKeyItem {


    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 0
     * endRow : 9
     * total : 10
     * pages : 1
     * list : [{"gid":"LC171897200277601000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"薏米","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277602000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"葡萄干面包","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277604000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"籼米粉","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277605000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"油条","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277606000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"挂面","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277607000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"黄油面包","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277608000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"绿豆","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277609000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"芋头","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277610000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"麦维面包","searchcount":0,"flag":1,"keyType":""},{"gid":"LC171897200277611000000000000000","status":101,"createUser":"search_user","createTime":1525685250000,"updateUser":"search_user","updateTime":1525685250000,"pageNum":null,"pageSize":null,"keyWord":"黑面包","searchcount":0,"flag":1,"keyType":""}]
     * prePage : 0
     * nextPage : 0
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     * navigateFirstPage : 1
     * navigateLastPage : 1
     * lastPage : 1
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
    private List<ListBean> list;
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
         * gid : LC171897200277601000000000000000
         * status : 101
         * createUser : search_user
         * createTime : 1525685250000
         * updateUser : search_user
         * updateTime : 1525685250000
         * pageNum : null
         * pageSize : null
         * keyWord : 薏米
         * searchcount : 0
         * flag : 1
         * keyType :
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private Object pageNum;
        private Object pageSize;
        private String keyWord;
        private int searchcount;
        private int flag;
        private String keyType;

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

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public int getSearchcount() {
            return searchcount;
        }

        public void setSearchcount(int searchcount) {
            this.searchcount = searchcount;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getKeyType() {
            return keyType;
        }

        public void setKeyType(String keyType) {
            this.keyType = keyType;
        }
    }
}
