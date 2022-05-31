package cn.gtea.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 根据自己需要配置的属性封装工具类
 * @author Ayeze_Mizon
 * 2022-05-29
 */
public class GsonUtil {

    /**
     * serializeNulls() 如果value为空，对应key也set到redis中
     * @return
     */
    public static Gson includeNullCreate() {
        GsonBuilder gsonB = new GsonBuilder();
        gsonB.serializeNulls();
        return gsonB.create();
    }
}
