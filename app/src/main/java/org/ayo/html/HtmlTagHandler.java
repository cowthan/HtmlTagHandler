package org.ayo.html;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 处理所有html标签，支持TextView本来的默认支持的标签，也支持轻松自定义，并不定时更新一下常用的标签
 *
 * 为什么有这个库：
 * 本来是打算自己写个支持<span style="">ddd</span>的TagHandler，但发现要取出style的东西有点麻烦，
 * 不如直接写个通用解析器，支持所有已支持的标签，并能轻松自定义，要做的工作就是把android.text.Html源码里的HtmlToSpannedConverter拷出来
 * 其实所有问题就是：源码里没把Attributes传出来，但是把XmlReader给传出来了，没弄明白为什么，XmlReader不知道应该怎么在这里用，
 * 要用Attributes，他又没给你传出来
 *
 * 用法：
 *
 *  Spanned s = HtmlHandler.fromHtml(html);
 * Created by Administrator on 2016/3/30.
 */
public class HtmlTagHandler {

    public static Spanned fromHtml(String source, Html.ImageGetter getter, TagHandler tagHandler){
        HtmlToSpannedConverter h = new HtmlToSpannedConverter(source, getter, tagHandler);
        Spanned s = h.convert();
        return s;
    }

    public static interface TagHandler{
        void handleTag(boolean open, String tag, Editable output, Attributes attrs);
    }

    static class HtmlToSpannedConverter implements ContentHandler {

        private static final float[] HEADER_SIZES = {
                1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1f,
        };

        private XMLReader mReader;
        private String mSource;
        private SpannableStringBuilder mSpannableStringBuilder;
        private Html.ImageGetter mImageGetter;
        private TagHandler mTagHandler;

        public HtmlToSpannedConverter(String source, Html.ImageGetter imageGetter, TagHandler taghandler) {
            mSource = source;
            mSpannableStringBuilder = new SpannableStringBuilder();
            mImageGetter = imageGetter;
            mTagHandler = taghandler;
        }

        public Spanned convert() {

            try{
                SAXParserFactory saxFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxFactory.newSAXParser();//利用获取到的对象创建一个解析器

                XMLReader xmlReader =saxFactory.newSAXParser().getXMLReader(); //获取一个XMLReader
                xmlReader.setContentHandler(this);
                xmlReader.parse(new InputSource(new StringReader(mSource)));
            }catch (Exception e){
                e.printStackTrace();
            }


//            try {
//                mReader.parse(new InputSource(new StringReader(mSource)));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (SAXException e) {
//                throw new RuntimeException(e);
//            }

            // Fix flags and range for paragraph-type markup.
            Object[] obj = mSpannableStringBuilder.getSpans(0, mSpannableStringBuilder.length(), ParagraphStyle.class);
            for (int i = 0; i < obj.length; i++) {
                int start = mSpannableStringBuilder.getSpanStart(obj[i]);
                int end = mSpannableStringBuilder.getSpanEnd(obj[i]);

                // If the last line of the range is blank, back off by one.
                if (end - 2 >= 0) {
                    if (mSpannableStringBuilder.charAt(end - 1) == '\n' &&
                            mSpannableStringBuilder.charAt(end - 2) == '\n') {
                        end--;
                    }
                }

                if (end == start) {
                    mSpannableStringBuilder.removeSpan(obj[i]);
                } else {
                    mSpannableStringBuilder.setSpan(obj[i], start, end, Spannable.SPAN_PARAGRAPH);
                }
            }

            return mSpannableStringBuilder;
        }

