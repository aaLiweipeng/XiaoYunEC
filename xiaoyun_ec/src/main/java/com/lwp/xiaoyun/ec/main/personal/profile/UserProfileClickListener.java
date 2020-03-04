package com.lwp.xiaoyun.ec.main.personal.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.lwp.xiaoyun.ec.R;
import com.lwp.xiaoyun.ec.main.personal.list.ListBean;
import com.lwp.xiaoyun.ui.date.DateDialogUtil;
import com.lwp.xiaoyun_core.delegates.XiaoYunDelegate;
import com.lwp.xiaoyun_core.net.RestClient;
import com.lwp.xiaoyun_core.net.callback.ISuccess;
import com.lwp.xiaoyun_core.util.callback.CallbackManager;
import com.lwp.xiaoyun_core.util.callback.CallbackType;
import com.lwp.xiaoyun_core.util.callback.IGlobalCallback;
import com.lwp.xiaoyun_core.util.log.XiaoYunLogger;

import retrofit2.http.DELETE;

/**
 * <pre>
 *     author : 李蔚蓬（简书_凌川江雪）
 *     time   : 2020/2/27 17:12
 *     desc   : 个人具体信息页面 点击事件监听器
 * </pre>
 */
public class UserProfileClickListener extends SimpleClickListener {

    //把 UserProfileDelegate（个人具体信息页面）的实例 传进来
    private final XiaoYunDelegate DELEGATE;

    private String[] mGenders = new String[]{"男","女","保密"};

    public UserProfileClickListener(XiaoYunDelegate delegate) {
        //把 UserProfileDelegate的实例 传进来
        DELEGATE = delegate;
    }

    //二参这个View，是被点击的整个Item，可以用这个View实例来find Item中的其他组件
    @Override
    public void onItemClick(BaseQuickAdapter adapter, final View view, int position) {
        //源码，Listener中有 成员变量baseQuickAdapter，存储了RecyclerView的Adapter
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);//拿到对应Item的数据Bean
        final int id = bean.getId();//id 来自 UserProfileDelegate！！！
        switch (id) {
            case 1:
                //个人具体信息页面头像点击，启动照相机或选择图片
                CallbackManager.getInstance()
                        .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                            @Override
                            public void executeCallback(Uri args) {
                                XiaoYunLogger.d("ON_CROP", args);
                                Toast.makeText(DELEGATE.getContext(), args+"", Toast.LENGTH_SHORT).show();
                                final ImageView avatar = (ImageView) view.findViewById(R.id.img_arrow_avatar);
                                Glide.with(DELEGATE)
                                        .load(args)
                                        .into(avatar);

//                                RestClient.builder()
//                                        .url(UploadConfig.UPLOAD_IMG)
//                                        .loader(DELEGATE.getContext())
//                                        .file(args.getPath())
//                                        .success(new ISuccess() {
//                                            @Override
//                                            public void onSuccess(String response) {
//                                                LatteLogger.d("ON_CROP_UPLOAD", response);
//                                                final String path = JSON.parseObject(response).getJSONObject("result")
//                                                        .getString("path");
//
//                                                //通知服务器更新信息
//                                                RestClient.builder()
//                                                        .url("user_profile.php")
//                                                        .params("avatar", path)
//                                                        .loader(DELEGATE.getContext())
//                                                        .success(new ISuccess() {
//                                                            @Override
//                                                            public void onSuccess(String response) {
//                                                                //获取更新后的用户信息，然后更新本地数据库
//                                                                //没有本地数据的APP，每次打开APP都请求API，获取信息
//                                                            }
//                                                        })
//                                                        .build()
//                                                        .post();
//                                            }
//                                        })
//                                        .build()
//                                        .upload();
                            }
                        });

                // 开始请求权限!!!
                // 随后进行权限结果处理——
                // 成功则开始选图或拍照，失败则。。详见 PermissionCheckerDelegate
                DELEGATE.startCameraWithCheck();
                break;
            case 2:
                //姓名信息更新
                //拿到 NameDelegate实例， 在 UserProfileDelegate中已经set过了
                final XiaoYunDelegate nameDelegate = bean.getDelegate();
                DELEGATE.getSupportDelegate().start(nameDelegate);
                break;

            case 3:
                //性别信息更新
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //把对应所点到的Item 其对应的 文本组件 拿到
                        final TextView textView = view.findViewById(R.id.tv_arrow_value);
                        textView.setText(mGenders[which]);
                        dialog.cancel();
                    }
                });
                break;

            case 4:
                //日期信息更新
                // 当选择一个日期时，要进行回调的逻辑
                final DateDialogUtil dateDialogUtil = new DateDialogUtil();
                dateDialogUtil.setDateListener(new DateDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        final TextView textView = view.findViewById(R.id.tv_arrow_value);
                        textView.setText(date);
                    }
                });
                //以上配置好回调，在 日期改动的时候 会被调用，
                //下面弹出Dialog
                dateDialogUtil.showDialog(DELEGATE.getContext());

                break;

            default:
                break;
        }
    }

    private void getGenderDialog(DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DELEGATE.getContext());
        //三参数：性别信息，默认选中，监听器
        builder.setSingleChoiceItems(mGenders, 0, listener)
               .show();
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
