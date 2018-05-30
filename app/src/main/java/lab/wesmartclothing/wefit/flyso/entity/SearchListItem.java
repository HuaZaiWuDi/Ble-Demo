package lab.wesmartclothing.wefit.flyso.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jk on 2018/5/9.
 */
public class SearchListItem implements Serializable{


    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 1
     * endRow : 10
     * total : 32
     * pages : 4
     * list : [{"gid":"AI242381575293153000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"顶味 鸡蛋面","foodImg":"http://s2.boohee.cn/house/upload_food/2008/6/14/56796_1213424197small.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":358,"unitCount":100,"unit":"克","description":"","remark":"","sort":967,"userId":null},{"gid":"AR766369969034952000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"嘉顿 生命面包 450g","foodImg":"http://s2.boohee.cn/house/food_small/small_photo_201512618245916435.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":284,"unitCount":100,"unit":"克","description":"","remark":"","sort":293,"userId":null},{"gid":"BU493756248793122000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"面包","foodImg":"http://s2.boohee.cn/house/new_food/small/3b28d1a3b65d4791b4c4ce25eff85f50.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":313,"unitCount":100,"unit":"克","description":"","remark":"","sort":68,"userId":null},{"gid":"BX608495576866550000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"通心面","foodImg":"http://s2.boohee.cn/house/food_small/s_1160655599421.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":351,"unitCount":100,"unit":"克","description":"","remark":"","sort":968,"userId":null},{"gid":"DM718317389832221000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"面包条（白）","foodImg":"http://s2.boohee.cn/house/new_food/small/00745cd4110f47bdac586e232386730e.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a09","typeName":"零食、点心、冷饮","heat":283,"unitCount":100,"unit":"克","description":"","remark":"","sort":743,"userId":null},{"gid":"DP471151713380836000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"全麦面包","foodImg":"http://s2.boohee.cn/house/new_food/small/59e4b55ed0354e90b40fb368b2eb80eb.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":246,"unitCount":100,"unit":"克","description":"","remark":"","sort":294,"userId":null},{"gid":"EW651824458541807000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"咸面包","foodImg":"http://s2.boohee.cn/house/new_food/small/765b395b939c4edf865613b1df4005a0.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":275,"unitCount":100,"unit":"克","description":"","remark":"","sort":599,"userId":null},{"gid":"FZ136159375265487000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"水面筋（泡发后）","foodImg":"http://s2.boohee.cn/house/new_food/small/aedc99f00e9e4eaba34f4bcf264fcee7.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":142,"unitCount":100,"unit":"克","description":"","remark":"","sort":983,"userId":null},{"gid":"GW461116652607506000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"果料面包","foodImg":"http://s2.boohee.cn/house/new_food/small/539005b854de498eb09d15e494feb5f3.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a02","typeName":"蛋类、肉类及制品","heat":279,"unitCount":100,"unit":"克","description":"","remark":"","sort":300,"userId":null},{"gid":"IJ964945178074115000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"面条(煮)","foodImg":"http://s2.boohee.cn/house/food_small/small_photo_20152415513316.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":110,"unitCount":100,"unit":"克","description":"","remark":"","sort":298,"userId":null}]
     * prePage : 0
     * nextPage : 2
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2,3,4]
     * navigateFirstPage : 1
     * navigateLastPage : 4
     * lastPage : 4
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

}
