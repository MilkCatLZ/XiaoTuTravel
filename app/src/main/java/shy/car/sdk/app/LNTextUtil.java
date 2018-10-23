package shy.car.sdk.app;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.base.util.DoubleUtil;
import com.base.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LNTextUtil extends StringUtils {
    /**
     * @param price
     * @param dot   小数点后几位保留，现在支持到2位
     *
     * @return doubleString
     */
    @NonNull
    public static String convertPriceText(double price, int dot) {

//        int ps = (int) Math.floor(price * 100);
//        price = ps / 100;
        
        BigDecimal bd = new BigDecimal(String.valueOf(price));
        String p = bd.setScale(dot, BigDecimal.ROUND_HALF_UP)
                     .toString();
        if (dot == 2) {
//            p = p.replaceAll(".00+?$", "");
            p = p.replaceAll("[.]$", "");
        } else {
//            p = p.replaceAll(".0+?$", "");
            p = p.replaceAll("[.]$", "");
        }
        return p;
    }
    
    /**
     * @param price
     * @param dot   小数点后几位保留，现在支持到9位
     *
     * @return doubleString
     */
    public static String convertPriceTextNoRound(double price, int dot) {
        float dotOffset = getDotOffset(dot);
        float ff = DoubleUtil.mul(price, dotOffset).floatValue();
        int ps = (int) Math.floor(ff);
        float prices = ps / dotOffset;
        
        BigDecimal bd = new BigDecimal(String.valueOf(prices));
        String p = bd.setScale(dot, BigDecimal.ROUND_HALF_UP)
                     .toString();
        if (dot == 2) {
            p = p.replaceAll(".00+?$", "");
            p = p.replaceAll("[.]$", "");
        } else {
            p = p.replaceAll(".0+?$", "");
            p = p.replaceAll("[.]$", "");
        }
        return p;
    }
    
    /**
     * @param price
     * @param dot   小数点后几位保留，现在支持到9位
     *
     * @return doubleString
     */
    public static String convertPriceTextRoundUp(double price, int dot) {
        float dotOffset = getDotOffset(dot);
        float ff = DoubleUtil.mul(price, dotOffset).floatValue();
        int ps = (int) Math.ceil(ff);
        float prices = ps / dotOffset;
        
        BigDecimal bd = new BigDecimal(String.valueOf(prices));
        String p = bd.setScale(dot, BigDecimal.ROUND_HALF_UP)
                     .toString();
        if (dot == 2) {
            p = p.replaceAll(".00+?$", "");
            p = p.replaceAll("[.]$", "");
        } else {
            p = p.replaceAll(".0+?$", "");
            p = p.replaceAll("[.]$", "");
        }
        return p;
    }
    
    /**
     * 支持1-9位数
     *
     * @param dot
     *
     * @return
     */
    private static float getDotOffset(int dot) {
        float dotOffset;
        switch (dot) {
            case 1:
                dotOffset = 10;
                break;
            case 2:
                dotOffset = 100;
                break;
            case 3:
                dotOffset = 1000;
                break;
            case 4:
                dotOffset = 10000;
                break;
            case 5:
                dotOffset = 100000;
                break;
            case 6:
                dotOffset = 1000000;
                break;
            case 7:
                dotOffset = 10000000;
                break;
            case 8:
                dotOffset = 100000000;
                break;
            case 9:
                dotOffset = 1000000000;
                break;
            default:
                dotOffset = 100;
                break;
        }
        return dotOffset;
    }
    
    /**
     * @param price
     * @param dot   小数点后几位保留，现在支持到2位
     *
     * @return doubleString
     */
    public static String convertPriceTextRoundUpNoMatter(double price, int dot) {
        return convertPriceTextRoundUp(price, dot);
    }
    
    /**
     * 四舍五入
     *
     * @param price
     *
     * @return
     */
    @NonNull
    public static String getPriceText(double price) {
        return convertPriceText(price, 2);
    }
    
    /**
     * 第三位小数直接去掉不进位
     *
     * @param price
     *
     * @return
     */
    @NonNull
    public static String getPriceTextNoRound(double price) {
        return convertPriceTextNoRound(price, 2);
    }
    
    /**
     * 第三位小数四舍五入
     *
     * @param price
     *
     * @return
     */
    @NonNull
    public static String getPriceTextRoundUp(double price) {
        return convertPriceTextRoundUp(price, 2);
    }
    
    /**
     * 第三位小数直接进位
     *
     * @param price
     *
     * @return
     */
    public static String getPriceTextRoundUpNoMatter(double price) {
        return convertPriceTextRoundUpNoMatter(price, 2);
    }
    
    /**
     * 去掉价格最后一位“0”
     *
     * @param price
     *
     * @return
     */
    @NonNull
    public static String getDiscountPrice(double price) {
        return convertPriceText(price, 1);
    }
    
    /**
     * 电话号码1300000000->130*****000
     *
     * @param phone
     *
     * @return
     */
    @NonNull
    public static String PhoneEncode(@Nullable String phone) {
        if (isEmpty(phone)) {
            return "";
        }
        String[] phones = phone.trim()
                               .split("");
        StringBuilder stringBuilder = new StringBuilder();
        //split出来phones[0]为空字符串，所以从1开始
        for(int i = 1; i < phones.length; i++) {
            if (i >= 4 && i <= 8) {
                stringBuilder.append("*");
            } else {
                stringBuilder.append(phones[i]);
            }
        }
        return stringBuilder.toString();
        
    }
    
    /**
     * 去掉指定字符串的开头和结尾的指定字符
     *
     * @param stream  要处理的字符串
     * @param trimstr 要去掉的字符串
     *
     * @return 处理后的字符串
     */
    public static String sideTrim(String stream, String trimstr) {
        // null或者空字符串的时候不处理
        if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
            return stream;
        }
        
        // 结束位置
        int epos = 0;
        
        // 正规表达式
        String regpattern = "[" + trimstr + "]*+";
        Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
        
        // 去掉结尾的指定字符
        StringBuilder buffer = new StringBuilder(stream).reverse();
        Matcher matcher = pattern.matcher(buffer);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = new StringBuilder(buffer.substring(epos)).reverse()
                                                              .toString();
        }
        
        // 去掉开头的指定字符
        matcher = pattern.matcher(stream);
        if (matcher.lookingAt()) {
            epos = matcher.end();
            stream = stream.substring(epos);
        }
        
        // 返回处理后的字符串
        return stream;
    }
    
    /**
     * 日期比较
     * <p>返回值：-1：date1小于date2,0:date1等于date2,1：date1大于date2</p>
     *
     * @param date1 yyyy-MM-dd
     * @param date2 yyyy-MM-dd
     *
     * @return
     */
    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            return dt1.compareTo(dt2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    public static String getBankText(String resouce) {
        if (TextUtils.isEmpty(resouce)) {
            return "";
        }
        char[] string = resouce.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < string.length; i++) {
            if (i != 0 && i % 4 == 0) {
                stringBuilder.append(" ");
            }
            if (i < 12) {
                stringBuilder.append('*');
            } else {
                stringBuilder.append(string[i]);
            }
        }
        return stringBuilder.toString();
    }
    
    public static String getCommonDate(int year, int month, int day) {
        String cacheMonth, dayCache;
        if (month < 10) {
            cacheMonth = "0" + month;
        } else {
            cacheMonth = String.valueOf(month);
        }
        if (day < 10) {
            dayCache = "0" + day;
        } else {
            dayCache = String.valueOf(day);
        }
        
        return year + "." + cacheMonth + "." + dayCache;
    }
    
    /**
     * @param time
     *
     * @return 2018.03.08
     */
    public static String getCommonDateTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }
    /**
     * @param time
     *
     * @return 2018.03.08
     */
    public static String getCommonDateTime(long time,String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }
}
