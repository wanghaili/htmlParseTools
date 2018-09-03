package cn.edu.bupt.rsx.htmlparser.dao;

import cn.edu.bupt.rsx.htmlparser.model.UserParserRecord;

import java.util.List;

/**
 * Created by renshuoxin on 2016/9/3.
 */
public interface IUserParserDao extends IBaseDao<UserParserRecord>{
   List<UserParserRecord> queryByFileNameAndUserSign(String fileName, String userSign);

   List<UserParserRecord> queryByUserSignAndStatus(String userName,Integer status);
}
