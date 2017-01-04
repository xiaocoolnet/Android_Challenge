package com.example.chy.challenge.Findpersoanl.mine.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/28 0028.
 */
public class MyInvitation implements Serializable {

    /**
     * userid : 301
     * realname : Gao
     * sex : 1
     * photo : avatar20161209092556301.png
     * jobtype : 技术专员/助理
     * address : The
     * create_time :
     */

    private String userid;
    private String realname;
    private String sex;
    private String photo;
    private String jobtype;
    private String address;
    private String create_time;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
