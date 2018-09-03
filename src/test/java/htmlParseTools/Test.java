package htmlParseTools;


import cn.edu.bupt.rsx.htmlparser.dao.IHtmlParserRecordDao;
import cn.edu.bupt.rsx.htmlparser.dao.IKeyWordsDao;
import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;
import cn.edu.bupt.rsx.htmlparser.model.KeyWords;
import cn.edu.bupt.rsx.htmlparser.service.IHtmlParserService;
import cn.edu.bupt.rsx.htmlparser.service.IUserParserService;
import cn.edu.bupt.rsx.htmlparser.tools.HttpUtils;
import cn.edu.bupt.rsx.htmlparser.tools.TikaTools;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.SAXException;
import processor.TestProcessor;
import us.codecraft.webmagic.Spider;

import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application_global.xml")
public class Test {
    @Autowired
    private IUserParserService userParserService;

    @Autowired
    private IHtmlParserRecordDao htmlParserRecordDao;

    @Autowired
    private IKeyWordsDao keyWordsDao;
    @org.junit.Test
    public void testStringToArr() {
        Spider.create(new TestProcessor()).addUrl("http://news.sina.com.cn/").thread(1).run();
    }


    @org.junit.Test
    public void testParserFile() {
        userParserService.parserByFileSign(27);
    }

