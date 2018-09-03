package cn.edu.bupt.rsx.htmlparser.controller;

import cn.edu.bupt.rsx.htmlparser.model.UserParserRecord;
import cn.edu.bupt.rsx.htmlparser.service.IUserParserService;
import cn.edu.bupt.rsx.textdeal.model.ReturnResult;
import cn.edu.bupt.rsx.textdeal.tools.ResponseUtils;
import com.google.common.base.Splitter;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by renshuoxin on 2016/9/3.
 */
@Controller
@RequestMapping("/userParser")
public class UserParserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserParserController.class);
    @Autowired
    private IUserParserService userParserService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult upload(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String content = new String(file.getBytes(),"utf-8");
        String fileName = file.getOriginalFilename();
        List<String> urls = Splitter.on("\n").splitToList(content);
        ReturnResult returnResult= new ReturnResult();
        returnResult.setResult(fileName);
        if(urls.isEmpty()){
            returnResult.setErrcode(ResponseUtils.USER_PARSER_CONTENT_IS_EMPTY);
        } else {
            returnResult = userParserService.judgeType(fileName,urls,returnResult);
        }

        return returnResult;
    }

    @RequestMapping("/selectMyFile")
    @ResponseBody
    public ReturnResult selectMyFile(@RequestParam(value="status",required = false) Integer status) {
        ReturnResult returnResult = new ReturnResult();
        String userName = SecurityUtils.getSubject().getPrincipal().toString();
        returnResult.setResult(userParserService.queryUserSignAndStatus(userName,status));
        return returnResult;
    }

    @RequestMapping("parserByFileSign")
    @ResponseBody
    public ReturnResult parserByFileSign(@RequestParam(value="fileId") Integer fileId){
        return userParserService.parserByFileSign(fileId);

    }

    @RequestMapping("countNumByCategory")
    @ResponseBody
    public ReturnResult countNumByCategory(@RequestParam(value="fileId") Integer fileId) {
        return userParserService.countNumByCategory(fileId);
    }

    @RequestMapping("countTop10")
    @ResponseBody
    public ReturnResult countTop10(@RequestParam(value="fileId") Integer fileId,
                                   @RequestParam(value="type") String type,
                                   @RequestParam(value = "num",required = false) Integer num) {
        if(num == null){
            num = 10;
        }
        return userParserService.countTop10(fileId,type,num);
    }
}
