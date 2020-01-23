package com.lwp.xiaoyun.ec.main.sort.content;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;

import java.util.List;

import butterknife.BindView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/19 0:29
 *     desc   :
 * </pre>
 */
public class ContentDelegate extends XiaoYunDelegate {

    //左侧的list 每点击一个Item
    // 右侧要切换到哪一个RecyclerView的布局，这里用一个ID来控制，

    // list中点击的时候，就newInstance()，
    // 创建一个 ContentDelegate实例 的同时，把ID传进来 ContentDelegate，供其加载
    private static final String ARG_CONTENT_ID = "CONTENT_ID";//封装Bundle的键
    private int mContentId = -1;

    //接收 服务器返回来并经过Converter处理的 数据
    private List<SectionBean> mData = null;

    @BindView(R2.id.rv_list_content)
    RecyclerView mRecyclerView = null;


    public static ContentDelegate newInstance(int contentId) {

        //简单工厂创建实例，这里乃是Fragment实用的的创建方法
        //传进来的Id会转换成Bundle，存储在Delegate实例中，然后返回这个实例，
        // 供外部调用，传进来Id！！！
        final Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_ID,contentId);

        final ContentDelegate delegate = new ContentDelegate();
        delegate.setArguments(args);//Fragment类中 有一个全局成员Bundle mArguments;可以拿来玩
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = getArguments();//把 newInstance()中设置的 bundle 拿出来
        if (args != null) {
            //如果有id传进来了
            mContentId = args.getInt(ARG_CONTENT_ID);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_list_content;
    }


    private void initData() {
        RestClient.builder()
                .url("http://lcjxg.cn/RestServer/api/sort_content_list.php?contentId=" + mContentId)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
//                        mData = new SectionDataConverter().convert(response);
//                        final SectionAdapter sectionAdapter =
//                                new SectionAdapter(R.layout.item_section_content,
//                                        R.layout.item_section_header, mData);
//                        mRecyclerView.setAdapter(sectionAdapter);
                    }
                })
                .build()
                .get();
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

        //初始化 RecyclerView
        //一参 多少列；二参 形态
        final StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        initData();
    }
}
