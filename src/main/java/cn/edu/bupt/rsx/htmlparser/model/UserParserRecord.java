package cn.edu.bupt.rsx.htmlparser.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户文件映射表
 * Created by renshuoxin on 2016/9/3.
 */
public class UserParserRecord implements IIdAware,Serializable {
    private long id;
    private String fileName;
    private String userSign;
    private Date createTime;
    private Date modifyTime;
    private int status = 0;



    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
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
