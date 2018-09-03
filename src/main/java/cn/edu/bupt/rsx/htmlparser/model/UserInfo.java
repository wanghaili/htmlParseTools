package cn.edu.bupt.rsx.htmlparser.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wanghl on 2016/8/27.
 */
public class UserInfo implements IIdAware,Serializable{
    private long id;

    //真实姓名
    private String realName;
    //用户名
    private String userName;
    //密码
    private String password;
    //用户权限
    private String permission;
    //创建时间
    private Date createTime;
    //修改时间
    private Date modifyTime;
    //用户状态
    private int status;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
