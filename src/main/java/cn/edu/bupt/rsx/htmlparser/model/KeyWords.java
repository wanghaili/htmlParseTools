package cn.edu.bupt.rsx.htmlparser.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by wanghl on 2016/12/4.
 */
public class KeyWords implements Serializable,IIdAware{
    private long id;
    private String keyWord;
    private int weight = 0;
    private long fileSign;
    private String pageType;
    private String url;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getFileSign() {
        return fileSign;
    }

    public void setFileSign(long fileSign) {
        this.fileSign = fileSign;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KeyWords{");
        sb.append("id=").append(id);
        sb.append(", keyWord='").append(keyWord).append('\'');
        sb.append(", weight=").append(weight);
        sb.append(", fileSign='").append(fileSign).append('\'');
        sb.append(", pageType='").append(pageType).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
