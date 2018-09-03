package cn.edu.bupt.rsx.htmlparser.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import cn.edu.bupt.rsx.htmlparser.service.impl.HtmlParserServiceImpl;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;
import cn.edu.bupt.rsx.htmlparser.service.IWebPageFeatureService;
import cn.edu.bupt.rsx.htmlparser.tools.GuavaCacheTools;
import cn.edu.bupt.rsx.htmlparser.tools.SplitWordsTools;
import cn.edu.bupt.rsx.htmlparser.tools.WebMagicTools;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;

@Controller
@RequestMapping("/feature")
public class WebPageFeatureController {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebPageFeatureController.class);
    @Resource
    private IWebPageFeatureService webPageFeatureService;
    @Resource
    private IHtmlParserService htmlParserService;

    @RequestMapping("/test")
    public void test() {
        WebPageFeature webPageFeature = new WebPageFeature();
        webPageFeature.setUrl("12222");
        webPageFeature.setSlashNum(2);
        webPageFeature.setNoLinkRatio(2);
        webPageFeature.setPunctuationRatio(2);
        webPageFeature.setTextLinkRatio(1 - (2));
        webPageFeature.setType("");
        webPageFeature.setCreateTime(System.currentTimeMillis());
        webPageFeature.setUpdateTime(System.currentTimeMillis());
        webPageFeatureService.insert(webPageFeature);
    }

    @RequestMapping("/single")
    @ResponseBody
    public ReturnResult single(@RequestParam("url") String url,
                               @RequestParam(value = "num", required = false) Integer num,
                               @RequestParam(value = "type", required = false) String type) {
        HtmlParserRecord htmlParserRecord;
        int status = 0;
        String content = "";
        String header = "";
        List<Map<String, Object>> keyWordsMap = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        ReturnResult result = new ReturnResult();
        htmlParserRecord = htmlParserService.queryByUrlAndFileId(url, 0);
        if (htmlParserRecord == null) {
            status = 1;
            String[] urls = {url};
            WebMagicTools.parser(urls, type);
            if (GuavaCacheTools.get(url) != null) {
                htmlParserRecord = (HtmlParserRecord) GuavaCacheTools.get(url);
            } else {
                htmlParserRecord = htmlParserService.queryByUrlAndFileId(url, 0);
            }
        }
        if ("电商".equals(htmlParserRecord.getType())) {
            resultMap = SplitWordsTools.splitWords(htmlParserRecord.getTitle(), "", num, 2);
        } else if ("视频".equals(htmlParserRecord.getType()) || "目录型".equals(htmlParserRecord.getType())) {
            header = htmlParserRecord.getTitle()
                    + htmlParserRecord.getKeyWords()
                    + htmlParserRecord.getDescription();
            resultMap = SplitWordsTools.splitWords(header, content, num, 1);
        } else {
            content = htmlParserRecord.getContent();
            header = htmlParserRecord.getTitle()
                    + htmlParserRecord.getKeyWords()
                    + htmlParserRecord.getDescription();
            resultMap = SplitWordsTools.splitWords(header, content, num, 0);
        }

        if (resultMap.get("keyWords") != null) {
            keyWordsMap = (List<Map<String, Object>>) resultMap.get("keyWords");
        }
        if (status == 1) {
            htmlParserRecord.setKeyWordsMap(JSON.toJSONString(keyWordsMap));
            htmlParserRecord.setCreateTime(System.currentTimeMillis());
            htmlParserService.insertOrUpdate(htmlParserRecord);
        }
        resultMap.put("title", htmlParserRecord.getTitle());
        resultMap.put("description", htmlParserRecord.getDescription());
        resultMap.put("keywords", htmlParserRecord.getKeyWords());
        resultMap.put("content", htmlParserRecord.getContent());
        resultMap.put("type", htmlParserRecord.getType());
        result.setResult(resultMap);
        return result;

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload(@RequestParam(value = "file", required = false) MultipartFile file) throws UnsupportedEncodingException, IOException {
        String content = new String(file.getBytes(), "utf-8");
        String[] urls = content.split("\n");
        WebMagicTools.parser(urls, "");
    }
}
