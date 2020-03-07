package com.lwp.xiaoyun.ec.main.sort.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.main.sort.SortDelegate;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.ui.loader.LoaderStyle;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/19 0:11
 *     desc   : 垂直列表Fragment
 * </pre>
 */
public class VerticalListDelegate extends XiaoYunDelegate {

    private List<MultipleItemEntity> data = new ArrayList<>();

    @BindView(R2.id.rv_vertical_menu_list)
    RecyclerView mRecyclerView = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_vertical_list;
    }

    private void initRecyclerView() {
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        //屏蔽动画效果
        mRecyclerView.setItemAnimator(null);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        initRecyclerView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        //预设 空数据的 adapter
        SortDelegate delegate = getParentDelegate();
        final SortRecyclerAdapter adapter = new SortRecyclerAdapter(data, delegate);
        mRecyclerView.setAdapter(adapter);

        OkHttpUtil.create()
                .loader(getContext(), LoaderStyle.BallRotateIndicator)
                .sendGetRequest("http://47.100.78.251/RestServer/api/sort_list.php", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //更新 数据
                        final String jsonString = response.body().string();
                        data = new VerticalListDataConverter().setJsonData(jsonString).convert();

                        XiaoYun.getHandler().post(new Runnable() {
                            @Override
                            public void run() {

                                //设置 新数据
                                adapter.setNewData(data);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

//        RestClient.builder()
//                .url("http://lcjxg.cn/RestServer/api/sort_list.php")
//                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                        final List<MultipleItemEntity> data =
//                                new VerticalListDataConverter().setJsonData(response).convert();
//
//                        final SortDelegate delegate = getParentDelegate();
//
//                        final SortRecyclerAdapter adapter = new SortRecyclerAdapter(data, delegate);
//                        mRecyclerView.setAdapter(adapter);
//
//                    }
//                })
//                .build()
//                .get();
    }
}