    @org.junit.Test
    public void testGetTotal() throws IOException, SAXException {
        HttpClient client = new DefaultHttpClient();
        String url = "http://finance.ifeng.com/a/20160827/14817188_0.shtml";
        HttpGet httpGet=new HttpGet(url);
        HttpResponse response= client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String strcontent = EntityUtils.toString(entity, "utf8");
        HtmlParserRecord content = TikaTools.parseContent(strcontent,url,"主题型");
        String contents = "国乒实力圈粉李冰冰 与张继科搭肩合影_凤凰财经 首页 资讯 财经 娱乐 体育 时尚 汽车 房产 科技 读书 游戏 文化 历史 军事 博客 彩票 佛教 凤凰卫视 更多 视频 旅游 国学 数码 健康 家居 星座 酒业 未来 注册登录 退出 凤凰财经 凤凰网财经 > 财经滚动新闻 > 正文 新闻 军事 娱乐 体育 财经 科技 历史 时尚 汽车 视频 读书 游戏 房产 彩票 FM App 首页 资讯 财经 娱乐 体育 时尚 汽车 房产 科技 读书 教育 文化 历史 军事 博客 公益 佛教 更多 国乒实力圈粉李冰冰 与张继科搭肩合影 2016年08月27日 13:03 来源：华龙网 用微信扫描二维码 分享至好友和朋友圈 人参与 评论 8月26日北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。张继科搭肩李冰冰显得十分亲密。8月26日北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。张继科搭肩李冰冰显得十分亲密。广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康 【摘要】 8月26日 北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。8月26日 北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。 8月26日北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。张继科搭肩李冰冰显得十分亲密。8月26日北京时间8月25日晚李冰冰在自己的社交媒体上晒出与乒乓球队聚餐的合影，张继科、马龙、马琳、刘国梁等人现身。张继科搭肩李冰冰显得十分亲密。广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物广东娱乐财经体育美食健康教育旅游汽车房产精彩购物 robot 看过本文章的人还看过： 标签：张继科 刘国梁 马龙 推荐 用微信扫描二维码 分享至好友和朋友圈 分享到： 新浪微博 QQ空间 人参与 评论 炒股大赛本周发2万奖金 1高手独享5000元    牛散lqxwcg逆势抓涨停 日赚12% 订阅 免责声明：本文仅代表作者个人观点，与凤凰网无关。其原创性以及文中陈述文字和内容未经本站证实，对本文以及其中全部或者部分内容、文字的真实性、完整性、及时性本站不作任何保证或承诺，请读者仅作参考，并请自行核实相关内容。 理财推荐 预期年化利率 最高13% 查看 凤凰金融-安全理财 凤凰点评： 凤凰集团旗下公司，轻松理财。 近一年 40.77% 购买 混合型-华安逆向策略 凤凰点评： 业绩长期领先，投资尖端行业。 今年以来 29.27% 购买 混合型-前海开源金银珠宝A 凤凰点评： 进可攻退可守，抗跌性能尤佳。 同系近一年收益 70%+ 预约 新方程私募精选E6 凤凰点评： 震荡市场首选，防御性能极佳且收益喜人，老总私人追加百万。 社会 娱乐 生活 探索 历史 她被硫酸毁容后 走上了纽约时装周 男子爬25楼欲轻生：我的苹果手机丢了 山东一男子倒车不慎 轧死两岁女儿(图) 夫妻联手“仙人跳” 妻子色诱招嫖进屋丈夫室外偷盗 男子婚内出轨与第三者生娃 情人带娃和其他人开房 6人吃完火锅都不愿给钱：互相推诿 店员满街追 女子费510万放生6387头羊引疑：盲目放生 害人害己 广州现4人“偷菜团伙”：一次偷走香蕉两万斤(图) 悲催了！丈夫外出打工 妻子竟同时和俩男幽会 女孩参加朋友婚礼 发现新郎竟然是消失多年的父亲 她被硫酸毁容后 走上了纽约时装周 郭德纲：曾想拜师李金斗解决北京户口 王菲演唱会票价贵过iPhone7，最便宜都要1800！ 太尴尬！舒淇刚嫁人就被发现连葱都不会洗… 工作人员否认Baby怀孕 称是衣服问题 90后女星离世 生前求助中医放血拔罐试图治癌 当郭德纲碰到林志玲，他居然想出这招套近乎… 四太梁安琪豪宅被盗 赌王惊呼：那么多山贼！ 穿龙袍穿青花瓷的范爷，这次居然跟猫撞衫了(图) 他曾是当红偶像却甘为老婆下厨20年 成最帅国民老爸 她被硫酸毁容后 走上了纽约时装周 艺术家诠释与别人睡觉的6个阶段 20岁女人和30岁女人的最大区别 婚前同居的女人都是为这事 男人秋季补肾必不可少三种肉 脸上长斑的原因竟然是因为这事做多了 老二这么生，才能把风险几率降到最低！ 美白护肤除斑 中医让你轻松变成白富美 致力于为用户提供简单、实用、贴心的导航服务 妇产科的男医生该如何接生呢？ 她被硫酸毁容后 走上了纽约时装周 国行iPhone 7供货一览：竟然还有现货！ 网曝10万部锤子T3全部返厂：官方愤怒否认 能测量你三围的减肥魔镜 哪里胖减哪里（图） 三星Galaxy Note 7销售将从10月初恢复正常 苹果惨遭打脸！iPhone 7续航竟不如iPhone SE？ 10月15日 北京市手机号码未实名将被停机 Zara创始人奥特加超越比尔·盖茨成世界首富 小伙分不清微信女友头像：转账5000元给女网友悲剧了 这个幽灵小镇 1943年后居民再也没回家 她被硫酸毁容后 走上了纽约时装周 毛泽东首次显露出军事才能是在何时？ 斯大林因“胆怯”多次回绝毛泽东的访苏请求？ 48年蒋介石大骂胡适选总统后自夸：我不做总统谁做 中国历史上皇帝离婚第一案：妃子事帝九年未蒙一幸 鲁迅当京官总收入3万3千银元：年薪约20万元人民币 49年蒋介石定暗杀计划 为何陈毅排第一 曾经积极支持王明的康生如何赢得毛泽东的信任？ 揭秘：张学良被软禁之后唯一一次指挥军队是做何事 重大发现!南昌海昏侯墓寻获《论语》失落篇 网罗天下 广东警方埋伏抓捕失足女 嫖客躺床一丝不苟 金龙鱼罕见产子过程 从嘴里往外不停吐小鱼 男老师猥亵女学生 回应家长质问“她太可爱” 监控:嫌犯审讯室徒手掰断手铐 捅天花板越狱 女子怀疑丈夫出轨：跟踪结果让人脸红 狮子捕食野牛后脑袋卡入肛门 被活活憋死 野猪大战狮子 出现丧心病狂一幕 又忘关摄像头？主播深夜直播造人全程 频道推荐 再次打败市场 巴菲特今年赚翻了 0条评论2016-09-09 07:18:46 华尔街情报：今天即将发生这些事(9月9日) 日媒：日企将投标纽约地铁 或与中企展开对决 纽约豪宅房价已到顶？低价房产备受连累 小摩预警：市场波动加剧 股市未来3年或下跌20% 华尔街也没有余粮：难敌电子交易 债券交易员“ 罗杰斯公布“中国投资策略”：买中国航空股 香港股市面临一严重问题 “僵尸股”流动性堪忧 巨头鲸搁浅海南海滩 民众整夜守护救助(图) 凤凰财经官方微信 凤凰财经 视频 实拍李克强讽美日:你想让我们打架？ 播放数：398260 实拍中方高官八字盛赞杜特尔特 播放数：2734572 实拍华春莹这表情批日：不识时务 播放数：2019470 日本地铁挤成这样了 姑娘们还好吗 播放数：5808920 财富派 战火锻造的富兰克林家族 点击数：1634796 奥巴马拒住的酒店原来是中国人的 点击数：1316626 为什么这个90后是未来的扎克伯格？ 点击数：1088314 陈曦：琴与弓的生活美学 点击数：1755854 图片新闻 被拍肩后高烧不退 点击数：2779774 河流被抗生素污染 点击数：2499127 全球最贵城市港沪京 点击数：1890200 北京制定疏解方案 点击数：2623085 讲堂 音乐 彩铃 视频 凤凰无线 杨钰莹旧照曝光清纯可人 许晴自拍秀长腿 百科 动脑 趣味 更多 欢乐答题汇 1 你知道啤酒瓶盖有多少个褶吗？ 2 不可思议！香烟竟然还有这些好处？ 3 切记！小轿车哪个方位相对来说最危险？ 4 什么东西在长毛之后，就代表成熟了？ 5 什么字，人人见了都会念错？ 答题解锁模糊图片 人的牙齿在呈现什么颜色的时候最健康？ 淡黄色 白色 透明色 黑色 恭喜答对，2秒后解锁! 请选择您认为正确的一项 很遗憾答错，请再试一次 凤凰财经 凤凰新媒体介绍投资者关系 Investor Relations广告服务诚征英才保护隐私权免责条款意见反馈凤凰卫视介绍 视频 ·纪实 ·直播 凤凰卫视 凤凰新媒体版权所有Copyright  2016 Phoenix New Media Limited All Rights Reserved. 分享到:";
        HttpPost httpPost = new HttpPost("http://111.207.243.66:8765/htmlParseTools/parser/getKeyWords");
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("content", contents));

