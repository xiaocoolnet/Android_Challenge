package com.example.chy.challenge.findcommany.findjob.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chy.challenge.Findpersoanl.talentmain.talentbean.FindPersonalBean;
import com.example.chy.challenge.NetInfo.NetBaseConstant;
import com.example.chy.challenge.R;
import com.example.chy.challenge.button.RoundImageView;
import com.example.chy.challenge.findcommany.findjob.bean.Job_evaluation_bean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class Job_evaluation_adapter extends BaseAdapter{
    private Context mContext;
    private List<Job_evaluation_bean.DataBean> worlist;
    private int type;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.ic_wode).showImageOnFail(R.mipmap.ic_wode).cacheInMemory(true).cacheOnDisc(true).build();

    public Job_evaluation_adapter(Context mContext, List<Job_evaluation_bean.DataBean> worlist, int type) {
        this.mContext = mContext;
        this.worlist = worlist;
        this.type = type;
    }

    @Override
    public int getCount() {
        return worlist.size();
    }

    @Override
    public Object getItem(int position) {
        return worlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder = null;
        if (convertView == null){
            switch (type){
                case 0:
                    convertView = View.inflate(mContext, R.layout.job_adapter,null);
                    holder =  new HolderView();
                    holder.headimage = (RoundImageView) convertView.findViewById(R.id.headimage);//评价人头像
                    holder.job_realname = (TextView)convertView.findViewById(R.id.job_realname);//评价人名称
                    holder.job_isinvaluation = (TextView)convertView.findViewById(R.id.job_isinvaluation);//已面试
                    holder.startone =(TextView) convertView.findViewById(R.id.startone);
                    holder.starttwo = (TextView) convertView.findViewById(R.id.starttwo);
                    holder.startthree = (TextView) convertView.findViewById(R.id.startthree);
                    holder.startfour = (TextView) convertView.findViewById(R.id.startfour);
                    holder.startfive = (TextView) convertView.findViewById(R.id.startfive);
                    holder.job_create_time = (TextView) convertView.findViewById(R.id.job_create_time);//面试评价创建时间
                    holder.invaluation_content = (TextView) convertView.findViewById(R.id.invaluation_content);//面试评价内容
                    break;
            }
            convertView.setTag(holder);
        }else{
            holder = (HolderView) convertView.getTag();
        }
        switch (type){
            case 0:
                if (worlist.get(position).getPhoto() != null&&worlist.get(position).getPhoto().length() > 0) {
                    imageLoader.displayImage(NetBaseConstant.NET_HOST + worlist.get(position).getPhoto(), holder.headimage, options);
                }else{
                    holder.headimage.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_wode));
                }
                holder.job_realname.setText(worlist.get(position).getUser_name()+"");
                if (worlist.get(position).getInvite_title() != null&&worlist.get(position).getInvite_title().length() > 0){
                    holder.job_isinvaluation.setText("已面试："+worlist.get(position).getInvite_title()+"");
                }else{
                    holder.job_isinvaluation.setText("");
                }
                if ("5".equals(worlist.get(position).getStart())){
                    holder.startone.setSelected(true);
                    holder.starttwo.setSelected(true);
                    holder.startthree.setSelected(true);
                    holder.startfour.setSelected(true);
                    holder.startfive.setSelected(true);
                }else if ("4".equals(worlist.get(position).getStart())){
                    holder.startone.setSelected(true);
                    holder.starttwo.setSelected(true);
                    holder.startthree.setSelected(true);
                    holder.startfour.setSelected(true);
                    holder.startfive.setSelected(false);
                }else if ("3".equals(worlist.get(position).getStart())){
                    holder.startone.setSelected(true);
                    holder.starttwo.setSelected(true);
                    holder.startthree.setSelected(true);
                    holder.startfour.setSelected(false);
                    holder.startfive.setSelected(false);
                }else if ("2".equals(worlist.get(position).getStart())){
                    holder.startone.setSelected(true);
                    holder.starttwo.setSelected(true);
                    holder.startthree.setSelected(false);
                    holder.startfour.setSelected(false);
                    holder.startfive.setSelected(false);
                }else if ("1".equals(worlist.get(position).getStart())){
                    holder.startone.setSelected(true);
                    holder.starttwo.setSelected(false);
                    holder.startthree.setSelected(false);
                    holder.startfour.setSelected(false);
                    holder.startfive.setSelected(false);
                }else{
                    holder.startone.setSelected(false);
                    holder.starttwo.setSelected(false);
                    holder.startthree.setSelected(false);
                    holder.startfour.setSelected(false);
                    holder.startfive.setSelected(false);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                holder.job_create_time.setText(simpleDateFormat.format(new Date(Integer.valueOf(worlist.get(position).getCreate_time())*1000))+"");
                holder.invaluation_content.setText(worlist.get(position).getContent()+"");

//                holder.ll_company_click.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                });
                break;
        }
        return convertView;
    }
    class HolderView{
        private RoundImageView headimage;
        private TextView job_realname,job_isinvaluation,startone,starttwo,startthree,startfour,startfive,job_create_time,invaluation_content;
    }
}
