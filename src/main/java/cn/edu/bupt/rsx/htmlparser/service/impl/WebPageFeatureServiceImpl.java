package cn.edu.bupt.rsx.htmlparser.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.edu.bupt.rsx.htmlparser.dao.IWebPageFeatureDao;
import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;
import cn.edu.bupt.rsx.htmlparser.model.vo.CalculationFactor;
import cn.edu.bupt.rsx.htmlparser.service.IWebPageFeatureService;
import cn.edu.bupt.rsx.htmlparser.tools.CalculateTools;
import cn.edu.bupt.rsx.htmlparser.tools.ConfigTools;
import cn.edu.bupt.rsx.htmlparser.tools.Constants;
@Service("webPageFeatureService")
public class WebPageFeatureServiceImpl implements IWebPageFeatureService{
	private final static Logger LOGGER = LoggerFactory.getLogger(WebPageFeatureServiceImpl.class);
	@Resource
	private IWebPageFeatureDao webPageFeatureDao;
	private final static int K = ConfigTools.getProperty("K", 10);
	@Override
	public WebPageFeature insert(WebPageFeature webPageFeature) {
		// TODO Auto-generated method stub
		webPageFeature = judgeType(webPageFeature);
		webPageFeatureDao.saveOrUpdate(webPageFeature);
		return webPageFeature;
	}
	
	public int queryByUrl(String url){
		return webPageFeatureDao.querByUrl(url).size();
	}
	
	/**
	 * 最邻近算法
	 * 
	 */
	public List<CalculationFactor> getCalculationFactors(WebPageFeature webPageFeature){
		List<WebPageFeature> list = webPageFeatureDao.queryAll();
		List<CalculationFactor> calculationFactors = new ArrayList<CalculationFactor>();
		for(WebPageFeature demo:list){
			double[] arg1 = {demo.getSlashNum(),demo.getNoLinkRatio(),demo.getPunctuationRatio(),demo.getTextLinkRatio()};
			double[] arg2 = {webPageFeature.getSlashNum(),webPageFeature.getNoLinkRatio(),webPageFeature.getPunctuationRatio(),demo.getTextLinkRatio()};
			double cosValue = CalculateTools.calculateCos(arg1, arg2);
			CalculationFactor calculationFactor = new CalculationFactor();
			calculationFactor.setDistance(cosValue);
			calculationFactor.setType(demo.getType());
			calculationFactors.add(calculationFactor);
		}
		Collections.sort(calculationFactors, (o1, o2) -> {
            // TODO Auto-generated method stub
            return new Double(o1.getDistance()).compareTo(new Double(o2.getDistance()));
        });
		return calculationFactors;
	}
	
	private WebPageFeature judgeType(WebPageFeature webPageFeature) {
		List<CalculationFactor> list = getCalculationFactors(webPageFeature);
		double mainValue = 0;
		double listValue = 0;
		for(int i = list.size()-1;i>=(list.size()-K-1);i--){
			CalculationFactor calculationFactor = list.get(i);
			double value = 0;
			if(calculationFactor.getDistance()!=0){
				value = 1/calculationFactor.getDistance();
			}
			
			if("非主题型".equals(calculationFactor.getType())){
				listValue += value;
			}else{
				mainValue += value;
			}
		}
		if(mainValue>=listValue){
			webPageFeature.setType(Constants.NEWS_PAGE_TYPE_MAIN);
		}else{
			webPageFeature.setType(Constants.NEWS_PAGE_TYPE_CATALOG);
		}
		return webPageFeature;
	}
}
