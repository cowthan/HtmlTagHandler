# HtmlTagHandler
支持TextView默认支持的所有标签，支持自定义，取代安卓默认的Html.TagHandler

------------------
为什么有这个库?

本来是打算自己写个支持span标签的TagHandler，如：
```xml
<span style="{color:#e60012}">这段文本要显示红色</span>
```

按照系统提供的接口，应该这样：
```java
Html.fromHtml(content, null, new Html.TagHandler());
```

但是这里要取出style属性，所以要用到接口handleTag里传出的XMLReader对象，发现太麻烦

看两眼android.text.Html的源码，发现handleStartTag(String tag, Attributes attributes)这里已经把属性都解析出来了，
但是没传到handleTag方法里

其实所有问题就是：源码里没把Attributes传出来，但是把XmlReader给传出来了，没弄明白为什么，XmlReader不知道应该怎么在这里用，
要用Attributes，他又没给你传出来

所以有了这个HtmlTagHandler类

* 要做的所有工作就是
    * 把android.text.Html源码里的HtmlToSpannedConverter拷出来
    * 提供一个类似Html.TagHandler()的接口，但去掉XmlReader，换成Attributes
    * 提供一个接口，将html文本转为span


 * 特性
     * 支持TextView默认支持的标签
     * 支持自定义
     * demo里有对span标签的解析示例
    
    
用法：
```java
String content = "<span style=\"{color:#e60012}\">哈哈哈</span>";
Spanned s = HtmlHandler.fromHtml(content, null, new SpanTagHandler2());
tv.setText(s);
```

