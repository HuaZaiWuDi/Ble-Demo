package lab.dxythch.com.commonproject.ui.main.slimming.weight;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lab.dxythch.com.commonproject.R;
import lab.dxythch.com.commonproject.base.BaseFragment;
import lab.dxythch.com.commonproject.entity.WeightInfoItem;
import lab.dxythch.com.netlib.net.RetrofitService;
import lab.dxythch.com.netlib.rx.NetManager;
import lab.dxythch.com.netlib.rx.RxManager;
import lab.dxythch.com.netlib.rx.RxNetSubscriber;
import okhttp3.RequestBody;

/**
 * Created by jk on 2018/5/7.
 */
@EFragment(R.layout.fragment_weight)
public class WeightFragment extends BaseFragment {

    public static WeightFragment getInstance() {
        return new WeightFragment_();
    }

    @ViewById
    RecyclerView mRecyclerView;

    private List<WeightInfoItem> weightLists = new ArrayList<>();
    private BaseQuickAdapter adapter;

    @Override
    public void initData() {
        String[] weight_title = getResources().getStringArray(R.array.weight_title);
        int[] img_icons = {R.mipmap.ic_age, R.mipmap.ic_bodyfat, R.mipmap.ic_bmr, R.mipmap.ic_habitus,
                R.mipmap.ic_ffm, R.mipmap.ic_weight, R.mipmap.ic_musculoskeletal, R.mipmap.ic_muscle,
                R.mipmap.ic_protein, R.mipmap.ic_water, R.mipmap.ic_subfat, R.mipmap.ic_bone,
                R.mipmap.ic_visfat, R.mipmap.ic_bmi};

        for (int i = 0; i < weight_title.length; i = i + 2) {
            WeightInfoItem item = new WeightInfoItem();
            item.setTitle_left(weight_title[i]);
            item.setTitle_right(weight_title[i + 1]);
            item.setImg_left(img_icons[i]);
            item.setImg_right(img_icons[i + 1]);
            weightLists.add(item);
        }
        adapter.setNewData(weightLists);

        getWeightData();
    }


    @Click
    void icon_weight() {
        RxActivityUtils.skipActivity(mActivity, WeightDataActivity_.class);
    }

    @AfterViews
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new BaseQuickAdapter<WeightInfoItem, BaseViewHolder>(R.layout.item_body_info) {

            @Override
            protected void convert(BaseViewHolder helper, WeightInfoItem item) {
                helper.setText(R.id.tv_left, item.getData_left());
                helper.setText(R.id.tv_right, item.getData_right());
                helper.setText(R.id.title_left, item.getTitle_left());
                helper.setText(R.id.title_right, item.getTitle_right());
                helper.setImageResource(R.id.img_left, item.getImg_left());
                helper.setImageResource(R.id.img_right, item.getImg_right());
            }
        };
        mRecyclerView.setAdapter(adapter);
    }


    private void getWeightData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "c82e9e7612a447358c2a82ef437f3d11");
            jsonObject.put("pageNum", 1);
            jsonObject.put("pageSize", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        RetrofitService dxyService = NetManager.getInstance().createString(RetrofitService.class);
        RxManager.getInstance().doNetSubscribe(dxyService.getWeightInfo(body))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tipDialog.show();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        tipDialog.dismiss();
                    }
                })
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("结束：" + s);
//                        FoodInfoItem item = new Gson().fromJson(s, FoodInfoItem.class);
//                        List<ListBean> beans = item.getList();
//                        adapter.setNewData(beans);
                    }

                    @Override
                    protected void _onError(String error) {
                        RxToast.error(error);
                    }
                });
    }

}
