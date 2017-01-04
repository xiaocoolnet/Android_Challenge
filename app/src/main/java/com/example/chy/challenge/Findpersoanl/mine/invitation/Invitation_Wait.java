package com.example.chy.challenge.Findpersoanl.mine.invitation;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chy.challenge.Findpersoanl.mine.bean.MyInvitation;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.CommonAdapter;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.NetBaseUtils;
import com.example.chy.challenge.Utils.ViewHolder;
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.login.register.register_bean.UserInfoBean;
import com.example.chy.challenge.pnlllist.PullToRefreshBase;
import com.example.chy.challenge.pnlllist.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/20 0020.
 */

public class Invitation_Wait extends Fragment{
    private static final int GETMYINVITED = 1;
    private View view;
    private Bundle bundle;
    private String pagename;
    private Context mContext;
    private ProgressDialog dialog;
    private PullToRefreshListView pulllistview;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private UserInfoBean infoBean;
    private List<MyInvitation> invitations;
    private CommonAdapter<MyInvitation> adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.findpersonalfragment,container,false);
        mContext = getActivity();
        infoBean = new UserInfoBean(mContext);
        invitations = new ArrayList<>();
        getview();
        bundle = getArguments();
        if (bundle != null){
            pagename = bundle.getString("pagename");
            if ("待面试".equals(pagename)){
                iniview();
            }else if ("已结束".equals(pagename)){
                iniviewwait();
            }
        }else{
            iniview();
        }

        return view;
    }

    private void getview() {
        //弹出刷新提示框
        dialog=new ProgressDialog(mContext, AlertDialog.THEME_HOLO_LIGHT);
        dialog.setCancelable(false);
        pulllistview = (PullToRefreshListView) view.findViewById(R.id.pulllistview);
        pulllistview.setPullLoadEnabled(true);
        pulllistview.setScrollLoadEnabled(false);
        pulllistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("待面试".equals(pagename)){
                    iniview();
                }else if ("已结束".equals(pagename)){
                    iniviewwait();
                }
                stopRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                stopRefresh();
            }
        });
        setLastData();
        lv_view = pulllistview.getRefreshableView();
        lv_view.setDivider(null);//去掉listview自带的分割线
    }
    //获取当前时间
    private void setLastData() {
        String text = formatdatatime(System.currentTimeMillis());
        pulllistview.setLastUpdatedLabel(text);
        Log.i("time", "-------->" + text);
    }

    //停止刷新
    private void stopRefresh() {
        pulllistview.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulllistview.onPullUpRefreshComplete();
                pulllistview.onPullDownRefreshComplete();
                setLastData();
            }
        }, 2000);
    }
    private String formatdatatime(long time) {
        if (0 == time) {
            return "";
        }
        return mdata.format(new Date(time));
    }
    private void iniview() {
        //我的待面试记录接口
        if (NetBaseUtils.isConnnected(mContext)) {
            dialog.setMessage("正在刷新..");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            String url = UserNetConstant.GETMYINVITED
                    + "&userid=" +infoBean.getUserid()
                    + "&type=0";
            VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    dialog.dismiss();
                    if (JsonResult.JSONparser(mContext, result)) {
                        setAdapter(result);
                    }
                }

                @Override
                public void onError() {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置适配器
     */
    private void setAdapter(String result) {
        invitations.clear();
        invitations = getBeanFromJson(result);
        adapter = new CommonAdapter<MyInvitation>(mContext,invitations,R.layout.invitation_wait_adapter) {
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
        lv_view.setAdapter(adapter);
    }

    private void iniviewwait() {
        //我的结束面试记录
        if (NetBaseUtils.isConnnected(mContext)) {
            dialog.setMessage("正在刷新..");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.show();
            String url = UserNetConstant.GETMYINVITED
                    + "&userid=" +infoBean.getUserid()
                    + "&type=1";
            VolleyUtil.VolleyGetRequest(mContext, url, new VolleyUtil.VolleyJsonCallback() {
                @Override
                public void onSuccess(String result) {
                    dialog.dismiss();
                    if (JsonResult.JSONparser(mContext, result)) {
                        setAdapter(result);
                    }
                }

                @Override
                public void onError() {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(mContext, R.string.net_error, Toast.LENGTH_SHORT).show();
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