        private void handleStartTag(String tag, Attributes attributes) {
            if (tag.equalsIgnoreCase("br")) {
                // We don't need to handle this. TagSoup will ensure that there's a </br> for each <br>
                // so we can safely emite the linebreaks when we handle the close tag.
            } else if (tag.equalsIgnoreCase("p")) {
                handleP(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("div")) {
                handleP(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("strong")) {
                start(mSpannableStringBuilder, new Bold());
            } else if (tag.equalsIgnoreCase("b")) {
                start(mSpannableStringBuilder, new Bold());
            } else if (tag.equalsIgnoreCase("em")) {
                start(mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("cite")) {
                start(mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("dfn")) {
                start(mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("i")) {
                start(mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("big")) {
                start(mSpannableStringBuilder, new Big());
            } else if (tag.equalsIgnoreCase("small")) {
                start(mSpannableStringBuilder, new Small());
            } else if (tag.equalsIgnoreCase("font")) {
                startFont(mSpannableStringBuilder, attributes);
            } else if (tag.equalsIgnoreCase("blockquote")) {
                handleP(mSpannableStringBuilder);
                start(mSpannableStringBuilder, new Blockquote());
            } else if (tag.equalsIgnoreCase("tt")) {
                start(mSpannableStringBuilder, new Monospace());
            } else if (tag.equalsIgnoreCase("a")) {
                startA(mSpannableStringBuilder, attributes);
            } else if (tag.equalsIgnoreCase("u")) {
                start(mSpannableStringBuilder, new Underline());
            } else if (tag.equalsIgnoreCase("sup")) {
                start(mSpannableStringBuilder, new Super());
            } else if (tag.equalsIgnoreCase("sub")) {
                start(mSpannableStringBuilder, new Sub());
            } else if (tag.length() == 2 &&
                    Character.toLowerCase(tag.charAt(0)) == 'h' &&
                    tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
                handleP(mSpannableStringBuilder);
                start(mSpannableStringBuilder, new Header(tag.charAt(1) - '1'));
            } else if (tag.equalsIgnoreCase("img")) {
                startImg(mSpannableStringBuilder, attributes, mImageGetter);
            } else if (mTagHandler != null) {
                mTagHandler.handleTag(true, tag, mSpannableStringBuilder, attributes);
            }
        }

        private void handleEndTag(String tag) {
            if (tag.equalsIgnoreCase("br")) {
                handleBr(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("p")) {
                handleP(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("div")) {
                handleP(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("strong")) {
                end(mSpannableStringBuilder, Bold.class, new StyleSpan(Typeface.BOLD));
            } else if (tag.equalsIgnoreCase("b")) {
                end(mSpannableStringBuilder, Bold.class, new StyleSpan(Typeface.BOLD));
            } else if (tag.equalsIgnoreCase("em")) {
                end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
            } else if (tag.equalsIgnoreCase("cite")) {
                end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
            } else if (tag.equalsIgnoreCase("dfn")) {
                end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
            } else if (tag.equalsIgnoreCase("i")) {
                end(mSpannableStringBuilder, Italic.class, new StyleSpan(Typeface.ITALIC));
            } else if (tag.equalsIgnoreCase("big")) {
                end(mSpannableStringBuilder, Big.class, new RelativeSizeSpan(1.25f));
            } else if (tag.equalsIgnoreCase("small")) {
                end(mSpannableStringBuilder, Small.class, new RelativeSizeSpan(0.8f));
            } else if (tag.equalsIgnoreCase("font")) {
                endFont(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("blockquote")) {
                handleP(mSpannableStringBuilder);
                end(mSpannableStringBuilder, Blockquote.class, new QuoteSpan());
            } else if (tag.equalsIgnoreCase("tt")) {
                end(mSpannableStringBuilder, Monospace.class,
                        new TypefaceSpan("monospace"));
            } else if (tag.equalsIgnoreCase("a")) {
                endA(mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("u")) {
                end(mSpannableStringBuilder, Underline.class, new UnderlineSpan());
            } else if (tag.equalsIgnoreCase("sup")) {
                end(mSpannableStringBuilder, Super.class, new SuperscriptSpan());
            } else if (tag.equalsIgnoreCase("sub")) {
                end(mSpannableStringBuilder, Sub.class, new SubscriptSpan());
            } else if (tag.length() == 2 &&
                    Character.toLowerCase(tag.charAt(0)) == 'h' &&
                    tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
                handleP(mSpannableStringBuilder);
                endHeader(mSpannableStringBuilder);
            } else if (mTagHandler != null) {
                mTagHandler.handleTag(false, tag, mSpannableStringBuilder, null);
            }
        }

        private static void handleP(SpannableStringBuilder text) {
            int len = text.length();

            if (len >= 1 && text.charAt(len - 1) == '\n') {
                if (len >= 2 && text.charAt(len - 2) == '\n') {
                    return;
                }

                text.append("\n");
                return;
            }

            if (len != 0) {
                text.append("\n\n");
            }
        }

        private static void handleBr(SpannableStringBuilder text) {
            text.append("\n");
        }

        private static Object getLast(Spanned text, Class kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
            Object[] objs = text.getSpans(0, text.length(), kind);

            if (objs.length == 0) {
                return null;
            } else {
                return objs[objs.length - 1];
            }
        }

        private static void start(SpannableStringBuilder text, Object mark) {
            int len = text.length();
            text.setSpan(mark, len, len, Spannable.SPAN_MARK_MARK);
        }

        private static void end(SpannableStringBuilder text, Class kind,
                                Object repl) {
            int len = text.length();
            Object obj = getLast(text, kind);
            int where = text.getSpanStart(obj);

            text.removeSpan(obj);

            if (where != len) {
                text.setSpan(repl, where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private static void startImg(SpannableStringBuilder text,
                                     Attributes attributes, Html.ImageGetter img) {
            String src = attributes.getValue("", "src");
            Drawable d = null;

            if (img != null) {
                d = img.getDrawable(src);
            }

            if (d == null) {
                d = Resources.getSystem().
                        getDrawable(android.R.drawable.ic_delete);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            }

            int len = text.length();
            text.append("\uFFFC");

            text.setSpan(new ImageSpan(d, src), len, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        private static void startFont(SpannableStringBuilder text,
                                      Attributes attributes) {
            String color = attributes.getValue("", "color");
            String face = attributes.getValue("", "face");

            int len = text.length();
            text.setSpan(new Font(color, face), len, len, Spannable.SPAN_MARK_MARK);
        }

        private static void endFont(SpannableStringBuilder text) {
            int len = text.length();
            Object obj = getLast(text, Font.class);
            int where = text.getSpanStart(obj);

            text.removeSpan(obj);

            if (where != len) {
                Font f = (Font) obj;

                if (!TextUtils.isEmpty(f.mColor)) {
                    if (f.mColor.startsWith("@")) {
                        Resources res = Resources.getSystem();
                        String name = f.mColor.substring(1);
                        int colorRes = res.getIdentifier(name, "color", "android");
                        if (colorRes != 0) {
                            ColorStateList colors = res.getColorStateList(colorRes);
                            text.setSpan(new TextAppearanceSpan(null, 0, 0, colors, null),
                                    where, len,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    } else {
                        int c = Color.parseColor(f.mColor);//Color.getHtmlColor(f.mColor);
                        if (c != -1) {
                            text.setSpan(new ForegroundColorSpan(c | 0xFF000000),
                                    where, len,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }

                if (f.mFace != null) {
                    text.setSpan(new TypefaceSpan(f.mFace), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private static void startA(SpannableStringBuilder text, Attributes attributes) {
            String href = attributes.getValue("", "href");

            int len = text.length();
            text.setSpan(new Href(href), len, len, Spannable.SPAN_MARK_MARK);
        }

        private static void endA(SpannableStringBuilder text) {
            int len = text.length();
            Object obj = getLast(text, Href.class);
            int where = text.getSpanStart(obj);

            text.removeSpan(obj);

            if (where != len) {
                Href h = (Href) obj;

                if (h.mHref != null) {
                    text.setSpan(new URLSpan(h.mHref), where, len,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private static void endHeader(SpannableStringBuilder text) {
            int len = text.length();
            Object obj = getLast(text, Header.class);

            int where = text.getSpanStart(obj);

            text.removeSpan(obj);

            // Back off not to change only the text, not the blank line.
            while (len > where && text.charAt(len - 1) == '\n') {
                len--;
            }

            if (where != len) {
                Header h = (Header) obj;

                text.setSpan(new RelativeSizeSpan(HEADER_SIZES[h.mLevel]),
                        where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.BOLD),
                        where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        public void setDocumentLocator(Locator locator) {
        }

        public void startDocument() throws SAXException {
        }

        public void endDocument() throws SAXException {
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            handleStartTag(localName, attributes);
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            handleEndTag(localName);
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            StringBuilder sb = new StringBuilder();

        /*
         * Ignore whitespace that immediately follows other whitespace;
         * newlines count as spaces.
         */

            for (int i = 0; i < length; i++) {
                char c = ch[i + start];

                if (c == ' ' || c == '\n') {
                    char pred;
                    int len = sb.length();

                    if (len == 0) {
                        len = mSpannableStringBuilder.length();

                        if (len == 0) {
                            pred = '\n';
                        } else {
                            pred = mSpannableStringBuilder.charAt(len - 1);
                        }
                    } else {
                        pred = sb.charAt(len - 1);
                    }

                    if (pred != ' ' && pred != '\n') {
                        sb.append(' ');
                    }
                } else {
                    sb.append(c);
                }
            }

            mSpannableStringBuilder.append(sb);
        }

        public void ignorableWhitespace(char ch[], int start, int length) throws SAXException {
        }

        public void processingInstruction(String target, String data) throws SAXException {
        }

        public void skippedEntity(String name) throws SAXException {
        }

        private static class Bold { }
        private static class Italic { }
        private static class Underline { }
        private static class Big { }
        private static class Small { }
        private static class Monospace { }
        private static class Blockquote { }
        private static class Super { }
        private static class Sub { }

        private static class Font {
            public String mColor;
            public String mFace;

            public Font(String color, String face) {
                mColor = color;
                mFace = face;
            }
        }

        private static class Href {
            public String mHref;

            public Href(String href) {
                mHref = href;
            }
        }

        private static class Header {
            private int mLevel;

            public Header(int level) {
                mLevel = level;
            }
        }
    }
}
