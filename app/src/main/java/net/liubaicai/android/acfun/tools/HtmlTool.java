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
        if (Settings.getIsNoPic()){
            //str = str.replaceAll("src=\"(.*?)\"","src=\"file:///android_asset/image_error_heng.png\"");
            str = str +
                    "<script type=\"text/javascript\">" +
                    "(function (){"+
                    "var imageList = document.getElementsByTagName(\"img\");"+
                    "for(var i=0; i<imageList.length; i++){"+
                    "var image = imageList[i];"+
                    "image.href = image.src;"+
                    "image.src = \"file:///android_asset/image_error_heng.png\";"+
                    "image.alt = \"点击加载图片\";"+
                    "image.onclick = function(){"+
                    "this.src = this.href;" +
                    "return false;"+
                    "}"+
                    "}"+
                    "}());"+
                    "</script>";
        }
        String htmlData = "<html><head><style>div,span,img,p,a,body {max-width: 100%; width:auto; height: auto;}</style></head><body>"
                +str
                +"</body></html>";
        return htmlData;
    }
}
