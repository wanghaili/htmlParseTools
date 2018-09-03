package cn.edu.bupt.rsx.htmlparser.controller;

import cn.edu.bupt.rsx.htmlparser.service.IToolsManagerService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghl on 2016/8/14.
 */
@Controller
@RequestMapping("/tools")
public class ToolsManagerController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolsManagerController.class);
    @Autowired
    private IToolsManagerService toolsManagerService;

    @RequestMapping(value = "/uploadEntity", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> uploadEntity(@RequestParam(value = "file_upload", required = false) MultipartFile file) throws IOException {
        LOGGER.info("请求开始");
        String fileName = file.getOriginalFilename();
        String content = new String(file.getBytes(), "gbk");
        Map<String, Object> map = new HashMap<>();
        toolsManagerService.uploadInfo(content);
        map.put("fileName", fileName);
        map.put("content", content);
        return map;
    }
}
