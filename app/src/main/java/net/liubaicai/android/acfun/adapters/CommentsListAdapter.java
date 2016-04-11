package net.liubaicai.android.acfun.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.models.CommentContent;
import net.liubaicai.android.acfun.tools.TextViewUtils;
import net.liubaicai.android.acfun.views.TextViewFixTouchConsume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liush on 2016/4/6.
 */
public class CommentsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private SparseArray<CommentContent> data;
    private List<Integer> commentIdList;
    private Context context;
    private int resource;

    public CommentsListAdapter(Context context, SparseArray<CommentContent> data, List<Integer> commentIdList, int resource) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.data = data;
        this.commentIdList = commentIdList;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return commentIdList == null ? 0 : commentIdList.size();
    }

    @Override
    public Object getItem(int position) {
        if (commentIdList == null || data.indexOfKey(commentIdList.get(position)) < 0)
            return null;
        return data.get(commentIdList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (commentIdList == null)
            return null;

        SimpleDraweeView avatar_image;
        TextView name_text;
        TextView time_text;
        TextView floor_text;
        TextViewFixTouchConsume content_text;
        LinearLayout s_comment;
        TextViewFixTouchConsume s_content_text;
        TextView s_floor_text;
        TextView s_name_text;
        TextView ss_comment;

        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            avatar_image = (SimpleDraweeView) convertView.findViewById(R.id.avatar_image);
            name_text = (TextView) convertView.findViewById(R.id.name_text);
            time_text = (TextView) convertView.findViewById(R.id.time_text);
            floor_text = (TextView) convertView.findViewById(R.id.floor_text);
            content_text = (TextViewFixTouchConsume) convertView.findViewById(R.id.content_text);
            s_comment = (LinearLayout) convertView.findViewById(R.id.s_comment);
            s_content_text = (TextViewFixTouchConsume) convertView.findViewById(R.id.s_content_text);
            s_floor_text = (TextView) convertView.findViewById(R.id.s_floor_text);
            s_name_text = (TextView) convertView.findViewById(R.id.s_name_text);
            ss_comment = (TextView) convertView.findViewById(R.id.ss_comment);

            ViewCache cache = new ViewCache();
            cache.avatar_image = avatar_image;
            cache.name_text = name_text;
            cache.time_text = time_text;
            cache.floor_text = floor_text;
            cache.content_text = content_text;
            cache.s_comment = s_comment;
            cache.s_content_text = s_content_text;
            cache.s_floor_text = s_floor_text;
            cache.s_name_text = s_name_text;
            cache.ss_comment = ss_comment;
            convertView.setTag(cache);
        } else {
            ViewCache cache = (ViewCache) convertView.getTag();
            avatar_image = cache.avatar_image;
            name_text = cache.name_text;
            time_text = cache.time_text;
            floor_text = cache.floor_text;
            content_text = cache.content_text;
            s_comment = cache.s_comment;
            s_content_text = cache.s_content_text;
            s_floor_text = cache.s_floor_text;
            s_name_text = cache.s_name_text;
            ss_comment = cache.ss_comment;
        }
        CommentContent commentListItem = (CommentContent) getItem(position);

        if (commentListItem != null) {
            avatar_image.setImageURI(Uri.parse(commentListItem.getUserImg()));
            name_text.setText(commentListItem.getUserName());
            if (commentListItem.getNameRed() == 1)
                name_text.setTextColor(context.getResources().getColor(R.color.news_number_color));
            else
                name_text.setTextColor(context.getResources().getColor(R.color.text_gray2_color));
            time_text.setText(commentListItem.getPostDate());
            floor_text.setText(String.valueOf(commentListItem.getCount()));
            //content_text.setText(commentListItem.getContent());
            TextViewUtils.setCommentContent(content_text, commentListItem);

            if (commentListItem.getQuoteId() > 0) {
                if (data.indexOfKey(commentListItem.getQuoteId()) >= 0) {
                    s_comment.setVisibility(View.VISIBLE);
                    CommentContent s_commentListItem = data.get(commentListItem.getQuoteId());
                    //s_content_text.setText(s_commentListItem.getContent());
                    TextViewUtils.setCommentContent(s_content_text, s_commentListItem);
                    s_floor_text.setText(String.valueOf(s_commentListItem.getCount()));
                    s_name_text.setText(s_commentListItem.getUserName());

                    if (s_commentListItem.getQuoteId() > 0) {
                        if (data.indexOfKey(s_commentListItem.getQuoteId()) >= 0) {
                            ss_comment.setVisibility(View.VISIBLE);
                        } else {
                            ss_comment.setVisibility(View.GONE);
                        }
                    } else {
                        ss_comment.setVisibility(View.GONE);
                    }
                } else {
                    s_comment.setVisibility(View.GONE);
                }
            } else {
                s_comment.setVisibility(View.GONE);
            }
        } else {
            avatar_image.setImageResource(R.drawable.img_default_avatar);
            name_text.setText("::该楼层已被删除");
            name_text.setTextColor(context.getResources().getColor(R.color.text_gray2_color));
            time_text.setText("::该楼层已被删除");
            floor_text.setText("0");
            content_text.setText("::该楼层已被删除");
        }

        return convertView;
    }


    private final class ViewCache {
        SimpleDraweeView avatar_image;
        TextView name_text;
        TextView time_text;
        TextView floor_text;
        TextViewFixTouchConsume content_text;
        LinearLayout s_comment;
        TextViewFixTouchConsume s_content_text;
        TextView s_floor_text;
        TextView s_name_text;
        TextView ss_comment;
    }
}