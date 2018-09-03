package cn.edu.bupt.rsx.htmlparser.service;

import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;

public interface IWebPageFeatureService {

	WebPageFeature insert(WebPageFeature webPageFeature);

	int queryByUrl(String url);
}
