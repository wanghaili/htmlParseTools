package cn.edu.bupt.rsx.htmlparser.service.impl;

import cn.edu.bupt.rsx.htmlparser.dao.IUserInfoDao;
import cn.edu.bupt.rsx.htmlparser.model.UserInfo;
import cn.edu.bupt.rsx.htmlparser.service.IUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by renshuoxin on 2016/9/3.
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements IUserInfoService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    private IUserInfoDao userInfoDao;
    @Override
    public UserInfo queryByName(String loginName) {
        return userInfoDao.queryByName(loginName);
    }
}
