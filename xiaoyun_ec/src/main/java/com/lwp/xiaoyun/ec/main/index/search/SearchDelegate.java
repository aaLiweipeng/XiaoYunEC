package com.lwp.xiaoyun.ec.main.index.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.StringUtils;
import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.storage.XiaoyunPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/14 0:01
 *     desc   : 搜索框
 * </pre>
 */
public class SearchDelegate extends XiaoYunDelegate {

    private SearchAdapter adapter;
    private List<MultipleItemEntity> data = new ArrayList<>();

    @BindView(R2.id.rv_search)
    RecyclerView mRecyclerView = null;//用来 显示历史记录 的RecyclerView
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchEdit = null;//搜索框

    //搜索文本组件
    @OnClick(R2.id.tv_top_search)
    void onCliclSearch() {

        OkHttpUtil.create()
                .loader(getContext())
                .sendGetRequest("http://47.100.78.251/RestServer/api/search.php", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        XiaoYun.getHandler().post(new Runnable() {
                            //切到主线程！！！
                            @Override
                            public void run() {
                                //把搜索记录存储到SP文件
                                final String searchItemText = mSearchEdit.getText().toString();
                                saveItem(searchItemText);
                                mSearchEdit.setText("");

                                adapter.setNewData(new SearchDataConverter().convert());
                                adapter.notifyDataSetChanged();

                                //后面可以跳到另外一个Delegate
                            }
                        });
                    }
                });

//        RestClient.builder()
//                .url("search.php?key=")
//                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                        final String searchItemText = mSearchEdit.getText().toString();
//                        saveItem(searchItemText);
//                        mSearchEdit.setText("");
//                        //展示一些东西
//                        //弹出一段话
//                    }
//                })
//                .build()
//                .get();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        adapter = new SearchAdapter(data);
        mRecyclerView.setAdapter(adapter);

        //分隔线配置
        final DividerItemDecoration itemDecoration = new DividerItemDecoration();
        itemDecoration.setDividerLookup(new DividerItemDecoration.DividerLookup() {
            @Override
            public Divider getVerticalDivider(int position) {
                //纵向分割线
                return null;
            }
            @Override
            public Divider getHorizontalDivider(int position) {
                return new Divider.Builder()
                        .size(2)
                        .margin(20, 20)
                        .color(Color.GRAY)
                        .build();
            }
        });
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    //返回箭头图标组件
    @OnClick(R2.id.icon_top_search_back)
    void onClickBack() {
        getSupportDelegate().pop();
    }

    @SuppressWarnings("unchecked")
    private void saveItem(String item) {
        if (!StringUtils.isEmpty(item) && !StringUtils.isSpace(item)) {

            List<String> history;
            final String historyStr =
                    XiaoyunPreference.getCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY);

            if (StringUtils.isEmpty(historyStr)) {
                history = new ArrayList<>();
            } else {
                history = JSON.parseObject(historyStr, ArrayList.class);//JSON字符串 转成 List
            }
            history.add(item);
            final String json = JSON.toJSONString(history);//List 转成 JSON字符串

            //把json存储到 SharePreference，在 SearchDataConverter 中使用 get
            XiaoyunPreference.addCustomAppProfile(SearchDataConverter.TAG_SEARCH_HISTORY, json);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_search;
    }


}
