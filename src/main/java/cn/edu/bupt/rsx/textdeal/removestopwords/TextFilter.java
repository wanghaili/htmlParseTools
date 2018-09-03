package cn.edu.bupt.rsx.textdeal.removestopwords;



import java.io.BufferedReader;
import java.util.*;
import java.util.regex.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.ansj.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.bupt.rsx.htmlparser.tools.ConfigTools;
import org.springframework.util.StringUtils;

/**
 * 该算法主要是将文本中的句子 抽取中文词； 
 * 
 *  
 * @author douyu
 *
 */
public class TextFilter{
	private final static Logger LOGGER = LoggerFactory.getLogger(TextFilter.class);
	public static boolean isnot_stopwords(String st,Set<String> stopwords)
	{

		if(stopwords.contains(st))
			return false;
		return true;

	}
	public static boolean is_Chinese_string(String string )
	{
		boolean mark=true;
		int n=0;	
		for (int i = 0; i < string.length(); i++) {
			n = (int)string.charAt(i);		
			if((19968 <= n && n <40623)) {	
			 
			}else {
				mark=false;
				break;
			}	 
		}
		return mark;
	}
	public static boolean is_English_string(String string)
	{
		boolean mark = true;
		int n=0;
		for(int i = 0;i < string.length();i++){
			n =(int)string.charAt(i);
			if((65<=n&&n<=90)||(97<=n&&n<=122)){
				
			}else{
				mark = false;
				break;
			}
			
		}
		return mark;
	}
	public static boolean isnot_regex_match(String string){
		String st = "^([a-z0-9A-Z\\u4e00-\\u9fa5])\\1+$";
		Pattern p = Pattern.compile(st);
		Matcher m = p.matcher(string);
		if(m.matches())
			return false;    //it's a  useless string


		return true;
	}
	public static Map<String, Object> run(List<Term> wordArr) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, String>> words = new ArrayList<>();
		List<Term> termList = new ArrayList<>();
		Set<String> wordMeans = new HashSet<>();
		Set<String> stopwords = new HashSet<>();
		try {
			BufferedReader inBufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(TextFilter.class.getResource("/").getPath()+
							ConfigTools.getProperty("stop_words", null)),Charset.forName("UTF-8")));
			String line;
			
			while((line = inBufferedReader.readLine()) != null)
			{
				line = line.trim();
				if(!stopwords.contains(line))
				{
					stopwords.add(line);
					
				}
			}
			inBufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
		} 
		 			
		for (int i = 0; i < wordArr.size(); i++) {
			Map<String, String> map = new HashMap<>();
			String termStr = wordArr.get(i).toString().trim();
			if(StringUtils.isEmpty(termStr)) {
				continue;
			}
			String[] strArr = termStr.split("/");
			if (strArr.length>0&&(strArr[0].length()>=2)&&(is_English_string(strArr[0])||is_Chinese_string(strArr[0]))&&isnot_stopwords(strArr[0],stopwords)&&isnot_regex_match(strArr[0])) {
				if(strArr.length==2){
					map.put("word",strArr[0]);
					map.put("wordMean",strArr[1]);
					termList.add(wordArr.get(i));
					words.add(map);
					wordMeans.add(strArr[1]);
				}
			}
			
		} 
		resultMap.put("words",words);
		resultMap.put("wordMeans",wordMeans);
		resultMap.put("termList",termList);
		return resultMap;
	}
}
