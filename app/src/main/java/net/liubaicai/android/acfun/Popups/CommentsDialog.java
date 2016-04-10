package net.liubaicai.android.acfun.Popups;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.liubaicai.android.acfun.R;
import net.liubaicai.android.acfun.adapters.CommentsListAdapter;
import net.liubaicai.android.acfun.adapters.CommentsQuoteListAdapter;
import net.liubaicai.android.acfun.models.CommentContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liush on 2016/4/9.
 */
public class CommentsDialog extends Dialog {

    Context context;
    int commentId;
    SparseArray<CommentContent> commentContentArr;
    CommentsDialog commentsDialog;

    public CommentsDialog(Context context) {
        super(context);
        this.context = context;
        this.commentsDialog = this;
    }

    public CommentsDialog(Context context, int commentId, SparseArray<CommentContent> commentContentArr) {
        super(context);
        this.context = context;
        this.commentId = commentId;
        this.commentContentArr = commentContentArr;
        this.commentsDialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("盖楼评论");

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comments_dialog, null);
        setContentView(view);

        List<Integer> commentList = new ArrayList<>();
        int tmp = commentId;
        commentList.add(tmp);
        while (commentContentArr.indexOfKey(tmp) >= 0 && commentContentArr.get(tmp).getQuoteId() > 0) {
            tmp = commentContentArr.get(tmp).getQuoteId();
            commentList.add(0, tmp);
        }

        ListView allCommentListView = (ListView) findViewById(R.id.allCommentListView);
        CommentsQuoteListAdapter adapter = new CommentsQuoteListAdapter(context,
                commentContentArr, commentList, R.layout.comments_list_item);
        allCommentListView.setAdapter(adapter);
        allCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                commentsDialog.hide();
            }
        });
    }
}
