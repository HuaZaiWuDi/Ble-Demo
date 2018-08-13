package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created by jk on 2018/8/10.
 */
public class DeviceListbean {


    /**
     * pageNum : 1
     * pageSize : 10
     * size : 2
     * startRow : 1
     * endRow : 2
     * total : 2
     * pages : 1
     * list : [{"gid":"4937180f9d2343f28b5a33d9645aa4be","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1528782325000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1528782325000,"userId":"c82e9e7612a447358c2a82ef437f3d11","productId":"65b1ad3eb4e04b81bb7aa08cb6718a42","productName":"体脂称","deviceNo":"ZS-TZC-0001","macAddr":"FB:F9:DB:0E:13:CC","linkStatus":1,"bindStatus":1,"bindTime":1528782325000,"onlineDuration":1800,"lastOnlineTime":1528782325000},{"gid":"a664230c3c3840caae4a4363c5cf1b3b","status":101,"createUser":"c82e9e7612a447358c2a82ef437f3d11","createTime":1528782373000,"updateUser":"c82e9e7612a447358c2a82ef437f3d11","updateTime":1528782373000,"userId":"c82e9e7612a447358c2a82ef437f3d11","productId":"c5da09c6cd184aba8e16358aa8fa9d26","productName":"瘦身衣","deviceNo":"ZS-SSY-0001","macAddr":"FB:F9:DB:0E:13:BC","linkStatus":1,"bindStatus":1,"bindTime":1528782373000,"onlineDuration":1800,"lastOnlineTime":1528782373000}]
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
     * firstPage : 1
     * lastPage : 1
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
         * gid : 4937180f9d2343f28b5a33d9645aa4be
         * status : 101
         * createUser : c82e9e7612a447358c2a82ef437f3d11
         * createTime : 1528782325000
         * updateUser : c82e9e7612a447358c2a82ef437f3d11
         * updateTime : 1528782325000
         * userId : c82e9e7612a447358c2a82ef437f3d11
         * productId : 65b1ad3eb4e04b81bb7aa08cb6718a42
         * productName : 体脂称
         * deviceNo : ZS-TZC-0001
         * macAddr : FB:F9:DB:0E:13:CC
         * linkStatus : 1
         * bindStatus : 1
         * bindTime : 1528782325000
         * onlineDuration : 1800
         * lastOnlineTime : 1528782325000
         */

        private String gid;
        private int status;
        private String createUser;
        private long createTime;
        private String updateUser;
        private long updateTime;
        private String userId;
        private String productId;
        private String productName;
        private String deviceNo;
        private String macAddr;
        private int linkStatus;
        private int bindStatus;
        private long bindTime;
        private int onlineDuration;
        private long lastOnlineTime;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getMacAddr() {
            return macAddr;
        }

        public void setMacAddr(String macAddr) {
            this.macAddr = macAddr;
        }

        public int getLinkStatus() {
            return linkStatus;
        }

        public void setLinkStatus(int linkStatus) {
            this.linkStatus = linkStatus;
        }

        public int getBindStatus() {
            return bindStatus;
        }

        public void setBindStatus(int bindStatus) {
            this.bindStatus = bindStatus;
        }

        public long getBindTime() {
            return bindTime;
        }

        public void setBindTime(long bindTime) {
            this.bindTime = bindTime;
        }

        public int getOnlineDuration() {
            return onlineDuration;
        }

        public void setOnlineDuration(int onlineDuration) {
            this.onlineDuration = onlineDuration;
        }

        public long getLastOnlineTime() {
            return lastOnlineTime;
        }

        public void setLastOnlineTime(long lastOnlineTime) {
            this.lastOnlineTime = lastOnlineTime;
        }
    }
}
