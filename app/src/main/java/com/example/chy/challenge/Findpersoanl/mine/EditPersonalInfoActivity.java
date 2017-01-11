package com.example.chy.challenge.Findpersoanl.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.chy.challenge.NetInfo.NetBaseConstant;
import com.example.chy.challenge.NetInfo.UserRequest;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.GalleryFinalUtil;
import com.example.chy.challenge.Utils.GetImageUtil;
import com.example.chy.challenge.Utils.ImgLoadUtil;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.PhotoWithPath;
import com.example.chy.challenge.Utils.PushImage;
import com.example.chy.challenge.Utils.PushImageUtil;
import com.example.chy.challenge.Utils.StringJoint;
import com.example.chy.challenge.Utils.ToastUtil;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.login.Login;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditPersonalInfoActivity extends FragmentActivity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.personal_relevance_submit)
    WaveView personalRelevanceSubmit;
    @BindView(R.id.personal_head_avater_rundimage)
    CircleImageView personalHeadAvaterRundimage;
    @BindView(R.id.personal_head_avater)
    WaveView personalHeadAvater;
    @BindView(R.id.personal_tv_realname)
    TextView personalTvRealname;
    @BindView(R.id.personal_real_name)
    WaveView personalRealName;
    @BindView(R.id.personal_tv_position)
    TextView personalTvPosition;
    @BindView(R.id.persoanl_mine_position)
    WaveView persoanlMinePosition;
    @BindView(R.id.personal_tv_email)
    TextView personalTvEmail;
    @BindView(R.id.personal_mine_email)
    WaveView personalMineEmail;
    @BindView(R.id.personal_tv_company)
    TextView personalTvCompany;
    @BindView(R.id.personal_current_company)
    WaveView personalCurrentCompany;
    @BindView(R.id.personal_tv_qq)
    TextView personalTvQq;
    @BindView(R.id.personal_relevance_QQ)
    WaveView personalRelevanceQQ;
    @BindView(R.id.personal_tv_weixin)
    TextView personalTvWeixin;
    @BindView(R.id.personal_relevance_weixin)
    WaveView personalRelevanceWeixin;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.personal_tv_weibo)
    TextView personalTvWeibo;
    @BindView(R.id.personal_relevance_weibo)
    WaveView personalRelevanceWeibo;

    private Context context;
    private UserInfoBean userInfoBean;
    private int type; //1--选择的图片 2--原有的图片

    private GalleryFinalUtil galleryFinalUtil;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private String picname = ""; //上传图片字符串

    private String pagetype;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x111:
                    if (msg.obj!=null){
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))){
                            ToastUtil.showShort(context, "修改成功");
                            userInfoBean.setPhoto(type == 1 ? picname : userInfoBean.getPhoto());
                            userInfoBean.setRealname(personalTvRealname.getText().toString());
                            userInfoBean.setMyjob(personalTvPosition.getText().toString());
                            userInfoBean.setEmail(personalTvEmail.getText().toString());
                            userInfoBean.setCompany(personalTvCompany.getText().toString());
                            userInfoBean.setQq(personalTvQq.getText().toString());
                            userInfoBean.setWeixin(personalTvWeixin.getText().toString());
                            userInfoBean.setWeibo(personalTvWeibo.getText().toString());
                            //注册第一次填写信息
                            if (pagetype.equals("register")){
                                finish();
                                Intent intent = new Intent(context, Login.class);
                                startActivity(intent);
                            }else {
                                finish();
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_personal_info);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        galleryFinalUtil = new GalleryFinalUtil(1);
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        pagetype = getIntent().getStringExtra("pagetype");
        setData();
    }

    /**
     * 初始化信息
     */
    private void setData() {
        ImgLoadUtil.display(NetBaseConstant.NET_HOST + userInfoBean.getPhoto(), personalHeadAvaterRundimage);
        personalTvRealname.setText(userInfoBean.getRealname());
        personalTvPosition.setText(userInfoBean.getMyjob());
        personalTvEmail.setText(userInfoBean.getEmail());
        personalTvCompany.setText(userInfoBean.getCompany());
        personalTvQq.setText(userInfoBean.getQq());
        personalTvWeixin.setText(userInfoBean.getWeixin());
        personalTvWeibo.setText(userInfoBean.getWeibo());
    }

    @OnClick({R.id.back, R.id.personal_relevance_submit, R.id.personal_head_avater, R.id.personal_real_name, R.id.persoanl_mine_position, R.id.personal_mine_email, R.id.personal_current_company, R.id.personal_relevance_QQ, R.id.personal_relevance_weixin, R.id.personal_relevance_weibo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.personal_relevance_submit:
                //提交修改信息
                submit();
                break;
            case R.id.personal_head_avater:
                //修改头像
                showActionSheet();
                break;
            case R.id.personal_real_name:
                //修改姓名
                Intent intent = new Intent(context, EditInfo2Activity.class);
                intent.putExtra("title", "姓名");
                intent.putExtra("description", "请填写您真实的姓名");
                intent.putExtra("number", "4");
                startActivityForResult(intent, 0x110);
                break;
            case R.id.persoanl_mine_position:
                //修改我的职位
                Intent intent1 = new Intent(context, EditInfo2Activity.class);
                intent1.putExtra("title", "我的职位");
                intent1.putExtra("description", "请填写您的现任职位，不能包含特殊符号");
                intent1.putExtra("number", "10");
                startActivityForResult(intent1, 0x111);
                break;
            case R.id.personal_mine_email:
                //修改我的邮箱
                Intent intent2 = new Intent(context, EditInfo2Activity.class);
                intent2.putExtra("title", "接受简历邮箱");
                intent2.putExtra("description", "请填写您的公司邮箱");
                intent2.putExtra("number", "20");
                startActivityForResult(intent2, 0x112);
                break;
            case R.id.personal_current_company:
                //修改当前公司
                Intent intent3 = new Intent(context, EditInfo2Activity.class);
                intent3.putExtra("title", "我的公司");
                intent3.putExtra("description", "请填写您的公司名称");
                intent3.putExtra("number", "15");
                startActivityForResult(intent3, 0x113);
                break;
            case R.id.personal_relevance_QQ:
                //修改QQ
                Intent intent4 = new Intent(context, EditInfo2Activity.class);
                intent4.putExtra("title", "我的QQ");
                intent4.putExtra("description", "请填写您的QQ");
                intent4.putExtra("number", "15");
                startActivityForResult(intent4, 0x114);
                break;
            case R.id.personal_relevance_weixin:
                //修改微信
                Intent intent5 = new Intent(context, EditInfo2Activity.class);
                intent5.putExtra("title", "我的微信");
                intent5.putExtra("description", "请填写您的微信号");
                intent5.putExtra("number", "20");
                startActivityForResult(intent5, 0x115);
                break;
            case R.id.personal_relevance_weibo:
                //修改微博
                Intent intent6 = new Intent(context, EditInfo2Activity.class);
                intent6.putExtra("title", "我的微博");
                intent6.putExtra("description", "请填写您的微博");
                intent6.putExtra("number", "20");
                startActivityForResult(intent6, 0x116);
                break;
        }
    }

    /**
     * 提交信息
     */
    private void submit() {
        final String name = personalTvRealname.getText().toString();
        final String position = personalTvPosition.getText().toString();
        final String email = personalTvEmail.getText().toString();
        final String company = personalTvCompany.getText().toString();
        final String qq = personalTvQq.getText().toString();
        final String weixin = personalTvWeixin.getText().toString();
        final String weibo = personalTvWeibo.getText().toString();
        if(name.equals("")||name.length() == 0){
            ToastUtil.showShort(context,"请输入您的姓名");
            return;
        }
        if(position.equals("")||position.length() == 0){
            ToastUtil.showShort(context,"请输入您的职位");
            return;
        }
        if(company.equals("")||company.length() == 0){
            ToastUtil.showShort(context,"请输入您的公司");
            return;
        }
        if(mPhotoList.size()>0){
            new PushImageUtil().setPushIamge(context, photoWithPaths, new PushImage() {
                @Override
                public void success(boolean state) {
                    type = 1;
                    ArrayList<String> picArray = new ArrayList<>();
                    for (PhotoWithPath photo : photoWithPaths) {
                        picArray.add(photo.getPicname());
                    }
                    picname = StringJoint.arrayJointchar(picArray, ",");
                    new UserRequest(context, handler).UPDATECOMMANY(userInfoBean.getUserid(), picname, name, position, email, company, qq, weixin, weibo, 0x111);
                }

                @Override
                public void error() {

                }
            });
        }else if(userInfoBean.getPhoto().equals("")||userInfoBean.getPhoto().length() == 0){
            ToastUtil.showShort(context,"请选择头像");
            return;
        }else{
            type = 2;
            new UserRequest(context, handler).UPDATECOMMANY(userInfoBean.getUserid(), userInfoBean.getPhoto(), name, position, email, company, qq, weixin, weibo, 0x111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0x110:
                    if (data != null) {
                        personalTvRealname.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x111:
                    if (data != null) {
                        personalTvPosition.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x112:
                    if (data != null) {
                        personalTvEmail.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x113:
                    if (data != null) {
                        personalTvCompany.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x114:
                    if (data != null) {
                        personalTvQq.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x115:
                    if (data != null) {
                        personalTvWeixin.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x116:
                    if (data != null) {
                        personalTvWeibo.setText(data.getStringExtra("value"));
                    }
                    break;
            }
        }
    }

    /**
     * 相册拍照弹出框
     */
    private void showActionSheet() {
        ActionSheet.createBuilder(context, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                        switch (index) {
                            case 0:
                                galleryFinalUtil.openAblum(context, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                                break;
                            case 1:
                                //获取拍照权限
                                if (galleryFinalUtil.openCamera(context, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback)) {
                                    return;
                                } else {
                                    String[] perms = {"android.permission.CAMERA"};
                                    int permsRequestCode = 200;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(perms, permsRequestCode);
                                    }
                                }
                                break;

                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 选择图片后 返回的图片数据
     */

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                photoWithPaths.clear();
                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                photoWithPaths.addAll(GetImageUtil.getImgWithPaths(resultList));
                ImgLoadUtil.display("file:/" + mPhotoList.get(0).getPhotoPath(), personalHeadAvaterRundimage);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 授权权限
     *
     * @param permsRequestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted) {
                    //授权成功之后，调用系统相机进行拍照操作等
                    galleryFinalUtil.openCamera(context, mPhotoList, REQUEST_CODE_CAMERA, mOnHanlderResultCallback);
                } else {
                    //用户授权拒绝之后，友情提示一下就可以了
                    ToastUtil.showShort(this, "已拒绝进入相机，如想开启请到设置中开启！");
                }

                break;

        }

    }
}
