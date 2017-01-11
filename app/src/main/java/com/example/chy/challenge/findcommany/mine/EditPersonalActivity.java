package com.example.chy.challenge.findcommany.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.example.chy.challenge.Findpersoanl.mine.EditInfo2Activity;
import com.example.chy.challenge.Findpersoanl.mine.bean.Dictionary;
import com.example.chy.challenge.NetInfo.NetBaseConstant;
import com.example.chy.challenge.NetInfo.SendRequest;
import com.example.chy.challenge.NetInfo.UserNetConstant;
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
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.Utils.WheelView;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.findcommany.location.CityPickerActivity;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditPersonalActivity extends FragmentActivity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.company_relevance_submit)
    WaveView companyRelevanceSubmit;
    @BindView(R.id.commany_head_avater_rundimage)
    CircleImageView commanyHeadAvaterRundimage;
    @BindView(R.id.commany_head_avater)
    WaveView commanyHeadAvater;
    @BindView(R.id.commany_tv_realname)
    TextView commanyTvRealname;
    @BindView(R.id.commany_real_name)
    WaveView commanyRealName;
    @BindView(R.id.commany_rbtn_woman)
    RadioButton commanyRbtnWoman;
    @BindView(R.id.commany_rbtn_man)
    RadioButton commanyRbtnMan;
    @BindView(R.id.commany_rg_sex)
    RadioGroup commanyRgSex;
    @BindView(R.id.commany_tv_local)
    TextView commanyTvLocal;
    @BindView(R.id.commany_current_local)
    WaveView commanyCurrentLocal;
    @BindView(R.id.commany_tv_jobtime)
    TextView commanyTvJobtime;
    @BindView(R.id.commany_job_time)
    WaveView commanyJobTime;
    @BindView(R.id.commany_tv_qq)
    TextView commanyTvQq;
    @BindView(R.id.commany_qq)
    WaveView commanyQq;
    @BindView(R.id.commany_tv_weixin)
    TextView commanyTvWeixin;
    @BindView(R.id.commany_weixin)
    WaveView commanyWeixin;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.commany_tv_weibo)
    TextView commanyTvWeibo;
    @BindView(R.id.commany_weibo)
    WaveView commanyWeibo;
    @BindView(R.id.textview)
    TextView textview;

    private Context context;
    private UserInfoBean userInfoBean;

    private GalleryFinalUtil galleryFinalUtil;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private String picname = ""; //上传图片字符串

    private String pagetype;
    private List<Dictionary> dictionaryList;
    private String sex;
    private int type;   //1--选择的图片 2--原有的图片

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    if (msg.obj!=null){
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))){
                            ToastUtil.showShort(context, "修改成功");
                            userInfoBean.setPhoto(type == 1 ? picname : userInfoBean.getPhoto());
                            userInfoBean.setRealname(commanyTvRealname.getText().toString());
                            userInfoBean.setSex(sex);
                            userInfoBean.setCity(commanyTvLocal.getText().toString());
                            userInfoBean.setWork_life(commanyTvJobtime.getText().toString());
                            userInfoBean.setQq(commanyTvQq.getText().toString());
                            userInfoBean.setWeixin(commanyTvWeixin.getText().toString());
                            userInfoBean.setWeibo(commanyTvWeibo.getText().toString());
                            //注册第一次填写信息，成功后跳转到教育经历填写
                            if (pagetype.equals("register")){
                                finish();
                                Intent intent = new Intent(context, EditEducaitonActivity.class);
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
        setContentView(R.layout.activity_edit_personal);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        dictionaryList = new ArrayList<>();
        galleryFinalUtil = new GalleryFinalUtil(1);
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        pagetype = getIntent().getStringExtra("pagetype");
        setData();
    }

    /**
     * 设置数据
     */
    private void setData() {
        ImgLoadUtil.display(NetBaseConstant.NET_HOST + userInfoBean.getPhoto(), commanyHeadAvaterRundimage);
        commanyTvRealname.setText(userInfoBean.getRealname());
        if(userInfoBean.getSex().equals("1")){
            commanyRbtnMan.setChecked(true);
        }else if(userInfoBean.getSex().equals("0")){
            commanyRbtnWoman.setChecked(true);
        }
        commanyTvLocal.setText(userInfoBean.getCity());
        commanyTvJobtime.setText(userInfoBean.getWork_life());
        commanyTvQq.setText(userInfoBean.getQq());
        commanyTvWeixin.setText(userInfoBean.getWeixin());
        commanyTvWeibo.setText(userInfoBean.getWeibo());
        commanyRgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == commanyRbtnMan.getId()) {
                    sex = "1";
                } else {
                    sex = "0";
                }
            }
        });
    }

    @OnClick({R.id.back, R.id.company_relevance_submit, R.id.commany_head_avater, R.id.commany_real_name, R.id.commany_current_local, R.id.commany_job_time, R.id.commany_qq, R.id.commany_weixin, R.id.commany_weibo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.company_relevance_submit:
                commit();
                break;
            case R.id.commany_head_avater:
                //修改个人头像
                showActionSheet();
                break;
            case R.id.commany_real_name:
                //修改姓名
                Intent intent = new Intent(context, EditInfo2Activity.class);
                intent.putExtra("title", "姓名");
                intent.putExtra("description", "请填写您真实的姓名");
                intent.putExtra("number", "4");
                startActivityForResult(intent, 0x110);
                break;
            case R.id.commany_current_local:
                //修改所在城市
                Intent intent1 = new Intent(context, CityPickerActivity.class);
                startActivityForResult(intent1, 0x111);
                break;
            case R.id.commany_job_time:
                //修改工作年限
                getDictionatyList();
                break;
            case R.id.commany_qq:
                //修改QQ
                Intent intent4 = new Intent(context, EditInfo2Activity.class);
                intent4.putExtra("title", "我的QQ");
                intent4.putExtra("description", "请填写您的QQ");
                intent4.putExtra("number", "15");
                startActivityForResult(intent4, 0x114);
                break;
            case R.id.commany_weixin:
                //修改微信
                Intent intent5 = new Intent(context, EditInfo2Activity.class);
                intent5.putExtra("title", "我的微信");
                intent5.putExtra("description", "请填写您的微信号");
                intent5.putExtra("number", "20");
                startActivityForResult(intent5, 0x115);
                break;
            case R.id.commany_weibo:
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
     * 提交
     */
    private void commit() {
        final String name = commanyTvRealname.getText().toString();
        final String city = commanyTvLocal.getText().toString();
        final String worklife = commanyTvJobtime.getText().toString();
        final String qq = commanyTvQq.getText().toString();
        final String weixin = commanyTvWeixin.getText().toString();
        final String weibo = commanyTvWeibo.getText().toString();
        if(name.equals("")||name.length() == 0){
            ToastUtil.showShort(context,"请输入您的姓名");
            return;
        }
        if(city.equals("")||city.length() == 0){
            ToastUtil.showShort(context,"请输入您所在城市");
            return;
        }
        if(worklife.equals("")||worklife.length() == 0){
            ToastUtil.showShort(context,"请选择工作年限");
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
                    String data = "&userid=" + userInfoBean.getUserid()
                            +"&avatar=" + picname
                            +"&realname=" + name
                            +"&sex=" + sex
                            +"&city=" + city
                            +"&work_life=" + worklife
                            +"&qq=" + qq
                            +"&weixin=" + weixin
                            +"&weibo=" + weibo;
                    new SendRequest(context,handler).updataPersonal(data,0x110);
                    //new UserRequest(context, handler).UPDATEPERSONAL(userInfoBean.getUserid(), picname, name, sex, city, worklife, qq, weixin, weibo, 0x110);
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
            //new UserRequest(context, handler).UPDATEPERSONAL(userInfoBean.getUserid(), userInfoBean.getPhoto(), name, sex, city, worklife, qq, weixin, weibo, 0x110);
            String data = "&userid=" + userInfoBean.getUserid()
                    +"&avatar=" + userInfoBean.getPhoto()
                    +"&realname=" + name
                    +"&sex=" + sex
                    +"&city=" + city
                    +"&work_life=" + worklife
                    +"&qq=" + qq
                    +"&weixin=" + weixin
                    +"&weibo=" + weibo;
            new SendRequest(context,handler).updataPersonal(data,0x110);
        }
    }

    /**
     * 获取字典列表
     */
    private void getDictionatyList() {
        String url = UserNetConstant.GETDICTIONARYLIST
                    +"&parentid=52";
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    dictionaryList = getBeanFromJson(result);
                    showNetPopView();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 需要网络请求的弹出框
     */
    private void showNetPopView() {
        List<String> strs = new ArrayList<>();
        if (dictionaryList.size() > 0) {
            for (int i = 0; i < dictionaryList.size(); i++) {
                strs.add(dictionaryList.get(i).getName());
            }
        }
        View layout = LayoutInflater.from(this).inflate(R.layout.popview_select_nature, null);
        final WheelView wheelView = (WheelView) layout.findViewById(R.id.wheelview);
        TextView textView = (TextView) layout.findViewById(R.id.tv_title);
        textView.setText("参加工作年份");
        wheelView.setItems(strs);
        wheelView.setSeletion(0);
        wheelView.setOffset(1);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(textview, Gravity.BOTTOM, 0, 0);
        // 设置背景颜色变暗
        final WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        //监听popwindow消失事件，取消遮盖层
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        layout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        layout.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                commanyTvJobtime.setText(wheelView.getSeletedItem());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0x110:
                    if (data != null) {
                        commanyTvRealname.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x111:
                    if(data != null){
                        commanyTvLocal.setText(data.getStringExtra("picked_city"));
                    }
                    break;
                case 0x114:
                    if (data != null) {
                        commanyTvQq.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x115:
                    if (data != null) {
                        commanyTvWeixin.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x116:
                    if (data != null) {
                        commanyTvWeibo.setText(data.getStringExtra("value"));
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
                ImgLoadUtil.display("file:/" + mPhotoList.get(0).getPhotoPath(), commanyHeadAvaterRundimage);
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

    private List<Dictionary> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<Dictionary>>() {
        }.getType());
    }
}
