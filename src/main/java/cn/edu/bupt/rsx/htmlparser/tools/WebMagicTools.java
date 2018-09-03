package cn.edu.bupt.rsx.htmlparser.tools;

import org.junit.Test;

import cn.edu.bupt.rsx.webmagic.processor.HtmlParserProcessor;
import us.codecraft.webmagic.Spider;

public class WebMagicTools {
	
	public static void parser(String[] urls,String type){
		HtmlParserProcessor htmlParserProcessor = new HtmlParserProcessor();
		htmlParserProcessor.setSite(urls);
		htmlParserProcessor.setType(type);
		Spider.create(htmlParserProcessor).thread(10).run();
	}

	@Test
	public void testParser(){
		parser(null,null);
	}
}
