package cn.edu.bupt.rsx.htmlparser.dao.impl;

import cn.edu.bupt.rsx.htmlparser.dao.AbstractBaseDao;
import cn.edu.bupt.rsx.htmlparser.dao.IUserInfoDao;
import cn.edu.bupt.rsx.htmlparser.model.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renshuoxin on 2016/9/3.
 */
@Repository
public class UserInfoDao extends AbstractBaseDao<UserInfo> implements IUserInfoDao {
    private static final String NAMESPACE = "UserInfoMapper";
    @Override
    protected String getNameSpace() {
        return NAMESPACE;
    }

    @Override
    public List<UserInfo> selectByIdList(List<Long> idList) {
        return null;
    }

    @Override
    public UserInfo queryByName(String loginName) {
        Map<String,Object> map = new HashMap<>();
        map.put("userName",loginName);
        return super.getSqlSession().selectOne(NAMESPACE + ".queryListByParams",map);
    }
}
