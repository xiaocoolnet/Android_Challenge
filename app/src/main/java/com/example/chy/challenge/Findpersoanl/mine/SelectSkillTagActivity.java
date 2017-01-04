package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.mine.bean.Dictionary;
import com.example.chy.challenge.NetInfo.UserNetConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.JsonResult;
import com.example.chy.challenge.Utils.ToastUtil;
import com.example.chy.challenge.Utils.VolleyUtil;
import com.example.chy.challenge.button.WaveView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectSkillTagActivity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_confirm)
    WaveView btnConfirm;
    @BindView(R.id.flowlayout)
    TagFlowLayout flowlayout;
    @BindView(R.id.btn_add_tag)
    TextView btnAddTag;

    private Context context;
    private List<Dictionary> dictionaries;
    private List<String> tags;
    private TagAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_skill_tag);
        ButterKnife.bind(this);
        context = this;
        dictionaries = new ArrayList<>();
        tags = new ArrayList<>();
        adapter = new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_skill_tag, null);
                textView.setText(s);
                return textView;
            }
        };
        flowlayout.setAdapter(adapter);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        String url = UserNetConstant.GETDICTIONARYLIST
                + "&parentid=36";
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
     *
     * @param result
     */
    private void setAdatper(String result) {
        dictionaries.clear();
        dictionaries = getBeanFromJson(result);
        for (int i = 0; i < dictionaries.size(); i++) {
            tags.add(dictionaries.get(i).getName());
        }
        adapter.notifyDataChanged();
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

    @OnClick({R.id.back, R.id.btn_confirm,R.id.btn_add_tag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_confirm:
                commit();
                break;
            case R.id.btn_add_tag:
                addTag();
                break;
        }
    }

    /**
     * 弹出添加技能标签弹出框
     */
    private void addTag() {
        if(flowlayout.getSelectedList().size()>=3){
            ToastUtil.showShort(context,"选择的标签不能超过三个");
            return;
        }
        final Dialog dialog = new Dialog(context,R.style.DialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_input_tag,null);
        final EditText ed_content = (EditText) view.findViewById(R.id.ed_content);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        TextView btn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.rounded_gray);
        dialog.setCancelable(false);
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed_content.getText().toString();
                if(content.length() == 0){
                    ToastUtil.showShort(context,"输入的内容不能为空");
                    return;
                }
                tags.add(content);
                adapter.notifyDataChanged();
                dialog.dismiss();
            }
        });
    }

    /**
     * 确认标签
     */
    private void commit() {
        if (flowlayout.getSelectedList().size() == 0) {
            ToastUtil.showShort(context, "请选择技能标签");
            return;
        }
        String skills = "";
        for (int str : flowlayout.getSelectedList()) {
            skills = skills + tags.get(str) + "-";
        }
        skills = skills.substring(0, skills.length() - 1);
        Intent intent = new Intent();
        intent.putExtra("skills", skills);
        setResult(RESULT_OK, intent);
        finish();
    }
}
