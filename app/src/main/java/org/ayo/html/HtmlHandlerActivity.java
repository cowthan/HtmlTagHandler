package org.ayo.html;

import android.os.Bundle;
import android.text.Spanned;
import android.widget.TextView;

import org.ayo.core.activity.AyoActivity;

/**
 * Created by Administrator on 2016/3/29.
 */
public class HtmlHandlerActivity extends AyoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_demo_spannable);

        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        TextView tv_2 = (TextView) findViewById(R.id.tv_2);

        String content = "呵呵呵<span style=\"{color:#e60012}\">哈哈哈</span>嘿嘿嘿";

        //普通TextView
        tv_1.setText(content);

        //使用自定义标签
        //content = "<h1>dddd</h1>";
        content = "<html><body>" + content + "</body></html>";
        Spanned s = HtmlTagHandler.fromHtml(content, null, new SpanTagHandler());
        tv_2.setText(s);

    }
}

