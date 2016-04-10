package net.liubaicai.android.acfun.tools;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Baicai on 2016/4/6.
 */
public class HtmlTool {

    private static final String regEx_style = "style=\"[^>]*?\"";

    public static String Covert2Html(String str){
        Pattern p_script = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(str);
        str = m_script.replaceAll(""); // 过滤script标签
        String htmlData = "<html><head><style>div,span,img,p,a,body {max-width: 100%; width:auto; height: auto;}</style></head><body>"
                +str
                +"</body></html>";
        return htmlData;
    }
}
