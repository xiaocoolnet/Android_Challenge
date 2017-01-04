package com.example.chy.challenge.findcommany.findjob;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chy.challenge.NetInfo.UserRequest;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.NetBaseUtils;
import com.example.chy.challenge.button.WaveView;
import com.example.chy.challenge.findcommany.findjob.adapter.Job_evaluation_adapter;
import com.example.chy.challenge.findcommany.findjob.bean.Job_evaluation_bean;
import com.example.chy.challenge.pnlllist.PullToRefreshBase;
import com.example.chy.challenge.pnlllist.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/24 0024.
 */

public class Job_Evaluation_interview extends Activity implements View.OnClickListener{
    private static final int GETEVALUATESTART = 1;
    private static final int GETECALUATELIST = 2;
    private Context mContext;
    private Intent intent;
    private String companyid;
    private TextView titletop;
    private WaveView back;
    private PullToRefreshListView pulllistview;
    private SimpleDateFormat mdata = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ListView lv_view;
    private View viewH;
    private TextView startone,starttwo,startthree,startfour,startfive,company_score,manager_score,corporate_culture;
    private Job_evaluation_bean.DataBean jobbean;
    private List<Job_evaluation_bean.DataBean> joblist = new ArrayList<>();
    private Job_evaluation_adapter jobadapter;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GETEVALUATESTART:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONObject jsonobj = json.getJSONObject("data");
                                if ("5".equals(jsonobj.getString("com_score"))){
                                    startone.setSelected(true);
                                    starttwo.setSelected(true);
                                    startthree.setSelected(true);
                                    startfour.setSelected(true);
                                    startfive.setSelected(true);
                                }else if ("4".equals(jsonobj.getString("com_score"))){
                                    startone.setSelected(true);
                                    starttwo.setSelected(true);
                                    startthree.setSelected(true);
                                    startfour.setSelected(true);
                                    startfive.setSelected(false);
                                }else if ("3".equals(jsonobj.getString("com_score"))){
                                    startone.setSelected(true);
                                    starttwo.setSelected(true);
                                    startthree.setSelected(true);
                                    startfour.setSelected(false);
                                    startfive.setSelected(false);
                                }else if ("2".equals(jsonobj.getString("com_score"))){
                                    startone.setSelected(true);
                                    starttwo.setSelected(true);
                                    startthree.setSelected(false);
                                    startfour.setSelected(false);
                                    startfive.setSelected(false);
                                }else if ("1".equals(jsonobj.getString("com_score"))){
                                    startone.setSelected(true);
                                    starttwo.setSelected(false);
                                    startthree.setSelected(false);
                                    startfour.setSelected(false);
                                    startfive.setSelected(false);
                                }else{
                                    startone.setSelected(false);
                                    starttwo.setSelected(false);
                                    startthree.setSelected(false);
                                    startfour.setSelected(false);
                                    startfive.setSelected(false);
                                }
                                company_score.setText(jsonobj.getString("company")+"");
                                manager_score.setText(jsonobj.getString("interviewer")+"");
                                corporate_culture.setText(jsonobj.getString("description")+"");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(mContext, R.string.net_error,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case GETECALUATELIST:
                    if (msg.obj != null){
                        String result = (String) msg.obj;
                        joblist.clear();
                        try {
                            JSONObject json = new JSONObject(result);
                            if ("success".equals(json.optString("status"))){
                                JSONArray jsonarray = json.getJSONArray("data");
                                if (jsonarray != null&&jsonarray.length() > 0){
                                    for (int i = 0;i < jsonarray.length();i++){
                                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                                        jobbean = new Job_evaluation_bean.DataBean();
                                        jobbean.setId(jsonobj.getString("id"));
                                        jobbean.setCompanyid(jsonobj.getString("companyid"));
                                        jobbean.setEvaluate_id(jsonobj.getString("evaluate_id"));
                                        jobbean.setStart(jsonobj.getString("start"));
                                        jobbean.setContent(jsonobj.getString("content"));
                                        jobbean.setChoose(jsonobj.getString("choose"));
                                        jobbean.setCreate_time(jsonobj.getString("create_time"));
                                        jobbean.setInvite_title(jsonobj.getString("invite_title"));
                                        jobbean.setUseful_num(jsonobj.getString("useful_num"));
                                        jobbean.setPhoto(jsonobj.getString("photo"));
                                        jobbean.setUser_name(jsonobj.getString("user_name"));
                                        joblist.add(jobbean);
                                    }
                                }
                                jobadapter = new Job_evaluation_adapter(mContext,joblist,0);
                                lv_view.setAdapter(jobadapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(mContext, R.string.net_error,Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_evaluation_interview);
        mContext = this;
        getview();
        intent = getIntent();
        companyid = intent.getStringExtra("companyid");
    }

    private void getview() {
        back = (WaveView) findViewById(R.id.back);
        titletop = (TextView) findViewById(R.id.title_top);
        pulllistview = (PullToRefreshListView) findViewById(R.id.pulllistview);
        pulllistview.setPullLoadEnabled(true);
        pulllistview.setScrollLoadEnabled(false);
        pulllistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getinfo();
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
        viewH = LayoutInflater.from(mContext).inflate(R.layout.job_top, null);//添加listview头（position-1）
        lv_view.addFooterView(viewH);
        jobtop();
    }

    private void jobtop() {
        startone = (TextView) viewH.findViewById(R.id.startone);
        starttwo = (TextView) viewH.findViewById(R.id.starttwo);
        startthree = (TextView) viewH.findViewById(R.id.startthree);
        startfour = (TextView) viewH.findViewById(R.id.startfour);
        startfive = (TextView) viewH.findViewById(R.id.startfive);
        company_score = (TextView) viewH.findViewById(R.id.company_score);
        manager_score = (TextView) viewH.findViewById(R.id.manager_score);
        corporate_culture = (TextView) viewH.findViewById(R.id.corporate_culture);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getinfo();
    }
    private void getinfo() {
        if (NetBaseUtils.isConnnected(mContext)) {
            new UserRequest(mContext, handler).GETEVALUATESTART(companyid,GETEVALUATESTART);
            new UserRequest(mContext, handler).GETECALUATELIST(companyid,GETECALUATELIST);
        }else{
            Toast.makeText(mContext, R.string.net_error,Toast.LENGTH_SHORT).show();
        }
    }
}
