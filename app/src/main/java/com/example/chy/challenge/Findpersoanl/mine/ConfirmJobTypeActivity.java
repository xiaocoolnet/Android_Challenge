package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.bean.Dictionary;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.CommonAdapter;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.ViewHolder;
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.button.WaveView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmJobTypeActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.lv_right)
    ListView lvRight;

    private Context context;
    private List<Dictionary> dictionaryList1;
    private List<Dictionary> dictionaryList2;
    private String type;
    private CommonAdapter<Dictionary> adapter1;
    private CommonAdapter<Dictionary> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comfirm_job_type);
        ButterKnife.bind(this);
        dictionaryList1 = new ArrayList<>();
        dictionaryList2 = new ArrayList<>();
        context = this;
        type = getIntent().getStringExtra("type");
        getData();
        setClick();
    }

    /**
     * 设置点击事件
     */
    private void setClick() {
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < dictionaryList1.size(); i++) {
                    if (i == position) {
                        dictionaryList1.get(i).setIsChecked(true);
                    } else {
                        dictionaryList1.get(i).setIsChecked(false);
                    }
                }
                adapter1.notifyDataSetChanged();
                getRightData();
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        String url = UserNetConstant.GETDICTIONARYLIST
                + "&parentid=" + type;
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    setLeftAdapter(result);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 设置左边的适配器
     *
     * @param result
     */
    private void setLeftAdapter(String result) {
        dictionaryList1.clear();
        dictionaryList1 = getBeanFromJson(result);
        if (dictionaryList1.size() > 0) {
            for (int i = 0;i<dictionaryList1.size();i++){
                if(i == 0){
                    dictionaryList1.get(i).setIsChecked(true);
                }else{
                    dictionaryList1.get(i).setIsChecked(false);
                }
            }
        }
        if (adapter1!=null){
            adapter1.notifyDataSetChanged();
        }else{
            adapter1 = new CommonAdapter<Dictionary>(context,dictionaryList1,R.layout.item_text) {
                @Override
                public void convert(ViewHolder holder, Dictionary dictionary) {
                    TextView textView = holder.getView(R.id.tv_text);
                    textView.setText(dictionary.getName());
                    if (dictionary.isChecked()){
                        textView.setTextColor(Color.parseColor("#64D991"));
                        textView.setBackgroundResource(R.color.white);
                    }else{
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setBackgroundResource(R.color.gray2);
                    }
                }
            };
        }
        lvLeft.setAdapter(adapter1);
        getRightData();

    }

    /**
     * 获取右边list的数据
     */
    private void getRightData() {
        String str = "";
        for (int i = 0;i<dictionaryList1.size();i++){
            if(dictionaryList1.get(i).isChecked()){
                str = dictionaryList1.get(i).getTerm_id();
            }
        }
        String url = UserNetConstant.GETDICTIONARYLIST
                + "&parentid=" + str;
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    setRightAdapter(result);
                } else {
                    dictionaryList2.clear();
                    setRightAdapterNodata();
                }
            }

            @Override
            public void onError() {

            }
        });
    }


    private void setRightAdapterNodata() {
        adapter2 = new CommonAdapter<Dictionary>(context,dictionaryList2,R.layout.item_text) {
            @Override
            public void convert(ViewHolder holder, Dictionary dictionary) {
                holder.setText(R.id.tv_text,dictionary.getName());
            }
        };
        lvRight.setAdapter(adapter2);
    }

    /**
     * 设置右边适配器
     * @param result
     */
    private void setRightAdapter(String result) {
        dictionaryList2.clear();
        dictionaryList2 = getBeanFromJson(result);
        adapter2 = new CommonAdapter<Dictionary>(context,dictionaryList2,R.layout.item_text) {
            @Override
            public void convert(ViewHolder holder, Dictionary dictionary) {
                holder.setText(R.id.tv_text,dictionary.getName());
            }
        };
        lvRight.setAdapter(adapter2);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("name",dictionaryList2.get(position).getName());
                setResult(RESULT_OK,intent);
                finish();
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

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
