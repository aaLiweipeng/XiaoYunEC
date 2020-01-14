package com.lwp.xiaoyun_core.ui.recycler;

import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwp.xiaoyun_core.R;
import com.lwp.xiaoyun_core.ui.banner.BannerCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/1/8 6:46
 *     desc   : BaseMultiItemQuickAdapter 是为了 适配多布局而诞生的
 *
 *              BaseMultiItemQuickAdapter<MultipleItemEntity,MultipleViewHolder>
 *              两个泛型分别是 Item数据Bean，Holder
 *
 *              使用 简单工厂模式 返回实例
 *
 * </pre>
 */
public class MultipleRecyclerAdapter extends
        BaseMultiItemQuickAdapter<MultipleItemEntity,MultipleViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup, OnItemClickListener {

    //因为Banner要只加载一次，但是RecyclerView划进划出 都会重新加载数据，
    // 所以这里整一个tag，用来定制 确保 Banner只被加载一次
    private boolean mIsInitBanner = false;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * 构造方法，设置成 protected，不被外部调用，就给下面的 简单工厂模式调用
     *
     * @param data A new list is created out of this one to avoid mutable list
     *             这里指的就是 包含了 RecycleView 的每一个Item的 数据 的List
     *
     */
    protected MultipleRecyclerAdapter(List<MultipleItemEntity> data) {
        super(data);
        //初始化，设置不同的item布局
        init();
    }

    //使用 简单工厂模式 返回实例
    public static MultipleRecyclerAdapter create(List<MultipleItemEntity> data) {
        return new MultipleRecyclerAdapter(data);
    }
    public static MultipleRecyclerAdapter create(DataConverter converter) {
        //重载   主页的数据转换类是 IndexDataConverter（extends DataConverter）
        return new MultipleRecyclerAdapter(converter.convert());
    }

    private void init() {
        //初始化，设置不同的item布局

        //addItemType(键值，布局)，调用之后，这个布局就已经添加到数组里面，
        // 查看框架源码，可以看到有一个 专门存放布局的数组 —— private SparseArray<Integer> layouts;
        // 在加载布局的时候，就会在数组里面根据其ItemType来取出相应的布局去进行渲染
        addItemType(ItemType.TEXT, R.layout.item_multiple_text);
        addItemType(ItemType.IMAGE, R.layout.item_multiple_image);
        addItemType(ItemType.TEXT_IMAGE, R.layout.item_multiple_image_text);
        addItemType(ItemType.BANNER, R.layout.item_multiple_banner);

        //设置宽度监听
        setSpanSizeLookup(this);
        openLoadAnimation();// To open the animation when loading 打开加载时的动画效果
        //多次执行动画
        //true just show anim when first loading
        // false show anim when load the data every time！！！
        isFirstOnly(false);
    }

    /**
     * 本方法乃是 onBindViewHolder()中的一个 逻辑处理单元
     * 注意这个设计模式！！！
     *
     * BaseQuickAdapter 中 写了一个 抽象方法 ——
     *  Implement this method and use the helper to adapt the view to the given item.
     *  param helper A fully initialized helper.
     *  param item   The item that needs to be displayed.
     *
     *  protected abstract void convert(K helper, T item);
     *  然后在 BaseQuickAdapter.onBindViewHolder() 中 抽象调用，在本类中具体实现！！！！！
     * -----------------------------------------------------------------------
     * public void onBindViewHolder(K holder, int positions)
     *
     * BaseQuickAdapter.onBindViewHolder() 中 把 holder 跟 数据 联系起来了
     *     —— autoLoadMore(positions);
     *         int viewType = holder.getItemViewType();
     *     convert(holder, mData.get(holder.getLayoutPosition() - getHeaderLayoutCount()));
     *
     *     这里抽象调用了 convert() 并传入了两个参数，参数的值，
     *     就对应传给本类的 convert(MultipleViewHolder holder, MultipleItemEntity entity) 的两个参数
     *     ！！！onBindViewHolder()是每次RecyclerView划进划出 都会调用，刷新加载数据，
     *     两个参数的意义，即
     * @param holder createBaseViewHolder() 设置了 MultipleViewHolder
     *               滑到的某个子项itemView 对应的 ViewHolder，应该在createBaseViewHolder中创建了实例；
     * @param entity 滑到的某个子项itemView 对应的 数据bean
     *                所有的Item的 总的数据 在本类的 实例创造方法create()处 传入
     */
    @Override
    protected void convert(MultipleViewHolder holder, MultipleItemEntity entity) {

        final String text;
        final String imageUrl;
        final ArrayList<String> bannerImages;

        //getItemViewType 是 RecyclerView.ViewHolder中的方法，
        //并在RecyclerView.createViewHolder()中 被赋值 具体可以查看源码
        switch (holder.getItemViewType()) {
            //getItemViewType()拿到的，就是我们在 init()中 设置进去时候的 四个键值 ItemType 之一.
            // 具体是哪一个 ItemType,就要看Holder对应的 ItemView了

            case ItemType.TEXT:
                text = entity.getField(MultipleFields.TEXT);
                holder.setText(R.id.text_single, text);
                //这里可以看一下 holder.setText() 的源码，
                // 源码中是通过ViewId（一参）拿到View，然后把文本（二参。text）设置进去
                break;

            case ItemType.IMAGE:
                imageUrl = entity.getField(MultipleFields.IMAGE_URL);
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存方式使用全缓存，原始图片和剪裁过后的图片都会被缓存起来
                        .dontAnimate()//这里已经有RecyclerView的动画，不需要图片的动画了
                        .centerCrop()
                        .into((ImageView) holder.getView(R.id.img_single));
                        //这里可以看一下 holder.getView()的源码 ，
                        // 是通过id 来返回一个 泛型View实例
                break;

            case ItemType.TEXT_IMAGE:
                text = entity.getField(MultipleFields.TEXT);
                imageUrl = entity.getField(MultipleFields.IMAGE_URL);

                holder.setText(R.id.tv_multiple, text);
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存方式使用全缓存，原始图片和剪裁过后的图片都会被缓存起来
                        .dontAnimate()//这里已经有RecyclerView的动画，不需要图片的动画了
                        .centerCrop()
                        .into((ImageView) holder.getView(R.id.img_multiple));
                break;

            case ItemType.BANNER:
                if (!mIsInitBanner) {
                    bannerImages = entity.getField(MultipleFields.BANNERS);
                    final ConvenientBanner<String> convenientBanner = holder.getView(R.id.banner_recycler_item);
                    //ConvenientBanner 同理用法见 LauncherScrollDelegate

                    //配置banner轮播图
                    BannerCreator.setDefault(convenientBanner,bannerImages,this);
                    mIsInitBanner = true;//加载完了设置为TRUE，下次不再加载
                }
                break;

            default:
                break;
        }
    }

    // 来自 BaseQuickAdapter
    ///**
    //     * if you want to use subclass of BaseViewHolder in the adapter,
    //     * you must override the method to create new ViewHolder.
    //     *
    //     * @param view view
    //     * @return new ViewHolder
    //     */
    //
    //    @SuppressWarnings("unchecked")
    //    protected K createBaseViewHolder(View view) {
    //        Class temp = getClass();
    //        Class z = null;
    //        while (z == null && null != temp) {
    //            z = getInstancedGenericKClass(temp);
    //            temp = temp.getSuperclass();
    //        }
    //        K k = createGenericKInstance(z, view);
    //        return null != k ? k : (K) new BaseViewHolder(view);
    //    }

    //return一个我们定义的ViewHolder
    // 把 view实例传进去这个 ViewHolder实例中,
    @Override
    protected MultipleViewHolder createBaseViewHolder(View view) {
        return MultipleViewHolder.create(view);
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        //getData() 获取的数据 来自于 构造方法，Items数据List
        // get(position)拿到某个Item的数据，
        // getField(MultipleFields.SPAN_SIZE) 拿到这个item 的 SPAN_SIZE
        return getData().get(position).getField(MultipleFields.SPAN_SIZE);
    }

    //子项点击方法
    @Override
    public void onItemClick(int position) {

    }
}
