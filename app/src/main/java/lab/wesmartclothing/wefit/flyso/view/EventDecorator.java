package lab.wesmartclothing.wefit.flyso.view;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.vondear.rxtools.utils.RxUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * @Package lab.wesmartclothing.wefit.flyso.view
 * @FileName EventDecorator
 * @Date 2018/10/27 16:40
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return dates.contains(calendarDay);
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
        dayViewFacade.addSpan(new DotSpan(RxUtils.dp2px(2.5f), color));
    }
}
