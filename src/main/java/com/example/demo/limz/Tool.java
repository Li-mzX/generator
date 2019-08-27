/*******************************************************************************
 * @(#)Tool.java 2019年08月27日 15:45
 * Copyright 2019 明医众禾科技（北京）有限责任公司. All rights reserved.
 *******************************************************************************/
package com.example.demo.limz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <b>Application name：</b> Tool.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 明医众禾科技（北京）有限责任公司 版权所有。<br>
 * <b>Company：</b> 明医众禾科技（北京）有限责任公司 <br>
 * <b>Date：</b> 2019年08月27日 15:45 <br>
 * <b>@author：</b> <a href="mailto:limz@miyzh.com"> 李明哲 </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
public class Tool {
    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 下划线转驼峰 */
    public static String lineToHump2(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        String s = sb.toString();
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}