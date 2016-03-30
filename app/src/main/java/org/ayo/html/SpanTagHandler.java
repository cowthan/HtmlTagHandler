package org.ayo.html;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import org.xml.sax.Attributes;

/**
 * 自定义标签解析类，处理：呵呵呵<span style="{color:#e60012}">哈哈哈</span>嘿嘿嘿
 * Created by Administrator on 2016/3/29.
 */
public class SpanTagHandler implements HtmlTagHandler.TagHandler {

    private String fontColor = "";
    private int startIndex = 0;
    private int endIndex = 0;

    @Override
    public void handleTag(boolean open, String tag, Editable output, Attributes attrs) {

        if(tag.toLowerCase().equals("span")){
            if(open){
                //开标签，output是空（sax还没读到），attrs有值
                for(int i = 0; i < attrs.getLength(); i++){
                    if(attrs.getLocalName(i).equals("style")){
                        String style = attrs.getValue(i); //{color:#e60012}
                        fontColor = style.replace("{", "").replace("}", "").replace("color", "").replace(":", "");
                    }
                }
                startIndex = output.length();
            }else{
                //闭标签，output有值了，attrs没值
                endIndex = output.length();
                output.setSpan(new ForegroundColorSpan(Color.parseColor(fontColor)), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }//~~over if
    }
}