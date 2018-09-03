package cn.edu.bupt.rsx.htmlparser.dao;

import cn.edu.bupt.rsx.htmlparser.model.UserInfo;

/**
 * Created by renshuoxin on 2016/9/3.
 */
public interface IUserInfoDao extends IBaseDao<UserInfo>{
    UserInfo queryByName(String loginName);
}
