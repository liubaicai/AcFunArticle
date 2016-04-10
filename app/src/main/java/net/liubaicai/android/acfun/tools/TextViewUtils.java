package net.liubaicai.android.acfun.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.models.CommentContent;
import net.liubaicai.android.acfun.views.TextViewFixTouchConsume;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Baicai on 2016/4/7.
 */
public class TextViewUtils {
    /**
     * 字符 转义字符
     * “ &quot;
     * & &amp;
     * < &lt;
     * > &gt;
     * &nbsp;
     */
    public static String getSource(String escapedHtml) {
        if (escapedHtml == null) return "";
        return escapedHtml.replaceAll("&quot;", "\"").replaceAll("&amp;", "&").replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">").replaceAll("&nbsp;", " ");
    }

    public static Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(view.getResources(), viewBmp);

    }


    public static void setCommentContent(final TextViewFixTouchConsume comment, CommentContent c) {
        String text = c.getContent();
        text = replace(text);
        try {
            comment.setText(Html.fromHtml(text, new Html.ImageGetter() {

                @Override
                public Drawable getDrawable(String source) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(comment.getContext().getAssets()
                                .open(source));
                        Drawable drawable = new BitmapDrawable(comment.getResources(), bm);
                        if (drawable != null) {
                            int w = comment.getResources().getDimensionPixelSize(
                                    R.dimen.emotions_column_width);
                            drawable.setBounds(0, 0, w, drawable.getIntrinsicHeight() * w
                                    / drawable.getIntrinsicWidth());
                        }

                        return drawable;

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }

                }
            }, new Html.TagHandler() {

                @Override
                public void handleTag(boolean opening, String tag, Editable output,
                                      XMLReader xmlReader) {
                    int len = output.length();
                    if (opening) {
                        if (tag.equalsIgnoreCase("strike")) {
                            output.setSpan(new StrikethroughSpan(), len, len,
                                    Spannable.SPAN_MARK_MARK);
                        }
                    } else {
                        if (tag.equalsIgnoreCase("strike")) {
                            end((SpannableStringBuilder) output, StrikethroughSpan.class,
                                    new StrikethroughSpan());
                        }
                    }
                }
            }));
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: text 的格式可能有问题
            comment.setText(text);
            Log.e("wtf", "set comment", e);
        }
        Pattern http = Pattern.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]",
                Pattern.CASE_INSENSITIVE);
        Linkify.addLinks(comment, http, "http://");
        comment.setOnTouchListener(ClickableSpanTextViewTouchListener.getInstance());
    }

    static void end(SpannableStringBuilder text, Class<?> kind,
                    Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);

        text.removeSpan(obj);

        if (where != len) {
            text.setSpan(repl, where < 0 ? 0 : where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return;
    }

    public static <T> T getLast(Spanned text, Class<T> kind) {
        /*
         * This knows that the last returned object from getSpans()
         * will be the most recently added.
         */
        Object[] objs = text.getSpans(0, text.length(), kind);

        if (objs.length == 0) {
            return null;
        } else {
            return (T) objs[objs.length - 1];
        }
    }

    private static String replace(String text) {
        String reg = "\\[emot=(.*?),(.*?)\\/\\]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String id = m.group(2);
            String cat = m.group(1);
            if (Integer.parseInt(id) > 54)
                id = "54";
            String replace = "<img src='emotion/ac/%02d.gif'/>";
            if (cat.equals("ais"))
                replace = "<img src='emotion/ais/%02d.gif'/>";
            if (cat.equals("brd"))
                replace = "<img src='emotion/brd/%02d.gif'/>";
            if (cat.equals("td"))
                replace = "<img src='emotion/td/%02d.gif'/>";
            if (cat.equals("tsj"))
                replace = "<img src='emotion/tsj/%02d.gif'/>";
            text = text.replace(m.group(), String.format(replace, Integer.parseInt(id)));
        }
        reg = "\\[at\\](.*?)\\[\\/at\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), "<font color=\"#FF9A03\" >@" + m.group(1) + "</font> ");
        }
        reg = "\\[color=(.*?)\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), "<font color=\"" + m.group(1) + "\" >");
        }
        text = text.replace("[/color]", "</font>");
        text = text.replaceAll("\\[size=(.*?)\\]", "").replace("[/size]", "");

        reg = "\\[img=(.*?)\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), m.group(1));
        }

        reg = "\\[ac=(.*?)\\](.*?)\\[\\/ac\\]";
        m = Pattern.compile(reg).matcher(text);
        while (m.find()) {
            text = text.replace(m.group(), "<a href=\"http://m.acfun.tv/v/?ac=" + m.group(1) + "\">" + m.group(2) + "</a>");
        }

        text = text.replace("[img]", "").replace("[/img]", "");
        text = text.replaceAll("\\[ac=\\d{5,}\\]", "").replace("[/ac]", "");
        text = text.replaceAll("\\[font[^\\]]*?\\]", "").replace("[/font]", "");
        text = text.replaceAll("\\[align[^\\]]*?\\]", "").replace("[/align]", "");
        text = text.replaceAll("\\[back[^\\]]*?\\]", "").replace("[/back]", "");
        text = text.replace("[s]", "<strike>").replace("[/s]", "</strike>");
        text = text.replace("[b]", "<b>").replace("[/b]", "</b>");
        text = text.replace("[u]", "<u>").replace("[/u]", "</u>");
        text = text.replace("[email]", "<font color=\"#FF9A03\"> ").replace("[/email]", "</font>");
        return text;
    }

    public static TextView createBubbleTextView(Context context, String text) {
        //creating textview dynamically
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setTextSize(14);
        tv.setBackgroundResource(R.color.light_blue);
        tv.setGravity(Gravity.CENTER);
        tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_reply, 0, 0, 0);
        return tv;
    }

    /**
     * @ see http://stackoverflow.com/questions/8558732/listview-textview-with-linkmovementmethod-makes-list-item-unclickable
     */
    public static class ClickableSpanTextViewTouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            CharSequence text = ((TextView) v).getText();
            Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
            TextView widget = (TextView) v;
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    }
                    ret = true;
                }
            }
            return ret;
        }

        private static ClickableSpanTextViewTouchListener sInstance;

        public static View.OnTouchListener getInstance() {
            if (sInstance == null) sInstance = new ClickableSpanTextViewTouchListener();
            return sInstance;
        }
    }
}
