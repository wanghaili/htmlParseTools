package cn.edu.bupt.rsx.htmlparser.service;

import cn.edu.bupt.rsx.htmlparser.model.UserParserRecord;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;

import java.util.List;

/**
 * Created by renshuoxin on 2016/9/3.
 */
public interface IUserParserService {
    ReturnResult judgeType(String fileName, List<String> urls,ReturnResult returnResult);

    UserParserRecord insertOrUpdate(UserParserRecord userParserRecord);

    List<UserParserRecord> queryByFileNameAndUserSign(String fileName,String userSign);

    List<UserParserRecord> queryUserSignAndStatus(String userName,Integer status);

    ReturnResult parserByFileSign(Integer fileId);

    ReturnResult countNumByCategory(Integer fileId);

    ReturnResult countTop10(Integer fileId, String type,int num);
}
