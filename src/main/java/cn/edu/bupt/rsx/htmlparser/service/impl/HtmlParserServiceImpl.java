package cn.edu.bupt.rsx.htmlparser.service.impl;

import javax.annotation.Resource;

import cn.edu.bupt.rsx.htmlparser.model.vo.JudgeResult;
import cn.edu.bupt.rsx.htmlparser.tools.ConfigTools;
import cn.edu.bupt.rsx.htmlparser.tools.HttpUtils;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.bupt.rsx.htmlparser.dao.IHtmlParserRecordDao;
import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("htmlParserService")
public class HtmlParserServiceImpl implements IHtmlParserService {
	private final static Logger LOGGER = LoggerFactory.getLogger(HtmlParserServiceImpl.class);
	private String judgeUrl = ConfigTools.getProperty("judgeUrl", "");

	@Resource
	private IHtmlParserRecordDao htmlParserRecordDao;
	@Override
	public void test() {
		// TODO Auto-generated method stub
		System.out.println("this is test");
	}

	@Override
	public void insertOrUpdate(HtmlParserRecord htmlParserRecord) {
		// TODO Auto-generated method stub
		htmlParserRecordDao.saveOrUpdate(htmlParserRecord);
		
	}

	@Override
	public HtmlParserRecord queryByUrlAndFileId(String url,Integer FileId) {
		List<HtmlParserRecord> list = htmlParserRecordDao.queryByUrlAndFileId(url,FileId);
		if(list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public String judgeType(String url) {
		String requestUrl = String.format("%s?url=%s", judgeUrl, url);
		try {
			return HttpUtils.okHttpGet(requestUrl);
		} catch (Exception e){
			LOGGER.error("judge type Exception:",e.getMessage());
			JudgeResult judgeResult = new JudgeResult();
			return JSON.toJSONString(judgeResult);
		}
	}

	@Override
	public void insertList(long fileSign, List<String> urls) throws Exception {
		try {
			List<HtmlParserRecord> htmlParserRecords = new ArrayList<>();
			for (String url : urls) {
				String result = judgeType(url.trim());
				if (!StringUtils.isEmpty(result)) {
					JudgeResult judgeResult = JSON.parseObject(result, JudgeResult.class);
					if (judgeResult.isSUCCESS()) {
						HtmlParserRecord htmlParserRecord = new HtmlParserRecord();
						htmlParserRecord.setFileSign(fileSign);
						htmlParserRecord.setType(judgeResult.getDATA());
						htmlParserRecord.setUrl(url.trim());
						htmlParserRecord.setCreateTime(System.currentTimeMillis());
						htmlParserRecords.add(htmlParserRecord);
					}
				}
			}
			if(!htmlParserRecords.isEmpty()){
				htmlParserRecordDao.insertList(htmlParserRecords);
			}
		}catch (Exception e){
			throw new Exception(e);
		}
	}

	@Override
	public List<HtmlParserRecord> queryByFileId(int fileId) {
		return htmlParserRecordDao.queryByFileId(fileId);
	}

	@Override
	public void modifyType(Integer id, String type) {
		HtmlParserRecord htmlParserRecord = new HtmlParserRecord();
		htmlParserRecord.setId(id);
		htmlParserRecord.setType(type);
		htmlParserRecord.setModifiTime(System.currentTimeMillis());
		htmlParserRecordDao.saveOrUpdate(htmlParserRecord);
	}

	@Override
	public List<Map<Object,Object>> countNumByCategory(Integer fileId) {
		return htmlParserRecordDao.countNumByCategory(fileId);
	}

	@Override
	public ReturnResult countTop10(Integer fileId, String type) {
		ReturnResult returnResult = new ReturnResult();
		List<HtmlParserRecord> list = htmlParserRecordDao.queryByFileIdAndType(fileId,type);
		if("电商".equals(type)) {
			return countMap(returnResult,list);
		} else {
			return countList(returnResult,list);
		}
	}

	@Override
	public List<HtmlParserRecord> queryByFileIdAndPage(int fileId, int pageNum, int pageSize) {
		return htmlParserRecordDao.queryByFileIdAndPage(fileId,pageNum*pageSize,pageSize);
	}

	/**
	 * 统计电商排行
	 * @param returnResult
	 * @param htmlParserRecords
     * @return
     */
	public ReturnResult countMap(ReturnResult returnResult,List<HtmlParserRecord> htmlParserRecords) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		for(HtmlParserRecord htmlParserRecord:htmlParserRecords) {
			Map keywords = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), HashMap.class);
			mapList.add(keywords);
		}
		returnResult.setResult(mapList);
		return returnResult;
	}

	/**
	 * 统计其他类型关键词排行
	 * @param returnResult
	 * @param htmlParserRecords
     * @return
     */
	public ReturnResult countList(ReturnResult returnResult,List<HtmlParserRecord> htmlParserRecords) {
		List<Map<String, Object>> mapList = new ArrayList<>();
		for(HtmlParserRecord htmlParserRecord:htmlParserRecords) {
			List keywords = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), List.class);
			mapList.addAll(keywords);
		}
		returnResult.setResult(mapList);
		return returnResult;
	}
}
