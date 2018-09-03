package cn.edu.bupt.rsx.textdeal.tools;

import cn.edu.bupt.rsx.textdeal.fileutil.DocExtractLibray;
import com.sun.jna.NativeLong;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DocExtractTools {

    public static void main(String[] args) {
        //testImportUserDict();
        entityExtract("Apple MacBook Air 13.3英寸笔记本电脑 银色(Core i5 处理器/8GB内存/128GB SSD闪存 MMGF2CH)");
    }

    /**
     * 测试导入用户自定义词
     */
    public static void importDict(String filePath) {
        String path = DocExtractTools.class.getResource("/").getPath().substring(1);
        if (DocExtractLibray.Instance.DE_Init(path, 1, "") == 0) {
            System.exit(1);
        } else {
            DocExtractLibray.Instance.DE_ImportUserDict(filePath, false);
        }
        DocExtractLibray.Instance.DE_Exit();
    }

    /**
     * 测试文章实体抽取
     */
    public static Map<String, String> entityExtract(String title) {
        Map<String, String> map = new HashMap<>();
        String path = DocExtractTools.class.getResource("/").getPath().substring(1);
        if (DocExtractLibray.Instance.DE_Init(path, 1, "") == 0) {
            System.out.println(DocExtractLibray.Instance.DE_GetLastErrMsg());
            System.exit(1);
        }
        NativeLong handle = DocExtractLibray.Instance.DE_ParseDocE(title, "shop_name#brand",
                true, DocExtractLibray.ALL_REQUIRED);
        map.put("entity", DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 1));
        map.put("brand", DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 2));
        DocExtractLibray.Instance.DE_ReleaseHandle(handle);
        DocExtractLibray.Instance.DE_Exit();

        return map;
    }

}
