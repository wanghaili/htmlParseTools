package cn.edu.bupt.rsx.htmlparser.service.impl;

import cn.edu.bupt.rsx.htmlparser.dao.IUserParserDao;
import cn.edu.bupt.rsx.htmlparser.dao.impl.KeyWordsDao;
import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.model.KeyWords;
import cn.edu.bupt.rsx.htmlparser.model.UserParserRecord;
import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import cn.edu.bupt.rsx.htmlparser.service.IUserParserService;
import cn.edu.bupt.rsx.htmlparser.tools.Constants;
import cn.edu.bupt.rsx.htmlparser.tools.GuavaCacheTools;
import cn.edu.bupt.rsx.htmlparser.tools.SplitWordsTools;
import cn.edu.bupt.rsx.htmlparser.tools.WebMagicTools;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;
import cn.edu.bupt.rsx.textdeal.tools.ResponseUtils;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by renshuoxin on 2016/9/3.
 */
@Service
public class UserParserServiceImpl implements IUserParserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserParserServiceImpl.class);
    @Autowired
    private IUserParserDao userParserDao;
    @Autowired
    private IHtmlParserService htmlParserService;
    @Autowired
    private KeyWordsDao keyWordsDao;

    @Override
    public ReturnResult judgeType(String fileName, List<String> urls, ReturnResult returnResult) {
        Object userName = SecurityUtils.getSubject().getPrincipal();
        String userSign = "visitor";
        if (userName != null) {
            userSign = userName.toString();
        }
        if (queryByFileNameAndUserSign(fileName, userSign).size() == 0) {
            UserParserRecord userParserRecord = new UserParserRecord();
            userParserRecord.setFileName(fileName);
            userParserRecord.setUserSign(userSign);
            userParserRecord.setCreateTime(new Date());
            userParserRecord.setModifyTime(new Date());
            userParserRecord = insertOrUpdate(userParserRecord);
            try {
                htmlParserService.insertList(userParserRecord.getId(), urls);
            } catch (Exception e) {
                LOGGER.error("batch upload error", e);
                returnResult.setErrcode(ResponseUtils.SYSTEM_ERROR);
            }
        }
        return returnResult;
    }

    @Override
    public UserParserRecord insertOrUpdate(UserParserRecord userParserRecord) {
        userParserDao.saveOrUpdate(userParserRecord);
        return queryByFileNameAndUserSign(userParserRecord.getFileName(), userParserRecord.getUserSign()).get(0);
    }

    @Override
    public List<UserParserRecord> queryByFileNameAndUserSign(String fileName, String userSign) {
        return userParserDao.queryByFileNameAndUserSign(fileName, userSign);
    }

    @Override
    public List<UserParserRecord> queryUserSignAndStatus(String userName, Integer status) {
        return userParserDao.queryByUserSignAndStatus(userName, status);
    }

    @Override
    public ReturnResult parserByFileSign(Integer fileId) {
        ReturnResult returnResult = new ReturnResult();
        List<HtmlParserRecord> htmlParserRecords = htmlParserService.queryByFileId(fileId);
        List<HtmlParserRecord> records = new ArrayList<>();
        List<Map<String, Object>> keyWordsMap = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        String content = "";
        String header = "";
        int count = 0;
        for (HtmlParserRecord htmlParserRecordTemp : htmlParserRecords) {
            HtmlParserRecord htmlParserRecord;
            String[] urls = {htmlParserRecordTemp.getUrl()};
            try {
                WebMagicTools.parser(urls, htmlParserRecordTemp.getType());
            } catch (Exception e) {
                LOGGER.error("parser url Exception:",e.getMessage());
                continue;
            }

            if(GuavaCacheTools.get(htmlParserRecordTemp.getUrl()) == null) {
                continue;
            }
            htmlParserRecord = (HtmlParserRecord) GuavaCacheTools.get(htmlParserRecordTemp.getUrl());
            if ("电商".equals(htmlParserRecord.getType())) {
                resultMap = SplitWordsTools.splitWords(htmlParserRecord.getTitle(), "", 20, 2);
            } else if ("视频".equals(htmlParserRecord.getType()) || "目录型".equals(htmlParserRecord.getType())) {
                header = htmlParserRecord.getTitle()
                        + htmlParserRecord.getKeyWords()
                        + htmlParserRecord.getDescription();
                resultMap = SplitWordsTools.splitWords(header, content, 20, 1);
            } else {
                content = htmlParserRecord.getContent();
                header = htmlParserRecord.getTitle()
                        + htmlParserRecord.getKeyWords()
                        + htmlParserRecord.getDescription();
                resultMap = SplitWordsTools.splitWords(header, content, 20, 0);
            }

            if (resultMap.get("keyWords") != null) {
                keyWordsMap = (List<Map<String, Object>>) resultMap.get("keyWords");
                htmlParserRecord.setKeyWordsMap(JSON.toJSONString(keyWordsMap));
            } else {
                htmlParserRecord.setKeyWordsMap(JSON.toJSONString(resultMap));
            }

            //TODO keyWordsMap

            htmlParserRecord.setId(htmlParserRecordTemp.getId());
            htmlParserRecord.setFileSign(fileId);
            htmlParserRecord.setModifiTime(System.currentTimeMillis());
            htmlParserService.insertOrUpdate(htmlParserRecord);
            if("电商".equals(htmlParserRecord.getType())) {
                insertMap(htmlParserRecord);
            } else {
                insertList(htmlParserRecord);
            }
            records.add(htmlParserRecord);
            if (count == 100) {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    LOGGER.error("sleep error:", e);
                }
                count = 0;
            } else {
                count++;
            }
        }
        returnResult.setResult(records);
        UserParserRecord userParserRecord = new UserParserRecord();
        userParserRecord.setId(fileId);
        userParserRecord.setModifyTime(new Date());
        userParserRecord.setStatus(Constants.FINISH_status);
        insertOrUpdate(userParserRecord);
        return returnResult;
    }

    @Override
    public ReturnResult countNumByCategory(Integer fileId) {
        ReturnResult returnResult = new ReturnResult();
        htmlParserService.countNumByCategory(fileId);
        returnResult.setResult(htmlParserService.countNumByCategory(fileId));
        return returnResult;
    }

    @Override
    public ReturnResult countTop10(Integer fileId, String type,int num) {
        ReturnResult returnResult = new ReturnResult();
        List<Map<Object,Object>> result = keyWordsDao.countTop10(fileId,type,num);
        returnResult.setResult(result);
        return returnResult;
    }

    /**
     * 除电商类型解析方法
     */
    public void insertList(HtmlParserRecord htmlParserRecord) {
        List<KeyWords> keyWordses = new ArrayList<>();
            if(htmlParserRecord.getKeyWordsMap()!=null) {
                List keywords = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), List.class);
                for(int i=0;i<keywords.size();i++) {
                    KeyWords keyWords = new KeyWords();
                    keyWords.setCreateTime(new Date());
                    keyWords.setUrl(htmlParserRecord.getUrl());
                    keyWords.setFileSign(htmlParserRecord.getFileSign());
                    keyWords.setPageType(htmlParserRecord.getType());
                    Map<String,Object> map = (Map<String,Object>)keywords.get(i);
                    keyWords.setKeyWord(map.get("keyword").toString());
                    keyWords.setWeight((Integer) map.get("weight"));
                    keyWordses.add(keyWords);
                }
            }
        keyWordsDao.insertList(keyWordses);
    }

    /**
     * 电商类解析方法
     * @param htmlParserRecord
     */
    public void insertMap(HtmlParserRecord htmlParserRecord) {

        List<KeyWords> keyWordses = new ArrayList<>();

            Map keyMap = null;
            if(htmlParserRecord.getKeyWordsMap()!=null) {
                try {
                    keyMap = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), HashMap.class);
                } catch (Exception e) {
                    return;
                }
                keyMap = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), HashMap.class);
                List<String> keywords = (List<String>) keyMap.get("entityList");
                List<String> brandList = (List<String>) keyMap.get("brandList");
                keywords.addAll(brandList);
                System.out.println(keywords.size());
                for(int i=0;i<keywords.size();i++) {
                    KeyWords keyWords = new KeyWords();
                    keyWords.setCreateTime(new Date());
                    keyWords.setUrl(htmlParserRecord.getUrl());
                    keyWords.setFileSign(htmlParserRecord.getFileSign());
                    keyWords.setPageType(htmlParserRecord.getType());
                    keyWords.setKeyWord(keywords.get(i));
                    keyWordses.add(keyWords);
                }
            }
        keyWordsDao.insertList(keyWordses);
    }

}
