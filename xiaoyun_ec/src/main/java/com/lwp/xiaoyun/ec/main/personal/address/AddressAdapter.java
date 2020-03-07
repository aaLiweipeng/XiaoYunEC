package com.lwp.xiaoyun.ec.main.personal.address;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ui.recycler.MultipleFields;
import com.lwp.xiaoyun.ui.recycler.MultipleItemEntity;
import com.lwp.xiaoyun.ui.recycler.MultipleRecyclerAdapter;
import com.lwp.xiaoyun.ui.recycler.MultipleViewHolder;
import com.lwp.xiaoyun_core.app.XiaoYun;
import com.lwp.xiaoyun_core.net.OkHttpUtil;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/3/6 2:41
 *     desc   : 收获管理页 适配器
 * </pre>
 */
public class AddressAdapter extends MultipleRecyclerAdapter {

    protected AddressAdapter(List<MultipleItemEntity> data) {
        super(data);

        //添加布局文件
        addItemType(AddressItemType.ITEM_ADDRESS, R.layout.item_address);
    }

    @Override
    protected void convert(final MultipleViewHolder holder, MultipleItemEntity entity) {
        super.convert(holder, entity);
        switch (holder.getItemViewType()) {

            case AddressItemType.ITEM_ADDRESS:

                //提取数据
                final String name = entity.getField(MultipleFields.NAME);
                final String phone = entity.getField(AddressItemFields.PHONE);
                final String address = entity.getField(AddressItemFields.ADDRESS);
                final boolean isDefault = entity.getField(MultipleFields.TAG);
                final int id = entity.getField(MultipleFields.ID);

                //实例化组件，加载布局
                final AppCompatTextView nameText = holder.getView(R.id.tv_address_name);
                final AppCompatTextView phoneText = holder.getView(R.id.tv_address_phone);
                final AppCompatTextView addressText = holder.getView(R.id.tv_address_address);
                final AppCompatTextView deleteTextView = holder.getView(R.id.tv_address_delete);
                deleteTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //删除本地UI Item，同时post同步到服务器
                        OkHttpUtil.create()
                                .addPostKV("position", holder.getAdapterPosition())
                                .sendPostRequest("http://47.100.78.251/RestServer/api/address_move.php", new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        //子线程！！！
                                        String jsonString = response.body().string();
                                        XiaoYunLogger.d("AddressDelegate", jsonString);
                                        final List<MultipleItemEntity> data = new AddressDataConverter().setJsonData(jsonString).convert();

                                        XiaoYun.getHandler().post(new Runnable() {
                                            //切到主线程！！！
                                            @Override
                                            public void run() {
                                                //删除本地UI Item
//                                                remove(holder.getLayoutPosition());
                                                setNewData(data);
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });

//                        RestClient.builder()
//                                .url("address.php")
//                                .params("id", id)
//                                .success(new ISuccess() {
//                                    @Override
//                                    public void onSuccess(String response) {
//                                        remove(holder.getLayoutPosition());
//                                    }
//                                })
//                                .build()
//                                .post();
                    }

                });
                //绑定数据
                nameText.setText(name);
                phoneText.setText(phone);
                addressText.setText(address);
                break;

            default:
                break;
        }
    }
}
