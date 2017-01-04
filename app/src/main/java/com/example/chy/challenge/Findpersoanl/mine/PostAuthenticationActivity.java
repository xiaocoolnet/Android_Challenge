package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chy.challenge.NetInfo.NetBaseConstant;
import com.example.chy.challenge.NetInfo.SendRequest;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.ImgLoadUtil;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.PhotoWithPath;
import com.example.chy.challenge.Utils.PushImage;
import com.example.chy.challenge.Utils.PushImageUtil;
import com.example.chy.challenge.Utils.StringJoint;
import com.example.chy.challenge.Utils.ToastUtil;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostAuthenticationActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.rl_companyinfo)
    RelativeLayout rlCompanyinfo;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.iv_commit)
    ImageView ivCommit;
    @BindView(R.id.btn_recommit)
    TextView btnRecommit;
    @BindView(R.id.btn_commit)
    TextView btnCommit;

    private Context context;
    private UserInfoBean userInfoBean;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private String picname;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    if (msg.obj!=null){
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))){
                            ToastUtil.showShort(context, "发送成功");
                            finish();
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
        setContentView(R.layout.activity_post_authentication);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        getDataFromIntent();
        setData();
    }

    /**
     * 设置值
     */
    private void setData() {
        tvName.setText(userInfoBean.getRealname());
        tvCompanyName.setText(userInfoBean.getCompany());
        tvIndustry.setText(userInfoBean.getMyjob());
        ImgLoadUtil.display(NetBaseConstant.NET_HOST + userInfoBean.getPhoto(), ivAvatar);
    }


    /**
     * 获取上个页面传来的值
     */
    private void getDataFromIntent() {
        mPhotoList = (ArrayList<PhotoInfo>) getIntent().getSerializableExtra("photolist");
        photoWithPaths = (ArrayList<PhotoWithPath>) getIntent().getSerializableExtra("photoWithPaths");
        tvType.setText(getIntent().getStringExtra("type"));
        ImgLoadUtil.display("file:/" + mPhotoList.get(0).getPhotoPath(), ivCommit);
    }



    @OnClick({R.id.back, R.id.rl_companyinfo, R.id.btn_recommit, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_companyinfo:

                break;
            case R.id.btn_recommit:
                //重新上传

                break;
            case R.id.btn_commit:
                //提交审核
                submit();
                break;
        }
    }

    /**
     * 提交审核
     */
    private void submit() {
        if(mPhotoList.size() > 0){
            new PushImageUtil().setPushIamge(context, photoWithPaths, new PushImage() {
                @Override
                public void success(boolean state) {
                    ArrayList<String> picArray = new ArrayList<>();
                    for (PhotoWithPath photo : photoWithPaths) {
                        picArray.add(photo.getPicname());
                    }
                    picname = StringJoint.arrayJointchar(picArray, ",");
                    String data = "&userid=" + userInfoBean.getUserid()
                            +"&companyname=" + userInfoBean.getCompany()
                            +"&phone=" + userInfoBean.getPhone()
                            +"&email=" + userInfoBean.getEmail()
                            +"&license=" + picname;
                    new SendRequest(context,handler).post_authentication_info(data,0x110);
                }

                @Override
                public void error() {

                }
            });
        }
    }
}
