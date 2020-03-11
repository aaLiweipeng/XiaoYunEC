package com.lwp.xiaoyun.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joanzapata.iconify.widget.IconTextView;
import com.lwp.ui.R;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import java.util.ArrayList;

/**
 * <pre>
 *     time   : 2020/3/11 1:37
 *     desc   :
 *              initAddIcon() --> onMeasure() --> onLayout()
 * </pre>
 */
public final class AutoPhotoLayout extends LinearLayoutCompat {

    //判断是第几个
    private int mCurrentNum = 0;
    //最大容下几张图
    private final int mMaxNum;
    //一行 几张图
    private final int mMaxLineNum;
    //加号图标
    private IconTextView mIconAdd = null;// 加号图标组件实例
    private static final String ICON_TEXT = "{fa-plus}";//加号图标
    private final float mIconSize;//图标大小
    //布局参数
    private LayoutParams mParams = null;
    //要删除的图片ID
    private int mDeleteId = 0;
    //选中的图片
    private AppCompatImageView mTargetImageVew = null;
    //图片之间的间隔
    private final int mImageMargin;
    //用于方便操作图片  传进来 OrderCommentDelegate实例
    private XiaoYunDelegate mDelegate = null;
    //一行有几张图，存在一个List中
    private ArrayList<View> mLineViews = null;

    private AlertDialog mTargetDialog = null;
    //存储所有的View
    // 一行一行地存，所以泛型为 ArrayList<View>
    private final ArrayList<ArrayList<View>> ALL_VIEWS = new ArrayList<>();
    //每一行的高度
    private final ArrayList<Integer> LINE_HEIGHTS = new ArrayList<>();

    //防止多次的测量和布局过程
    private boolean mIsOnceInitOnMeasure = false;
    private boolean mHasInitOnLayout = false;


