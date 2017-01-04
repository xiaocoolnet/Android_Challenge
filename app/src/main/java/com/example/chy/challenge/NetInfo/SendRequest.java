package com.example.chy.challenge.NetInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 潘 on 2016/3/31.
 */
public class SendRequest {
    private Context mContext;
    private Handler handler;


    public SendRequest(Context context, Handler handler) {
        super();
        this.mContext = context;
        this.handler = handler;

    }


    public void commit_company_info(final String data,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String result_data = NetUtil.getResponse(UserNetConstant.COMPANY_INFO, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
    public void post_job(final String data,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String result_data = NetUtil.getResponse(UserNetConstant.PUBLISHJOB, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void post_authentication_info(final String data,final int KEY) {
        new Thread() {
            Message msg = Message.obtain();
            public void run() {
                String result_data = NetUtil.getResponse(UserNetConstant.UPDATECOMPANYCERTIFY, data);
                try {
                    JSONObject obj = new JSONObject(result_data);
                    msg.what = KEY;
                    msg.obj = obj;
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }
}
