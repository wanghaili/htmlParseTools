package cn.edu.bupt.rsx.htmlparser.dao;

import cn.edu.bupt.rsx.htmlparser.model.KeyWords;

import java.util.List;
import java.util.Map;


/**
 * Created by renshuoxin on 2016/9/3.
 */
public interface IKeyWordsDao extends IBaseDao<KeyWords>{
   void insertList(List<KeyWords> keyWordses);

   List<Map<Object, Object>> countTop10(long fileId, String pageType,int num);
}
