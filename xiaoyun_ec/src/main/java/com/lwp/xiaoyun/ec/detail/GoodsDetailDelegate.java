package com.lwp.xiaoyun.ec.detail;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.DefaultTransformer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.YoYo;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ec.main.sort.content.SectionDataConverter;
import com.lwp.xiaoyun.ui.animation.BezierAnimation;
import com.lwp.xiaoyun.ui.animation.BezierUtil;
import com.lwp.xiaoyun.ui.banner.HolderCreator;
import com.lwp.xiaoyun.ui.widget.CircleTextView;
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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/17 21:14
 *     desc   : 商品详情页
 * </pre>
 */
public class GoodsDetailDelegate extends XiaoYunDelegate implements
        AppBarLayout.OnOffsetChangedListener,
         BezierUtil.AnimationListener
{

    @BindView(R2.id.goods_detail_toolbar)
    Toolbar mToolbar = null;

    //TabLayout ViewPager 组合
    @BindView(R2.id.tab_layout)
    TabLayout mTabLayout = null;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager = null;

    // 折叠标题栏
    // 折叠栏中的滚动图
    @BindView(R2.id.detail_banner)
    ConvenientBanner<String> mBanner = null;
    @BindView(R2.id.collapsing_toolbar_detail)
    CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    @BindView(R2.id.app_bar_detail)
    AppBarLayout mAppBar = null;

    //底部
    @BindView(R2.id.icon_favor)//“喜欢”图标
    IconTextView mIconFavor = null;
    @BindView(R2.id.tv_shopping_cart_amount)
    CircleTextView mCircleTextView = null;//购物车数量TextView
    @BindView(R2.id.rl_add_shop_cart)
    RelativeLayout mRlAddShopCart = null;//“加入购物车”按钮的 父布局（相对布局）
    @BindView(R2.id.icon_shop_cart)
    IconTextView mIconShopCart = null;//购物车图标

    //用作 Bundle的键
    private static final String ARG_GOODS_ID = "ARG_GOODS_ID";
    //用于 接收Bundle的值
    private int mGoodsId = -1;

    //目标图片Url
    private String mGoodsThumbUrl = null;
    //购物车数量TextView
    private int mShopCount = 0;

    //“加入购物车”按钮的 父布局（相对布局）点击监听
    @OnClick(R2.id.rl_add_shop_cart)
    void onClickAddShopCart() {

        final CircleImageView animImg = new CircleImageView(getContext());
        Glide.with(this)
                .load(mGoodsThumbUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .override(100, 100)
                .into(animImg);

        //购物车飞入动画
        BezierAnimation.addCart(this, mRlAddShopCart, mIconShopCart, animImg, this);
    }

    private void setShopCartCount(JSONObject data) {

        mGoodsThumbUrl = data.getString("thumb");

        if (mShopCount == 0) {
            mCircleTextView.setVisibility(View.GONE);
        }
    }

    //工厂方法，创建一个 GoodsDetailDelegate实例，用于跳转
    // 而形参 【int】goodsId，是跳转时 由调用者 传过来的数据，
    // 这里 暴露给外部使用
    // 用例见 IndexItemClickListener
    public static GoodsDetailDelegate create(int goodsId) {

        final Bundle args = new Bundle();
        args.putInt(ARG_GOODS_ID, goodsId);

        final GoodsDetailDelegate delegate = new GoodsDetailDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取出Bundle
        final Bundle args = getArguments();
        if (args != null) {
            mGoodsId = args.getInt(ARG_GOODS_ID);
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_goods_detail;
    }

    //初始化布局***************************************************************************
    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        //折叠时显示的颜色
        mCollapsingToolbarLayout.setContentScrimColor(
                ContextCompat.getColor(getContext(), R.color.app_main));

        //AppBarLayout 添加滚动事件监听
        mAppBar.addOnOffsetChangedListener(this);
        //设置购物车数量图标背景颜色
        mCircleTextView.setCircleBackground(
                ContextCompat.getColor(getContext(), R.color.app_main));

        //初始化各种UI和数据
        initData();
        initTabLayout();
    }
    //初始化布局***************************************************************************

    private void initTabLayout() {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//平均分配空间，不可滑动
        mTabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(getContext(), R.color.app_main));//指示器颜色
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));//字体颜色
        mTabLayout.setBackgroundColor(Color.WHITE);//背景色
        mTabLayout.setupWithViewPager(mViewPager);//配置ViewPager！！！！！！1
    }
    private void initData() {

        OkHttpUtil.create()
                .loader(getContext())
                .sendGetRequest(
                        "http://47.100.78.251/RestServer/api/goods_detail.php?goods_id=" + mGoodsId,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getContext(), "没有对应页面数据或者网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //子线程！！！
                                final String jsonString = response.body().string();
                                XiaoYun.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {

                                        //主线程
                                        final JSONObject data =
                                                JSON.parseObject(jsonString).getJSONObject("data");

                                        initBanner(data);//初始化商品详情页中Banner的数据
                                        initGoodsInfo(data);//初始化商品详细文本信息
                                        initPager(data);//Tab、ViewPager更新
                                        setShopCartCount(data);//购物车
                                    }
                                });
                            }
                        });

