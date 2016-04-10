package net.liubaicai.android.acfun.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.models.ChannelList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liush on 2016/3/24.
 */
public class ChannelListAdapter extends BaseAdapter {

    private List<ChannelList> channelListItems;
    private int resource;
    private LayoutInflater inflater;
    private Context basecontext;

    public ChannelListAdapter(Context context, List<ChannelList> channelListItems, int resource) {
        this.channelListItems = channelListItems;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        basecontext=context;
    }

    @Override
    public int getCount() {
        return channelListItems==null?0:channelListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return channelListItems==null?null:channelListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(channelListItems==null){
            return null;
        }

        TextView article_title;
        TextView article_description;
        TextView user_name;
        TextView views_num;
        TextView update_time_string;
        TextView comments_num;

        if(convertView==null){
            convertView = inflater.inflate(resource, null);
            article_title = (TextView) convertView.findViewById(R.id.article_title);
            article_description = (TextView) convertView.findViewById(R.id.article_description);
            user_name = (TextView) convertView.findViewById(R.id.user_name);
            views_num = (TextView) convertView.findViewById(R.id.views_num);
            update_time_string = (TextView) convertView.findViewById(R.id.update_time_string);
            comments_num = (TextView) convertView.findViewById(R.id.comments_num);

            ViewCache cache = new ViewCache();
            cache.article_title = article_title;
            cache.article_description = article_description;
            cache.user_name = user_name;
            cache.views_num = views_num;
            cache.update_time_string = update_time_string;
            cache.comments_num = comments_num;
            convertView.setTag(cache);
        }else{
            ViewCache cache = (ViewCache) convertView.getTag();
            article_title = cache.article_title;
            article_description = cache.article_description;
            user_name = cache.user_name;
            views_num = cache.views_num;
            update_time_string = cache.update_time_string;
            comments_num = cache.comments_num;
        }
        ChannelList channelListItem = channelListItems.get(position);

        article_title.setText(Html.fromHtml(channelListItem.getTitle()));
        article_description.setText(Html.fromHtml(Html.fromHtml(channelListItem.getDescription()).toString()));
        user_name.setText(channelListItem.getUser().getUsername());
        views_num.setText(String.valueOf(channelListItem.getViews()));
        update_time_string.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(channelListItem.getReleaseDate())));
        comments_num.setText(String.valueOf(channelListItem.getComments()));
        return convertView;
    }

    private final class ViewCache{
        TextView article_title;
        TextView article_description;
        TextView user_name;
        TextView views_num;
        TextView update_time_string;
        TextView comments_num;
    }
}
