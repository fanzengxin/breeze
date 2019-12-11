package org.breeze.core.bean.login;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

/**
 * @Description: 用户登录信息
 * @Auther: 黑面阿呆
 * @Date: 2019/8/8 14:21
 * @Version: 1.0.0
 */
public class LoginInfo {

    // 用户数据唯一ID
    private String uid;
    // 用户账户
    private String userId;
    // 用户名
    private String userName;
    // 用户性别
    private String gender;
    // 用户联系方式
    private String phoneNum;
    // 用户邮箱
    private String eMail;
    // 用户状态 0正常，1锁定，2禁用
    private int userStatus = 0;
    // 登录时间
    private long loginTime = 0;
    // 登录IP地址
    private String loginIP = null;
    // 用户角色
    private Set<String> role = null;
    // 用户部门
    private Set<String> dept = null;
    // 用户权限
    private Set<String> permissions = null;
    // 用户接口资源
    private Set<String> resources = null;

    public LoginInfo() {

    }

    public LoginInfo(String uid, String userId, String userName, String gender, String phoneNum,
                     String eMail, Set<String> role, Set<String> dept, Set<String> permissions,
                     Set<String> resources) {
        this.uid = uid;
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.eMail = eMail;
        this.role = role;
        this.dept = dept;
        this.permissions = permissions;
        this.resources = resources;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public Set<String> getDept() {
        return dept;
    }

    public void setDept(Set<String> dept) {
        this.dept = dept;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getResources() {
        return resources;
    }

    public void setResources(Set<String> resources) {
        this.resources = resources;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
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
        loginInfo.setUid(json.getString("uid"));
        return loginInfo;
    }

    /**
     * 重写toString方法
     *
     * @return
     */
    public String toString() {
        StringBuffer info = new StringBuffer();
        info.append("{\"uid\":\"").append(this.uid).append("\"");
        info.append(",\"userId\":\"").append(this.userId).append("\"");
        info.append(",\"userName\":\"").append(this.userName).append("\"");
        info.append(",\"gender\":\"").append(this.gender).append("\"");
        info.append(",\"phoneNum\":\"").append(this.phoneNum).append("\"");
        info.append(",\"eMail\":\"").append(this.eMail).append("\"");
        info.append(",\"userStatus\":\"").append(this.userStatus).append("\"");
        info.append(",\"loginTime\":\"").append(this.loginTime).append("\"");
        info.append(",\"role\":\"");
        for (String roleStr : this.role) {
            if (!",".equalsIgnoreCase(info.substring(info.length()-1, info.length()))) {
                info.append(",");
            }
            info.append(roleStr);
        }
        info.append("\"");
        info.append(",\"dept\":\"");
        for (String deptStr : this.dept) {
            if (!",".equalsIgnoreCase(info.substring(info.length()-1, info.length()))) {
                info.append(",");
            }
            info.append(deptStr);
        }
        info.append("\"");
        info.append(",\"permissions\":\"");
        for (String permissionsStr : this.permissions) {
            if (!",".equalsIgnoreCase(info.substring(info.length()-1, info.length()))) {
                info.append(",");
            }
            info.append(permissionsStr);
        }
        info.append("\"");
        info.append(",\"resources\":\"");
        for (String resourcesStr : this.role) {
            if (!",".equalsIgnoreCase(info.substring(info.length()-1, info.length()))) {
                info.append(",");
            }
            info.append(resourcesStr);
        }
        info.append("\"}");
        return info.toString();
    }
}
