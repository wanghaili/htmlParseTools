package cn.edu.bupt.rsx.htmlparser.dao;

import java.util.List;

import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;

public interface IWebPageFeatureDao extends IBaseDao<WebPageFeature>{

	List<WebPageFeature> queryAll();

	List<WebPageFeature> querByUrl(String url);
}
