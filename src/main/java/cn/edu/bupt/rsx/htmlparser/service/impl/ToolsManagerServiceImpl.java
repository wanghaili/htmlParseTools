package cn.edu.bupt.rsx.htmlparser.service.impl;

import cn.edu.bupt.rsx.htmlparser.service.IToolsManagerService;
import cn.edu.bupt.rsx.htmlparser.tools.ConfigTools;
import cn.edu.bupt.rsx.textdeal.tools.DocExtractTools;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wanghl on 2016/8/14.
 */
@Service("toolsManagerService")
public class ToolsManagerServiceImpl implements IToolsManagerService {

    @Override
    public void uploadInfo(String content) {
        FileWriter writer = null;
        String filePath = ToolsManagerServiceImpl.class.getResource("/").getPath()+
                ConfigTools.getProperty("dictPath", null);
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(filePath, true);
            writer.write("\n"+content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DocExtractTools.importDict(filePath.substring(1));

    }
}
