package cn.edu.bupt.rsx.htmlparser.dao.impl;

import cn.edu.bupt.rsx.htmlparser.dao.AbstractBaseDao;
import cn.edu.bupt.rsx.htmlparser.dao.IKeyWordsDao;
import cn.edu.bupt.rsx.htmlparser.model.KeyWords;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wanghl on 2016/12/4.
 */
@Repository("keyWordsDao")
public class KeyWordsDao extends AbstractBaseDao<KeyWords> implements IKeyWordsDao {
    private static final String NAMESPACE = "KeyWordsMapper";


    @Override
    protected String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    public List<KeyWords> selectByIdList(List<Long> idList) {
        return null;
    }

    @Override
    public void insertList(List<KeyWords> keyWordses) {
        super.getSqlSession().insert(NAMESPACE + ".insertList",keyWordses);
    }

    @Override
    public List<Map<Object, Object>> countTop10(long fileId, String pageType, int num) {
        Map<String , Object> map = new HashMap<>();
        map.put("fileSign",fileId);
        map.put("pageType",pageType);
        map.put("num",num);
        return super.getSqlSession().selectList(NAMESPACE + ".queryCountTop10", map);
    }
}
