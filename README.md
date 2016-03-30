# HtmlTagHandler
支持TextView默认支持的所有标签，支持自定义，取代安卓默认的Html.TagHandler
------------------------------------------------

* 特性
    * 支持TextView默认支持的标签
    * 支持自定义标签，接口类似Html.TagHandler


1 自定义标签：

```java
/**
*  解析<span style=\"{color:#e60012}\">哈哈哈</span>
*/
public class SpanTagHandler implements HtmlTagHandler.TagHandler {

    private String fontColor = "";

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
            }else{
                //闭标签，output有值了，attrs没值
                output.setSpan(new ForegroundColorSpan(Color.parseColor(fontColor)), 0, output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
```

2 html转换成spanned
```java
String content = "<span style=\"{color:#e60012}\">哈哈哈</span>";
Spanned s = HtmlTagHandler.fromHtml(content, null, new SpanTagHandler());
tv.setText(s);
```

3 为什么要有这个库

按照安卓默认提供的方式，让TextView显示html得这样：
tv_2.setText(Html.fromHtml(content, null, new Html.TagHandler()));

但是Html.TagHandler提供的接口是：
public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader)

span标签默认是不被支持的，所以要自己写的话，要得到span标签的文本和style属性，但Attributes没传出来，
反而传了个XmlReader对象出来，XmlReader对象我不会用

看android.text.Html的源码：
handleStartTag(String tag, Attributes attributes)
这里其实已经解析出属性了，为何不传出来呢？？？

所以HtmlTagHandler就干了两件事，一是拷出源码，二是更改接口，去掉XmlReader，换成Attributes

4 问题

如果直接解析：
```java
String content = "呵呵呵<span style=\"{color:#e60012}\">哈哈哈</span>嘿嘿嘿";
```
会报错，需要处理成纯xml格式
```java
content = "<html><body>" + content + "</body></html>";
```
