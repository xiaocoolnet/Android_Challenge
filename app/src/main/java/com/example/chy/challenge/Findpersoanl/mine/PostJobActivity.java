package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.bean.Dictionary;
import com.example.chy.challenge.NetInfo.SendRequest;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.ProvincePopupview;
import com.example.chy.challenge.Utils.ToastUtil;
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.Utils.WheelView;
import com.example.chy.challenge.button.WaveView;
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

public class PostJobActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.rl_companyinfo)
    RelativeLayout rlCompanyinfo;
    @BindView(R.id.tv_job_type)
    TextView tvJobType;
    @BindView(R.id.rl_job_type)
    RelativeLayout rlJobType;
    @BindView(R.id.tv_job_name)
    TextView tvJobName;
    @BindView(R.id.rl_job_name)
    RelativeLayout rlJobName;
    @BindView(R.id.tv_skill_demand)
    TextView tvSkillDemand;
    @BindView(R.id.rl_skill_demand)
    RelativeLayout rlSkillDemand;
    @BindView(R.id.tv_salary_range)
    TextView tvSalaryRange;
    @BindView(R.id.rl_salary_range)
    RelativeLayout rlSalaryRange;
    @BindView(R.id.tv_job_nature)
    TextView tvJobNature;
    @BindView(R.id.rl_job_nature)
    RelativeLayout rlJobNature;
    @BindView(R.id.tv_experience_comand)
    TextView tvExperienceComand;
    @BindView(R.id.rl_experience_comand)
    RelativeLayout rlExperienceComand;
    @BindView(R.id.tv_degree_comand)
    TextView tvDegreeComand;
    @BindView(R.id.rl_degree_comand)
    RelativeLayout rlDegreeComand;
    @BindView(R.id.tv_job_city)
    TextView tvJobCity;
    @BindView(R.id.rl_job_city)
    RelativeLayout rlJobCity;
    @BindView(R.id.tv_job_location)
    TextView tvJobLocation;
    @BindView(R.id.rl_job_location)
    RelativeLayout rlJobLocation;
    @BindView(R.id.tv_job_description)
    TextView tvJobDescription;
    @BindView(R.id.rl_job_description)
    RelativeLayout rlJobDescription;
    @BindView(R.id.tv_company_welfare)
    TextView tvCompanyWelfare;
    @BindView(R.id.rl_company_welfare)
    RelativeLayout rlCompanyWelfare;
    @BindView(R.id.btn_post)
    TextView btnPost;
    @BindView(R.id.view_bottom)
    View viewBottom;

    private Context context;
    private UserInfoBean userInfoBean;
    private String strSkill = "";   //技能标签字符串
    private String strSalary = "";  //薪资范围
    private String strJobDescription = "";   //职位描述
    private String strCompanyWelfare = "";  //公司福利
    private int type; //带请求的弹出框 1--经验要求 2--学历要求
    private List<Dictionary> dictionaryList;
    private ProvincePopupview poplocat; //二级联动省市选择框
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    if (msg.obj!=null){
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))){
                            ToastUtil.showShort(context,"发布成功");
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
        setContentView(R.layout.activity_post_job);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        dictionaryList = new ArrayList<>();
        poplocat = new ProvincePopupview(PostJobActivity.this);
    }

    @OnClick({R.id.back, R.id.rl_companyinfo, R.id.rl_job_type, R.id.rl_job_name, R.id.rl_skill_demand, R.id.rl_salary_range, R.id.rl_job_nature, R.id.rl_experience_comand, R.id.rl_degree_comand, R.id.rl_job_city, R.id.rl_job_location, R.id.rl_job_description, R.id.rl_company_welfare,R.id.btn_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_companyinfo:
                startActivity(new Intent(context, ShowCompanyInfoActivity.class));
                break;
            case R.id.rl_job_type:
                startActivityForResult(new Intent(context, SelectJobTypeActivity.class), 0x110);
                break;
            case R.id.rl_job_name:
                Intent intent = new Intent(context, EditInfoActivity.class);
                intent.putExtra("title", "职位名称");
                intent.putExtra("description", "请填写您的职位名称");
                intent.putExtra("hint", "");
                startActivityForResult(intent, 0x111);
                break;
            case R.id.rl_skill_demand:
                startActivityForResult(new Intent(context, SelectSkillTagActivity.class), 0x112);
                break;
            case R.id.rl_salary_range:
                showSalaryPopupview();
                break;
            case R.id.rl_job_nature:
                showNaturePopupview();
                break;
            case R.id.rl_experience_comand:
                type = 1;
                getDictionatyList(type);
                break;
            case R.id.rl_degree_comand:
                type = 2;
                getDictionatyList(type);
                break;
            case R.id.rl_job_city:
                poplocat.showAsDropDown(viewBottom,"location");
                break;
            case R.id.rl_job_location:
                Intent intent1 = new Intent(context, EditInfoActivity.class);
                intent1.putExtra("title", "工作地点");
                intent1.putExtra("description", "请填写您想工作的地点");
                intent1.putExtra("hint", "");
                startActivityForResult(intent1, 0x113);
                break;
            case R.id.rl_job_description:
                Intent intent2 = new Intent(context, EditInfoLongActivity.class);
                intent2.putExtra("title", "职位描述");
                intent2.putExtra("hint", "请输入职位描述");
                startActivityForResult(intent2, 0x114);
                break;
            case R.id.rl_company_welfare:
                Intent intent3 = new Intent(context, EditInfoLongActivity.class);
                intent3.putExtra("title", "公司福利");
                intent3.putExtra("hint", "请输入公司福利");
                startActivityForResult(intent3, 0x115);
                break;
            case R.id.btn_post:
                commit();
                break;
        }
    }

    /**
     * 提交发布职业信息
     */
    private void commit() {
        String jobType = tvJobType.getText().toString();
        String jobName = tvJobName.getText().toString();
        String jobNature = tvJobNature.getText().toString();
        String experience = tvExperienceComand.getText().toString();
        String degree = tvDegreeComand.getText().toString();
        String jobCity = tvJobCity.getText().toString();
        String location = tvJobLocation.getText().toString();
        if(jobType.length() == 0){
            ToastUtil.showShort(context,"请选择职位类型");
            return;
        }
        if(jobName.length() == 0){
            ToastUtil.showShort(context,"请填写职位名称");
            return;
        }
        if(strSkill.length() == 0){
            ToastUtil.showShort(context,"请选择技能要求");
            return;
        }
        if(strSalary.length() == 0){
            ToastUtil.showShort(context,"请选择薪资范围");
            return;
        }
        if(jobNature.length() == 0){
            ToastUtil.showShort(context,"请选择工作性质");
            return;
        }
        if(experience.length() == 0){
            ToastUtil.showShort(context,"请选择经验要求");
            return;
        }
        if(degree.length() == 0){
            ToastUtil.showShort(context,"请选择学历要求");
            return;
        }
        if(jobCity.length() == 0){
            ToastUtil.showShort(context,"请选择工作城市");
            return;
        }
        if(location.length() == 0){
            ToastUtil.showShort(context,"请填写工作地点");
            return;
        }
        if(strJobDescription.length() == 0){
            ToastUtil.showShort(context,"请填写职位描述");
            return;
        }
        if(strCompanyWelfare.length() == 0){
            ToastUtil.showShort(context,"请填写公司福利");
            return;
        }
        String data = "&userid=" + userInfoBean.getUserid()
                +"&jobtype=" + jobType
                +"&title=" + jobName
                +"&skill=" + strSkill
                +"&salary=" + strSalary
                +"&work_property=" + jobNature
                +"&experience=" + experience
                +"&education=" + degree
                +"&city=" + jobCity
                +"&address=" + location
                +"&description_job=" + strJobDescription
                +"&welfare=" + strCompanyWelfare;
        new SendRequest(context,handler).post_job(data,0x110);
    }

    /**
     * 获取字典列表
     */
    private void getDictionatyList(int type) {
        String url = "";
        if(type == 1){
            url = UserNetConstant.GETDICTIONARYLIST
                    +"&parentid=52";
        }else if(type == 2){
            url = UserNetConstant.GETDICTIONARYLIST
                    +"&parentid=60";
        }
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context,result)){
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
        if(type == 1){
            textView.setText("经验要求");
        }else if(type == 2){
            textView.setText("学历要求");
        }
        wheelView.setItems(strs);
        wheelView.setSeletion(0);
        wheelView.setOffset(1);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(viewBottom, Gravity.BOTTOM, 0, 0);
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
                if(type == 1){
                    tvExperienceComand.setText(wheelView.getSeletedItem());
                }else if(type == 2){
                    tvDegreeComand.setText(wheelView.getSeletedItem());
                }

            }
        });
    }

    /**
     * 工作性质选择框
     */
    private void showNaturePopupview() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popview_select_nature, null);
        final WheelView wheelView = (WheelView) layout.findViewById(R.id.wheelview);
        List<String> datas = new ArrayList<>();
        datas.add("全职");
        datas.add("兼职");
        wheelView.setItems(datas);
        wheelView.setSeletion(0);
        wheelView.setOffset(1);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(viewBottom, Gravity.BOTTOM, 0, 0);
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
                tvJobNature.setText(wheelView.getSeletedItem());
            }
        });
    }

    /**
     * 弹出薪资选择框
     */
    private void showSalaryPopupview() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popview_select_salary, null);
        final NumberPicker picker1 = (NumberPicker) layout.findViewById(R.id.np_left);
        final NumberPicker picker2 = (NumberPicker) layout.findViewById(R.id.np_right);
        picker1.setMaxValue(15);
        picker1.setMinValue(1);
        picker1.setValue(1);
        picker2.setMaxValue(15);
        picker2.setMinValue(1);
        picker2.setValue(1);
        picker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                picker2.setMinValue(newVal);
                picker2.setValue(newVal);
            }
        });
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(viewBottom, Gravity.BOTTOM, 0, 0);
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
                strSalary = picker1.getValue() + "-" + picker2.getValue();
                tvSalaryRange.setText(strSalary + "k");
                popupWindow.dismiss();
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
                        tvJobType.setText(data.getStringExtra("name"));
                    }
                    break;
                case 0x111:
                    if (data != null) {
                        tvJobName.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x112:
                    if (data != null) {
                        strSkill = data.getStringExtra("skills");
                        tvSkillDemand.setText(strSkill.split("-").length + "个技能");
                    }
                    break;
                case 0x113:
                    if (data != null) {
                        tvJobLocation.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x114:
                    if (data != null) {
                        strJobDescription = data.getStringExtra("value");
                        tvJobDescription.setText("已填写");
                    }
                    break;
                case 0x115:
                    if (data != null) {
                        strCompanyWelfare = data.getStringExtra("value");
                        tvCompanyWelfare.setText("已填写");
                    }
                    break;
            }
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
