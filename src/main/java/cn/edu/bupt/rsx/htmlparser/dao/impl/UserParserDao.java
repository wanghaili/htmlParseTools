package cn.edu.bupt.rsx.htmlparser.dao.impl;

import cn.edu.bupt.rsx.htmlparser.dao.AbstractBaseDao;
import cn.edu.bupt.rsx.htmlparser.dao.IUserParserDao;
import cn.edu.bupt.rsx.htmlparser.model.UserParserRecord;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renshuoxin on 2016/9/3.
 */
@Repository
public class UserParserDao extends AbstractBaseDao<UserParserRecord> implements IUserParserDao{
    private static final String NAMESPACE = "UserParserMapper";

    @Override
    protected String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    public List<UserParserRecord> selectByIdList(List<Long> idList) {
        return null;
    }

    @Override
    public List<UserParserRecord> queryByFileNameAndUserSign(String fileName, String userSign) {
        Map<String , Object> map = new HashMap<>();
        map.put("fileName",fileName);
        map.put("userSign",userSign);
        return super.getSqlSession().selectList(NAMESPACE + ".queryListByParams",map);
    }

    @Override
    public List<UserParserRecord> queryByUserSignAndStatus(String userName,Integer status) {
        Map<String , Object> map = new HashMap<>();
        map.put("userSign",userName);
        map.put("status",status);
        return super.getSqlSession().selectList(NAMESPACE + ".queryListByParams",map);
    }
}
