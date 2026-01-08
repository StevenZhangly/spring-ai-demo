package org.example.ai.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 01423171
 * @ClassName JsonUtils
 * @description: TODO
 * @datetime 2022/8/6 20:24
 * @version: 1.0
 */
public class JsonUtils {

    /**
     * 数据字符串转list
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> List<T> parseJsonToList(Class cls, String jsonStr){
        return JSON.parseObject(jsonStr, new TypeReference<List<T>>(cls){});
    }

    /**
     * 字符串转数组
     * @param jsonStr
     * @return
     */
    public static Integer[] toIntegerArray(String jsonStr){
        return JSON.parseObject(jsonStr, new TypeReference<Integer[]>() {});
    }

    /**
     * 字符串转数组
     * @param jsonStr
     * @return
     */
    public static String[] toStringArray(String jsonStr){
        return JSON.parseObject(jsonStr, new TypeReference<String[]>() {});
    }

    /**
     * 字符串转List
     * @param jsonStr
     * @return
     */
    public static <T> List<T> jsonArrayToList(String jsonStr, Class<T> cls){
        return JSON.parseArray(jsonStr, cls);
    }

    /**
     * 对象转Map
     * @param obj
     * @return
     */
    public static Map<String, Object> parseObjectToMap(Object obj){
        return JSON.parseObject(JSON.toJSONString(obj), new TypeReference<Map<String, Object>>(){});
    }

    /**
     * Json字符串转Map
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJsonStrToMap(String jsonStr){
        return JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>(){});
    }

    /**
     * Json字符串转Map
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJsonStrToLinkedHashMap(String jsonStr){
        return JSON.parseObject(jsonStr, new TypeReference<LinkedHashMap<String, Object>>(){});
    }

    /**
     * Map转对象
     * @param map
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseMapToObject(Map<String, Object> map, Class<T> cls){
        return JSON.parseObject(JSON.toJSONString(map), cls);
    }

    /**
     * json字符串转对象
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToObject(String json, Class<T> cls){
        return JSON.parseObject(json, cls);
    }

    /**
     * 通过字段名获取字段值
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getObjectValue(Object obj, String fieldName){
        return getObjectValueFromJson(JSON.toJSONString(obj), fieldName);
    }

    /**
     * 从json字符串中获取指定字段的值
     * @param json
     * @param fieldName
     * @return
     */
    public static Object getObjectValueFromJson(String json, String fieldName){
        if(StringUtils.isBlank(json)){
            return null;
        }
        return JSON.parseObject(json).get(fieldName);
    }

    /**
     * 从json字符串中获取指定字段的值并维持key的顺序
     * @param json
     * @param fieldName
     * @return
     */
    public static Object getObjectValueFromJsonByOrder(String json, String fieldName){
        if(StringUtils.isBlank(json)){
            return null;
        }
        return JSON.parseObject(json, Feature.OrderedField).get(fieldName);
    }

    /**
     * 从对象中获取指定字段的值
     * @param obj
     * @param fieldName
     * @return
     */
    public static String getStringValueFromObject(Object obj, String fieldName){
        return getStringValueFromJson(JSON.toJSONString(obj), fieldName);
    }

    /**
     * 从json字符串中获取指定字段的值
     * @param json
     * @param fieldName
     * @return
     */
    public static String getStringValueFromJson(String json, String fieldName){
        if(StringUtils.isBlank(json)){
            return "";
        }
        Object object = JSON.parseObject(json).get(fieldName);
        if(object == null){
            return "";
        } else {
            return (String) object;
        }
    }

    /**
     * 对象转json字符串，不忽略字段值为null的字段
     * @param object
     * @return
     */
    public static String toJsonString(Object object){
        return JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteNullNumberAsZero);
    }

    /**
     * 对象转json字符串，不忽略字段值为null的字段
     * @param object
     * @return
     */
    public static String toJsonStringNotIgnoreNullValue(Object object){
        return JSON.toJSONString(object, SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse,SerializerFeature.WriteNullNumberAsZero,SerializerFeature.WriteMapNullValue);
    }

    /**
     * JSONObject转对象
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToObject(JSONObject json, Class<T> cls){
        return JSON.toJavaObject(json, cls);
    }

    /**
     * 对象列表转Map
     * @param list
     * @param key
     * @param separator
     * @param <T>
     * @return
     */
    public static <T> Map<Object, T> parseObjectListToMap(List<T> list, String key, String separator){
        Map<Object, T> map = new LinkedHashMap<>();
        String[] keys = key.split(separator);
        for (T t : list) {
            List<Object> values = new ArrayList<>();
            for (int i = 0; i < keys.length; i++) {
                Object value = getObjectValue(t, keys[i]);
                values.add(value);
            }
            map.put(CollectionUtil.join(values, separator), t);
        }
        return map;
    }

    /**
     * 校验字符串是否是json格式
     * @param str
     * @return
     */
    public static boolean checkJsonStr(String str){
        if(StringUtils.isBlank(str)){
            return false;
        }
        boolean checkResult;
        try {
            JSON.parse(str);
            checkResult = true;
        } catch (Exception e) {
            checkResult = false;
        }
        return checkResult;
    }
}
