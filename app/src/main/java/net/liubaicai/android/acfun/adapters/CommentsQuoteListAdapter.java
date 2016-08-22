package net.liubaicai.android.acfun.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.models.CommentContent;
import net.liubaicai.android.acfun.tools.TextViewUtils;
import net.liubaicai.android.acfun.views.TextViewFixTouchConsume;

import java.util.List;

/**
 * Created by liush on 2016/4/9.
 */
public class CommentsQuoteListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private SparseArray<CommentContent> data;
    private List<Integer> commentIdList;
    private Context context;
    private int resource;

    public CommentsQuoteListAdapter(Context context, SparseArray<CommentContent> data, List<Integer> commentIdList, int resource) {
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

        try{
            SimpleDraweeView avatar_image;
            TextView name_text;
            TextView time_text;
            TextView floor_text;
            TextViewFixTouchConsume content_text;

            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                avatar_image = (SimpleDraweeView) convertView.findViewById(R.id.avatar_image);
                name_text = (TextView) convertView.findViewById(R.id.name_text);
                time_text = (TextView) convertView.findViewById(R.id.time_text);
                floor_text = (TextView) convertView.findViewById(R.id.floor_text);
                content_text = (TextViewFixTouchConsume) convertView.findViewById(R.id.content_text);

                ViewCache cache = new ViewCache();
                cache.avatar_image = avatar_image;
                cache.name_text = name_text;
                cache.time_text = time_text;
                cache.floor_text = floor_text;
                cache.content_text = content_text;
                convertView.setTag(cache);
            } else {
                ViewCache cache = (ViewCache) convertView.getTag();
                avatar_image = cache.avatar_image;
                name_text = cache.name_text;
                time_text = cache.time_text;
                floor_text = cache.floor_text;
                content_text = cache.content_text;
            }
            CommentContent commentListItem = (CommentContent) getItem(position);

            if (commentListItem != null) {
                if (commentListItem.getUserImg()!=null&&!commentListItem.getUserImg().isEmpty())
                    avatar_image.setImageURI(Uri.parse(commentListItem.getUserImg()));
                else
                    avatar_image.setImageResource(R.drawable.img_default_avatar);
                name_text.setText(commentListItem.getUserName());
                if (commentListItem.getNameRed() == 1)
                    name_text.setTextColor(context.getResources().getColor(R.color.news_number_color));
                else
                    name_text.setTextColor(context.getResources().getColor(R.color.text_gray2_color));
                time_text.setText(commentListItem.getPostDate());
                floor_text.setText(String.valueOf(commentListItem.getCount()));
                //content_text.setText(commentListItem.getContent());
                TextViewUtils.setCommentContent(content_text, commentListItem);
            } else {
                avatar_image.setImageResource(R.drawable.img_default_avatar);
                name_text.setText("::该楼层已被删除");
                name_text.setTextColor(context.getResources().getColor(R.color.text_gray2_color));
                time_text.setText("::该楼层已被删除");
                floor_text.setText("0");
                content_text.setText("::该楼层已被删除");
            }
            return convertView;
        }catch (Exception ex){
            return null;
        }
    }


    private final class ViewCache {
        SimpleDraweeView avatar_image;
        TextView name_text;
        TextView time_text;
        TextView floor_text;
        TextViewFixTouchConsume content_text;
    }
}