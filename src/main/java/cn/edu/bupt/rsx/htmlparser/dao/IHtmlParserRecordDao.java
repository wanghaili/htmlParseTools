package cn.edu.bupt.rsx.htmlparser.dao;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;

import java.util.List;
import java.util.Map;

public interface IHtmlParserRecordDao extends IBaseDao<HtmlParserRecord>{

    List<HtmlParserRecord> queryByUrlAndFileId(String url,Integer FileId);

    void insertList(List<HtmlParserRecord> htmlParserRecords);

    List<HtmlParserRecord> queryByFileId(int fileId);

    List<Map<Object, Object>> countNumByCategory(Integer fileId);

    List<HtmlParserRecord> queryByFileIdAndType(Integer fileId, String type);

    List<HtmlParserRecord> queryByFileIdAndPage(int fileId, int i, int pageSize);
}