    //处理成 最终只调用 一个构造方法
    public AutoPhotoLayout(Context context) {
        this(context, null);
    }
    public AutoPhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public AutoPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //从xml文件中 获得 自定义属性 —— obtainStyledAttributes()
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.camera_flow_layout);
        //把 四个自定义属性 转换过来，并为之 配置默认值
        mMaxNum = typedArray.getInt(R.styleable.camera_flow_layout_max_count, 1);
        mMaxLineNum = typedArray.getInt(R.styleable.camera_flow_layout_line_count, 3);
        mImageMargin = typedArray.getInt(R.styleable.camera_flow_layout_item_margin, 0);
        mIconSize = typedArray.getDimension(R.styleable.camera_flow_layout_icon_size, 20);
        //进行资源回收，避免内存泄漏
        typedArray.recycle();
    }

    //用于方便操作图片  传进来 OrderCommentDelegate实例
    public final void setDelegate(XiaoYunDelegate delegate) {
        this.mDelegate = delegate;
    }


    public final void onCropTarget(Uri uri) {
        createNewImageView();

        Glide.with(mDelegate)
                .load(uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mTargetImageVew);
    }

    //用于创建新的ImageView
    private void createNewImageView() {
        mTargetImageVew = new AppCompatImageView(getContext());
        mTargetImageVew.setId(mCurrentNum);
        mTargetImageVew.setLayoutParams(mParams);

        mTargetImageVew.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取要删除的图片ID
                mDeleteId = v.getId();
                mTargetDialog.show();

                final Window window = mTargetDialog.getWindow();
                if (window != null) {
                    window.setContentView(R.layout.dialog_image_click_panel);
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.5f;
                    window.setAttributes(params);

                    //删除按钮
                    window.findViewById(R.id.dialog_image_clicked_btn_delete)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //得到要删除的图片
                                    final AppCompatImageView deleteImageViwe =
                                            (AppCompatImageView) findViewById(mDeleteId);

                                    //设置图片逐渐消失的动画
                                    final AlphaAnimation animation = new AlphaAnimation(1, 0);
                                    animation.setDuration(500);
                                    animation.setRepeatCount(0);
                                    animation.setFillAfter(true);
                                    animation.setStartOffset(0);
                                    deleteImageViwe.setAnimation(animation);
                                    animation.start();

                                    AutoPhotoLayout.this.removeView(deleteImageViwe);
                                    mCurrentNum -= 1;//序号减一

                                    //当数目达到上限时隐藏添加按钮，不足时显示
                                    if (mCurrentNum < mMaxNum) {
                                        mIconAdd.setVisibility(VISIBLE);
                                    }
                                    mTargetDialog.cancel();
                                }
                            });

                    //待定按钮
                    window.findViewById(R.id.dialog_image_clicked_btn_undetermined)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTargetDialog.cancel();
                                }
                            });

                    //取消按钮
                    window.findViewById(R.id.dialog_image_clicked_btn_cancel)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mTargetDialog.cancel();
                                }
                            });
                }
            }
        });

        //添加子View的时候传入位置
        this.addView(mTargetImageVew, mCurrentNum);
        mCurrentNum++;

        //当添加数目 大于 mMaxNum时，自动隐藏添加按钮
        if (mCurrentNum >= mMaxNum) {
            mIconAdd.setVisibility(View.GONE);
        }
    }

    //onMeasure()在渲染过程中会多次被调用 去测量、onLayout同
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //onMeasure 返回的是本类View也就ViewGroup对应的 宽高尺寸，由xml布局时指定！！

        //getSize
        // return the size in pixels defined in the supplied measure specification
        // 返回的是 所提供的 度量规范(MeasureSpecification)中 定义的像素大小
        // Extracts the size from the supplied measure specification.
        //从提供的测量规范(MeasureSpecification) 中 提取尺寸。
        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //Extracts the mode from the supplied measure specification.
        // 从提供的 测量规范(MeasureSpecification)中 提取模式。
        final int modeWith = MeasureSpec.getMode(widthMeasureSpec);

        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //用来累计wrap_content模式下的 ViewGroup（AutoPhotoLayout）宽高
        int width = 0;//累积到一个 最终行宽（一行所有子View 累积占据的宽度） 便是止境
        int height = 0;//累积到一个 最终列宽（一列所有子View 累积占据的高度） 便是止境

        //用于临时记录 每一行的宽度与高度
        int lineWith = 0;
        int lineHeight = 0;

        //得到内部元素个数
        int cCount = getChildCount();
        //遍历处理子元素
        for (int i = 0; i < cCount; i++) {
            final View child = getChildAt(i);

            //测量！！ 子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到 子元素的LayoutParams，强转为MarginLayoutParams 用于获取 间距Margin！
            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //一个子View所占据的宽度 = 本身（测量得到）的宽度 + 需要腾出来的宽度
            final int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //一个子View占据的高度 = 本身（测量得到）的高度 + 需要腾出来的高度
            final int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;


            //sizeWidth - getPaddingLeft() - getPaddingRight() —— ViewGroup 预留给一行子View的宽度
            //lineWith + childWidth —— 累计行宽 + 本轮遍历的子View宽，即即将累计的行宽
            if (lineWith + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {

                //即将累计的行宽 大于 ViewGroup 预留给一行子View的宽度，便要进行 换行操作

                //对比得到最大宽度
                width = Math.max(width, lineWith);

                //重置lineWidth，进入下一行的行宽累加
                lineWith = childWidth;

                height += lineHeight;//换行，累积高度

                lineHeight = childHeight;
            } else {

                //未换行
                //叠加行宽
                lineWith += childWidth;

                //得到当前最大的高度
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //最后一个子控件
            if (i == cCount - 1) {
                width = Math.max(lineWith, width);
                //判断是否超过最大拍照限制
                height += lineHeight;
            }

        }//for结束

        setMeasuredDimension(
                //如果是指定了宽高尺寸的，就按指定的来sizeWith、sizeHeight；
                // 如果没有指定，如wrap_content 那便是 子元素总内容有多大 就多大
                modeWith == MeasureSpec.EXACTLY  ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY  ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );

        //***********？？？？？？？？**************
        //设置一行所有图片的宽高
        final int imageSideLen = sizeWidth / mMaxLineNum;//***********？？？？？？？？**************
        //只初始化一次
        if (!mIsOnceInitOnMeasure) {//***********？？？？？？？？**************
            mParams = new LayoutParams(imageSideLen, imageSideLen);//***********？？？？？？？？**************
            mIsOnceInitOnMeasure = true;//***********？？？？？？？？**************
        }
        //***********？？？？？？？？**************
    }

    //onLayout()在渲染过程中会多次被调用 去测量、onMeasure()同
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ALL_VIEWS.clear();
        LINE_HEIGHTS.clear();

        // 当前ViewGroup的宽度
        final int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //控制值初始化
        if (!mHasInitOnLayout) {
            mLineViews = new ArrayList<>();
            mHasInitOnLayout = true;
        }

        final int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            final View child = getChildAt(i);

            final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            final int childWith = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if (childWith + lineWidth + lp.leftMargin + lp.rightMargin >
                    width - getPaddingLeft() - getPaddingRight()) {
                //记录lineHeight
                LINE_HEIGHTS.add(lineHeight);
                //记录当前一行的Views
                ALL_VIEWS.add(mLineViews);

                //重置宽和高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //重置View集合
                mLineViews.clear();
            }

            //同理onMesure()
            lineWidth += childWith + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, lineHeight + lp.topMargin + lp.bottomMargin);

            mLineViews.add(child);
        } //for结束

        //处理最后一行   类似onMesure()
        LINE_HEIGHTS.add(lineHeight);
        ALL_VIEWS.add(mLineViews);


        //用于设置子View位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        final int lineNum = ALL_VIEWS.size();

        //以行为单位 遍历 所有子View
        for (int i = 0; i < lineNum; i++) {

            //当前行所有的View
            mLineViews = ALL_VIEWS.get(i);
            //当前行的高
            lineHeight = LINE_HEIGHTS.get(i);

            //当前行的子View个数
            final int size = mLineViews.size();

            //遍历当前行的子View
            for (int j = 0; j < size; j++) {
                final View child = mLineViews.get(j);

                //判断child的状态
                if (child.getVisibility() == GONE) {
                    //如果不显示，则直接进入下一次循环
                    continue;
                }

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                //设置子View的边距
                final int lc = left + lp.leftMargin;
                final int tc = top + lp.topMargin;
                final int rc = lc + child.getMeasuredWidth() - mImageMargin;
                final int bc = tc + child.getMeasuredHeight();

                //为子View进行布局
                child.layout(lc, tc, rc, bc);
                //行内左间距累积
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

                //左侧累积间距时完整累积，右侧却减掉一个 mImageMargin往左缩减间距，
                // 上一个的右侧往左缩减，下一个的左侧却不受影响，
                // 由此完成 图片间隔
            }

            //一行结束了，进入下一行，
            // 宽度重置，高度累积行高
            left = getPaddingLeft();
            top += lineHeight;

        }//遍历完所有行（即layout完毕所有子View）

        //***********？？？？？？？？**************
        mIconAdd.setLayoutParams(mParams);//***********？？？？？？？？**************
        mHasInitOnLayout = false;//解锁onLayout()，以可以再次回调到onLayout()
    }

    //初始化 加号图标组件实例
    private void initAddIcon() {
        mIconAdd = new IconTextView(getContext());

        mIconAdd.setText(ICON_TEXT);
        mIconAdd.setGravity(Gravity.CENTER);
        mIconAdd.setTextSize(mIconSize);
        mIconAdd.setBackgroundResource(R.drawable.border_text);
        mIconAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求权限，并弹出Dialog弹框 内容：三个按钮，拍照 选图 取消
                mDelegate.startCameraWithCheck();
            }
        });

        //把View（加号图标组件）加进 ViewGroup，
        // 本类就是一个LinearLayout！！
        this.addView(mIconAdd);
    }

    //onFinishInflate()在 从xml加载完所有布局和组件之后 调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAddIcon();//初始化 加号图标组件实例
        mTargetDialog = new AlertDialog.Builder(getContext()).create();//初始化 弹框
    }
}
