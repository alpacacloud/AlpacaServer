package com.alpaca.infrastructure.core.utils;


import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DataUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    private DataUtil() {
    }


    /**
     * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
     * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
     * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
     * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
     * 得到的结果是txt文件的在系统中的绝对路径。
     *
     * @param relatedPath 相对路径
     * @param cls         用来定位的类
     * @return 相对路径所对应的绝对路径
     * @throws IOException 因为本方法将查询文件系统，所以可能抛出IO异常
     */
    public static final String getFullPathRelateClass(String relatedPath, Class<?> cls) {
        String path = null;
        if (relatedPath == null) {
            throw new NullPointerException();
        }
        String clsPath = getPathFromClass(cls);
        File clsFile = new File(clsPath);
        String tempPath = clsFile.getParent() + File.separator + relatedPath;
        File file = new File(tempPath);
        try {
            path = file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取class文件所在绝对路径
     *
     * @param cls
     * @return
     * @throws IOException
     */
    public static final String getPathFromClass(Class<?> cls) {
        String path = null;
        if (cls == null) {
            throw new NullPointerException();
        }
        URL url = getClassLocationURL(cls);
        if (url != null) {
            path = url.getPath();
            if ("jar".equalsIgnoreCase(url.getProtocol())) {
                try {
                    path = new URL(path).getPath();
                } catch (MalformedURLException e) {
                }
                int location = path.indexOf("!/");
                if (location != -1) {
                    path = path.substring(0, location);
                }
            }
            File file = new File(path);
            try {
                path = file.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 获取类的class文件位置的URL
     *
     * @param cls
     * @return
     */
    private static URL getClassLocationURL(final Class<?> cls) {
        if (cls == null)
            throw new IllegalArgumentException("null input: cls");
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null)
                result = cs.getLocation();
            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
                            result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
                        else if (new File(result.getFile()).isDirectory())
                            result = new URL(result, clsAsResource);
                    } catch (MalformedURLException ignore) {
                    }
                }
            }
        }
        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ? clsLoader.getResource(clsAsResource)
                    : ClassLoader.getSystemResource(clsAsResource);
        }
        return result;
    }

    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static final boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj == "")
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection<?>) {
            if (((Collection<?>) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map<?, ?>) {
            if (((Map<?, ?>) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static final boolean isNotEmpty(Object pObj) {
        if (pObj == null)
            return false;
        if (pObj == "")
            return false;
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return false;
            }
        } else if (pObj instanceof Collection<?>) {
            if (((Collection<?>) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map<?, ?>) {
            if (((Map<?, ?>) pObj).size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * JS输出含有\n的特殊处理
     *
     * @param pStr
     * @return
     */
    public static final String replace4JsOutput(String pStr) {
        pStr = pStr.replace("\r\n", "<br/>&nbsp;&nbsp;");
        pStr = pStr.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        pStr = pStr.replace(" ", "&nbsp;");
        return pStr;
    }

    /**
     * 十进制字节数组转十六进制字符串
     *
     * @param b
     * @return
     */
    public static final String byte2hex(byte[] b) { // 一个字节数，转成16进制字符串
        StringBuilder hs = new StringBuilder(b.length * 2);
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString(); // 转成大写
    }

    /**
     * 十六进制字符串转十进制字节数组
     *
     * @param b
     * @return
     */
    public static final byte[] hex2byte(String hs) {
        byte[] b = hs.getBytes();
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个十进制字节
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }


    /**
     * 初始化设置默认值
     */
    public static final <K> K ifNull(K k, K defaultValue) {
        if (k == null) {
            return defaultValue;
        }
        return k;
    }


    public static String getAbName(String str) {
        if (isChinese(str)) {
            //是中文就取最后一个字符
            return str.substring(str.length() - 1);
        } else {
            //否则取第一个字符
            return str.substring(0, 1);
        }
    }

    /**
     * 判断是否是中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 判断是否乱码
     *
     * @param str
     * @return
     */
    public static boolean isMessyCode(String str) {
        if(DataUtil.isNotEmpty(str)){
            //判断是否乱码 (GBK包含全部中文字符；UTF-8则包含全世界所有国家需要用到的字符。)  ,判断是不是GBK编码 即是否乱码
            if (!(java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(str))) {
                //不是GBK 就是乱码
                return true;
            } else {
                //否则不是乱码
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 去除空格和换行符
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (DataUtil.isNotEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 过滤特殊字符
     * @param dict
     */
    public static void filterSpecialStr(Map<String, Object> dict) {
        for (Map.Entry<String, Object> map : dict.entrySet()) {
            Object value = map.getValue();
            if (DataUtil.isNotEmpty(value)) {
                //map对象值是字符串,就过滤掉特殊字符
                if (value.getClass() == String.class) {
                    String val = (String) value;
                    map.setValue(DataUtil.stringReplace(val));
                }
            }
        }
    }




    /**
     * 替换掉< >
     *  \n 回车(\u000a) ,\t 水平制表符(\u0009) , \r 换行(\u000d)
     * @param str
     * @return
     */
    public static String stringReplace(String str) {
        //1 去掉  回车 ,水平制表符 ,换行
        String dest = "";
        if (DataUtil.isNotEmpty(str)) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll(",");
        }

        //2 去掉特殊字符
        if (DataUtil.isNotEmpty(dest)) {
            if (dest.contains("<")) {
                dest = dest.replace("<", "");
            }
            if (dest.contains(">")) {
                dest = dest.replace(">", "");
            }
            dest = replaceNull(dest);
        }
        return dest;
    }

    /**
     * 替换null为空字符串
     * @param dest
     * @return
     */
    private static String replaceNull(String dest) {
        if (StringUtils.containsIgnoreCase(dest, "NULL")) {
            if (dest.contains("Null")) {
                dest = dest.replace("Null", "");
            }
            if (dest.contains("null")) {
                dest = dest.replace("null", "");
            }
            if (dest.contains("NULL")) {
                dest = dest.replace("NULL", "");
            }
        }
        return dest;
    }


    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param str
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }


    //region 以下为集合

    /**
     * 两个List对比中，返回t2中与t1不相同的元素
     *
     * @param t1
     * @param t2
     * @return
     */
    public static <T> List<T> compare(List<T> t1, List<T> t2) {
        List<T> list2 = new ArrayList<T>();
        for (T t : t2) {
            if (!t1.contains(t)) {
                list2.add(t);
            }
        }
        return list2;
    }

    /**
     * 充分利用类集的特性，Set中不允许有重复的元素。判断list中是否有重复元素
     *
     * @param list
     * @return
     * @Author jinzhuz
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static boolean hasSame(List<? extends Object> list) {
        if (null == list) {
            return true;  //没重复
        }
        return list.size() == new HashSet<Object>(list).size();  //false 代表重复了
    }

    /**
     * 排序加去重
     *
     * @param list
     * @Author jinzhuz
     */
    public static List<? extends Object> distinctSort(List<? extends Object> list) {
        List<? extends Object> newList = new ArrayList<>(new TreeSet<Object>(list));
        return newList;
    }

    /**
     * 去重
     *
     * @param list
     * @Author jinzhuz
     */
    public static List<? extends Object> distinct(List<? extends Object> list) {
        List<? extends Object> newList = new ArrayList<>(new HashSet<Object>(list));
        return newList;
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为n个部分
     * oracle 中：ORA-01795:列表中的最大表达式数为1000 解決方法
     *
     * @param list
     * @param len  一般是999（重点）
     * @return
     * @author jinzhuz
     */
    public static <T> List<List<T>> splitList(List<T> list, int len) {
        if (DataUtil.isEmpty(list) || len < 1) {
            return null;
        }
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    //endregion


    //region 以下为map

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     * 将一个 JavaBean 对象转化为一个  Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, Object> entityToHashMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> mapResult = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    //这里的value可以判null  如果为null 就为""
                    mapResult.put(key, value);
                }
            }
        } catch (Exception e) {
            logger.error("Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map 错误 {}", e);
        }
        return mapResult;
    }


    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param cla 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class cla, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException, Exception {
        logger.info("将一个 Map 对象转化为一个 JavaBean 开始");
        boolean flag = true;
        Object proValue = null;  //失败打印的属性值
        Object proName = null;  //失败打印的属性名
        Object obj = cla.newInstance(); // 创建 JavaBean 对象
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(cla); // 获取类属性
            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    proName = propertyName;
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    proValue = value;
                    //将一个Integer类型装换为Long型会报错 ,这里没有做类型转换
                    descriptor.getWriteMethod().invoke(obj, args);  //使用getWriteMethod() 为类的属性赋值。 //obj的PropertyDescriptor赋值args
                }
            }
        } catch (Exception e) {
            flag = false;
            if (proName != null && proValue != null) {
                logger.error("将一个 Map 对象转化为一个 JavaBean 失败 {},失败对应的属性名:{},失败对应的属性值:{}", e.getMessage(), proName.toString(), proValue.toString(), e);
                throw new Exception("将一个 Map对象转化为一个 JavaBean失败 " + e.getMessage() + "失败对应的属性名:" + proName.toString() + "失败对应的属性值:" + proValue.toString());
            }
        }
        if (flag) {
            logger.info("将一个 Map 对象转化为一个 JavaBean 成功");
        }
        return obj;
    }
    //endregion


    //region 以下为反射

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            //log.debug("字段类型:{}", fields[i].getType().toString()); //String
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /*
     * 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    //endregion


    /**
     * 判断字符串是什么编码
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * get请求数据中编码转换
     *
     * @param str
     * @return
     */
    public final static String convertCharacter(String str) {
        try {
            str = new String(str.getBytes("iso8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("get请求数据中编码转换错误", e);
        }
        return str;
    }


    /**
     * 将 oracle raw 类型转为 十六进制字符串
     * @param obj
     * @return
     */
    public String toHexStringAboutRaw(Object obj) {
        if (!(obj instanceof byte[])) {
            return "";
        }
        byte[] bytes = (byte[]) obj;
        //System.out.println("bytes.length=" + bytes.length);//16
        StringBuffer raw = new StringBuffer();
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            raw.append(hex.toUpperCase());
        }
        return raw.toString();
    }


    public static void main(String[] args) {
        /* Map<String, Object> map = new LinkedHashMap<>();
         map.put("SPYT","\\");
         map.put("XZQDM","<Null>");
         map.put("GLQ","<");
         map.put("XZMC",">");
         map.put("TBYBH","null");
         map.put("QSDWMC",",");
         map.put("QSXZ","1");
         map.put("DLBM","2");
         map.put("GJXFDL","3");
         map.put("YPDL","   ");
         map.put("FX_CZC","aaa/bbb");
         map.put("FX_TBBZ","aaa\bbb");
         map.put("FX_GDLX","111,222");
         map.put("FX_GDZZ","1");
         map.put("FX_XZKD","2");
         map.put("FX_WJZLX","dfsdfdsfa");
         map.put("FX_TBLX",",,,,");
         map.put("WYGZLX","\\\\\\");
         map.put("TBMJ","<<<<<<");
         map.put("NYBZ",">>>>>>");
         map.put("NYYPDW","     ");
         map.put("NYYPR","111\n" +
                 "222\n" +
                 "\t333");
         map.put("NYJCR","111222333");
         map.put("NYSHR","aaa\\abb");
         map.put("NYSHSJ","q");
         map.put("TSBZ","w");
         map.put("ID","e");
         map.put("YSGLQMC","y");
         map.put("YSXZQMC","u");
         map.put("YSXZMC","i");
         map.put("LAYERID","o");
         filterSpecialStr(map);
         System.out.println(map);
        */

       /* String str = "批复物流仓储用地";
        replaceBlank(str);
        String s = stringReplace(str);
        System.out.println("aa:"+s);

        String url = "http://tdgl.tdxx.wpl:6080/arcgis/rest/services/JCDL/XZCY/MapServer/0/query";
        String paramerter = "f=json&geometry={\"spatialReference\":{\"wkt\":\"PROJCS[\\\"WuHan_WH2000_at_11420_E_CGCS2000\\\",GEOGCS[\\\"GCS_WuHanCGCS2000\\\",DATUM[\\\"D_WuHanCGCS2000\\\",SPHEROID[\\\"ElliCGCS2000\\\",6378137.0,298.257222101]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"false_easting\\\",800000.0],PARAMETER[\\\"false_northing\\\",-3000000.0],PARAMETER[\\\"central_meridian\\\",114.3333333333333],PARAMETER[\\\"scale_factor\\\",1.0],PARAMETER[\\\"latitude_of_origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"},\"rings\":[[[806253.8322,390889.903100001],[806246.3069,390889.8924],[806246.4886,390909.638800001],[806206.7238,390908.6779],[806206.694,390875.745100001],[806180.5875,390877.3356],[806180.5901,390882.8303],[806180.5903,390883.0066],[806180.5943,390891.532299999],[806180.5963,390891.692],[806180.5965,390891.7063],[806180.6046,390892.378900001],[806180.5929,390896.523499999],[806180.5577,390909.0067],[806180.5699,390913.1063],[806180.5998,390923.188100001],[806180.6113,390927.044500001],[806180.6192,390929.706700001],[806180.621,390930.334799999],[806180.6399,390931.7356],[806182.16,390963.657199999],[806182.238,390964.4925],[806182.4126,390966.362600001],[806186.8226,390998.188100001],[806189.7046,391010.9575],[806194.2162,391030.947699999],[806194.2204,391030.9659],[806204.6659,391069.213500001],[806205.9652,391073.971],[806217.7441,391117.0989],[806279.5448,391099.5375],[806301.2972,391091.442500001],[806305.1265,391090.0174],[806284.2824,391020.23],[806269.6157,390996.380899999],[806258.638,390958.567399999],[806254.2602,390943.4879],[806254.2559,390942.9559],[806254.0872,390921.8312],[806253.87,390894.6383],[806253.8698,390894.616599999],[806253.8322,390889.903100001]]]}&geometryType=esriGeometryPolygon&inSR={\"wkt\":\"PROJCS[\\\"WuHan_WH2000_at_11420_E_CGCS2000\\\",GEOGCS[\\\"GCS_WuHanCGCS2000\\\",DATUM[\\\"D_WuHanCGCS2000\\\",SPHEROID[\\\"ElliCGCS2000\\\",6378137.0,298.257222101]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"false_easting\\\",800000.0],PARAMETER[\\\"false_northing\\\",-3000000.0],PARAMETER[\\\"central_meridian\\\",114.3333333333333],PARAMETER[\\\"scale_factor\\\",1.0],PARAMETER[\\\"latitude_of_origin\\\",0.0],UNIT[\\\"Meter\\\",1.0]]\"}&outFields=乡名,乡代码&returnGeometry=true&spatialRel=esriSpatialRelIntersects";
        System.out.println(String.format("url: %s , parameter: %s", new Object[] { url, paramerter }));
        String r = HttpHelper.sendPost(url, paramerter);
        System.out.println(String.format("result: %s", new Object[] { r })) ;*/
    }


}
