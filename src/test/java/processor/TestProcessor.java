package processor;

import cn.edu.bupt.rsx.htmlparser.tools.TikaTools;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class TestProcessor implements PageProcessor {

    private Site page = Site.me()
            .setTimeOut(60000)
            .setRetryTimes(3)
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
            .setSleepTime(1000);

    @Override
    public void process(Page page) {
        String content = page.toString();
        TikaTools.parseContent(content, "http://news.sina.com.cn/", "");
    }

    @Override
    public Site getSite() {
        // TODO Auto-generated method stub
        return page;
    }

}
