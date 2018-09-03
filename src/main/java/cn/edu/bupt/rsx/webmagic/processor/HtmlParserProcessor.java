package cn.edu.bupt.rsx.webmagic.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.model.WebPageFeature;
import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import cn.edu.bupt.rsx.htmlparser.service.IWebPageFeatureService;
import cn.edu.bupt.rsx.htmlparser.tools.CalculateTools;
import cn.edu.bupt.rsx.htmlparser.tools.Constants;
import cn.edu.bupt.rsx.htmlparser.tools.GuavaCacheTools;
import cn.edu.bupt.rsx.htmlparser.tools.ServiceLocator;
import cn.edu.bupt.rsx.htmlparser.tools.TikaTools;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
@Component
public class HtmlParserProcessor implements PageProcessor{
	private static final Logger LOGGER = LoggerFactory.getLogger(HtmlParserProcessor.class);
	private String type = "";
	private Site page = Site.me()
			.setTimeOut(60000)
			.setRetryTimes(3)
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
			.setSleepTime(1000); 
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		IWebPageFeatureService webPageFeatureService = (IWebPageFeatureService) ServiceLocator
				.getBean("webPageFeatureService");
		WebPageFeature webPageFeature = new WebPageFeature();
		String pageType = type;
		Html html = page.getHtml();
		String url = page.getUrl().toString();
        Document dom = html.getDocument();
        Document allElement = dom;
        Elements elements = dom.getElementsByTag("style");
        Elements elements2 = dom.getElementsByTag("script");
        elements2.remove();
        elements.remove();
        Element element = dom.getElementsByTag("body").get(0);
		if("新闻".equals(pageType)) {
			int param1 = collectPageFeature(url);
			double param2 = countContentLen(allElement);
			double param3 = countNotLinksLen(element);
			double param4 = countTotalLen(element);
			double param5 = countPunctuationLen(element);
			LOGGER.info("网页特征值：" + param1 + ",网页大小：" + param2
					+ ",非链接字符数大小：" + param3 + "文本总长度" + param4 + ",标点符号大小：" + param5);
			double noLinkRatio = CalculateTools.division(param3, param2);
			double punctuationRatio = CalculateTools.division(param5, param2);
			double textLinkRatio = 1 - CalculateTools.division(param3, param4);
			webPageFeature.setUrl(url);
			webPageFeature.setSlashNum(param1);
			webPageFeature.setNoLinkRatio(noLinkRatio);
			webPageFeature.setPunctuationRatio(punctuationRatio);
			webPageFeature.setTextLinkRatio(1 - (textLinkRatio));
			webPageFeature.setCreateTime(System.currentTimeMillis());
			webPageFeature.setUpdateTime(System.currentTimeMillis());

			webPageFeature = webPageFeatureService.insert(webPageFeature);
			pageType = webPageFeature.getType();
		}
		HtmlParserRecord htmlParserRecord = TikaTools.parseContent(html.toString(), url, pageType);
		GuavaCacheTools.put(url, htmlParserRecord);
	}

	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return page;
	}
	
	public Site addRequests(String[] urls){
		return page;
	}
	//统计url特征
	public int collectPageFeature(String url){
		int result = url.split("/").length - 1;
		if(url.contains("index")){
			result = 2;
		}
		return result;
	}
	
	//统计文本总长度
	public double countContentLen(Document dom){
		String content = dom.text();
		return getByte(content);
	}
	public double countTotalLen(Element e){
		double contentSize = getByte(e.text());
		return contentSize;
	}
	//统计非链接字符长度
	public double countNotLinksLen(Element e){
		double linksSize = 0;
		double contentSize = getByte(e.text());
		Elements elements = e.getElementsByAttribute("href");
		if(elements!=null){
			linksSize = getByte(elements.text());
		}
		return contentSize - linksSize;
	}
	
	//统计标点长度
	public double countPunctuationLen(Element e){
		String content = e.text();
		int len = content.length();
		content = content.replaceAll(Constants.ZH_PUNCTUATION, "");
		int zhLen = len - content.length();
		content = content.replaceAll(Constants.EN_PUNCTUATION, "");
		int enLen = len - zhLen - content.length();
		double result = zhLen*2 + enLen;
		return result;
		
	}
	
	public static File createFile(String content , String fileName){
		String filePath = "";
		if(StringUtils.isEmpty(fileName)){
			long date = System.currentTimeMillis();
			filePath = "G:/workspace_for_java/htmlParseTools/release/" + "/" + date + ".txt";
		}
		FileWriter resultFile = null;
		File myFilePath = null;
		try {
			myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			resultFile = new FileWriter(myFilePath);
			PrintWriter Myfile = new PrintWriter(resultFile);
			Myfile.println(content);
			resultFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("创建文件失败"+e.getMessage());
		}
		return myFilePath;
	}
	
	private int getByte(String content){
		int result = 0;
		try {
			result = content.getBytes("UTF-8").length;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("获取文本byte值异常：" + e.getMessage());
		}
		return result;
	}
	
	public void setSite(String[] urls){
	    if(urls!=null){
	      for(String url:urls){
	        Request request = new Request(url.trim());
	        page.addStartRequest(request);
	      }
	    }
	}

	public void setType(String type) {
		this.type = type;
	}
}
