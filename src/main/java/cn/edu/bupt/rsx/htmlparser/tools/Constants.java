package cn.edu.bupt.rsx.htmlparser.tools;

public class Constants {
	//中文标点集合
	public final static String ZH_PUNCTUATION = "[，。！？]";
	//英文标点集合
	public final static String EN_PUNCTUATION = "[,.!?]";
	//目录型网页标识
	public final static String NEWS_PAGE_TYPE_CATALOG = "非主题型";
	//主题型网页标识
	public final static String NEWS_PAGE_TYPE_MAIN = "主题型";
	//一般类型标识
	public final static int SPLIT_WORDS_TYPE_NORMAL=0;
	//去除停用词类型标识
	public final static int SPLIT_WORDS_TYPE_REMOVE_STOP=1;

	public final static int JUDGE_TYPE_ERROR = -1;

	public final static int DEFAULT_KEYWORDS_NUM = 20;

	public final static int NOMAL_status = 0;

	public final static int FINISH_status = 1;

	/**
	 * 网页编码正则
	 */
	public static final String CHARSET_PATTERN = "<meta(.)*charset=(\\S+?)\"";
}
