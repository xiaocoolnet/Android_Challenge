package com.example.chy.challenge.findcommany.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.EditInfo2Activity;
import com.example.chy.challenge.Findpersoanl.mine.SelectIndustryActivity;
import com.example.chy.challenge.Findpersoanl.mine.SelectJobTypeActivity;
import com.example.chy.challenge.Findpersoanl.mine.SelectSkillTagActivity;
import com.example.chy.challenge.R;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditJobLiveActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.submit_job_experice)
    WaveView submitJobExperice;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.work_tv_commany)
    TextView workTvCommany;
    @BindView(R.id.work_commany_name)
    WaveView workCommanyName;
    @BindView(R.id.work_tv_commany_industry)
    TextView workTvCommanyIndustry;
    @BindView(R.id.work_commany_industry)
    WaveView workCommanyIndustry;
    @BindView(R.id.work_tv_Position_type)
    TextView workTvPositionType;
    @BindView(R.id.work_Position_type)
    WaveView workPositionType;
    @BindView(R.id.work_tv_skills_show)
    TextView workTvSkillsShow;
    @BindView(R.id.work_skills_show)
    WaveView workSkillsShow;
    @BindView(R.id.work_tv_work_time)
    TextView workTvWorkTime;
    @BindView(R.id.work_work_time)
    WaveView workWorkTime;
    @BindView(R.id.work_et_describe)
    EditText workEtDescribe;
    @BindView(R.id.work_look_other_personal)
    TextView workLookOtherPersonal;
    @BindView(R.id.txtnum)
    TextView txtnum;
    @BindView(R.id.txtnum_big)
    TextView txtnumBig;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.textview)
    TextView textview;

    private Context context;
    private UserInfoBean userInfoBean;
    private String strSkill;    //技能标签拼接字符串
    private TimePopView popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_job_live);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        popView = new TimePopView(EditJobLiveActivity.this);
    }

    /**
     * 提交工作经历
     */
    private void submit() {
        String company = workTvCommany.getText().toString();
        String industry = workTvCommanyIndustry.getText().toString();
        String jobtype = workTvPositionType.getText().toString();
        String period = workTvWorkTime.getText().toString();
        String content = workEtDescribe.getText().toString();
    }

    @OnClick({R.id.back, R.id.submit_job_experice, R.id.work_commany_name, R.id.work_commany_industry, R.id.work_Position_type, R.id.work_skills_show, R.id.work_work_time, R.id.work_look_other_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit_job_experice:
                submit();
                break;
            case R.id.work_commany_name:
                //公司名称
                Intent intent = new Intent(context, EditInfo2Activity.class);
                intent.putExtra("title", "公司名称");
                intent.putExtra("description", "请输入公司名称");
                intent.putExtra("number", "20");
                startActivityForResult(intent, 0x110);
                break;
            case R.id.work_commany_industry:
                //公司行业
                Intent intent2 = new Intent(context, SelectIndustryActivity.class);
                startActivityForResult(intent2, 0x111);
                break;
            case R.id.work_Position_type:
                //职位类型
                startActivityForResult(new Intent(context, SelectJobTypeActivity.class), 0x112);
                break;
            case R.id.work_skills_show:
                //技能展示
                startActivityForResult(new Intent(context, SelectSkillTagActivity.class), 0x113);
                break;
            case R.id.work_work_time:
                //任职时间段
                popView.showAsDropDown(textview);
                break;
            case R.id.work_look_other_personal:
                //看看别人怎么写

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0x110:
                    if (data != null) {
                        workTvCommany.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x111:
                    if (data != null) {
                        workTvCommanyIndustry.setText(data.getStringExtra("value"));
                    }
                    break;
                case 0x112:
                    if (data != null) {
                        workTvPositionType.setText(data.getStringExtra("name"));
                    }
                    break;
                case 0x113:
                    if (data != null) {
                        strSkill = data.getStringExtra("skills");
                        workTvSkillsShow.setText(strSkill.split("-").length + "个技能");
                    }
                    break;
            }
        }
    }


}
