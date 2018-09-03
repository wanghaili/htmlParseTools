package cn.edu.bupt.rsx.textdeal.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils implements Serializable{

	private static final long serialVersionUID = 8938664819517184895L;

	public final static int SYSTEM_ERROR = -1;

	public final static int SPLIT_ERROR = 1;

	public final static int GET_KEYWORDS_ERROR = 2;

	public final static int USER_PARSER_CONTENT_IS_EMPTY = 3;

	public final static int REQUEST_PARAM_IS_NOT_EMPTY = 4;

	public final static Map<Integer, String> msgMap = new HashMap<>();
	
	static {
		msgMap.put(SYSTEM_ERROR,"系统内部错误");
		msgMap.put(SPLIT_ERROR, "分词错误");
		msgMap.put(GET_KEYWORDS_ERROR,"文本内容不能为空");
		msgMap.put(USER_PARSER_CONTENT_IS_EMPTY,"批量解析文本内容为空");
		msgMap.put(REQUEST_PARAM_IS_NOT_EMPTY,"请求参数不能为空");
	}
	
	public static String getMsg(int errCode){
		return msgMap.get(errCode);
	}
}
