package cn.edu.bupt.rsx.htmlparser.tools;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghl on 2016/4/28.
 */
@Component
public class GuavaCacheTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheTools.class);
    private static Cache<String,Object> cache = CacheBuilder.newBuilder().concurrencyLevel(5).expireAfterWrite(3600, TimeUnit.SECONDS).maximumSize(1000).build();

    public static void put(String key,Object value){
        cache.put(key,value);
    }

    public static Object get(String key){
        try {
            return cache.get(key, new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                    return null;
                }
            });
        } catch (Exception e) {
            LOGGER.error("从guava缓存中读取数据出现异常： " + e);
            return null;
        }
    }

    public static void remove(String key){
        cache.invalidate(key);
    }
}
