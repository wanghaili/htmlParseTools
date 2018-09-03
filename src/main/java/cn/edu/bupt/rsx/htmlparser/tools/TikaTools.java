package cn.edu.bupt.rsx.htmlparser.tools;

import java.io.*;
import java.util.List;
import java.util.Map;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.BoilerpipeContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.eclipse.jetty.util.ajax.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.xml.sax.ContentHandler;

import cn.edu.bupt.rsx.htmlparser.model.HtmlParserRecord;

public class TikaTools {
    private final static Logger LOGGER = LoggerFactory.getLogger(TikaTools.class);

    public static HtmlParserRecord parseContent(String content, String url, String type) {
        HtmlParserRecord htmlParserRecord = new HtmlParserRecord();
        htmlParserRecord.setUrl(url);
        htmlParserRecord.setType(type);
        String charset = HttpUtils.getCharSet(content);
        if (!charset.toLowerCase().contains("utf")) {
            charset = "gbk";
        }
        StringWriter textMainBuffer = new StringWriter();
        Metadata metadata = new Metadata();
        try {
            InputStream inputStream = new ByteArrayInputStream(content.getBytes(charset));
            Parser parser = new AutoDetectParser();
            ContentHandler handler = new TeeContentHandler(
                    getTextMainContentHandler(textMainBuffer));
            ParseContext context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
        } catch (Exception e) {
            LOGGER.error("tika parse the website error：", e);
        }
        if ("主题型".equals(type)) {
            if(StringUtils.isEmpty(textMainBuffer.toString())) {
                htmlParserRecord.setContent("");
            } else {
                htmlParserRecord.setContent(textMainBuffer.toString());
            }
        } else {
            htmlParserRecord.setContent("");
        }
        for (String name : metadata.names()) {
            String value;
            if ("keywords".equals(name.toLowerCase())) {
                if(StringUtils.isEmpty(metadata.get(name))) {
                    value = "";
                }else{
                    value = metadata.get(name);
                }
                htmlParserRecord.setKeyWords(value);
            } else if ("description".equals(name.toLowerCase())) {
                if(StringUtils.isEmpty(metadata.get(name))) {
                    value = "";
                }else{
                    value = metadata.get(name);
                }
                htmlParserRecord.setDescription(value);
            } else if ("title".equals(name.toLowerCase())) {
                if(StringUtils.isEmpty(metadata.get(name))) {
                    value = "";
                }else{
                    value = metadata.get(name);
                }
                htmlParserRecord.setTitle(value);
                if ("电商".equals(type)) {
                    htmlParserRecord.setContent(value);
                }
            }
        }
        return htmlParserRecord;
    }

    private static ContentHandler getTextMainContentHandler(Writer writer) {
        return new BoilerpipeContentHandler(writer);
    }
}
