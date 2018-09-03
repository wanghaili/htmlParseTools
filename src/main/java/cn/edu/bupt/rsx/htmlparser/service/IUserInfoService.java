package cn.edu.bupt.rsx.htmlparser.service;

import cn.edu.bupt.rsx.htmlparser.model.UserInfo;

/**
 * Created by renshuoxin on 2016/9/3.
 */
public interface IUserInfoService {
    UserInfo queryByName(String loginName);
}
