package cn.edu.bupt.rsx.htmlparser.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.bupt.rsx.textdeal.model.ReturnResult;
import org.springframework.stereotype.Repository;

import cn.edu.bupt.rsx.htmlparser.dao.AbstractBaseDao;
import cn.edu.bupt.rsx.htmlparser.dao.IHtmlParserRecordDao;
import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;

@Repository("htmlParserRecordDao")
public class HtmlParserRecordDao extends AbstractBaseDao<HtmlParserRecord> implements IHtmlParserRecordDao {
	private static final String NAMESPACE = "HtmlParserRecordMapper";
	@Override
	public List<HtmlParserRecord> selectByIdList(List<Long> idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}


	@Override
	public List<HtmlParserRecord> queryByUrlAndFileId(String url,Integer fileId) {
		Map<String , Object> map = new HashMap<>();
		map.put("url",url);
		map.put("fileSign",fileId);
		return super.getSqlSession().selectList(NAMESPACE + ".queryListByParams",map);
	}

	@Override
	public void insertList(List<HtmlParserRecord> htmlParserRecords) {
		super.getSqlSession().insert(NAMESPACE + ".insertList",htmlParserRecords);
	}

	@Override
	public List<HtmlParserRecord> queryByFileId(int fileId) {
		Map<String , Object> map = new HashMap<>();
		map.put("fileSign",fileId);
		return super.getSqlSession().selectList(NAMESPACE + ".queryListByParams",map);
	}

	@Override
	public List<Map<Object, Object>> countNumByCategory(Integer fileId) {
		return super.getSqlSession().selectList(NAMESPACE + ".queryCountNum", String.valueOf(fileId));
	}

	@Override
	public List<HtmlParserRecord> queryByFileIdAndType(Integer fileId, String type) {
		Map<String , Object> map = new HashMap<>();
		map.put("fileSign",fileId);
		map.put("type",type);
		return super.getSqlSession().selectList(NAMESPACE + ".queryListByParams", map);
	}

	@Override
	public List<HtmlParserRecord> queryByFileIdAndPage(int fileId, int pageNum, int pageSize) {
		Map<String , Object> map = new HashMap<>();
		map.put("fileSign",fileId);
		map.put("pageNum",pageNum);
		map.put("pageSize",pageSize);
		return super.getSqlSession().selectList(NAMESPACE + ".queryListByParamsLimit", map);
	}


}
