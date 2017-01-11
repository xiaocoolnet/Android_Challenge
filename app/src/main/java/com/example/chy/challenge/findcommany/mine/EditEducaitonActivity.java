package com.example.chy.challenge.findcommany.mine;

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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.EditInfo2Activity;
import com.example.chy.challenge.Findpersoanl.mine.bean.Dictionary;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.NetInfo.UserRequest;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.JsonResult;
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

public class EditEducaitonActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.resume_education_next)
    WaveView resumeEducationNext;
    @BindView(R.id.resume_et_school)
    TextView resumeEtSchool;
    @BindView(R.id.resume_graduate_institutions)
    WaveView resumeGraduateInstitutions;
    @BindView(R.id.resume_et_major)
    TextView resumeEtMajor;
    @BindView(R.id.resume_graduate_major)
    WaveView resumeGraduateMajor;
    @BindView(R.id.resume_tv_readtime)
    TextView resumeTvReadtime;
    @BindView(R.id.resume_Atthe_time)
    WaveView resumeAttheTime;
    @BindView(R.id.resume_tv_education)
    TextView resumeTvEducation;
    @BindView(R.id.resume_education)
    WaveView resumeEducation;
    @BindView(R.id.resume_et_experience)
    EditText resumeEtExperience;
    @BindView(R.id.resume_tv_experience)
    TextView resumeTvExperience;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.textview_pop)
    TextView textviewPop;

    private Context context;
    private String strPeriod;   //就读时间段拼接字符串
    private List<Dictionary> dictionaryList;
    private UserInfoBean userInfo;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x110:
                    if (msg.obj!=null){
                        if (JsonResult.JSONparser(context, String.valueOf(msg.obj))){
                            ToastUtil.showShort(context, "录入成功");
                            //注册第一次填写信息，成功后跳转到教育经历填写
                            finish();
                            Intent intent = new Intent(context, EditJobLiveActivity.class);
                            startActivity(intent);
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
        setContentView(R.layout.activity_edit_educaiton);
        ButterKnife.bind(this);
        context = this;
        userInfo = new UserInfoBean(context);
        dictionaryList = new ArrayList<>();
    }

    @OnClick({R.id.back, R.id.resume_education_next, R.id.resume_graduate_institutions, R.id.resume_graduate_major, R.id.resume_Atthe_time, R.id.resume_education})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.resume_education_next:
                next();
                break;
            case R.id.resume_graduate_institutions:
                //毕业院校
                Intent intent = new Intent(context, EditInfo2Activity.class);
                intent.putExtra("title", "毕业院校");
                intent.putExtra("description", "请输入毕业院校");
                intent.putExtra("number", "20");
                startActivityForResult(intent, 0x110);
                break;
            case R.id.resume_graduate_major:
                //所学专业
                Intent intent1 = new Intent(context, EditInfo2Activity.class);
                intent1.putExtra("title", "所学专业");
                intent1.putExtra("description", "请输入所学专业");
                intent1.putExtra("number", "20");
                startActivityForResult(intent1, 0x111);
                break;
            case R.id.resume_Atthe_time:
                //就读时间段
                showPeriodPopupview();
                break;
            case R.id.resume_education:
                //学历
                getDictionatyList();
                break;
        }
    }


    /**
     * 填写完成，修改下一项
     */
    private void next() {
        String school = resumeEtSchool.getText().toString();
        String major = resumeEtMajor.getText().toString();
        String degree = resumeTvEducation.getText().toString();
        String experience = resumeEtExperience.getText().toString();
        if(school.equals("")||school.length() == 0){
            ToastUtil.showShort(context,"请填写毕业院校");
            return;
        }
        if(major.equals("")||major.length() == 0){
            ToastUtil.showShort(context,"请填写所学专业");
            return;
        }
        if(strPeriod.equals("")||strPeriod.length() == 0){
            ToastUtil.showShort(context,"请选择就读时间段");
            return;
        }
        if(degree.equals("")||degree.length() == 0){
            ToastUtil.showShort(context,"请选择学历");
            return;
        }
        if(experience.equals("")||experience.length() == 0){
            ToastUtil.showShort(context,"请填写在校经历");
            return;
        }
        new UserRequest(context, handler).PUBLISHEDUCATION(userInfo.getUserid(),school,major,degree,strPeriod,experience, 0x110);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0x110:
                    if (data != null) {
                        resumeEtSchool.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x111:
                    if (data != null) {
                        resumeEtMajor.setText(data.getStringExtra("value"));
                    }
                    break;
            }
        }
    }

    /**
     * 弹出就读时间段
     */
    private void showPeriodPopupview() {
        View layout = LayoutInflater.from(this).inflate(R.layout.popview_select_period, null);
        final NumberPicker picker1 = (NumberPicker) layout.findViewById(R.id.np_left);
        final NumberPicker picker2 = (NumberPicker) layout.findViewById(R.id.np_right);
        picker1.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "年";
            }
        });
        picker2.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + "年";
            }
        });
        picker1.setMaxValue(2020);
        picker1.setMinValue(1970);
        picker1.setValue(1970);
        picker2.setMaxValue(2020);
        picker2.setMinValue(1970);
        picker2.setValue(1970);
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
        popupWindow.showAtLocation(textviewPop, Gravity.BOTTOM, 0, 0);
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
                strPeriod = picker1.getValue() + "-" + picker2.getValue();
                resumeTvReadtime.setText(picker1.getValue() + "年-" + picker2.getValue() + "年");
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 获取字典列表
     */
    private void getDictionatyList() {
        String url = UserNetConstant.GETDICTIONARYLIST
                    +"&parentid=60";
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    dictionaryList = getBeanFromJson(result);
                    showDegreePopView();
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
    private void showDegreePopView() {
        List<String> strs = new ArrayList<>();
        if (dictionaryList.size() > 0) {
            for (int i = 0; i < dictionaryList.size(); i++) {
                strs.add(dictionaryList.get(i).getName());
            }
        }
        View layout = LayoutInflater.from(this).inflate(R.layout.popview_select_nature, null);
        final WheelView wheelView = (WheelView) layout.findViewById(R.id.wheelview);
        TextView textView = (TextView) layout.findViewById(R.id.tv_title);
        textView.setText("学历要求");
        wheelView.setItems(strs);
        wheelView.setSeletion(0);
        wheelView.setOffset(1);
        final PopupWindow popupWindow = new PopupWindow(layout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(textviewPop, Gravity.BOTTOM, 0, 0);
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
                resumeTvEducation.setText(wheelView.getSeletedItem());
            }
        });
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
