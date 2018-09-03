package cn.edu.bupt.rsx.htmlparser.model.vo;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by wanghl on 2016/7/17.
 */
public class JudgeResult implements Serializable {

    private boolean SUCCESS = true;
    private String DATA = "未知";
    private String MSG = "成功";

    public boolean isSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(boolean SUCCESS) {
        this.SUCCESS = SUCCESS;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    @Override
    public String toString() {
        return "JudgeResult{" +
                "SUCCESS=" + SUCCESS +
                ", DATA='" + DATA + '\'' +
                ", MSG='" + MSG + '\'' +
                '}';
    }
}
