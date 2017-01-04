package com.example.chy.challenge.findcommany.findjob.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26 0026.
 */

public class Job_evaluation_bean {

    /**
     * status : success
     * data : [{"id":"1","companyid":"1","evaluate_id":"301","start":"5","content":"asdsa","choose":"ddd","create_time":"1478314344","invite_title":"android","useful_num":"0","photo":"avatar20161209092556301.png"}]
     */

    private String status;
    /**
     * id : 1
     * companyid : 1
     * evaluate_id : 301
     * start : 5
     * content : asdsa
     * choose : ddd
     * create_time : 1478314344
     * invite_title : android
     * useful_num : 0
     * photo : avatar20161209092556301.png
     */

    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String companyid;
        private String evaluate_id;
        private String start;
        private String content;
        private String choose;
        private String create_time;
        private String invite_title;
        private String useful_num;
        private String photo;
        private String user_name;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyid() {
            return companyid;
        }

        public void setCompanyid(String companyid) {
            this.companyid = companyid;
        }

        public String getEvaluate_id() {
            return evaluate_id;
        }

        public void setEvaluate_id(String evaluate_id) {
            this.evaluate_id = evaluate_id;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChoose() {
            return choose;
        }

        public void setChoose(String choose) {
            this.choose = choose;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getInvite_title() {
            return invite_title;
        }

        public void setInvite_title(String invite_title) {
            this.invite_title = invite_title;
        }

        public String getUseful_num() {
            return useful_num;
        }

        public void setUseful_num(String useful_num) {
            this.useful_num = useful_num;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
