package lab.wesmartclothing.wefit.flyso.entity.sql;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created icon_hide_password jk on 2018/5/11.
 */
@Table(name = "SearchWordTabs")
public class SearchWordTab extends Model {

    @Column
    public long addTime = 0; // 添加时间

    @Column
    public String searchKey = ""; // 搜索词


    public SearchWordTab() {
        super();
    }

    public SearchWordTab(long addTime, String searchKey) {
        this.addTime = addTime;
        this.searchKey = searchKey;
    }


    public static List<SearchWordTab> getAll() {
        return new Select()
                .from(SearchWordTab.class)
                .orderBy("Id ASC")
                .execute();
    }

    public static SearchWordTab getKey(String searchKey) {
        return new Select()
                .from(SearchWordTab.class)
                .where("searchKey = ?", searchKey)
                .orderBy("RANDOM()")
                .executeSingle();
    }

    public static void update(long addTime, String searchKey) {
        new Update(SearchWordTab.class)
                .set("addTime = ?", addTime)
                .where("searchKey = ?", searchKey)
                .execute();
    }

    //按时间排序，倒序
    public static List<SearchWordTab> soft(List<SearchWordTab> all) {
        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < all.size(); j++) {
                if (all.get(i).addTime > all.get(j).addTime) {
                    SearchWordTab temp = all.get(j);
                    all.set(j, all.get(i));
                    all.set(i, temp);
                }
            }
        }
        return all;
    }

    public static boolean isExist(String string) {
        List<SearchWordTab> all = getAll();
        for (SearchWordTab tab : all) {
            if (string.equals(tab.searchKey)) {
                return true;
            }
        }
        return false;
    }

    public static void deleteAll() {
        new Delete().from(SearchWordTab.class).execute();
    }


    @Override
    public String toString() {
        return "SearchWordTab{" +
                "addTime=" + addTime +
                ", searchKey='" + searchKey + '\'' +
                '}';
    }
}
