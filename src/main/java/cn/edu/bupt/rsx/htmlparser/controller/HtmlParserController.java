package cn.edu.bupt.rsx.htmlparser.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.model.vo.JudgeResult;
import cn.edu.bupt.rsx.htmlparser.tools.Constants;
import cn.edu.bupt.rsx.textdeal.tools.DocExtractTools;
import cn.edu.bupt.rsx.textdeal.tools.ResponseUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import cn.edu.bupt.rsx.htmlparser.tools.SplitWordsTools;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;

@Controller
@RequestMapping("/parser")
public class HtmlParserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(HtmlParserController.class);
    @Resource
    private IHtmlParserService htmlParserService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        DocExtractTools.entityExtract("");

        return "this is a test";
    }

    /**
     * 解析文本内容并返回
     * @param file
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/uploadText", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> upload(@RequestParam(value = "file_upload", required = false) MultipartFile file) throws UnsupportedEncodingException, IOException {
        LOGGER.info("request begin");
        String fileName = file.getOriginalFilename();
        String content = new String(file.getBytes(), "utf-8");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName", fileName);
        map.put("content", content);
        return map;
    }

    /**
     * 分词
     * @param splitContent
     * @param num
     * @param session
     * @return
     */
    @RequestMapping("/splitWords")
    public
    @ResponseBody
    ReturnResult splitWords(@RequestParam(value = "splitContent", required = false) String splitContent,
                            @RequestParam(value = "num", required = false) Integer num,
                            HttpSession session) {
        if (splitContent != null) {
            session.setAttribute("splitContent", splitContent);
        } else {
            Object content = session.getAttribute("splitContent");
            if (content != null) {
                splitContent = (String) content;
            }
        }
        ReturnResult result = new ReturnResult();
        Map<String, Object> resultMap = SplitWordsTools.splitWords("", splitContent, num ,3);
        result.setResult(resultMap);
        return result;


    }

    @RequestMapping("/removeStopWord")
    @ResponseBody
    public ReturnResult removeStopWord(@RequestParam(value = "num", required = false) Integer num, HttpSession session) {
        Object content = session.getAttribute("splitContent");
        String splitContent = "";
        if (content != null) {
            splitContent = (String) content;
        }
        ReturnResult result = new ReturnResult();
        Map<String, Object> resultMap = SplitWordsTools.splitWords("", splitContent, num,3);
        result.setResult(resultMap);
        return result;
    }

    /**
     * 判断网页类型
     * @param url
     * @return
     */
    @RequestMapping("/judgeType")
    @ResponseBody
    public ReturnResult judgeType(@RequestParam("url") String url) {
        ReturnResult returnResult = new ReturnResult();
        String result = htmlParserService.judgeType(url);
        if (!StringUtils.isEmpty(result)) {
            JudgeResult judgeResult = JSON.parseObject(result, JudgeResult.class);
            if (judgeResult.isSUCCESS()) {
                returnResult.setResult(judgeResult.getDATA());
            } else {
                returnResult.setErrcode(Constants.JUDGE_TYPE_ERROR);
                returnResult.setErrMsg(judgeResult.getMSG());
            }
        }
        return returnResult;
    }

    /**
     * 提取关键词
     * @param content
     * @param num
     * @return
     */
    @RequestMapping("/getKeyWords")
    @ResponseBody
    public ReturnResult getKeyWords(@RequestParam(value = "content", required = false) String content,
                            @RequestParam(value = "num", required = false) Integer num) {
        ReturnResult result = new ReturnResult();
        if(StringUtils.isEmpty(content)){
            result.setErrcode(2);
            return result;
        }
        Map<String, Object> resultMap = SplitWordsTools.splitWords("", content, num ,3);
        result.setResult(resultMap.get("keyWords"));
        return result;
    }

    /**
     * 根据文件标识过滤记录
     * @param fileId
     * @return
     */
    @RequestMapping("getByFileId")
    @ResponseBody
    public ReturnResult getByFileId(@RequestParam(value = "fileId") int fileId,
                                              @RequestParam(value = "pageNum") int pageNum,
                                              @RequestParam(value = "pageSize") int pageSize) {
        ReturnResult returnResult = new ReturnResult();
        returnResult.setResult(htmlParserService.queryByFileIdAndPage(fileId,pageNum,pageSize));
        return returnResult;
    }

    /**
     * 修改链接类型
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("modifyType")
    @ResponseBody
    public ReturnResult modifyType(@RequestParam(value = "id") Integer id,
                                     @RequestParam(value = "type") String type) {
        ReturnResult result = new ReturnResult();
        if(!StringUtils.isEmpty(type)) {
            htmlParserService.modifyType(id,type);
        } else {
            result.setErrcode(ResponseUtils.REQUEST_PARAM_IS_NOT_EMPTY);
        }
        return result;
    }
}
