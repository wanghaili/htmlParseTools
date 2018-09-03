package cn.edu.bupt.rsx.htmlparser.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.bupt.rsx.textdeal.tools.DocExtractTools;
import com.google.common.base.Splitter;
import org.ansj.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import cn.edu.bupt.rsx.textdeal.getkeywords.GetKeywords;
import cn.edu.bupt.rsx.textdeal.removestopwords.TextFilter;
import cn.edu.bupt.rsx.textdeal.splitwords.Participle_Nlpir;

public class SplitWordsTools {
	private final static Logger LOGGER = LoggerFactory.getLogger(SplitWordsTools.class);

	/**
	 *
	 * @param header
	 * @param content
	 * @param num
	 * @param mode 0:主题型，未知类型 1：目录型，视频类 2：电商类 3：文本类
     * @return
     */
	public static Map<String, Object> splitWords(String header,String content,Integer num ,int mode){
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, String>> words = new ArrayList<>();
		List<Map<String, Object>> keyWords;
		Set<String> wordMeans = new HashSet<>();
		List<Term> splitResult = Participle_Nlpir.splitWords(header+content);
		if(mode!=2) {
			if (splitResult != null) {
				for (Term term : splitResult) {
					String str = term.toString().trim();
					if (!StringUtils.isEmpty(str)) {
						Map<String, String> splitMap = new HashMap<>();
						String[] strArr = str.split("/");
						if (strArr.length == 2) {
							splitMap.put("word", strArr[0]);
							splitMap.put("wordMean", strArr[1]);
							words.add(splitMap);
							wordMeans.add(strArr[1]);
							resultMap.put("words", words);
							resultMap.put("wordMeans", wordMeans);
						}
					}
				}
				Map<String, Object> stopMap = TextFilter.run(splitResult);
				if (stopMap != null) {
					wordMeans = (Set<String>) stopMap.get("wordMeans");
					words = (List<Map<String, String>>) stopMap.get("words");
					splitResult = (List<Term>) stopMap.get("termList");
					resultMap.put("stopWords", words);
					resultMap.put("stopWordMeans", wordMeans);
				}
			}
		}
		if(mode == 3) {
			keyWords = getKeyWords1(splitResult, num);
			resultMap.put("keyWords", keyWords);
		} else if(mode == 2) {
			resultMap = getKeyWords4(header, resultMap);
		} else if(mode == 1) {
			keyWords = getKeyWords3(splitResult, num);
			resultMap.put("keyWords", keyWords);
		}else {
			keyWords = getKeyWords2(header, splitResult, num);
			resultMap.put("keyWords", keyWords);
		}
		return resultMap;
	}

	public static List<Map<String,Object>> getKeyWords1(List<Term> termList,Integer num){
		if(num==null||num==0){
			num=20;
		}
		List<Map<String,Object>> keyWords = GetKeywords.getKeyWords(termList, num);
		return toInteger(keyWords);
	}

	public static List<Map<String,Object>> getKeyWords2(String header,List<Term> termList,Integer num){
		if(num==null||num==0){
			num=20;
		}
		List<Map<String,Object>> keyWords = GetKeywords.getKeyWords1(header,termList, num);
		return toInteger(keyWords);
	}

	public static List<Map<String,Object>> getKeyWords3(List<Term> termList,Integer num){
		if(num==null||num==0){
			num=20;
		}
		List<Map<String,Object>> keyWords = GetKeywords.getKeyWords2(termList, num);
		return toInteger2(keyWords);
	}

	public static Map<String, Object> getKeyWords4(String title,Map<String, Object> resultMap){
		Map<String,String> entityMap = DocExtractTools.entityExtract(title);
		String entity = entityMap.get("entity");
		String brand = entityMap.get("brand");
		List<String> entityLists = new ArrayList<>();
		List<String> brandLists = new ArrayList<>();
		if(!StringUtils.isEmpty(entity)) {
			entityLists = Splitter.on("#").splitToList(entity.substring(0,entity.length()-1));
		}

		if(!StringUtils.isEmpty(brand)) {
			brandLists = Splitter.on("#").splitToList(brand.substring(0,brand.length()-1));
		}

		resultMap.put("entityList",entityLists);
		resultMap.put("brandList",brandLists);
		return resultMap;
	}

	public static List<Map<String, Object>> toInteger(List<Map<String, Object>> maps) {
		List<Map<String,Object>> changeMaps = new ArrayList<>();
		if(maps.size()>0) {
			Float compareObj = (Float) maps.get(0).get("weight");
			for (int i = 0; i < maps.size(); i++) {
				Map<String, Object> map = maps.get(i);
				Float f = (Float) map.get("weight");
				Integer val = (int) (100 * f / compareObj);
				map.put("weight", val);
				changeMaps.add(map);
			}
		}
		return changeMaps;
	}

	public static List<Map<String, Object>> toInteger2(List<Map<String, Object>> maps) {
		List<Map<String,Object>> changeMaps = new ArrayList<>();
		Integer compareObj = (Integer) maps.get(0).get("weight");
		for(int i =0;i<maps.size();i++) {
			Map<String,Object> map = maps.get(i);
			Integer f = (Integer)map.get("weight");
			Integer val =100*f/compareObj;
			map.put("weight",val);
			changeMaps.add(map);
		}
		return changeMaps;
	}

}
