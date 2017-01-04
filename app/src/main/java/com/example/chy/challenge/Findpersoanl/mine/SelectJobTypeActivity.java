package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class SelectJobTypeActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.lv_job_type)
    ListView lvJobType;

    private Context context;
    private CommonAdapter<Dictionary> adapter;
    private List<Dictionary> dictionaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_job_type);
        ButterKnife.bind(this);
        context = this;
        dictionaries = new ArrayList<>();
        getData();
        lvJobType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ConfirmJobTypeActivity.class);
                intent.putExtra("type", dictionaries.get(position).getTerm_id());
                startActivityForResult(intent, 0x110);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            switch (requestCode){
                case 0x110:
                    if(data!=null){
                        String name = data.getStringExtra("name");
                        Intent intent = new Intent();
                        intent.putExtra("name",name);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    break;
            }
        }
    }

    /**
     * 获取数据列表
     */
    private void getData() {
        String url = UserNetConstant.GETDICTIONARYLIST
                + "&parentid=1";
        VolleyUtil.VolleyGetRequest(context, url, new VolleyUtil.VolleyJsonCallback() {
            @Override
            public void onSuccess(String result) {
                if (JsonResult.JSONparser(context, result)) {
                    setAdatper(result);
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
    private void setAdatper(String result) {
        dictionaries.clear();
        dictionaries = getBeanFromJson(result);
        adapter = new CommonAdapter<Dictionary>(context,dictionaries,R.layout.item_job_type) {
            @Override
            public void convert(ViewHolder holder, Dictionary dictionary) {
                holder.setText(R.id.tv_type_name,dictionary.getName())
                        .setText(R.id.tv_type_description,dictionary.getDescription());
            }
        };
        lvJobType.setAdapter(adapter);
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
