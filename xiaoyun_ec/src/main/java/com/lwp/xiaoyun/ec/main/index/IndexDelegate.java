package com.lwp.xiaoyun.ec.main.index;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.delegates.bottom.BottomItemDelegate;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.ui.recycler.MultipleFields;
import com.lwp.xiaoyun_core.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.ui.refresh.RefreshHandler;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/4 3:25
 *     desc   :
 * </pre>
 */
public class IndexDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_index)
    RecyclerView mRecyclerView = null;
    @BindView(R2.id.srl_index)
    SwipeRefreshLayout mRefreshLayout = null;
    @BindView(R2.id.tb_index)
    Toolbar mToolbar = null;
    @BindView(R2.id.icon_index_scan)
    IconTextView mIconScan = null;
    @BindView(R2.id.et_search_view)
    AppCompatEditText mSearchView = null;

    private RefreshHandler mRefreshHandler = null;
//    private static int HEHE = 0;

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mRefreshHandler = RefreshHandler.create(mRefreshLayout, mRecyclerView, new IndexDataConverter());
//        if (HEHE == 0) {
//            OkHttpUtil.sendOkHttpRequest("http://lcjxg.cn/RestServer/api/index.php", new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    final IndexDataConverter converter = new IndexDataConverter();
//                    converter.setJsonData(response.body().string());
//
//                    //拿到 转换结果——包含了 每一个Item数据 的 数据List
//                    final ArrayList<MultipleItemEntity> list = converter.convert();
//                    //取出 第二个Item数据来测试
//                    final String image = list.get(1).getField(MultipleFields.IMAGE_URL);
//                    //测试成功
//                    Looper.prepare();
//                    Toast.makeText(getContext(), image, Toast.LENGTH_SHORT).show();
//                    Looper.loop();// 进入loop中的循环，查看消息队列
//
//                }
//            });
//            HEHE++;
//        }

    }

    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_purple,
                android.R.color.holo_blue_bright
        );
        //一参数：使得下拉过程中，加载动画球会 由小变大，
        // 界面往上回弹的时候，又会 由大变小；
        // 二三参指的是 动画球的起始高度和终止高度
        mRefreshLayout.setProgressViewOffset(true,120,300);
    }

    private void initRecyclerView() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        mRecyclerView.setLayoutManager(manager);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRefreshLayout();
        initRecyclerView();
        mRefreshHandler.firstPage("http://lcjxg.cn/RestServer/api/index.php");
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_index;
    }
}
