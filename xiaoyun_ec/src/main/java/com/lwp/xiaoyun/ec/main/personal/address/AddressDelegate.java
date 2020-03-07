package com.lwp.xiaoyun.ec.main.personal.address;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnLongClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/6 4:40
 *     desc   : 收货地址管理页
 * </pre>
 */
public class AddressDelegate extends XiaoYunDelegate implements ISuccess {

    private List<MultipleItemEntity> data = new ArrayList<>();
    final AddressAdapter mAddressAdapter = new AddressAdapter(data);

    @BindView(R2.id.rv_address)
    RecyclerView mRecyclerView = null;

    @OnLongClick(R2.id.icon_address_add)
    boolean onLongClickAddAddress() {
        OkHttpUtil.create()
                .loader(getContext())
                .sendGetRequest("http://47.100.78.251/RestServer/api/address_recover.php", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //子线程！！！
                        String jsonString = response.body().string();
                        XiaoYunLogger.d("AddressDelegate", jsonString);
                        data = new AddressDataConverter().setJsonData(jsonString).convert();

                        XiaoYun.getHandler().post(new Runnable() {
                            //切到主线程！！！
                            @Override
                            public void run() {

                                mAddressAdapter.setNewData(data);
                                mAddressAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
        return false;
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_address;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAddressAdapter);

        OkHttpUtil.create()
                .loader(getContext())
                .sendGetRequest("http://47.100.78.251/RestServer/api/address.php", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        //子线程！！！
                        String jsonString = response.body().string();
                        XiaoYunLogger.d("AddressDelegate", jsonString);
                        data = new AddressDataConverter().setJsonData(jsonString).convert();

                        XiaoYun.getHandler().post(new Runnable() {
                            //切到主线程！！！
                            @Override
                            public void run() {

                                mAddressAdapter.setNewData(data);
                                mAddressAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

//        RestClient.builder()
//                .url("address.php")
//                .loader(getContext())
//                .success(this)
//                .build()
//                .get();
    }

    @Override
    public void onSuccess(String response) {
        XiaoYunLogger.d("AddressDelegate", response);
        data = new AddressDataConverter().setJsonData(response).convert();
        mAddressAdapter.setNewData(data);
        mAddressAdapter.notifyDataSetChanged();
    }
}
