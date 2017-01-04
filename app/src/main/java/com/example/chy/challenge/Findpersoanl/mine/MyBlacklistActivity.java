package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.example.chy.challenge.Findpersoanl.mine.bean.Blacklist;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.CommonAdapter;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.ToastUtil;
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

public class MyBlacklistActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.lv_blacklist)
    ListView lvBlacklist;

    private Context context;
    private UserInfoBean userInfoBean;
    private List<Blacklist> blacklists;
    private CommonAdapter<Blacklist> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_blacklist);
        ButterKnife.bind(this);
        context = this;
        userInfoBean = new UserInfoBean(context);
        blacklists = new ArrayList<>();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        String url = UserNetConstant.GETBLACKlIST
                + "&userid=" + userInfoBean.getUserid()
                //+ "&userid=301"
                +"&type=2";
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    setAdapter(result);
                } else {
                    ToastUtil.showShort(context, "没有黑名单");
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 设置适配器
     * @param result
     */
    private void setAdapter(String result) {
        blacklists.clear();
        blacklists.addAll(getBeanFromJson(result));
        if (adapter!=null){
            adapter.notifyDataSetChanged();
        }else {
            adapter = new CommonAdapter<Blacklist>(context,blacklists,R.layout.black_friend_adapter) {
                @Override
                public void convert(ViewHolder holder, final Blacklist blacklist) {
                    holder.setText(R.id.black_realname,blacklist.getBlacks().getRealname())
                            .setText(R.id.black_position,blacklist.getBlacks().getPosition_type())
                            .setText(R.id.adapter_location_city,blacklist.getBlacks().getCity())
                            .setText(R.id.adapter_jobtime_worklife,blacklist.getBlacks().getWork_life())
                            .setText(R.id.adapter_personal_degree,blacklist.getBlacks().getEducation().get(0).getDegree())
                            .setText(R.id.adapter_work_property,blacklist.getBlacks().getWork_property());
                    holder.getView(R.id.black_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelBlacklist(blacklist.getBlackid());
                        }
                    });
                }
            };
        }
        lvBlacklist.setAdapter(adapter);
    }

    /**
     * 取消黑名单
     * @param blackid
     */
    private void cancelBlacklist(String blackid) {
        String url = UserNetConstant.DELBLACKLIST
                +"&type=2"
                + "&userid=" + userInfoBean.getUserid()
                //+ "&userid=301"
                + "&blackid=" + blackid;
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if(JsonResult.JSONparser(context,result)){
                    ToastUtil.showShort(context,"取消成功");
                    getData();
                }else{
                    ToastUtil.showShort(context,"取消失败");
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    private List<Blacklist> getBeanFromJson(String result) {
        String data = "";
        try {
            JSONObject json = new JSONObject(result);
            data = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(data, new TypeToken<List<Blacklist>>() {
        }.getType());
    }
}
