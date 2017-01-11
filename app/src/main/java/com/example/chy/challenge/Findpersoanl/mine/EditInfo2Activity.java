package com.example.chy.challenge.Findpersoanl.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chy.challenge.R;
import com.example.chy.challenge.Utils.ToastUtil;
import com.example.chy.challenge.button.WaveView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfo2Activity extends Activity {

    @BindView(R.id.back)
    WaveView back;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_confirm)
    WaveView btnConfirm;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info2);
        ButterKnife.bind(this);
        context = this;
        edContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNumber.setText(s.toString().length() + "");

            }
        });
        getIntentFrom();
    }

    /**
     * 接受上个页面传来的值
     */
    private void getIntentFrom() {
        tvTitle.setText(getIntent().getStringExtra("title"));   //标题
        tvHint.setText(getIntent().getStringExtra("description"));  //描述
        edContent.setHint(getIntent().getStringExtra("hint"));      //提示
        tvRight.setText("/" + getIntent().getStringExtra("number"));    //输入框限制字数
        edContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(getIntent().getStringExtra("number")))});
    }

    @OnClick({R.id.back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_confirm:
                String value = edContent.getText().toString();
                if(value.length() == 0){
                    ToastUtil.showShort(context, "内容不能为空！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("value",value);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
