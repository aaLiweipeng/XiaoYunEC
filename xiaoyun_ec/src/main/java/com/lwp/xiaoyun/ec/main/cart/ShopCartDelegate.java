package com.lwp.xiaoyun.ec.main.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.main.sort.content.SectionDataConverter;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/7 16:33
 *     desc   :
 * </pre>
 */
public class ShopCartDelegate extends BottomItemDelegate {

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_cart;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        OkHttpUtil.build()
                .sendOkHttpRequest(
                        "http://lcjxg.cn/RestServer/api/shop_cart.php",
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getContext(), "没有对应页面数据或者网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //更新 数据
                                final String jsonString = response.body().string();
                                successHandler(jsonString);
                            }
                        });

//        RestClient.builder()
//                .url("shop_cart.php")
//                .loader(getContext())
//                .success(this)
//                .build()
//                .get();
    }

    private void successHandler(final String responseString) {
        //                                mData = new SectionDataConverter().convert(jsonString);
        XiaoYun.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), responseString, Toast.LENGTH_SHORT).show();
//                                        //设置 新数据
//                                        sectionAdapter.setNewData(mData);
//                                        sectionAdapter.notifyDataSetChanged();
            }
        });
    }

//    @Override
//    public void onSuccess(String response) {
//        final ArrayList<MultipleItemEntity> data =
//                new ShopCartDataConverter()
//                        .setJsonData(response)
//                        .convert();
//        mAdapter = new ShopCartAdapter(data);
//        mAdapter.setCartItemListener(this);
//        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setAdapter(mAdapter);
//        mTotalPrice = mAdapter.getTotalPrice();
//        mTvTotalPrice.setText(String.valueOf(mTotalPrice));
//        checkItemCount();
//    }
}
