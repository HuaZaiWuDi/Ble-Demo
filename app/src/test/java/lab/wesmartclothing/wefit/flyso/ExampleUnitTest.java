package lab.wesmartclothing.wefit.flyso;

import org.junit.Test;

import lab.wesmartclothing.wefit.flyso.rxbus.SportsDataTab;
import lab.wesmartclothing.wefit.flyso.utils.FixSizeLinkedList;
import lab.wesmartclothing.wefit.flyso.utils.HeartRateUtil;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private FixSizeLinkedList<Double> mLinkedList = new FixSizeLinkedList<>(5);

    /**
     * 测试配速公式的计算
     */
    @Test
    public void peisu() {
        //配速
        int step = 0;
        HeartRateUtil heartRateUtil = new HeartRateUtil();
        for (int i = 0; i < 1000; i++) {
            step += Math.random() * 10;

            SportsDataTab sportsDataTab = heartRateUtil.addRealTimeData(step);
            if (sportsDataTab == null) {
                continue;
            }
            System.out.println("配速：" + sportsDataTab.toString());
        }
    }
}
