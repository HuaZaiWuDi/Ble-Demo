package lab.wesmartclothing.wefit.flyso.entity.sql;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.List;

/**
 * Created icon_hide_password Jack on 2018/5/9.
 */
@Table(name = "SearchKeyTabs")
public class SearchKeyTab extends Model {


    @Column(name = "addTime")
    public long addTime = 0; // 添加时间

    @Column(name = "searchKey")
    public String searchKey = ""; // 搜索词


    public SearchKeyTab() {
        super();
    }

    public SearchKeyTab(long addTime, String searchKey) {
        this.addTime = addTime;
        this.searchKey = searchKey;
    }

    public static List<SearchKeyTab> getAll() {
        return new Select()
                .from(SearchKeyTab.class)
                .orderBy("Id ASC")
                .execute();
    }

    public static List<SearchKeyTab> getKey(String searchKey) {
        return new Select()
                .from(SearchKeyTab.class)
                .where("searchKey", searchKey)
                .execute();
    }

    public static void update(String addTime, String searchKey) {
        new Update(SearchKeyTab.class)
                .set("addTime", addTime)
                .where("searchKey", searchKey)
                .execute();
    }

    //按时间排序，倒序
    public static List<SearchKeyTab> soft(List<SearchKeyTab> all) {
        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < all.size(); j++) {
                if (all.get(i).addTime > all.get(j).addTime) {
                    SearchKeyTab temp = all.get(j);
                    all.set(j, all.get(i));
                    all.set(i, temp);
                }
            }
        }
        return all;
    }


    public static void deleteAll() {
        new Delete().from(SearchKeyTab.class).execute();
    }


}
