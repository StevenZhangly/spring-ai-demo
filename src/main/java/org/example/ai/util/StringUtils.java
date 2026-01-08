package org.example.ai.util;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.AntPathMatcher;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author 01423171
 */
public class StringUtils
{
    /** 空字符串 */
    private static final String NULLSTR = "";

    /** 下划线 */
    private static final char SEPARATOR = '_';

    public static final int INDEX_NOT_FOUND = -1;

    private static final DecimalFormat df2  = new DecimalFormat("0.00");

    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 判断是否为空，并且不是空白字符
     * 
     * @param str 要判断的value
     * @return 结果
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     * 
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     * 
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty())
        {
            // 没必要转换
            return "";
        }
        else if (!name.contains("_"))
        {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty())
            {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s, Boolean toLowerCase)
    {
        if (s == null)
        {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        String str = sb.toString();
        if(toLowerCase){
            str = str.toLowerCase();
        }
        return str;
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     * 
     * @param str 指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置: 
     * ? 表示单个字符; 
     * * 表示一层路径内的任意字符串，不可跨层级; 
     * ** 表示任意层路径;
     * 
     * @param pattern 匹配规则
     * @param url 需要匹配的url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     * 
     * @param num 数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     * 
     * @param s 原始字符串
     * @param size 字符串指定长度
     * @param c 用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getSubtractValue(String s1, String s2){
        long i1 = 0, i2 = 0;
        if(StringUtils.isNotBlank(s1)){
            i1 = Long.parseLong(s1);
        }
        if(StringUtils.isNotBlank(s2)){
            i2 = Long.parseLong(s2);
        }
        long subtract = i1 - i2;
        return String.valueOf(subtract);
    }

    public static Long getSubtractValue(Long s1, Long s2){
        long i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        long subtract = i1 - i2;
        return subtract;
    }

    public static int getSubtractValue(Integer s1, Integer s2){
        int i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        return i1 - i2;
    }

    public static BigDecimal getSubtractValueWithNull(BigDecimal s1, BigDecimal s2){
        if(s1 == null){
            s1 = new BigDecimal(0);
        }
        if(s2 == null){
            s2 = new BigDecimal(0);
        }
        return s1.subtract(s2);
    }

    public static BigDecimal getSubtractValue(BigDecimal s1, BigDecimal s2){
        if(s1 == null || s2 == null){
            return null;
        }
        return s1.subtract(s2);
    }

    public static BigDecimal getSubtractValueWithMultiply(BigDecimal s1, BigDecimal s2){
        if(s1 == null || s2 == null){
            return null;
        }
        return getSubtractValue(s1, s2).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public static String getDodValue(BigDecimal s1, BigDecimal s2){
        if(s1 == null || s2 == null || s2.compareTo(BigDecimal.ZERO) == 0){
            return null;
        }
        BigDecimal rate = (s1.subtract(s2)).divide(s2, 4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        return rate + "%";
    }

    public static String getBigDecimalRatio(BigDecimal s1, BigDecimal s2){
        if(s2 == null || s2.compareTo(BigDecimal.ZERO) == 0){
            return "";
        }
        if(s1 == null){
            return "0.00%";
        }
        if(s1.equals(s2)){
            return "100%";
        }
        BigDecimal rate = s1.divide(s2, 4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        return rate + "%";
    }

    public static BigDecimal getDivideValue(BigDecimal s1, BigDecimal s2){
        if(s1 == null || s2 == null || BigDecimal.ZERO.compareTo(s2) == 0){
            return null;
        }
        return s1.divide(s2, 10, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getDivideValue(BigDecimal s1, Long s2){
        if(s1 == null || s2 == null || s2 == 0L){
            return null;
        }
        return s1.divide(new BigDecimal(s2), 10, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getDodValue(BigDecimal s1, Integer s2){
        if(s1 == null || s2 == null || s2.intValue() == 0){
            return null;
        }

        return s1.divide(new BigDecimal(s2), 4,BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public static String getDodValue(Long s1, Long s2){
        if(s1 == null || s2 == null || s2 == 0L){
            return null;
        }
        Long subtract = getSubtractValue(s1, s2);
        return getCoverageRatio(subtract, s2);
    }

    public static String getSubtractRatio(Integer s1, Integer s2){
        int i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        if(i1 == 0){
            return "0.00%";
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        float s = ((float)(i1 - i2) / i1) * 100;
        return df2.format(s) + "%";
    }

    public static String getSubtractRatio(Long s1, Long s2){
        long i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        if(i1 == 0){
            return "0.00%";
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        float s = ((float)(i1 - i2) / i1) * 100;
        return df2.format(s) + "%";
    }

    public static String getSubtractRatio(Long s1, Long s2, Long s3, Long s4){
        long i1 = 0, i2 = 0, i3 = 0, i4 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        if(s3 != null){
            i3 = s3.longValue();
        }
        if(s4 != null){
            i4 = s4.longValue();
        }
        if(s2 == null || s4 == null){
            return "";
        }
        float f1 = (float) i1 / i2;
        float f2 = (float) i3 / i4;
        float s = (f1 - f2) * 100;
        return df2.format(s) + "%";
    }

    public static String getRatio(Integer s1, Integer s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        if(i1 == 0){
            return "0.00%";
        }
        float s = ((float) i2 / i1) * 100;
        return df2.format(s) + "%";
    }

    public static String getRatio(Long s1, Long s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        if(i1 == 0){
            return "0.00%";
        }
        float s = ((float) i2 / i1) * 100;
        return df2.format(s) + "%";
    }

    public static String getCoverageRatio(Integer s1, Integer s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        if(i1 == 0){
            return "0.00%";
        }
        if(i2 == 0){
            return "0.00%";
        }
        float s = ((float) i1 / i2) * 100;
        return df2.format(s) + "%";
    }

    public static String getCoverageRatio(Long s1, Long s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        if(i1 == 0 && i2 == 0){
            return "100.00%";
        }
        if(i1 == 0){
            return "0.00%";
        }
        if(i2 == 0){
            return "0.00%";
        }
        float s = ( i1 / i2) * 100;
        return df2.format(s) + "%";
    }

    public static String getCoverage(Long s1, Long s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        if(i1 == 0){
            return "0.00";
        }
        if(i2 == 0){
            return "0.00";
        }
        float s = ( i1 / i2) ;
        return df2.format(s);
    }

    public static String getAddValue(String s1, String s2){
        float i1 = 0, i2 = 0;
        if(StringUtils.isNotBlank(s1)){
            i1 = Float.parseFloat(s1);
        }
        if(StringUtils.isNotBlank(s2)){
            i2 = Float.parseFloat(s2);
        }
        float add = i1 + i2;
        return String.valueOf(add);
    }

    public static int getAddValue(Integer s1, Integer s2){
        int i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        return i1 + i2;
    }

    public static long getAddValue(Long s1, Long s2){
        long i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        return i1 + i2;
    }

    public static String formatRatio(float number){
        return df2.format(number) + "%";
    }

    public static String formatRatio(Float number, int scale){
        if(number == null){
            return null;
        }
        StringBuffer sb = new StringBuffer(scale == 0 ? "0": "0.");
        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }
        DecimalFormat df  = new DecimalFormat(sb.toString());
        return df.format(number) + "%";
    }

    public static String formatFloat(Float number, int scale){
        if(number == null){
            return null;
        }
        StringBuffer sb = new StringBuffer(scale == 0 ? "0": "0.");
        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }
        DecimalFormat df  = new DecimalFormat(sb.toString());
        return df.format(number);
    }

    public static String formatFloat(BigDecimal number, int scale){
        if(number == null){
            return null;
        }
        StringBuffer sb = new StringBuffer(scale == 0 ? "0": "0.");
        for (int i = 0; i < scale; i++) {
            sb.append("0");
        }
        DecimalFormat df  = new DecimalFormat(sb.toString());
        return df.format(number);
    }

    public static String getRatio(String s1){
        float i1 = 0;
        if(StringUtils.isNotBlank(s1)){
            i1 = Float.parseFloat(s1);
        }
        if(i1 == 0){
            return "0";
        }
        float s = i1 * 100;
        return df2.format(s) + "%";
    }

    public static int getTotalCount(Integer... counts){
        int total = 0;
        int temp;
        for (int i = 0; i < counts.length; i++) {
            temp = counts[i] == null ? 0 : counts[i].intValue();
            total = total + temp;
        }
        return total;
    }

    public static long getTotalCount(Long... counts){
        long total = 0;
        long temp;
        for (int i = 0; i < counts.length; i++) {
            temp = counts[i] == null ? 0 : counts[i].longValue();
            total = total + temp;
        }
        return total;
    }

    public static Long getTotalCountWithNull(Long... counts){
        Long total = null;
        long temp;
        for (int i = 0; i < counts.length; i++) {
            if(counts[i] != null){
                if(total == null){
                    total = counts[i];
                } else {
                    total = total + counts[i];
                }
            }
        }
        return total;
    }

    public static BigDecimal getTotalCount(BigDecimal... counts){
        BigDecimal total = new BigDecimal(0);
        BigDecimal temp;
        for (int i = 0; i < counts.length; i++) {
            temp = counts[i] == null ? new BigDecimal(0) : counts[i];
            total = total.add(temp);
        }
        return total;
    }

    public static BigDecimal getTotalCountWithNull(BigDecimal... counts){
        BigDecimal total = null;
        for (int i = 0; i < counts.length; i++) {
            if(counts[i] != null){
                if(total == null){
                    total = counts[i];
                } else {
                    total = total.add(counts[i]);
                }
            }
        }
        return total;
    }

    public static int getSubtractCount(Integer... counts){
        int subtract = 0;
        int temp;
        for (int i = 0; i < counts.length; i++) {
            temp = counts[i] == null ? 0 : counts[i].intValue();
            if(i == 0){
                subtract = temp;
            } else {
                subtract = subtract -temp;
            }
        }
        return subtract;
    }

    public static long getSubtractCount(Long... counts){
        long subtract = 0;
        long temp;
        for (int i = 0; i < counts.length; i++) {
            temp = counts[i] == null ? 0 : counts[i].longValue();
            if(i == 0){
                subtract = temp;
            } else {
                subtract = subtract -temp;
            }
        }
        return subtract;
    }

    public static BigDecimal getBigDecimalRatio(Integer s1, Integer s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        float s = 0;
        if(i2 != 0){
            s = i1 / i2;
        }
        return BigDecimal.valueOf(s).setScale(4,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getBigDecimalRatio(Integer s1, Integer s2, int newScale){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        float s = 0;
        if(i2 != 0){
            s = i1 / i2;
        }
        return BigDecimal.valueOf(s).setScale(newScale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getBigDecimalRatio(Long s1, Long s2, int newScale){
        if(s2 == null || s2.longValue() == 0){
            return null;
        }
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.longValue();
        }
        if(s2 != null){
            i2 = s2.longValue();
        }
        float s = 0;
        if(i2 != 0){
            s = i1 / i2;
        }
        return BigDecimal.valueOf(s).setScale(newScale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getBigDecimalRatio(BigDecimal s1, Integer s2){
        float i1 = 0, i2 = 0;
        if(s1 != null){
            i1 = s1.intValue();
        }
        if(s2 != null){
            i2 = s2.intValue();
        }
        float s = 0;
        if(i2 != 0){
            s = i1 / i2;
        }
        return BigDecimal.valueOf(s).setScale(4,BigDecimal.ROUND_HALF_UP);
    }

    public static String getRatioWithPercent(BigDecimal s1){
        s1 = s1.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
        return s1 + "%";
    }

    public static BigDecimal getOrgBigDecimalWithW(BigDecimal s1){
        return s1.divide(new BigDecimal(10000));
    }

    public static BigDecimal getBigDecimalWithW(BigDecimal s1){
        return s1.divide(new BigDecimal(10000)).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getBigDecimalWithW(BigDecimal s1, int newScale){
        return s1.divide(new BigDecimal(10000)).setScale(newScale,BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal getIntegerWithW(Integer s1, int newScale){
        return getBigDecimalRatio(s1, 10000, newScale);
    }

    public static String formatIntThousands(Integer value){
        return NumberUtil.decimalFormat(",###", value);
    }

    public static String formatBigDecimalThousands(BigDecimal value){
        return NumberUtil.decimalFormat("##,##0.00", value);
    }

    public static String formatBigDecimalThousands(String pattern, BigDecimal value){
        return NumberUtil.decimalFormat(pattern, value);
    }

    public static BigDecimal getMultiplyBigDecimalValue(BigDecimal s1, BigDecimal s2){
        if(s1 == null || s2 == null){
            return null;
        }
        return s1.multiply(s2);
    }

    public static Long getMultiplyLongValue(Long s1, BigDecimal s2){
        if(s1 == null || s2 == null){
            return null;
        }
        return new BigDecimal(s1).multiply(s2).setScale( 0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 数值格式化
     * 如果数值大于等于10000，则除以10000后保留三位小数，千分位显示，最后带上万
     * 如果数值小于10000，千分位显示
     * @param value
     * @return
     */
    public static String formatInteger(Integer value){
        if(value == null){
            return "0";
        }
        if(value.compareTo(10000) >= 0){
            return formatBigDecimalThousands("##,##0.000", getIntegerWithW(value, 3)) + "万";
        } else {
            return formatIntThousands(value);
        }
    }

    /**
     * 金额格式化
     * 如果金额大于等于10000，则除以10000后保留三位小数，千分位显示，最后带上万
     * 如果金额小于10000，保留两位小数，千分位显示
     * @param value
     * @return
     */
    public static String formatBigDecimal(BigDecimal value){
        if(value == null){
            return "0.00";
        }
        if(value.compareTo(new BigDecimal(10000)) >= 0){
            return formatBigDecimalThousands("##,##0.000", getBigDecimalWithW(value, 3)) + "万";
        } else {
            return formatBigDecimalThousands(value);
        }
    }

    /**
     * 校验Object是否不为空
     * @param object
     * @return
     */
    public static boolean checkObjectIsNotNull(Object object){
        boolean result = true;
        if(object == null){
            result = false;
        } else if(object instanceof String){
            if(StringUtils.isBlank((String) object)){
                result = false;
            }
        } else if(object instanceof ArrayList){
            if(CollectionUtil.isEmpty((ArrayList) object)){
                result = false;
            }
        } else if(object instanceof JSONArray){
            if(CollectionUtil.isEmpty((JSONArray) object)){
                result = false;
            }
        }
        return result;
    }

    /**
     * 比较两个百分比的大小
     * @param baseRate
     * @param realRate
     * @return
     */
    public static boolean compareBaseRate(String baseRate, String realRate){
        if(StringUtils.isBlank(realRate)){
            return false;
        }
        String baseDate = baseRate.replace("%","");
        String realDate = realRate.replace("%","");
        if(new BigDecimal(baseDate).compareTo(new BigDecimal(realDate)) > 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个百分比的大小
     * @param baseRate
     * @param realRate
     * @return
     */
    public static boolean compareNotEqualBaseRate(String baseRate, String realRate){
        if(StringUtils.isBlank(realRate)){
            return false;
        }
        String baseDate = baseRate.replace("%","");
        String realDate = realRate.replace("%","");
        if(new BigDecimal(baseDate).compareTo(new BigDecimal(realDate)) == 0){
            return false;
        } else {
            return true;
        }
    }

    public static String subString(String str, Integer start, Integer end){
        if(isBlank(str)){
            return null;
        }
        if(start != null && end != null){
            if(str.length() < end){
                return str;
            }
            return str.substring(start, end);
        } else {
            return str.substring(start);
        }
    }

    public static String replaceAllLineSeparator(String text){
        if(isBlank(text)){
            return text;
        }
        return text.replace("\r\n", "").replace("\n", "");
    }

    public static Set<String> splitStrToSet(String str, String split){
        Set<String> set = new HashSet<>();
        if(isNotBlank(str)){
            String[] strs = str.split(split);
            for (int i = 0; i < strs.length; i++) {
                set.add(strs[i]);
            }
        }
        return set;
    }

    public static List<String> splitStrToList(String str, String split){
        List<String> list = new ArrayList<>();
        if(isNotBlank(str)){
            String[] strs = str.split(split);
            for (int i = 0; i < strs.length; i++) {
                list.add(strs[i]);
            }
        }
        return list;
    }

    public static String[] splitStrToArray(String str, String split){
        if(isBlank(str)){
            return null;
        }
        return str.split(split);
    }

    public static Set<Integer> splitStrToIntegerSet(String str, String split){
        Set<Integer> set = new HashSet<>();
        if(isNotBlank(str)){
            String[] strs = str.split(split);
            for (int i = 0; i < strs.length; i++) {
                set.add(Integer.parseInt(strs[i]));
            }
        }
        return set;
    }

    public static String mergeString(String str1, String str2, String split){
        Set<String> set = new HashSet<>();
        if(isNotBlank(str1)){
            String[] str1s = str1.split(split);
            for (int i = 0; i < str1s.length; i++) {
                set.add(str1s[i]);
            }
        }
        if(isNotBlank(str2)){
            String[] str2s = str2.split(split);
            for (int i = 0; i < str2s.length; i++) {
                set.add(str2s[i]);
            }
        }
        return CollectionUtil.join(set, split);
    }

    public static String mergeString(String split, String... strs){
        if(strs == null){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if(i < strs.length - 1){
                sb.append(split);
            }
        }
        return sb.toString();
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isNotBlankAndNull(CharSequence cs) {
        if(cs == null || "null".equals(cs) || "NULL".equals(cs) || "Null".equals(cs)){
            return false;
        }
        return !isBlank(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static String capitalize(final String str) {
        final int strLen = length(str);
        if (strLen == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int newCodePoints[] = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return "";
        }
        final int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringBeforeLast(final String str, final String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringBetween(final String str, final String tag) {
        return substringBetween(str, tag, tag);
    }

    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            final int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    public static String substringAfterLast(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return "";
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    public static String substringEndWithSpecialChar(String str){
        if(isBlank(str)){
            return str;
        }
        int endIndex = str.length();
        if(str.contains("(")){
            endIndex = str.indexOf("(");
        } else if (str.contains("（")){
            endIndex = str.indexOf("（");
        } else if (str.contains("[")){
            endIndex = str.indexOf("[");
        } else if (str.contains("【")){
            endIndex = str.indexOf("【");
        } else if (str.contains("{")){
            endIndex = str.indexOf("{");
        }
        return str.substring(0, endIndex).trim();
    }

    /**
     * 去掉小数点的字符
     *
     * @param text
     * @return
     */
    public static String decimalPointText(String text) {
        if (text == null || text.equals("")) {
            text = "";
        } else if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }else if (text.endsWith(".00")) {
            text = text.substring(0, text.length() - 3);
        }
        return text;
    }

    /**
     * 通配符正则匹配
     * @param text
     * @param pattern
     * @return
     */
    public static boolean patternMatch(String text, String pattern) {
        String regex = pattern
                .replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", ".");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * 校验字符串是否在数组中
     * @param arrays
     * @param target
     * @return
     */
    public static boolean containArray(String[] arrays, String target){
        boolean result = false;
        for (int i = 0; i < arrays.length; i++) {
            if(target.equals(arrays[i])){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 校验字符串是否在数组中
     * @param arrays
     * @param target
     * @return
     */
    public static boolean containArray(Integer[] arrays, Integer target){
        boolean result = false;
        if(arrays != null && arrays.length > 0){
            for (int i = 0; i < arrays.length; i++) {
                if(target.equals(arrays[i])){
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 校验字符串是否在数组中
     * @param arrays
     * @param target
     * @return
     */
    public static boolean containArrayInTarget(String[] arrays, String target){
        boolean result = false;
        for (int i = 0; i < arrays.length; i++) {
            if(target.contains(arrays[i])){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 两个可分割的字符串中是否含有交集
     * @param source
     * @param target
     * @param split
     * @return
     */
    public static boolean containSplitStringWithIn(String source, String target, String split){
        if(isBlank(source) || isBlank(target)){
            return false;
        }
        boolean result = false;
        Set<String> sourceSet = splitStrToSet(source, split);
        return containSetSplitStringWithIn(sourceSet, target, split);
    }

    /**
     * 两个可分割的字符串中是否含有交集
     * @param sourceSet
     * @param target
     * @param split
     * @return
     */
    public static boolean containSetSplitStringWithIn(Set<String> sourceSet, String target, String split){
        if(isEmpty(sourceSet) || isBlank(target)){
            return false;
        }
        boolean result = false;
        String[] targetArr = target.split(split);
        for (int i = 0; i < targetArr.length; i++) {
            if(sourceSet.contains(targetArr[i])){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 判断是否为Json字符串
     * @param str
     * @return
     */
    public static boolean isJsonString(String str){
        boolean result;
        try {
            JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result=false;
        }
        return result;
    }

    /**
     * 匹配目标字符串中以字符串列表中的某个字符开头的字符
     * @param list
     * @param target
     * @return
     */
    public static String startsWithList(List<String> list, String target){
        for (String s : list) {
            if(target.startsWith(s)){
                return s;
            }
        }
        return null;
    }

    /**
     * 比较两个字符串是否相等，忽略空字符44
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equalsIgnoreBlank(String s1, String s2){
        if(isBlank(s1)){
            s1 = "";
        }
        if(isBlank(s2)){
            s2 = "";
        }
        return s1.equals(s2);
    }

    /**
     * 将字符串中的所有大写字母转换为小写字母
     * @param str
     * @return
     */
    public static String toLowerCase(String str){
        if(isBlank(str)){
            return str;
        }
        return str.toLowerCase();
    }

    /**
     * 字符串转Float
     * @param str
     * @return
     */
    public static Float formatStringToFloat(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        str = str.replace("%","");
        return Float.parseFloat(str);
    }

    /**
     * 截取并替换
     * @param str
     * @param maxLength
     * @param replace
     * @return
     */
    public static String substringAndReplace(final String str, int maxLength, String replace){
        StringBuffer sb = new StringBuffer();
        if(isNotBlank(str)){
            if(str.length() > maxLength){
                sb.append(str, 0, maxLength).append(replace);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    /**
     * 小数点格式化为占比的百分数
     * @param value
     * @return
     */
    public static String formatFloatToPercentRatio(Float value){
        if(value == null){
            return "";
        }
        return df2.format(value * 100) + "%";
    }

    /**
     * 百分比转浮点数
     * @param value
     * @return
     */
    public static Float getFloatValueByPercentRatio(String value){
        value = value.replace("%", "").trim();
        return Float.parseFloat(value) / 100;
    }

    /**
     * BASE64编码
     * @param str
     * @return
     */
    public static String getBase64Encode(String str){
        if(isBlank(str)){
            return str;
        }
        return Base64.encode(str);
    }

    /**
     * BASE64解码
     * @param str
     * @return
     */
    public static String getBase64Decode(String str){
        if(isBlank(str)){
            return str;
        }
        return Base64.decodeStr(str);
    }

    /**
     * 多个字符串的拼接
     * @param strs
     * @return
     */
    public static String appendStrs(String... strs){
        if(strs == null || strs.length == 0){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if(isNotBlank(strs[i])){
                sb.append(strs[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 多个字符串的拼接
     * @param strs
     * @param connector
     * @return
     */
    public static String appendStrs(String[] strs, String connector){
        if(strs == null || strs.length == 0){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            sb.append(strs[i]);
            if(i < strs.length - 1){
                sb.append(connector);
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否以指定字符开头
     * @param realValue
     * @param targetValue
     * @param split
     * @return
     */
    public static boolean startWith(String realValue, String targetValue, String split){
        String[] targetValues = targetValue.split(split);
        for (int i = 0; i < targetValues.length; i++) {
            if(realValue.startsWith(targetValues[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串转数值
     * @param str
     * @return
     */
    public static Integer stringToInteger(String str){
        if(isBlank(str)){
            return null;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new RuntimeException("字符串转数值异常", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(formatFloat(new BigDecimal("-22593042.4500000000"), 4));
    }
}
