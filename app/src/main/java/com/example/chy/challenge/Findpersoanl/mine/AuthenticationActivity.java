package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.GalleryFinalUtil;
import com.example.chy.challenge.Utils.GetImageUtil;
import com.example.chy.challenge.Utils.PhotoWithPath;
import com.example.chy.challenge.button.WaveView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class AuthenticationActivity extends FragmentActivity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.btn_left)
    ImageView btnLeft;
    @BindView(R.id.iv_gongpai)
    ImageView ivGongpai;
    @BindView(R.id.tv_gongpai)
    TextView tvGongpai;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;
    @BindView(R.id.btn_right)
    ImageView btnRight;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.btn_need_know)
    TextView btnNeedKnow;
    @BindView(R.id.btn_take_photo)
    TextView btnTakePhoto;
    @BindView(R.id.btn_select_photo)
    TextView btnSelectPhoto;
    @BindView(R.id.ll_step2)
    LinearLayout llStep2;
    @BindView(R.id.rl_step)
    RelativeLayout rlStep;
    @BindView(R.id.btn_step1)
    ImageView btnStep1;
    @BindView(R.id.btn_step2)
    ImageView btnStep2;
    @BindView(R.id.btn_benefit)
    TextView btnBenefit;

    private Context context;
    private int step = 1;   //当前步骤
    private int type = 1;   //1--工牌 2--营业执照

    private GalleryFinalUtil galleryFinalUtil;
    private ArrayList<PhotoInfo> mPhotoList;
    private ArrayList<PhotoWithPath> photoWithPaths;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        context = this;
        galleryFinalUtil = new GalleryFinalUtil(1);
        mPhotoList = new ArrayList<>();
        photoWithPaths = new ArrayList<>();
        btnLeft.setClickable(false);
    }

    @OnClick({R.id.back, R.id.btn_left, R.id.btn_confirm, R.id.btn_right, R.id.btn_need_know, R.id.btn_take_photo, R.id.btn_select_photo, R.id.btn_step1, R.id.btn_step2, R.id.btn_benefit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_left:
                if (type == 2) {
                    type = 1;
                    btnLeft.setImageResource(R.mipmap.jiantou3);
                    btnRight.setImageResource(R.mipmap.jiantou);
                    btnRight.setClickable(true);
                    btnLeft.setClickable(false);
                    ivGongpai.setImageResource(R.mipmap.ic_gongpai);
                    tvGongpai.setText("工牌");
                }
                break;
            case R.id.btn_confirm:
                btnConfirm.setVisibility(View.GONE);
                step = 2;
                tv1.setText("第二步：拍摄提交认证资料");
                tv2.setText(type == 1 ? "（您选择提交工牌）" : "（您选择提交营业执照）");
                btnNeedKnow.setVisibility(View.GONE);
                llStep2.setVisibility(View.VISIBLE);
                btnStep1.setImageResource(R.mipmap.ic_shijiandiangray);
                btnStep2.setImageResource(R.mipmap.ic_shijiandian);
                rlStep.setBackgroundResource(R.mipmap.ic_xuanze);
                btnLeft.setImageResource(R.mipmap.jiantou3);
                btnLeft.setClickable(false);
                btnRight.setClickable(false);
                btnRight.setImageResource(R.mipmap.jiantou2);
                break;
            case R.id.btn_right:
                if (type == 1) {
                    type = 2;
                    btnLeft.setImageResource(R.mipmap.jiantou1);
                    btnRight.setImageResource(R.mipmap.jiantou2);
                    btnRight.setClickable(false);
                    btnLeft.setClickable(true);
                    ivGongpai.setImageResource(R.mipmap.ic_yingyezhizhao);
                    tvGongpai.setText("营业执照");
                }
                break;
            case R.id.btn_need_know:
                showNeedKnowDialog();
                break;
            case R.id.btn_take_photo:
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
            case R.id.btn_select_photo:
                galleryFinalUtil.openAblum(context, mPhotoList, REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
                break;
            case R.id.btn_step1:
                break;
            case R.id.btn_step2:
                break;
            case R.id.btn_benefit:
                showBenefitDialog();
                break;
        }
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
                Intent intent = new Intent(context, PostAuthenticationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("photolist", mPhotoList);
                bundle.putSerializable("photoWithPaths", photoWithPaths);
                intent.putExtras(bundle);
                intent.putExtra("type", type == 1 ? "工牌" : "营业执照");
                startActivity(intent);
                finish();
                //ImgLoadUtil.display("file:/" + mPhotoList.get(0).getPhotoPath(), ivLogo);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 认证须知弹出框
     */
    private void showNeedKnowDialog() {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        View view = LayoutInflater.from(context).inflate(type == 1 ? R.layout.needknow_dialog : R.layout.needknow_dialog1, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.benefit_info_bg);
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 认证好处弹出框
     */
    private void showBenefitDialog() {
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.benefit_dialog, null);
        TextView button = (TextView) view.findViewById(R.id.btn_iknow);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.vip_info_bg);
        dialog.setCancelable(false);
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
