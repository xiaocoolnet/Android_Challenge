package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.bean.CompanyInformation;
import com.example.chy.challenge.NetInfo.NetBaseConstant;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.ImgLoadUtil;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowCompanyInfoActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.iv_logo)
    CircleImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_scope)
    TextView tvScope;
    @BindView(R.id.tv_period)
    TextView tvPeriod;
    private Context context;
    private CompanyInformation information;
    private UserInfoBean userInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_company_info);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        getData();
    }

    /**
     * 获取公司信息
     */
    private void getData() {
        String url = UserNetConstant.GETCOMPANYLIST_NO
                + "&userid=" + userInfoBean.getUserid();
        //+"&userid=301";
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    String data = "";
                    try {
                        JSONObject json = new JSONObject(result);
                        data = json.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    List<CompanyInformation> informations = new Gson().fromJson(data, new TypeToken<List<CompanyInformation>>() {
                    }.getType());
                    information = informations.size() > 0 ? informations.get(0) : null;
                    setData();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (information != null) {
            ImgLoadUtil.display(NetBaseConstant.NET_HOST + information.getLogo(), ivLogo);
            tvName.setText(information.getCompany_name());
            tvIndustry.setText(information.getIndustry());
            tvPeriod.setText(information.getFinancing());
            tvScope.setText(information.getCount());
            tvWebsite.setText(information.getCompany_web());
        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
