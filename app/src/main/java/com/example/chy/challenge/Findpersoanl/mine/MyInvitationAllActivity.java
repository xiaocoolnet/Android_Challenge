package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.bean.MyInvitation;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.CommonAdapter;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.ViewHolder;
import com.example.chy.challenge.Utils.VolleyUtil;
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

public class MyInvitationAllActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.tv_type_name)
    TextView tvTypeName;
    @BindView(R.id.btn_down)
    LinearLayout btnDown;
    @BindView(R.id.lv_invitation)
    ListView lvInvitation;

    private Context context;
    private UserInfoBean infoBean;
    private List<MyInvitation> invitations;
    private CommonAdapter<MyInvitation> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_invitation_all);
        ButterKnife.bind(this);
        context = this;
        infoBean = new UserInfoBean(context);
        invitations = new ArrayList<>();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        String url = UserNetConstant.GETMYINVITED
                + "&userid=" +infoBean.getUserid();
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    setAdapter(result);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 设置适配器
     */
    private void setAdapter(String result) {
        invitations.clear();
        invitations = getBeanFromJson(result);
        adapter = new CommonAdapter<MyInvitation>(context,invitations,R.layout.invitation_wait_adapter) {
            @Override
            public void convert(ViewHolder holder, MyInvitation myInvitation) {
                holder.setText(R.id.invitation_realname,myInvitation.getRealname())
                        .setText(R.id.invitation_personal, myInvitation.getJobtype())
                        .setImageByUrl(R.id.image_head, myInvitation.getPhoto())
                        .setText(R.id.invatation_location, myInvitation.getAddress())
                        .setImageResource(R.id.invitation_sex, myInvitation.getSex().equals("1") ? R.mipmap.ic_nansheng : R.mipmap.ic_nvsheng)
                        .setTimeText(R.id.invitation_time, myInvitation.getCreate_time());
            }
        };
        lvInvitation.setAdapter(adapter);
    }

    @OnClick({R.id.back, R.id.btn_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_down:
                break;
        }
    }

    private List<MyInvitation> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<MyInvitation>>() {
        }.getType());
    }
}
