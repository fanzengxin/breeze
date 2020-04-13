package org.breeze.core.bean.login;

import com.alibaba.fastjson.JSONObject;
import org.breeze.core.utils.date.UtilDateTime;

import java.util.*;

/**
 * @Description: 用户登录信息
 * @Auther: 黑面阿呆
 * @Date: 2019/8/8 14:21
 * @Version: 1.0.0
 */
public class LoginInfo {

    /**
     * 用户数据唯一ID
     */
    private Long uid;
    /**
     * 用户账户
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 用户联系方式
     */
    private String phoneNum;
    /**
     * 用户邮箱
     */
    private String eMail;
    /**
     * 登录状态 0=正常，1=用户名不存在，2=密码错误，3=用户禁用，4=用户被锁定
     */
    private int loginStatus = 0;
    /**
     * 用户状态 0正常，1锁定，2禁用
     */
    private int userStatus = 0;
    /**
     * 登录时间
     */
    private String loginTime = null;
    /**
     * 登录IP地址
     */
    private String loginIP = null;
    /**
     * 用户角色
     */
    private List<String> role = new ArrayList<>();
    /**
     * 用户部门
     */
    private List<String> dept = new ArrayList<>();
    /**
     * 用户权限
     */
    private List<String> permissions = new ArrayList<>();

    public LoginInfo() {

    }

    public LoginInfo(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public LoginInfo(Long uid, String userId, String userName, String gender, String phoneNum,
                     String eMail, List<String> role, List<String> dept, List<String> permissions) {
        this.uid = uid;
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.eMail = eMail;
        this.role = role;
        this.dept = dept;
        this.permissions = permissions;
        this.loginTime = UtilDateTime.formatTime(new Date());
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public List<String> getDept() {
        return dept;
    }

    public void setDept(List<String> dept) {
        this.dept = dept;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    /**
     * 封装LoginInfo对象
     *
     * @param info
     * @return
     */
    public static LoginInfo parseLoginInfo(String info) {
        LoginInfo loginInfo = new LoginInfo();
        JSONObject json = JSONObject.parseObject(info);
        loginInfo.setUid(json.getLong("uid"));
        loginInfo.setUserId(json.getString("userId"));
        loginInfo.setUserName(json.getString("userName"));
        loginInfo.setGender(json.getString("gender"));
        loginInfo.setPhoneNum(json.getString("phoneNum"));
        loginInfo.seteMail(json.getString("eMail"));
        loginInfo.setUserStatus(json.getInteger("userStatus"));
        loginInfo.setLoginTime(json.getString("loginTime"));
        loginInfo.setRole(json.getJSONArray("roles").toJavaList(String.class));
        loginInfo.setDept(json.getJSONArray("depts").toJavaList(String.class));
        loginInfo.setPermissions(json.getJSONArray("permissions").toJavaList(String.class));
        return loginInfo;
    }

    /**
     * 重写toString方法
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer info = new StringBuffer();
        info.append("{\"uid\":").append(this.uid);
        info.append(",\"userId\":\"").append(this.userId).append("\"");
        info.append(",\"userName\":\"").append(this.userName).append("\"");
        info.append(",\"gender\":\"").append(this.gender).append("\"");
        info.append(",\"phoneNum\":\"").append(this.phoneNum).append("\"");
        info.append(",\"eMail\":\"").append(this.eMail).append("\"");
        info.append(",\"userStatus\":\"").append(this.userStatus).append("\"");
        info.append(",\"loginTime\":\"").append(this.loginTime).append("\"");
        info.append(",\"roles\":[");
        for (int i = 0; i < this.role.size(); i++) {
            if (i > 0) {
                info.append(",");
            }
            info.append("\"").append(this.role.get(i)).append("\"");
        }
        info.append("],\"depts\":[");
        for (int i = 0; i < this.dept.size(); i++) {
            if (i > 0) {
                info.append(",");
            }
            info.append("\"").append(this.dept.get(i)).append("\"");
        }
        info.append("],\"permissions\":[");
        for (int i = 0; i < this.permissions.size(); i++) {
            if (i > 0) {
                info.append(",");
            }
            info.append("\"").append(this.permissions.get(i)).append("\"");
        }
        info.append("]}");
        return info.toString();
    }
}
