package cn.edu.bupt.rsx.htmlparser.service;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;

import java.util.List;
import java.util.Map;

public interface IHtmlParserService {

	void test();
	
	void insertOrUpdate(HtmlParserRecord htmlParserRecord);

	HtmlParserRecord queryByUrlAndFileId(String url,Integer FileId);

	String judgeType(String url);

	void insertList(long fileSign,List<String> urls) throws Exception;

	List<HtmlParserRecord> queryByFileId(int fileId);

	void modifyType(Integer id, String type);

	List<Map<Object,Object>> countNumByCategory(Integer fileId);

	ReturnResult countTop10(Integer fileId, String type);

	List<HtmlParserRecord> queryByFileIdAndPage(int fileId, int pageNum, int pageSize);
}
