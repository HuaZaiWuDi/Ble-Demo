package lab.wesmartclothing.wefit.flyso.entity;

/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName FoodTypeBean
 * @Date 2018/11/20 14:18
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class FoodTypeBean {


    /**
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * sort : 1
     * status : 101
     * typeName : string
     * typeNo : string
     * updateTime : 1511248354000
     * updateUser : 1
     */

    private long createTime;
    private String gid;
    private int sort;
    private int status;
    private String typeName;
    private String typeNo;
    private long updateTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

}