        HttpEntity entitys = HttpUtils.postData(httpPost, formParams);
        System.out.println(EntityUtils.toString(entitys, "UTF-8"));
    }
    @org.junit.Test
    public void insert() {
        List<HtmlParserRecord> htmlParserRecords = htmlParserRecordDao.queryByFileId(27);

        List<KeyWords> keyWordses = new ArrayList<>();

        for(HtmlParserRecord htmlParserRecord:htmlParserRecords) {
            if(htmlParserRecord.getKeyWordsMap()!=null) {
                List keywords = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), List.class);
                for(int i=0;i<keywords.size();i++) {
                    KeyWords keyWords = new KeyWords();
                    keyWords.setCreateTime(new Date());
                    keyWords.setUrl(htmlParserRecord.getUrl());
                    keyWords.setFileSign(htmlParserRecord.getFileSign());
                    keyWords.setPageType(htmlParserRecord.getType());
                    Map<String,Object> map = (Map<String,Object>)keywords.get(i);
                    keyWords.setKeyWord(map.get("keyword").toString());
                    keyWords.setWeight((Integer) map.get("weight"));
                    keyWordses.add(keyWords);
                    if(keyWordses.size()==1000) {
                        keyWordsDao.insertList(keyWordses);
                        keyWordses.clear();
                    }
                }
            }
        }
        keyWordsDao.insertList(keyWordses);
    }

    @org.junit.Test
    public void insertMap() {
        List<HtmlParserRecord> htmlParserRecords = htmlParserRecordDao.queryByFileId(27);

        List<KeyWords> keyWordses = new ArrayList<>();

        for(HtmlParserRecord htmlParserRecord:htmlParserRecords) {
            Map keyMap = null;
            if(htmlParserRecord.getKeyWordsMap()!=null) {
                try {
                    keyMap = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), HashMap.class);
                } catch (Exception e) {
                    continue;
                }
                keyMap = JSON.parseObject(htmlParserRecord.getKeyWordsMap(), HashMap.class);
                List<String> keywords = (List<String>) keyMap.get("entityList");
                List<String> brandList = (List<String>) keyMap.get("brandList");
                keywords.addAll(brandList);
                        System.out.println(keywords.size());
                for(int i=0;i<keywords.size();i++) {
                    KeyWords keyWords = new KeyWords();
                    keyWords.setCreateTime(new Date());
                    keyWords.setUrl(htmlParserRecord.getUrl());
                    keyWords.setFileSign(htmlParserRecord.getFileSign());
                    keyWords.setPageType(htmlParserRecord.getType());
                    keyWords.setKeyWord(keywords.get(i));
                    keyWordses.add(keyWords);
                    if(keyWordses.size()==1000) {
                        keyWordsDao.insertList(keyWordses);
                        keyWordses.clear();
                    }
                }
            }
        }
        keyWordsDao.insertList(keyWordses);
    }
}
