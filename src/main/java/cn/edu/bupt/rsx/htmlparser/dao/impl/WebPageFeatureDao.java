package cn.edu.bupt.rsx.htmlparser.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.bupt.rsx.htmlparser.dao.AbstractBaseDao;
import cn.edu.bupt.rsx.htmlparser.dao.IWebPageFeatureDao;
import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;
@Repository("webPageFeatureDao")
public class WebPageFeatureDao extends AbstractBaseDao<WebPageFeature> implements IWebPageFeatureDao{
	private static final String NAMESPACE = "WebPageFeatureMapper";
	@Override
	public List<WebPageFeature> selectByIdList(List<Long> idList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public List<WebPageFeature> queryAll() {
		// TODO Auto-generated method stub
		return super.getSqlSession().selectList(NAMESPACE + ".queryAll");
	}

	@Override
	public List<WebPageFeature> querByUrl(String url) {
		return super.getSqlSession().selectList(NAMESPACE + ".queryByUrl",url);
	}

}
