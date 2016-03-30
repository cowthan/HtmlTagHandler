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

    @Override
    public void handleTag(boolean open, String tag, Editable output, Attributes attrs) {

        if(tag.toLowerCase().equals("span")){
            if(open){
                //开标签，output是空（sax还没读到），attrs有值
                for(int i = 0; i < attrs.getLength(); i++){
                    Log.i("22222", "====" + attrs.getLocalName(i) + ": " + attrs.getQName(i) + ": " + attrs.getValue(i));
                    if(attrs.getLocalName(i).equals("style")){
                        String style = attrs.getValue(i); //{color:#e60012}
                        fontColor = style.replace("{", "").replace("}", "").replace("color", "").replace(":", "");
                        Log.i("22222", "fontColor=" + fontColor);
                    }
                }
            }else{
                //闭标签，output有值了，attrs没值
                output.setSpan(new ForegroundColorSpan(Color.parseColor(fontColor)), 0, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }//~~over if

    }
}