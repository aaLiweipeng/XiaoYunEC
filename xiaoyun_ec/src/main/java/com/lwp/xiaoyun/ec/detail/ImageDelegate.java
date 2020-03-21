package com.lwp.xiaoyun.ec.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.R2;
import com.lwp.xiaoyun.ui.recycler.ItemType;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/20 5:18
 *     desc   :
 * </pre>
 */
public class ImageDelegate extends XiaoYunDelegate {

    @BindView(R2.id.rv_image_container)
    RecyclerView mRecyclerView = null;

    //用作Bundle的键
    private static final String ARG_PICTURES = "ARG_PICTURES";

    @Override
    public Object setLayout() {
        return R.layout.delegate_image;
    }

    private void initImages() {
        final ArrayList<String> pictures =
                getArguments().getStringArrayList(ARG_PICTURES);

        final ArrayList<MultipleItemEntity> entities = new ArrayList<>();

        final int size;
        if (pictures != null) {
            size = pictures.size();

            //遍历 图片url数组
            for (int i = 0; i < size; i++) {
                final String imageUrl = pictures.get(i);

                final MultipleItemEntity entity = MultipleItemEntity
                        .builder()
                        .setItemType(ItemType.SINGLE_BIG_IMAGE)
                        .setField(MultipleFields.IMAGE_URL, imageUrl)
                        .build();
                entities.add(entity);
            }

            //配置 RecyclerAdapter
            final RecyclerImageAdapter adapter = new RecyclerImageAdapter(entities);
            mRecyclerView.setAdapter(adapter);
        }
    }

    //静态工厂模式
    public static ImageDelegate create(ArrayList<String> pictures) {

        final Bundle args = new Bundle();
        args.putStringArrayList(ARG_PICTURES, pictures);

        final ImageDelegate delegate = new ImageDelegate();
        delegate.setArguments(args);
        return delegate;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        //配置RecyclerView
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        initImages();
    }
}