//        RestClient.builder()
//                .url("goods_detail.php")
//                .params("goods_id", mGoodsId)
//                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                        final JSONObject data =
//                                JSON.parseObject(response).getJSONObject("data");
//
//                        initBanner(data);
//                        initGoodsInfo(data);
//                        initPager(data);
//                        setShopCartCount(data);
//                    }
//                })
//                .build()
//                .get();
    }

    //初始化商品详情页中Banner的数据
    private void initBanner(JSONObject data) {
        final JSONArray array = data.getJSONArray("banners");

        //图片的Url数组
        final List<String> images = new ArrayList<>();
        final int size = array.size();
        for (int i = 0; i < size; i++) {
            images.add(array.getString(i));
        }

        mBanner
                .setPages(new HolderCreator(), images)
                .setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPageTransformer(new DefaultTransformer())
                .startTurning(3000)
                .setCanLoop(true);
    }
    //初始化商品详细文本信息
    private void initGoodsInfo(JSONObject data) {
        final String goodsData = data.toJSONString();
        //**********************************************************************
        //frame_goods_info 是详情页中的 FrameLayout，以下，把 GoodsInfoDelegate 动态加载到 FrameLayout上
        getSupportDelegate().
                loadRootFragment(R.id.frame_goods_info, GoodsInfoDelegate.create(goodsData));
    }
    private void initPager(JSONObject data) {
        final PagerAdapter adapter = new TabPagerAdapter(getFragmentManager(), data);
        mViewPager.setAdapter(adapter);
    }

    //AppBarLayout 添加滚动事件监听
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    //BezierUtil 框架中的接口方法
    // 处理动画结束后的逻辑，不要涉及动画相关的View，
    // 这里是 飞入动画结束后 回调本方法
    @Override
    public void onAnimationEnd() {

        //弹性伸缩动画
        YoYo.with(new ScaleUpAnimator())
                .duration(500)
                .playOn(mIconShopCart);

        //更新数量数据
        OkHttpUtil.create()
                .addPostKV("count", mShopCount)
                .sendPostRequest(
                        "http://47.100.78.251/RestServer/api/add_shop_cart_count.php",
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }
                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                //子线程！！！
                                final String jsonString = response.body().string();

                                XiaoYun.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        XiaoYunLogger.json("ADD", jsonString);

                                        final boolean isAdded = JSON.parseObject(jsonString).getBoolean("data");
                                        //isAdded 为TRUE 指服务器更新成功 返回确定信息，可以更新客户端UI
                                        if (isAdded) {

                                            mShopCount++;
                                            mCircleTextView.setVisibility(View.VISIBLE);//可见
                                            mCircleTextView.setText(String.valueOf(mShopCount));//更新数据
                                        }
                                    }
                                });
                            }
                        });

//        //更新数量数据
//        RestClient.builder()
//                .url("add_shop_cart_count.php")
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        XiaoYunLogger.json("ADD", response);
//
//                        final boolean isAdded = JSON.parseObject(response).getBoolean("data");
//                        if (isAdded) {
//
//                            mShopCount++;
//                            mCircleTextView.setVisibility(View.VISIBLE);//可见
//                            mCircleTextView.setText(String.valueOf(mShopCount));//更新数据
//                        }
//                    }
//                })
//                .params("count", mShopCount)
//                .build()
//                .post();
    }

//    @Override
//    public FragmentAnimator onCreateFragmentAnimator() {
//        //设定横向转场切入动画
//        return new DefaultHorizontalAnimator();
//    }
}




