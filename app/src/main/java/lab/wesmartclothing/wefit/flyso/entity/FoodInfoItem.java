package lab.wesmartclothing.wefit.flyso.entity;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/5/9.
 */
public class FoodInfoItem {


    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * startRow : 1
     * endRow : 10
     * total : 112
     * pages : 12
     * list : [{"gid":"159a406c8b5247eebb6d34b677d6cf57","status":101,"updateUser":"123","createUser":"123","updateTime":1525835728804,"createTime":1525835728804,"pageNum":null,"pageSize":null,"foodName":"米饭","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":100,"unitCount":1,"unit":"碗","description":"","remark":"一碗米饭约100卡","sort":0,"userId":null},{"gid":"3d6a6a1a0fb846368762630964e7e5d4","status":101,"updateUser":"123","createUser":"123","updateTime":1525765537551,"createTime":1525765537551,"pageNum":null,"pageSize":null,"foodName":"小米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":100,"unitCount":2,"unit":"克","description":"","remark":"一碗等于200千卡","sort":0,"userId":null},{"gid":"507fa1c947584ee8962466f6dfb6ad92","status":101,"updateUser":"123","createUser":"123","updateTime":1525768668099,"createTime":1525768668099,"pageNum":null,"pageSize":null,"foodName":"大米大米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":100,"unitCount":100,"unit":"g","description":"","remark":"12","sort":0,"userId":null},{"gid":"5d6971f55c5f4c62bb67271880b79195","status":101,"updateUser":"123","createUser":"123","updateTime":1525766293679,"createTime":1525766293679,"pageNum":null,"pageSize":null,"foodName":"深海猪油","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"食用油、油脂及制品","heat":99999999,"unitCount":1111,"unit":"吨","description":"","remark":"11111","sort":0,"userId":null},{"gid":"60a259ff6690460dacab9e6884e28bd6","status":101,"updateUser":"123","createUser":"123","updateTime":1525772119616,"createTime":1525772119616,"pageNum":null,"pageSize":null,"foodName":"大米大大米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":1111111111,"unitCount":10,"unit":"毫升","description":"","remark":"1","sort":0,"userId":null},{"gid":"6ab31289a0b34f7ca0f48cc813f1a061","status":101,"updateUser":"123","createUser":"123","updateTime":1525772093840,"createTime":1525772093840,"pageNum":null,"pageSize":null,"foodName":"大米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":11,"unitCount":10,"unit":"毫升","description":"","remark":"11","sort":0,"userId":null},{"gid":"72ac87d0941446558cb5fc3e270c23d4","status":101,"updateUser":"123","createUser":"123","updateTime":1525772107829,"createTime":1525770023427,"pageNum":null,"pageSize":null,"foodName":"大米大大米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":1,"unitCount":10,"unit":"毫升","description":"","remark":"1","sort":0,"userId":null},{"gid":"77ad0db77cf3464086c166a9aaf2d5d0","status":101,"updateUser":"123","createUser":"123","updateTime":1525765318796,"createTime":1525765318796,"pageNum":null,"pageSize":null,"foodName":"籼米粉","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":356,"unitCount":100,"unit":"克","description":"","remark":"334","sort":0,"userId":null},{"gid":"86f26ab8ec6f46d386391ba8122cb427","status":101,"updateUser":"123","createUser":"123","updateTime":1525745766753,"createTime":1525745766753,"pageNum":null,"pageSize":null,"foodName":"米","foodImg":"","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":100,"unitCount":1,"unit":"碗","description":"","remark":"好吃","sort":0,"userId":null},{"gid":"AF706637337299045000000000000000","status":101,"updateUser":"userid0001","createUser":"userid0001","updateTime":1525510000000,"createTime":1525510000000,"pageNum":null,"pageSize":null,"foodName":"鱼丸","foodImg":"http://s2.boohee.cn/house/new_food/small/bf70d65d96e840e490bdb4e2cae28327.jpg","typeId":"6cec5aef380f4374af8cb6a2ef453a01","typeName":"谷薯芋、杂豆、主食","heat":107,"unitCount":100,"unit":"克","description":"","remark":"","sort":80,"userId":null}]
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
